package puzmon;

/**
 * バトル処理で使う定数をまとめたクラス。
 */
public final class BattleConstants {
    /** コンボ倍率の基準値 */
    public static final double COMBO_MULTIPLIER_BASE = 1.5;
    /** ランダム補正の振れ幅 */
    public static final double RANDOM_FACTOR_OFFSET = 0.1;
    /** 回復処理の基準値 */
    public static final int RECOVERY_BASE_AMOUNT = 20;
    /** 最低ダメージ */
    public static final int MIN_DAMAGE = 1;

    private BattleConstants() {
    }
}
