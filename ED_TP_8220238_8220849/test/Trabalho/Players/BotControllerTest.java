package Trabalho.Players;

import Trabalho.Game.*;
import Trabalho.Events.QuestionPool;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotControllerTest {

    @Test
    void botFicaNaMesmaSalaSeNaoHouverVizinhos() {
        Labyrinth lab = new Labyrinth();
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        lab.addRoom(entry);

        QuestionPool pool = new QuestionPool();
        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        Player bot = new Player("Bot", entry, new BotController());
        players.addToRear(bot);

        GameState state = new GameState(lab, players, pool, GameMode.AUTOMATIC, Difficulty.NORMAL);

        Room chosen = bot.getController().chooseMove(bot, lab, state);

        assertEquals(entry, chosen,
                "Se não há vizinhos, o bot deve ficar na sala atual.");
    }

    @Test
    void botMoveseParaUmVizinhoQuandoExistemOpcaoes() {
        Labyrinth lab = new Labyrinth();
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Room r2 = new Room(2, "R2", RoomType.NORMAL);
        lab.addRoom(entry);
        lab.addRoom(r2);
        lab.addCorridor(entry, r2, 1.0, false);

        QuestionPool pool = new QuestionPool();
        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        Player bot = new Player("Bot", entry, new BotController());
        players.addToRear(bot);

        GameState state = new GameState(lab, players, pool, GameMode.AUTOMATIC, Difficulty.NORMAL);

        Room chosen = bot.getController().chooseMove(bot, lab, state);

        assertNotNull(chosen);
        assertTrue(chosen == entry || chosen == r2,
                "O bot só deve poder escolher entre a sala atual e as vizinhas acessíveis.");
    }
}
