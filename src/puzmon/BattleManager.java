package puzmon;

import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

public class BattleManager {
    private final Party party;
    private final Monster enemy;
    private final CommandReader commandReader;
    private final DamageCalculator damageCalculator;
    private final OutputProvider output;

    public BattleManager(Party party, Monster enemy, CommandReader commandReader) {
        this(party, enemy, commandReader, new StandardOutputProvider());
    }

    public BattleManager(Party party, Monster enemy, CommandReader commandReader, OutputProvider output) {
        this.party = party;
        this.enemy = enemy;
        this.commandReader = commandReader;
        this.damageCalculator = new DamageCalculator();
        this.output = output;
    }

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

    public void playerTurn() {
        output.printf("%n【%sのターン】(HP=%d)%n", party.getName(), party.getTotalHp());
        Display.showBattleField(party, enemy);

        String command = commandReader.readCommand();
        int[] indexes = commandReader.parseCommand(command);
        party.getGemBoard().moveGems(indexes[0], indexes[1]);

        evaluateGems();
    }

    public void enemyTurn() {
        output.printf("【%sのターン】(HP=%d)%n", enemy.getName(), enemy.getHp());
        int baseDamage = enemy.getAttackPower() - party.getAverageDefense();
        int damage = Math.max(1, (int) (baseDamage * damageCalculator.getRandomFactor()));
        output.printf("%d のダメージを受けた%n", damage);
        party.takeDamage(damage);
    }

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

    private void doRecover(int matchCount, int combo) {
        double comboBoost = damageCalculator.getComboBoost(matchCount, combo);
        int heal = Math.max(1, (int) (20 * comboBoost * damageCalculator.getRandomFactor()));
        int beforeHp = party.getTotalHp();
        party.heal(heal);
        int actualHeal = party.getTotalHp() - beforeHp;
        output.printf("%sのHPは %d 回復した%n", party.getName(), actualHeal);
    }
}
