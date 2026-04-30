package puzmon;

import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

public class Display {
    private static OutputProvider output = new StandardOutputProvider();

    /**
     * 出力プロバイダを設定（テスト時等に使用）。
     */
    public static void setOutputProvider(OutputProvider provider) {
        output = provider;
    }

    /**
     * 出力プロバイダをリセット。
     */
    public static void resetOutputProvider() {
        output = new StandardOutputProvider();
    }
    public static void showPartyInfo(Party party) {
        output.println("＜パーティ編成＞");
        printLine();
        for (Monster friend : party.getFriends()) {
            showMonsterName(friend);
            output.printf(" HP=%3d 攻撃=%2d 防御=%2d%n",
                    friend.getHp(), friend.getAttackPower(), friend.getDefensePower());
        }
        printLine();
        output.println("");
    }

    public static void showBattleField(Party party, Monster enemy) {
        output.println("バトルフィールド");
        showMonsterName(enemy);
        output.printf(" HP = %3d / %3d%n%n", enemy.getHp(), enemy.getMaxHp());

        for (int i = 0; i < party.getFriends().size(); i++) {
            if (i > 0) {
                output.print(" ");
            }
            showMonsterName(party.getFriends().get(i));
        }
        output.printf("%nHP = %3d / %3d%n", party.getTotalHp(), party.getMaxHp());
        printLine();

        for (int i = 0; i < GameData.GEMS_LENGTH; i++) {
            if (i > 0) {
                output.print(" ");
            }
            output.print(String.valueOf((char) ('A' + i)));
        }
        output.println("");
        showGems(party.getGemBoard());
        printLine();
    }

    public static void showGems(GemBoard board) {
        Element[] gems = board.getGems();
        for (int i = 0; i < gems.length; i++) {
            if (i > 0) {
                output.print(" ");
            }
            showGem(gems[i]);
        }
        output.println("");
    }

    public static void showMonsterName(Monster monster) {
        Element element = monster.getElement();
        String symbol = element.getSymbol();
        String color = "4" + element.getColorCode();
        String ansiSequence = "\u001B[" + color + "m\u001B[30m" + symbol + monster.getName() + symbol + "\u001B[0m";
        output.print(ansiSequence);
    }

    private static void showGem(Element element) {
        String symbol = element.getSymbol();
        String color = "4" + element.getColorCode();
        String ansiSequence = "\u001B[" + color + "m\u001B[30m" + symbol + "\u001B[0m";
        output.print(ansiSequence);
    }

    public static void printLine() {
        output.println("-".repeat(GameData.LINE_LENGTH));
    }
}
