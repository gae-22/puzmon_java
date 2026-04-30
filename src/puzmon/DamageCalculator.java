package puzmon;

import java.util.concurrent.ThreadLocalRandom;

public class DamageCalculator {
    private final ElementBoostMatrix boostMatrix = new ElementBoostMatrix();

    public int getBaseDamage(int attackPower, int defensePower) {
        if (attackPower < 0 || defensePower < 0) {
            throw new IllegalArgumentException("攻撃力と防御力は非負である必要があります");
        }
        return Math.max(1, attackPower - defensePower);
    }

    public double getElementBoost(Element attacker, Element defender) {
        return boostMatrix.getBoost(attacker, defender);
    }

    public double getComboBoost(int gemCount, int comboCount) {
        if (gemCount < 0 || comboCount < 0) {
            throw new IllegalArgumentException("ジェム数とコンボ数は非負である必要があります");
        }
        int boostValue = Math.max(1, gemCount - 3 + comboCount);
        return Math.pow(1.5, boostValue);
    }

    public double getRandomFactor() {
        return 1.0 + ThreadLocalRandom.current().nextDouble(-0.1, 0.1);
    }

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
