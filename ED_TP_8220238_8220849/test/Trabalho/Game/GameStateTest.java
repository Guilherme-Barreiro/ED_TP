package Trabalho.Game;

import Estruturas.ArrayUnorderedList;
import Trabalho.Events.QuestionPool;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Trabalho.Players.BotController;
import Trabalho.Players.Player;
import interfaces.UnorderedListADT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void jogoTerminaQuandoJogadorChegaAoCenter() {
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

        GameState state = new GameState(lab, players, pool, GameMode.MANUAL, Difficulty.NORMAL);

        state.nextTurn();

        assertTrue(state.isGameOver(), "O jogo devia estar terminado após chegar ao CENTER.");
        assertEquals(p, state.getWinner(), "O vencedor devia ser o único jogador.");
    }

    @Test
    void jogadorBloqueadoNaoJogaEDecrementaBlockedTurns() {
        Labyrinth lab = new Labyrinth();
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        lab.addRoom(entry);

        QuestionPool pool = new QuestionPool();

        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        Player p = new Player("Jogador", entry, new BotController());
        players.addToRear(p);

        GameState state = new GameState(lab, players, pool, GameMode.MANUAL, Difficulty.NORMAL);

        p.setBlockedTurns(2);

        int turnoInicial = state.getCurrentTurn();
        state.nextTurn();

        assertEquals(1, p.getBlockedTurns(),
                "blockedTurns devia ter sido decrementado de 2 para 1.");
        assertFalse(state.isGameOver(),
                "O jogo não devia acabar só porque o jogador está bloqueado.");

        assertEquals(turnoInicial + 1, state.getCurrentTurn(),
                "Com um único jogador, o número de turno devia ter aumentado.");
    }
}
