package puzmon;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * 相性行列を EnumMap で管理。
 */
public class ElementBoostMatrix {
    private final Map<Element, Map<Element, Double>> boostMatrix;

    /**
     * コンストラクタ。相性行列を初期化する。
     */
    public ElementBoostMatrix() {
        this.boostMatrix = createBoostMatrix();
    }

    /**
     * 属性相性の行列を作成する。
     * 各属性の相手属性に対する倍率を定義。
     *
     * @return 属性相性の行列
     */
    private static Map<Element, Map<Element, Double>> createBoostMatrix() {
        Map<Element, Map<Element, Double>> matrix = new EnumMap<>(Element.class);

        // FIRE (火) -> 風に強、土に弱
        Map<Element, Double> fireBoosts = new EnumMap<>(Element.class);
        fireBoosts.put(Element.FIRE, 1.0);
        fireBoosts.put(Element.WIND, 2.0);
        fireBoosts.put(Element.EARTH, 0.5);
        fireBoosts.put(Element.WATER, 1.0);
        fireBoosts.put(Element.LIFE, 1.0);
        fireBoosts.put(Element.NONE, 1.0);
        matrix.put(Element.FIRE, Collections.unmodifiableMap(fireBoosts));

        // WIND (風) -> 土に強、火に弱
        Map<Element, Double> windBoosts = new EnumMap<>(Element.class);
        windBoosts.put(Element.FIRE, 0.5);
        windBoosts.put(Element.WIND, 1.0);
        windBoosts.put(Element.EARTH, 2.0);
        windBoosts.put(Element.WATER, 1.0);
        windBoosts.put(Element.LIFE, 1.0);
        windBoosts.put(Element.NONE, 1.0);
        matrix.put(Element.WIND, Collections.unmodifiableMap(windBoosts));

        // EARTH (土) -> 水に強、風に弱
        Map<Element, Double> earthBoosts = new EnumMap<>(Element.class);
        earthBoosts.put(Element.FIRE, 2.0);
        earthBoosts.put(Element.WIND, 0.5);
        earthBoosts.put(Element.EARTH, 1.0);
        earthBoosts.put(Element.WATER, 1.0);
        earthBoosts.put(Element.LIFE, 1.0);
        earthBoosts.put(Element.NONE, 1.0);
        matrix.put(Element.EARTH, Collections.unmodifiableMap(earthBoosts));

        // WATER (水) -> 火に強、土に弱
        Map<Element, Double> waterBoosts = new EnumMap<>(Element.class);
        waterBoosts.put(Element.FIRE, 1.0);
        waterBoosts.put(Element.WIND, 1.0);
        waterBoosts.put(Element.EARTH, 1.0);
        waterBoosts.put(Element.WATER, 1.0);
        waterBoosts.put(Element.LIFE, 1.0);
        waterBoosts.put(Element.NONE, 1.0);
        matrix.put(Element.WATER, Collections.unmodifiableMap(waterBoosts));

        // LIFE, NONE はすべて 1.0
        Map<Element, Double> neutralBoosts = new EnumMap<>(Element.class);
        for (Element e : Element.values()) {
            neutralBoosts.put(e, 1.0);
        }
        Map<Element, Double> immutableNeutral = Collections.unmodifiableMap(neutralBoosts);
        matrix.put(Element.LIFE, immutableNeutral);
        matrix.put(Element.NONE, immutableNeutral);

        return Collections.unmodifiableMap(matrix);
    }

    /**
     * 攻撃側と防御側の相性倍率を取得。
     *
     * @param attacker 攻撃者の属性
     * @param defender 防御者の属性
     * @return 相性倍率（1.0 = 等倍、2.0 = 効果抜群、0.5 = 今ひとつ）
     */
    public double getBoost(Element attacker, Element defender) {
        if (attacker == null || defender == null) {
            return 1.0;
        }

        Map<Element, Double> defenderMap = boostMatrix.get(attacker);
        if (defenderMap == null) {
            return 1.0;
        }

        return defenderMap.getOrDefault(defender, 1.0);
    }
}
