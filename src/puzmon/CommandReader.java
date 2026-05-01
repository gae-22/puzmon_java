package puzmon;

import puzmon.input.InputProvider;
import puzmon.input.SystemInInputProvider;
import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

/**
 * プレイヤーのコマンド入力を管理するクラス。
 * プレイヤー名の入力、ジェム移動コマンドの入力と検証を行う。
 */
public class CommandReader {
    private static final String PLAYER_NAME_PROMPT = "プレイヤー名を入力してください>";
    private static final String COMMAND_PROMPT = "コマンド？>";
    private static final String ERROR_PLAYER_NAME = "エラー：プレイヤー名を入力してください";
    private static final String ERROR_TWO_CHAR = "2文字で入力して下さい。";
    private static final String ERROR_RANGE = "A~Nの範囲で入力してください";
    private static final String ERROR_SAME_CHAR = "1文字目と2文字目が同じ値です";

    private final InputProvider inputProvider;
    private final OutputProvider output;

    /**
     * コンストラクタ。デフォルトの入出力プロバイダを使用。
     */
    public CommandReader() {
        this(new SystemInInputProvider(), new StandardOutputProvider());
    }

    /**
     * コンストラクタ。入力プロバイダを指定。
     *
     * @param inputProvider 入力プロバイダ
     */
    public CommandReader(InputProvider inputProvider) {
        this(inputProvider, new StandardOutputProvider());
    }

    /**
     * コンストラクタ。入出力プロバイダを指定。
     *
     * @param inputProvider 入力プロバイダ
     * @param output 出力プロバイダ
     */
    public CommandReader(InputProvider inputProvider, OutputProvider output) {
        this.inputProvider = inputProvider;
        this.output = output;
    }

    /**
     * プレイヤー名を入力させる。
     * 空文字列以外が入力されるまでループする。
     *
     * @return 入力されたプレイヤー名
     */
    public String readPlayerName() {
        while (true) {
            output.print(PLAYER_NAME_PROMPT);
            String playerName = inputProvider.nextLine();
            if (!playerName.isEmpty()) {
                return playerName;
            }
            output.println(ERROR_PLAYER_NAME);
        }
    }

    /**
     * ジェム移動コマンドを入力させる。
     * 有効なコマンドが入力されるまでループする。
     *
     * @return 入力されたコマンド文字列
     */
    public String readCommand() {
        while (true) {
            output.print(COMMAND_PROMPT);
            String command = inputProvider.nextLine();
            if (isValidCommand(command)) {
                return command;
            }
        }
    }

    /**
     * コマンド文字列をパースして移動前後のジェムインデックスを取得する。
     *
     * @param command 2文字のコマンド文字列
     * @return [移動前のインデックス, 移動後のインデックス]
     */
    public int[] parseCommand(String command) {
        int beforeIndex = parseIndex(command.charAt(0));
        int afterIndex = parseIndex(command.charAt(1));
        return new int[] { beforeIndex, afterIndex };
    }

    /**
     * コマンドの妥当性を検証する。
     * 2文字、有効な範囲、異なる位置かどうかを確認。
     *
     * @param command 検証するコマンド文字列
     * @return 有効な場合はtrue
     */
    private boolean isValidCommand(String command) {
        if (command == null) {
            output.println(ERROR_TWO_CHAR);
            return false;
        }

        if (command.length() != 2) {
            output.println(ERROR_TWO_CHAR);
            return false;
        }

        int beforeIndex = parseIndex(command.charAt(0));
        int afterIndex = parseIndex(command.charAt(1));
        if (beforeIndex < 0 || afterIndex < 0) {
            output.println(ERROR_RANGE);
            return false;
        }

        if (beforeIndex >= GameData.GEMS_LENGTH || afterIndex >= GameData.GEMS_LENGTH) {
            output.println(ERROR_RANGE);
            return false;
        }

        if (beforeIndex == afterIndex) {
            output.println(ERROR_SAME_CHAR);
            return false;
        }

        return true;
    }

    /**
    * 文字をジェムインデックスに変換する。
    * A-Nの文字を受け付ける。
     *
     * @param character 変換する文字
     * @return インデックス値、無効な文字の場合は-1
     */
    private int parseIndex(char character) {
        char upper = Character.toUpperCase(character);
        if (upper >= 'A' && upper <= 'N') {
            return upper - 'A';
        }
        return -1;
    }
}
