package puzmon.output;

/**
 * 出力を提供する抽象層。
 * テスト用やGUI/ファイル出力への対応を容易にする。
 */
public interface OutputProvider {
    /**
     * テキストを出力（改行なし）。
     */
    void print(String text);

    /**
     * テキストを出力（改行あり）。
     */
    void println(String text);

    /**
     * フォーマット出力。
     */
    void printf(String format, Object... args);

    /**
     * 色付きテキストを出力（ANSI 制御コード使用、改行なし）。
     */
    void printColored(String text, String ansiColor);

    /**
     * 色付きテキストを出力（ANSI 制御コード使用、改行あり）。
     */
    void printlnColored(String text, String ansiColor);

    /**
     * フラッシュ（必要に応じて）。
     */
    void flush();
}
