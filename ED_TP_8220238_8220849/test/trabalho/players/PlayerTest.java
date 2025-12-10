package trabalho.players;

import trabalho.map.Room;
import trabalho.map.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void construtorSoAceitaSalaInicialEntry() {
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Room normal = new Room(2, "Normal", RoomType.NORMAL);

        assertDoesNotThrow(() -> new Player("Ok", entry, new BotController()));
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Falha", normal, new BotController()));
    }

    @Test
    void setCurrentRoomAtualizaCurrentRoomEStats() {
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Player p = new Player("Jogador", entry, new BotController());

        Room r2 = new Room(2, "Sala 2", RoomType.NORMAL);
        p.setCurrentRoom(r2);

        assertEquals(r2, p.getCurrentRoom());
        assertEquals(2, p.getStats().getPath().size());
    }

    @Test
    void moveBackRecuaQuandoNaoHaSwapBoundary() {
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Room r2 = new Room(2, "Sala 2", RoomType.NORMAL);
        Room r3 = new Room(3, "Sala 3", RoomType.NORMAL);

        Player p = new Player("Jogador", entry, new BotController());
        p.setCurrentRoom(r2);
        p.setCurrentRoom(r3);

        p.moveBack(2);

        assertEquals(entry, p.getCurrentRoom());
    }

    @Test
    void moveBackNaoPodePassarLimiteDepoisDeSwapBoundary() {
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Room r2 = new Room(2, "Sala 2", RoomType.NORMAL);
        Room r3 = new Room(3, "Sala 3", RoomType.NORMAL);

        Player p = new Player("Jogador", entry, new BotController());
        p.setCurrentRoom(r2);
        p.setCurrentRoom(r3);

        p.markSwapBoundary();

        p.moveBack(5);

        assertEquals(r3, p.getCurrentRoom());
    }

    @Test
    void decrementBlockedTurnsNaoDesceAbaixoDeZero() {
        Room entry = new Room(1, "Entrada", RoomType.ENTRY);
        Player p = new Player("Jogador", entry, new BotController());

        p.setBlockedTurns(1);
        p.decrementBlockedTurns();
        assertEquals(0, p.getBlockedTurns());
        p.decrementBlockedTurns();
        assertEquals(0, p.getBlockedTurns());
    }
}
