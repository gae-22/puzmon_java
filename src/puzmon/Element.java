package puzmon;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColorCode() {
        return colorCode;
    }

    public boolean isPlayable() {
        return this != LIFE && this != NONE;
    }

    public boolean isSpecial() {
        return this == LIFE || this == NONE;
    }

    public static Element randomPlayable(Random random) {
        return PLAYABLE_ELEMENTS.get(random.nextInt(PLAYABLE_ELEMENTS.size()));
    }

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
