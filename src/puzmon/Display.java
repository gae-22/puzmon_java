package puzmon;

import puzmon.output.OutputProvider;

/**
 * バトル画面の表示を管理するクラス。
 * パーティ情報、バトルフィールド、ジェム状態などを表示する。
 */
public class Display {
    private final OutputProvider output;

    /**
     * 出力プロバイダを受け取って表示クラスを作成する。
     *
     * @param output 出力プロバイダ
     */
    public Display(OutputProvider output) {
        this.output = output;
    }

    /**
     * パーティの情報を表示する。
     * パーティに属する各モンスターのHPと攻防を表示。
     *
     * @param party 表示するパーティ
     */
    public void showPartyInfo(Party party) {
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
    public void showBattleField(Party party, Monster enemy) {
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
    public void showGems(GemBoard board) {
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
    public void showMonsterName(Monster monster) {
        output.print(formatColoredText(
                monster.getElement(),
                monster.getName(),
                true));
    }

    /**
     * 単一のジェムを色付きで表示する。
     *
     * @param element 表示するジェムの属性
     */
    private void showGem(Element element) {
        output.print(formatColoredText(element, "", false));
    }

    private String formatColoredText(Element element, String text, boolean wrapWithSymbol) {
        String symbol = element.getSymbol();
        String color = "4" + element.getColorCode();
        String body = wrapWithSymbol ? symbol + text + symbol : symbol;
        return "\u001B[" + color + "m\u001B[30m" + body + "\u001B[0m";
    }

    /**
     * 区切り線を表示する。
     */
    public void printLine() {
        output.println("-".repeat(GameData.LINE_LENGTH));
    }
}
