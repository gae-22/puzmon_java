package puzmon;

import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

/**
 * バトル画面の表示を管理するクラス。
 * パーティ情報、バトルフィールド、ジェム状態などを表示する。
 */
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
    /**
     * パーティの情報を表示する。
     * パーティに属する各モンスターのHPと攻防を表示。
     *
     * @param party 表示するパーティ
     */
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

    /**
     * バトルフィールドを表示する。
     * 敵と味方のHP、ジェムボードを表示。
     *
     * @param party プレイヤーのパーティ
     * @param enemy 敵モンスター
     */
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

    /**
     * ジェムボードを表示する。
     *
     * @param board 表示するジェムボード
     */
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

    /**
     * モンスター名を色付きで表示する。
     * 属性に応じた色でモンスター名を表示。
     *
     * @param monster 表示するモンスター
     */
    public static void showMonsterName(Monster monster) {
        Element element = monster.getElement();
        String symbol = element.getSymbol();
        String color = "4" + element.getColorCode();
        String ansiSequence = "\u001B[" + color + "m\u001B[30m" + symbol + monster.getName() + symbol + "\u001B[0m";
        output.print(ansiSequence);
    }

    /**
     * 単一のジェムを色付きで表示する。
     *
     * @param element 表示するジェムの属性
     */
    private static void showGem(Element element) {
        String symbol = element.getSymbol();
        String color = "4" + element.getColorCode();
        String ansiSequence = "\u001B[" + color + "m\u001B[30m" + symbol + "\u001B[0m";
        output.print(ansiSequence);
    }

    /**
     * 区切り線を表示する。
     */
    public static void printLine() {
        output.println("-".repeat(GameData.LINE_LENGTH));
    }
}
