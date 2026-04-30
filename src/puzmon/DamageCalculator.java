package puzmon;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ダメージ計算を行うクラス。
 * 基本ダメージ、相性倍率、コンボ倍率、ランダム因子を統合して最終ダメージを計算する。
 */
public class DamageCalculator {
    private final ElementBoostMatrix boostMatrix = new ElementBoostMatrix();

    /**
     * 基本ダメージを計算する。
     * 攻撃力から防御力を引き、最小値0を保証する。
     *
     * @param attackPower 攻撃力
     * @param defensePower 防御力
     * @return 基本ダメージ
     * @throws IllegalArgumentException 攻撃力または防御力が負の場合
     */
    public int getBaseDamage(int attackPower, int defensePower) {
        if (attackPower < 0 || defensePower < 0) {
            throw new IllegalArgumentException("攻撃力と防御力は非負である必要があります");
        }
        return Math.max(0, attackPower - defensePower);
    }

    /**
     * 属性相性倍率を取得する。
     *
     * @param attacker 攻撃者の属性
     * @param defender 防御者の属性
     * @return 相性倍率（1.0=等倍、2.0=効果抜群、0.5=今ひとつ）
     */
    public double getElementBoost(Element attacker, Element defender) {
        return boostMatrix.getBoost(attacker, defender);
    }

    /**
     * コンボ倍率を計算する。
     * ジェム数とコンボ数に基づいて計算される。
     *
     * @param gemCount マッチしたジェム数
     * @param comboCount コンボ回数
     * @return コンボ倍率
     * @throws IllegalArgumentException ジェム数またはコンボ数が負の場合
     */
    public double getComboBoost(int gemCount, int comboCount) {
        if (gemCount < 0 || comboCount < 0) {
            throw new IllegalArgumentException("ジェム数とコンボ数は非負である必要があります");
        }
        int boostValue = Math.max(1, gemCount - 3 + comboCount);
        return Math.pow(1.5, boostValue);
    }

    /**
     * ランダム因子を取得する。
     * -10%から+10%の範囲でランダムな倍率を返す。
     *
     * @return ランダム因子（0.9～1.1）
     */
    public double getRandomFactor() {
        return 1.0 + ThreadLocalRandom.current().nextDouble(-0.1, 0.1);
    }

    /**
     * 最終ダメージを計算する。
     * 基本ダメージに相性倍率、コンボ倍率、ランダム因子を適用する。
     *
     * @param attackPower 攻撃力
     * @param defensePower 防御力
     * @param attackerElement 攻撃者の属性
     * @param defenderElement 防御者の属性
     * @param gemCount マッチしたジェム数
     * @param comboCount コンボ回数
     * @return 最終ダメージ（最小値1）
     */
    public int calculateFinalDamage(
            int attackPower,
            int defensePower,
            Element attackerElement,
            Element defenderElement,
            int gemCount,
            int comboCount) {

        int baseDamage = getBaseDamage(attackPower, defensePower);
        double elementBoost = getElementBoost(attackerElement, defenderElement);
        double comboBoost = getComboBoost(gemCount, comboCount);
        double randomFactor = getRandomFactor();

        double result = baseDamage * elementBoost * comboBoost * randomFactor;
        return Math.max(1, (int) result);
    }
}
