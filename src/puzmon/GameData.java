package puzmon;

/**
 * ゲームの定数とデータを管理するクラス。
 * 敵とプレイヤーのモンスターデータを生成する。
 */
public final class GameData {
    /** ジェムボードの長さ */
    public static final int GEMS_LENGTH = 14;
    /** パーティの最大サイズ */
    public static final int PARTY_SIZE = 4;
    /** 画面の区切り線の長さ */
    public static final int LINE_LENGTH = 29;

    private GameData() {
    }

    /**
     * 敵モンスターの配列を生成する。
     *
     * @return 敵モンスターの配列
     */
    public static Monster[] createEnemies() {
        return new Monster[] {
                new Monster("スライム", 100, Element.WATER, 10, 1),
                new Monster("ゴブリン", 200, Element.EARTH, 20, 5),
                new Monster("オオコウモリ", 300, Element.WIND, 30, 10),
                new Monster("ウェアウルフ", 400, Element.WIND, 40, 15),
                new Monster("ドラゴン", 600, Element.FIRE, 50, 20),
        };
    }

    /**
     * プレイヤーのモンスター配列を生成する。
     *
     * @return プレイヤーモンスターの配列
     */
    public static Monster[] createPlayers() {
        return new Monster[] {
                new Monster("朱雀", 150, Element.FIRE, 30, 10),
                new Monster("青龍", 150, Element.WIND, 20, 10),
                new Monster("白虎", 150, Element.EARTH, 25, 5),
                new Monster("玄武", 150, Element.WATER, 25, 15),
        };
    }
}
