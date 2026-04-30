package puzmon.input;

import java.util.Scanner;

/**
 * System.in から入力を取得する実装。
 */
public class SystemInInputProvider implements InputProvider {
    private final Scanner scanner;

    public SystemInInputProvider() {
        this(new Scanner(System.in));
    }

    public SystemInInputProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String nextLine() {
        return scanner.nextLine().trim();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
