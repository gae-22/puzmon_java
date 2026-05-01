package puzmon;

import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

/**
 * バトル進行を管理するクラス。
 * プレイヤーと敵のターン処理、ジェムの評価、ダメージ計算を統括する。
 */
public class BattleManager {
    private final Party party;
    private final Monster enemy;
    private final CommandReader commandReader;
    private final Display display;
    private final DamageCalculator damageCalculator;
    private final OutputProvider output;

    /**
     * コンストラクタ。デフォルトの出力プロバイダを使用。
     *
     * @param party プレイヤーのパーティ
     * @param enemy 敵モンスター
     * @param commandReader コマンド入力用リーダー
     */
    public BattleManager(Party party, Monster enemy, CommandReader commandReader) {
        this(party, enemy, commandReader, new Display(new StandardOutputProvider()), new StandardOutputProvider());
    }

    /**
     * コンストラクタ。出力プロバイダを指定。
     *
     * @param party プレイヤーのパーティ
     * @param enemy 敵モンスター
     * @param commandReader コマンド入力用リーダー
     * @param output 出力プロバイダ
     */
    public BattleManager(Party party, Monster enemy, CommandReader commandReader, OutputProvider output) {
        this(party, enemy, commandReader, new Display(output), output);
    }

    /**
     * コンストラクタ。表示クラスと出力プロバイダを指定。
     *
     * @param party プレイヤーのパーティ
     * @param enemy 敵モンスター
     * @param commandReader コマンド入力用リーダー
     * @param display 表示クラス
     * @param output 出力プロバイダ
     */
    public BattleManager(Party party, Monster enemy, CommandReader commandReader, Display display, OutputProvider output) {
        this.party = party;
        this.enemy = enemy;
        this.commandReader = commandReader;
        this.display = display;
        this.damageCalculator = new DamageCalculator();
        this.output = output;
        party.getGemBoard().setDisplay(display);
    }

    /**
     * バトルを開始する。
     * プレイヤーターンと敵ターンを交互に繰り返し、どちらかが戦闘不能になるまで続ける。
     *
     * @return 勝利した場合はtrue、敗北した場合はfalse
     */
    public boolean battle() {
        display.showMonsterName(enemy);
        output.println("が現れた！");

        while (true) {
            playerTurn();
            if (handleEnemyDefeat()) {
                waitForNextTurn(500);
                return true;
            }

            enemyTurn();
            if (party.isDefeated()) {
                output.println("パーティのHPが0になった");
                return false;
            }
        }
    }

    /**
     * プレイヤーターンを実行する。
     * ジェムの移動入力を受け付け、ジェムを評価する。
     */
    public void playerTurn() {
        output.printf("%n【%sのターン】(HP=%d)%n", party.getName(), party.getTotalHp());
        display.showBattleField(party, enemy);

        String command = commandReader.readCommand();
        int[] indexes = commandReader.parseCommand(command);
        moveGems(indexes);

        evaluateGems();
    }

    /**
     * 敵ターンを実行する。
     * 敵がプレイヤーパーティにダメージを与える。
     */
    public void enemyTurn() {
        output.printf("【%sのターン】(HP=%d)%n", enemy.getName(), enemy.getHp());
        int baseDamage = enemy.getAttackPower() - party.getAverageDefense();
        int damage = Math.max(BattleConstants.MIN_DAMAGE, (int) (baseDamage * damageCalculator.getRandomFactor()));
        output.printf("%d のダメージを受けた%n", damage);
        party.takeDamage(damage);
    }

    /**
     * ジェムを評価し、マッチしたジェムに対してアクションを実行する。
     * コンボカウントを管理し、攻撃と回復を処理する。
     */
    public void evaluateGems() {
        int combo = 0;

        while (true) {
            int matchIndex = party.getGemBoard().findMatchIndex();
            if (matchIndex < 0) {
                break;
            }

            combo = resolveMatch(matchIndex, combo);
            refillBoardIfNeeded();
        }

        output.println("");
    }

    private void moveGems(int[] indexes) {
        party.getGemBoard().moveGems(indexes[0], indexes[1]);
    }

    private boolean handleEnemyDefeat() {
        if (!enemy.isDead()) {
            return false;
        }

        display.showMonsterName(enemy);
        output.println("を倒した！");
        return true;
    }

    private int resolveMatch(int matchIndex, int combo) {
        GemBoard board = party.getGemBoard();
        int matchCount = board.getMatchCount(matchIndex);
        Element element = board.getGem(matchIndex);
        board.clearGems(matchIndex, matchCount);

        if (element == Element.LIFE) {
            doRecover(matchCount, combo);
            return combo;
        }

        Monster friend = party.findFriendByElement(element);
        if (friend != null) {
            int nextCombo = combo + 1;
            doAttack(friend, matchCount, nextCombo);
            return nextCombo;
        }

        return combo;
    }

    private void refillBoardIfNeeded() {
        GemBoard board = party.getGemBoard();
        if (!board.hasEmptyCells()) {
            return;
        }

        board.shiftGems();
        board.spawnGems();
    }

    /**
     * 攻撃を実行する。
     * 最終ダメージを計算して敵に与える。
     *
     * @param friend 攻撃側のモンスター
     * @param matchCount マッチしたジェム数
     * @param combo コンボカウント
     */
    private void doAttack(Monster friend, int matchCount, int combo) {
        int damage = damageCalculator.calculateFinalDamage(
                friend.getAttackPower(),
                enemy.getDefensePower(),
                friend.getElement(),
                enemy.getElement(),
                matchCount,
                combo);

        output.println("");
        display.showMonsterName(friend);
        output.print("の攻撃！");
        if (combo > 1) {
            output.print(" " + combo + " Combo!!");
        }
        output.println("");

        display.showMonsterName(enemy);
        output.printf("に %d のダメージを与えた%n", damage);
        output.println("");
        enemy.takeDamage(damage);
    }

    /**
     * パーティを回復する。
     * ジェム数とコンボ数に応じた回復量を計算し、パーティを回復させる。
     *
     * @param matchCount マッチしたジェム数
     * @param combo コンボカウント
     */
    private void doRecover(int matchCount, int combo) {
        double comboBoost = damageCalculator.getComboBoost(matchCount, combo);
        int heal = Math.max(BattleConstants.MIN_DAMAGE,
                (int) (BattleConstants.RECOVERY_BASE_AMOUNT * comboBoost * damageCalculator.getRandomFactor()));
        int beforeHp = party.getTotalHp();
        party.heal(heal);
        int actualHeal = party.getTotalHp() - beforeHp;
        output.printf("%sのHPは %d 回復した%n", party.getName(), actualHeal);
    }

    /**
     * 次のターンまで一定時間待機する。
     *
     * @param millis 待機時間（ミリ秒）
     */
    private void waitForNextTurn(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
