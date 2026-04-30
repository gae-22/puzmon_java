package puzmon;

import java.util.Arrays;
import java.util.Random;

/**
 * ジェムボードの状態を管理するクラス。
 * ジェムの移動、マッチ判定、生成を処理する。
 */
public class GemBoard {
    private static final Random RANDOM = new Random();

    private final Element[] gems = new Element[GameData.GEMS_LENGTH];

    /**
     * コンストラクタ。ジェムボードを初期化する。
     */
    public GemBoard() {
        initialize();
    }

    /**
     * ジェムボードを初期化する。
     * 初期配置は固定値を使う。
     */
    public final void initialize() {
        if (GameData.INITIAL_GEMS.length != gems.length) {
            throw new IllegalStateException("初期ジェム数がボード長と一致しません");
        }
        System.arraycopy(GameData.INITIAL_GEMS, 0, gems, 0, gems.length);
    }

    /**
     * ジェムボード上の全ジェムのコピーを取得する。
     *
     * @return ジェム配列のコピー
     */
    public Element[] getGems() {
        return Arrays.copyOf(gems, gems.length);
    }

    /**
     * 指定位置のジェムを取得する。
     *
     * @param index ジェムの位置
     * @return 指定位置のジェム
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public Element getGem(int index) {
        if (index < 0 || index >= gems.length) {
            throw new IndexOutOfBoundsException("無効なジェム位置です: " + index);
        }
        return gems[index];
    }

    /**
     * 2つのジェムを交換する。
     *
     * @param index1 交換するジェムの位置1
     * @param index2 交換するジェムの位置2
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public void swapGems(int index1, int index2) {
        validateIndex(index1);
        validateIndex(index2);

        Element temp = gems[index1];
        gems[index1] = gems[index2];
        gems[index2] = temp;
    }

    /**
     * ジェムを移動する。アニメーション表示付き。
     *
     * @param fromIndex 移動元のジェム位置
     * @param toIndex 移動先のジェム位置
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public void moveGems(int fromIndex, int toIndex) {
        validateIndex(fromIndex);
        validateIndex(toIndex);

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex < toIndex) {
            for (int i = fromIndex; i < toIndex; i++) {
                swapGems(i, i + 1);
                Display.showGems(this);
            }
        } else {
            for (int i = fromIndex; i > toIndex; i--) {
                swapGems(i, i - 1);
                Display.showGems(this);
            }
        }
    }
    /**
     * ジェムを移動する。
     *
     * @param fromIndex 移動元のジェム位置
     * @param toIndex 移動先のジェム位置
     * @param animate アニメーション表示の有無
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public void moveGems(int fromIndex, int toIndex, boolean animate) {
        validateIndex(fromIndex);
        validateIndex(toIndex);

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex < toIndex) {
            for (int i = fromIndex; i < toIndex; i++) {
                swapGems(i, i + 1);
                if (animate) {
                    Display.showGems(this);
                }
            }
        } else {
            for (int i = fromIndex; i > toIndex; i--) {
                swapGems(i, i - 1);
                if (animate) {
                    Display.showGems(this);
                }
            }
        }
    }

    /**
     * マッチしたジェムの最初の位置を探す。
     * 3個以上連続している最初のジェムのインデックスを返す。
     *
     * @return マッチしたジェムのインデックス、マッチがない場合は-1
     */
    public int findMatchIndex() {
        for (int i = 0; i <= gems.length - 3; i++) {
            Element element = gems[i];
            if (element == Element.NONE) {
                continue;
            }
            if (element == gems[i + 1] && element == gems[i + 2]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定位置から連続してマッチしているジェムの個数を取得する。
     *
     * @param index ジェムの位置
     * @return マッチしているジェムの個数
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public int getMatchCount(int index) {
        validateIndex(index);

        Element element = gems[index];
        if (element == Element.NONE) {
            return 0;
        }

        int count = 0;
        for (int i = index; i < gems.length; i++) {
            if (element != gems[i]) {
                break;
            }
            count++;
        }
        return count;
    }

    /**
     * 指定位置からcount個のジェムを削除する。
     *
     * @param index 削除開始位置
     * @param count 削除するジェムの個数
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     * @throws IllegalArgumentException countが負の場合
     */
    public void clearGems(int index, int count) {
        validateIndex(index);
        if (count < 0) {
            throw new IllegalArgumentException("count must be non-negative");
        }

        for (int i = index; i < index + count && i < gems.length; i++) {
            gems[i] = Element.NONE;
        }

        Display.showGems(this);
    }

    /**
     * 空きセルを埋めるため、ジェムを移動させる。
     */
    public void shiftGems() {
        Display.showGems(this);

        int noneCnt = 0;
        for (int i = gems.length - 1; i >= 0; i--) {
            if (gems[i] == Element.NONE) {
                noneCnt++;
                moveGems(i, gems.length - noneCnt, false);
                Display.showGems(this);
            }
        }
    }

    /**
     * 空きセルに新しいジェムを生成する。
     */
    public void spawnGems() {
        for (int i = 0; i < gems.length; i++) {
            if (gems[i] == Element.NONE) {
                gems[i] = Element.randomPlayable(RANDOM);
            }
        }

        Display.showGems(this);
    }

    /**
     * 空きセルが存在するかどうかを判定する。
     *
     * @return 空きセルがある場合はtrue
     */
    public boolean hasEmptyCells() {
        for (Element gem : gems) {
            if (gem == Element.NONE) {
                return true;
            }
        }
        return false;
    }

    /**
     * ジェム位置のインデックスを検証する。
     *
     * @param index 検証するインデックス
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= gems.length) {
            throw new IndexOutOfBoundsException("無効なジェム位置です: " + index);
        }
    }
}
