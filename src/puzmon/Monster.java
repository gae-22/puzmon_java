package puzmon;

/**
 * モンスターを表すクラス。
 * 名前、体力、属性、攻撃力、防御力の情報を持つ。
 */
public class Monster {
    private final String name;
    private final int maxHp;
    private final Element element;
    private final int attackPower;
    private final int defensePower;
    private int hp;

    /**
     * コンストラクタ。モンスターを生成する。
     *
     * @param name モンスター名
     * @param maxHp 最大HP
     * @param element 属性
     * @param attackPower 攻撃力
     * @param defensePower 防御力
     */
    public Monster(String name, int maxHp, Element element, int attackPower, int defensePower) {
        this.name = name;
        this.maxHp = maxHp;
        this.element = element;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.hp = maxHp;
    }

    /**
     * モンスター名を取得する。
     *
     * @return モンスター名
     */
    public String getName() {
        return name;
    }

    /**
     * 現在のHPを取得する。
     *
     * @return 現在のHP
     */
    public int getHp() {
        return hp;
    }

    /**
     * 最大HPを取得する。
     *
     * @return 最大HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * 属性を取得する。
     *
     * @return 属性
     */
    public Element getElement() {
        return element;
    }

    /**
     * 攻撃力を取得する。
     *
     * @return 攻撃力
     */
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * 防御力を取得する。
     *
     * @return 防御力
     */
    public int getDefensePower() {
        return defensePower;
    }

    /**
     * ダメージを受ける。
     * HPが0未満にならないようにクリップする。
     *
     * @param damage 受けるダメージ
     */
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - Math.max(0, damage));
    }

    /**
     * HPを回復する。
     * HPが最大HPを超えないようにクリップする。
     *
     * @param amount 回復量
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + Math.max(0, amount));
    }

    /**
     * 戦闘不能状態かどうかを判定する。
     *
     * @return HPが0以下の場合はtrue
     */
    public boolean isDead() {
        return hp <= 0;
    }
}
