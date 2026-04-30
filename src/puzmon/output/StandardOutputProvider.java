package puzmon.output;

/**
 * System.out へ出力する実装。
 * 標準的なコンソール出力用。
 */
public class StandardOutputProvider implements OutputProvider {
    private static final String RESET = "\u001B[0m";

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public void printColored(String text, String ansiColor) {
        System.out.print("\u001B[" + ansiColor + "m" + text + RESET);
    }

    @Override
    public void printlnColored(String text, String ansiColor) {
        System.out.println("\u001B[" + ansiColor + "m" + text + RESET);
    }

    @Override
    public void flush() {
        System.out.flush();
    }
}
