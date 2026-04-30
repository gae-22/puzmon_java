package puzmon;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * ゲーム内の属性を定義するEnum。
 * 火、風、土、水、命、無の6つの属性を定義。
 */
public enum Element {
    FIRE("火", "$", "1"),
    WIND("風", "@", "2"),
    EARTH("土", "#", "3"),
    WATER("水", "~", "6"),
    LIFE("命", "&", "5"),
    NONE("無", " ", "7");

    private static final List<Element> PLAYABLE_ELEMENTS = Collections.unmodifiableList(
            Arrays.asList(FIRE, WIND, EARTH, WATER));

    private static final Map<String, Element> BY_DISPLAY_NAME = createDisplayNameMap();

    private final String displayName;
    private final String symbol;
    private final String colorCode;

    Element(String displayName, String symbol, String colorCode) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.colorCode = colorCode;
    }

    private static Map<String, Element> createDisplayNameMap() {
        Map<String, Element> map = new HashMap<>();
        for (Element element : Element.values()) {
            map.put(element.displayName, element);
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * 属性の表示名を取得する。
     *
     * @return 属性の表示名
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 属性のシンボルを取得する。
     *
     * @return 属性のシンボル文字
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 属性の色コードを取得する。
     *
     * @return ANSI色コード
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * プレイ可能な属性かどうかを判定する。
     *
     * @return プレイ可能な場合はtrue
     */
    public boolean isPlayable() {
        return this != LIFE && this != NONE;
    }

    /**
     * 特殊な属性（命または無）かどうかを判定する。
     *
     * @return 特殊な属性の場合はtrue
     */
    public boolean isSpecial() {
        return this == LIFE || this == NONE;
    }

    /**
     * プレイ可能な属性からランダムに1つ選択する。
     *
     * @param random 乱数生成器
     * @return ランダムに選ばれたプレイ可能な属性
     */
    public static Element randomPlayable(Random random) {
        return PLAYABLE_ELEMENTS.get(random.nextInt(PLAYABLE_ELEMENTS.size()));
    }

    /**
     * プレイ可能な属性のリストを取得する。
     *
     * @return プレイ可能な属性のリスト（不変）
     */
    public static List<Element> getPlayableElements() {
        return PLAYABLE_ELEMENTS;
    }

    /**
     * 表示名から Element を取得。
     * Map ベースで O(1) の高速検索。
     *
     * @param displayName 表示名（例："火", "風"）
     * @return 対応する Element、なければ NONE
     */
    public static Element fromString(String displayName) {
        return BY_DISPLAY_NAME.getOrDefault(displayName, NONE);
    }
}
