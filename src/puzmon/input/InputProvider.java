package puzmon.input;

/**
 * ユーザー入力を提供する抽象層。
 * テスト用、GUI等別UIへの対応を容易にする。
 */
public interface InputProvider {
    /**
     * 1行分の入力を取得。トリミング済み。
     *
     * @return トリミングされた入力文字列
     */
    String nextLine();

    /**
     * 入力ストリームを閉じる。
     */
    void close();
}
