package puzmon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Party {
    private final String name;
    private final List<Monster> friends;
    private final GemBoard gemBoard;
    private int hp;
    private final int maxHp;
    private final int averageDefense;

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

    public Party(String name, Monster[] friends) {
        this(name, Arrays.asList(friends));
    }

    public String getName() {
        return name;
    }

    public List<Monster> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public GemBoard getGemBoard() {
        return gemBoard;
    }

    public int getTotalHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAverageDefense() {
        return averageDefense;
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - Math.max(0, damage));
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + Math.max(0, amount));
    }

    public boolean isDefeated() {
        return hp <= 0;
    }

    public Monster findFriendByElement(Element element) {
        for (Monster friend : friends) {
            if (friend.getElement() == element) {
                return friend;
            }
        }
        return null;
    }
}
