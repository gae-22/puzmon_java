package puzmon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ダンジョンを表すクラス。
 * フロアごとに敵モンスターを1体ずつ保持し、進行管理を行う。
 */
public class Dungeon {
    private final String name;
    private final List<Monster> floors;
    private int currentFloorIndex;

    public Dungeon(String name, List<Monster> floors) {
        this.name = name;
        this.floors = new ArrayList<>();
        for (Monster floor : floors) {
            this.floors.add(floor);
        }
        this.currentFloorIndex = 0;
    }

    /** ダンジョン名を取得する。 */
    public String getName() {
        return name;
    }

    /** フロア数を取得する。 */
    public int getTotalFloors() {
        return floors.size();
    }

    /** 現在のフロアの敵モンスターを取得する。 */
    public Monster getCurrentFloorEnemy() {
        if (currentFloorIndex < 0 || currentFloorIndex >= floors.size()) {
            return null;
        }
        return floors.get(currentFloorIndex);
    }

    /** 次のフロアへ進む。進めたらtrue、最後のフロアで進めなければfalseを返す。 */
    public boolean advanceFloor() {
        if (currentFloorIndex + 1 < floors.size()) {
            currentFloorIndex++;
            return true;
        }
        return false;
    }

    /** ダンジョンをクリア済みか判定する。 */
    public boolean isCleared() {
        return floors.isEmpty() || currentFloorIndex >= floors.size();
    }

    /** ダンジョン進行をリセットする。 */
    public void reset() {
        currentFloorIndex = 0;
    }

    /** フロア一覧のコピーを返す。 */
    public List<Monster> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    /** 現在のフロアインデックスを取得する（0-based）。 */
    public int getCurrentFloorIndex() {
        return currentFloorIndex;
    }
}
