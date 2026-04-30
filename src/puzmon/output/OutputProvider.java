package puzmon.output;

/**
 * 出力を提供する抽象層。
 * テスト用、GUI/ファイル出力への対応を容易にする。
 */
public interface OutputProvider {
    /**
     * テキストを出力（改行なし）。
     *
     * @param text 出力するテキスト
     */
    void print(String text);

    /**
     * テキストを出力（改行あり）。
     *
     * @param text 出力するテキスト
     */
    void println(String text);

    /**
     * フォーマット出力を行う。
     *
     * @param format フォーマット文字列
     * @param args フォーマットの引数
     */
    void printf(String format, Object... args);

    /**
     * 色付きテキストを出力（ANSI 制御コード使用、改行なし）。     *
     * @param text 出力するテキスト
     * @param ansiColor ANSIカラーコード     */
    void printColored(String text, String ansiColor);

    /**
     * 色付きテキストを出力（ANSI 制御コード使用、改行あり）。     *
     * @param text 出力するテキスト
     * @param ansiColor ANSIカラーコード     */
    void printlnColored(String text, String ansiColor);

    /**
     * フラッシュ（必要に応じて）。
     */
    void flush();
}
