package puzmon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * パーティを表すクラス。
 * 複数のモンスター、ジェムボード、総HP、平均防御力を管理する。
 */
public class Party {
    private final String name;
    private final List<Monster> friends;
    private final GemBoard gemBoard;
    private int hp;
    private final int maxHp;
    private final int averageDefense;

    /**
     * コンストラクタ。パーティを生成する。
     *
     * @param name パーティ名
     * @param friends パーティに属するモンスターのリスト
     * @throws IllegalArgumentException friendsがnullまたは空の場合
     */
    public Party(String name, List<Monster> friends) {
        if (friends == null || friends.isEmpty()) {
            throw new IllegalArgumentException("friends must not be empty");
        }
        this.name = name;
        this.friends = new ArrayList<>(friends);
        this.gemBoard = new GemBoard();

        int totalHp = 0;
        int totalDefense = 0;
        for (Monster friend : this.friends) {
            totalHp += friend.getHp();
            totalDefense += friend.getDefensePower();
        }
        this.hp = totalHp;
        this.maxHp = this.friends.stream().mapToInt(Monster::getMaxHp).sum();
        this.averageDefense = (int) Math.ceil((double) totalDefense / this.friends.size());
    }

    /**
     * コンストラクタ。パーティを生成する。
     *
     * @param name パーティ名
     * @param friends パーティに属するモンスターの配列
     */
    public Party(String name, Monster[] friends) {
        this(name, Arrays.asList(friends));
    }

    /**
     * パーティ名を取得する。
     *
     * @return パーティ名
     */
    public String getName() {
        return name;
    }

    /**
     * パーティに属するモンスターのリストを取得する。
     *
     * @return モンスターのリスト（不変）
     */
    public List<Monster> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    /**
     * ジェムボードを取得する。
     *
     * @return ジェムボード
     */
    public GemBoard getGemBoard() {
        return gemBoard;
    }

    /**
     * パーティの総HPを取得する。
     *
     * @return 総HP
     */
    public int getTotalHp() {
        return hp;
    }

    /**
     * パーティの最大総HPを取得する。
     *
     * @return 最大総HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * パーティの平均防御力を取得する。
     *
     * @return 平均防御力
     */
    public int getAverageDefense() {
        return averageDefense;
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
     * パーティが戦闘不能状態かどうかを判定する。
     *
     * @return HPが0以下の場合はtrue
     */
    public boolean isDefeated() {
        return hp <= 0;
    }

    /**
     * 指定された属性のモンスターをパーティから探す。
     *
     * @param element 検索する属性
     * @return 該当するモンスター、見つからない場合はnull
     */
    public Monster findFriendByElement(Element element) {
        for (Monster friend : friends) {
            if (friend.getElement() == element) {
                return friend;
            }
        }
        return null;
    }
}
