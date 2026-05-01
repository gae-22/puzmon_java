package puzmon;

import puzmon.input.SystemInInputProvider;
import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;

/**
 * ゲーム「Puzzle & Monsters」のメインクラス。
 * ゲームの初期化と進行を管理する。
 */
public class Main {
    /**
     * ゲームのメインメソッド。
     * プレイヤーの入力を受け付け、複数の敵とのバトルを進行させる。
     *
     * @param args コマンドライン引数（使用されない）
     */
    public static void main(String[] args) {
        OutputProvider output = new StandardOutputProvider();
        Display display = new Display(output);

        CommandReader commandReader = new CommandReader(new SystemInInputProvider(), output);

        String playerName = commandReader.readPlayerName();
        output.println("*** Puzzle & Monsters ***");

        Party party = GameData.createParty(playerName);
        display.showPartyInfo(party);

        Dungeon dungeon = GameData.createDungeon();

        int winCount = 0;

        while (dungeon.getCurrentFloorEnemy() != null) {
            Monster enemy = dungeon.getCurrentFloorEnemy();
            BattleManager battleManager = new BattleManager(party, enemy, commandReader, display, output);
            boolean won = battleManager.battle();
            if (won) {
                winCount++;
            }

            if (party.isDefeated()) {
                output.println(party.getName() + "はダンジョンから逃げ出した");
                break;
            }

            if (dungeon.advanceFloor()) {
                output.println(party.getName() + "はさらに奥へと進んだ");
                output.println("=".repeat(GameData.LINE_LENGTH));
            } else {
                break;
            }
        }

        if (winCount >= dungeon.getTotalFloors()) {
            output.println("*** GAME CLEARED!! ***");
        } else {
            output.println("*** GAME OVER!! ***");
        }

        output.printf("倒したモンスター数=%d%n", winCount);
    }
}
