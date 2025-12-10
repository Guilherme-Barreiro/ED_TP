package Trabalho.IO;

import Estruturas.ArrayUnorderedList;
import Trabalho.Events.QuestionPool;
import Trabalho.Game.*;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Trabalho.Players.BotController;
import Trabalho.Players.Player;
import interfaces.UnorderedListADT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameReportJsonTest {

    @Test
    void generateContemCamposBasicos() {
        Labyrinth lab = new Labyrinth();
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Room center = new Room(2, "Centro", RoomType.CENTER);
        lab.addRoom(entry);
        lab.addRoom(center);
        lab.addCorridor(entry, center, 1.0, false);

        QuestionPool pool = new QuestionPool();
        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        Player p = new Player("Bot", entry, new BotController());
        players.addToRear(p);

        GameState state = new GameState(lab, players, pool, GameMode.AUTOMATIC, Difficulty.NORMAL);

        state.nextTurn();

        String json = GameReportJson.generate(state);

        assertTrue(json.contains("\"turns\""), "JSON deve conter o campo 'turns'.");
        assertTrue(json.contains("\"winner\""), "JSON deve conter o campo 'winner'.");
        assertTrue(json.contains(p.getName()), "JSON deve conter o nome do jogador.");
    }
}
