package puzmon.input;

import java.util.Scanner;

/**
 * System.in から入力を取得する実装。
 * 標準入力からの行入力をトリミング処理して提供する。
 */
public class SystemInInputProvider implements InputProvider {
    private final Scanner scanner;

    /**
     * コンストラクタ。デフォルトのScannerを使用。
     */
    public SystemInInputProvider() {
        this(new Scanner(System.in));
    }

    /**
     * コンストラクタ。Scannerを指定。
     *
     * @param scanner 使用するScanner
     */
    public SystemInInputProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * 1行分の入力を取得（トリミング済み）。
     *
     * @return トリミングされた入力文字列
     */
    @Override
    public String nextLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Scannerを閉じて入力ストリームを終了する。
     */
    @Override
    public void close() {
        scanner.close();
    }
}
