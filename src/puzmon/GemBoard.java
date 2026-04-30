package puzmon;

import java.util.Arrays;
import java.util.Random;

public class GemBoard {
    private static final Random RANDOM = new Random();

    private final Element[] gems = new Element[GameData.GEMS_LENGTH];

    public GemBoard() {
        initialize();
    }

    public final void initialize() {
        for (int i = 0; i < gems.length; i++) {
            gems[i] = Element.randomPlayable(RANDOM);
        }
    }

    public Element[] getGems() {
        return Arrays.copyOf(gems, gems.length);
    }

    public Element getGem(int index) {
        if (index < 0 || index >= gems.length) {
            throw new IndexOutOfBoundsException("無効なジェム位置です: " + index);
        }
        return gems[index];
    }

    public void swapGems(int index1, int index2) {
        validateIndex(index1);
        validateIndex(index2);

        Element temp = gems[index1];
        gems[index1] = gems[index2];
        gems[index2] = temp;
    }

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

    public void spawnGems() {
        for (int i = 0; i < gems.length; i++) {
            if (gems[i] == Element.NONE) {
                gems[i] = Element.randomPlayable(RANDOM);
            }
        }

        Display.showGems(this);
    }

    public boolean hasEmptyCells() {
        for (Element gem : gems) {
            if (gem == Element.NONE) {
                return true;
            }
        }
        return false;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= gems.length) {
            throw new IndexOutOfBoundsException("無効なジェム位置です: " + index);
        }
    }
}
