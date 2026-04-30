package puzmon;

public class Monster {
    private final String name;
    private final int maxHp;
    private final Element element;
    private final int attackPower;
    private final int defensePower;
    private int hp;

    public Monster(String name, int maxHp, Element element, int attackPower, int defensePower) {
        this.name = name;
        this.maxHp = maxHp;
        this.element = element;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.hp = maxHp;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public Element getElement() {
        return element;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - Math.max(0, damage));
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + Math.max(0, amount));
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
