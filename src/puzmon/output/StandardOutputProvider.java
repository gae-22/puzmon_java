package puzmon.output;

/**
 * System.out へ出力する実装。
 * 標準的なコンソール出力用。
 * ANSI色コードにも対応している。
 */
public class StandardOutputProvider implements OutputProvider {
    private static final String RESET = "\u001B[0m";

    /**
     * テキストを出力（改行なし）。
     *
     * @param text 出力するテキスト
     */
    @Override
    public void print(String text) {
        System.out.print(text);
    }

    /**
     * テキストを出力（改行あり）。
     *
     * @param text 出力するテキスト
     */
    @Override
    public void println(String text) {
        System.out.println(text);
    }

    /**
     * フォーマット出力を行う。
     *
     * @param format フォーマット文字列
     * @param args フォーマットの引数
     */
    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * 色付きテキストを出力（改行なし）。
     *
     * @param text 出力するテキスト
     * @param ansiColor ANSIカラーコード
     */
    @Override
    public void printColored(String text, String ansiColor) {
        System.out.print("\u001B[" + ansiColor + "m" + text + RESET);
    }

    /**
     * 色付きテキストを出力（改行あり）。
     *
     * @param text 出力するテキスト
     * @param ansiColor ANSIカラーコード
     */
    @Override
    public void printlnColored(String text, String ansiColor) {
        System.out.println("\u001B[" + ansiColor + "m" + text + RESET);
    }

    /**
     * 出力バッファをフラッシュする。
     */
    @Override
    public void flush() {
        System.out.flush();
    }
}
