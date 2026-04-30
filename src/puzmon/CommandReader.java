package puzmon;

import puzmon.input.InputProvider;
import puzmon.input.SystemInInputProvider;
import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

public class CommandReader {
    private final InputProvider inputProvider;
    private final OutputProvider output;

    public CommandReader() {
        this(new SystemInInputProvider(), new StandardOutputProvider());
    }

    public CommandReader(InputProvider inputProvider) {
        this(inputProvider, new StandardOutputProvider());
    }

    public CommandReader(InputProvider inputProvider, OutputProvider output) {
        this.inputProvider = inputProvider;
        this.output = output;
    }

    public String readPlayerName() {
        while (true) {
            output.print("プレイヤー名を入力してください>");
            String playerName = inputProvider.nextLine();
            if (!playerName.isEmpty()) {
                return playerName;
            }
            output.println("エラー：プレイヤー名を入力してください");
        }
    }

    public String readCommand() {
        while (true) {
            output.print("コマンド？>");
            String command = inputProvider.nextLine();
            if (isValidCommand(command)) {
                return command;
            }
        }
    }

    public int[] parseCommand(String command) {
        int beforeIndex = parseIndex(command.charAt(0));
        int afterIndex = parseIndex(command.charAt(1));
        return new int[] { beforeIndex, afterIndex };
    }

    private boolean isValidCommand(String command) {
        if (command == null) {
            output.println("2文字で入力して下さい。");
            return false;
        }

        if (command.length() != 2) {
            output.println("2文字で入力して下さい。");
            return false;
        }

        int beforeIndex = parseIndex(command.charAt(0));
        int afterIndex = parseIndex(command.charAt(1));
        if (beforeIndex < 0 || afterIndex < 0) {
            output.println("A~Nの範囲で入力してください");
            return false;
        }

        if (beforeIndex >= GameData.GEMS_LENGTH || afterIndex >= GameData.GEMS_LENGTH) {
            output.println("A~Nの範囲で入力してください");
            return false;
        }

        if (beforeIndex == afterIndex) {
            output.println("1文字目と2文字目が同じ値です");
            return false;
        }

        return true;
    }

    private int parseIndex(char character) {
        char upper = Character.toUpperCase(character);
        if (upper >= 'A' && upper <= 'N') {
            return upper - 'A';
        }
        if (upper >= '0' && upper <= '9') {
            return upper - '0';
        }
        return -1;
    }
}
