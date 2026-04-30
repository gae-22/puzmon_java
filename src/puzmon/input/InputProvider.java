package puzmon.input;

/**
 * ユーザー入力を提供する抽象層。
 * テスト用や別のUI（GUI等）への対応を容易にする。
 */
public interface InputProvider {
    /**
     * 1行分の入力を取得。トリミング済み。
     */
    String nextLine();

    /**
     * 入力ストリームを閉じる。
     */
    void close();
}
