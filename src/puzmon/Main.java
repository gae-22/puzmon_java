package puzmon;

import puzmon.input.SystemInInputProvider;
import puzmon.output.OutputProvider;
import puzmon.output.StandardOutputProvider;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        OutputProvider output = new StandardOutputProvider();
        Display.setOutputProvider(output);

        CommandReader commandReader = new CommandReader(new SystemInInputProvider(), output);

        String playerName = commandReader.readPlayerName();
        output.println("*** Puzzle & Monsters ***");

        Party party = new Party(playerName, Arrays.asList(GameData.createPlayers()));
        Display.showPartyInfo(party);

        Monster[] enemies = GameData.createEnemies();
        int winCount = 0;

        for (Monster enemy : enemies) {
            BattleManager battleManager = new BattleManager(party, enemy, commandReader, output);
            boolean won = battleManager.battle();
            if (won) {
                winCount++;
            }

            if (party.isDefeated()) {
                output.println(party.getName() + "はダンジョンから逃げ出した");
                break;
            }

            if (winCount < enemies.length) {
                output.println(party.getName() + "はさらに奥へと進んだ");
                output.println("=".repeat(GameData.LINE_LENGTH));
            }
        }

        if (winCount >= enemies.length) {
            output.println("*** GAME CLEARED!! ***");
        } else {
            output.println("*** GAME OVER!! ***");
        }

        output.printf("倒したモンスター数=%d%n", winCount);
    }
}
