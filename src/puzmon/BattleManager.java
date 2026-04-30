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
        this(party, enemy, commandReader, new StandardOutputProvider());
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
        this.party = party;
        this.enemy = enemy;
        this.commandReader = commandReader;
        this.damageCalculator = new DamageCalculator();
        this.output = output;
    }

    /**
     * バトルを開始する。
     * プレイヤーターンと敵ターンを交互に繰り返し、どちらかが戦闘不能になるまで続ける。
     *
     * @return 勝利した場合はtrue、敗北した場合はfalse
     */
    public boolean battle() {
        Display.showMonsterName(enemy);
        output.println("が現れた！");

        while (true) {
            playerTurn();
            if (enemy.isDead()) {
                Display.showMonsterName(enemy);
                output.println("を倒した！");
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
        Display.showBattleField(party, enemy);

        String command = commandReader.readCommand();
        int[] indexes = commandReader.parseCommand(command);
        party.getGemBoard().moveGems(indexes[0], indexes[1], true);

        evaluateGems();
    }

    /**
     * 敵ターンを実行する。
     * 敵がプレイヤーパーティにダメージを与える。
     */
    public void enemyTurn() {
        output.printf("【%sのターン】(HP=%d)%n", enemy.getName(), enemy.getHp());
        int baseDamage = enemy.getAttackPower() - party.getAverageDefense();
        int damage = Math.max(1, (int) (baseDamage * damageCalculator.getRandomFactor()));
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

            int matchCount = party.getGemBoard().getMatchCount(matchIndex);
            Element element = party.getGemBoard().getGem(matchIndex);
            party.getGemBoard().clearGems(matchIndex, matchCount);

            if (element == Element.LIFE) {
                doRecover(matchCount, combo);
            } else {
                Monster friend = party.findFriendByElement(element);
                if (friend != null) {
                    combo++;
                    doAttack(friend, matchCount, combo);
                }
            }

            if (party.getGemBoard().hasEmptyCells()) {
                party.getGemBoard().shiftGems();
                party.getGemBoard().spawnGems();
            }
        }

        output.println("");
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
        Display.showMonsterName(friend);
        output.print("の攻撃！");
        if (combo > 1) {
            output.print(" " + combo + " Combo!!");
        }
        output.println("");

        Display.showMonsterName(enemy);
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
        int heal = Math.max(1, (int) (20 * comboBoost * damageCalculator.getRandomFactor()));
        int beforeHp = party.getTotalHp();
        party.heal(heal);
        int actualHeal = party.getTotalHp() - beforeHp;
        output.printf("%sのHPは %d 回復した%n", party.getName(), actualHeal);
    }
}
