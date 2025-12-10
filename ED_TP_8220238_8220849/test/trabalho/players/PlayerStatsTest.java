package trabalho.players;

import trabalho.events.EventLogEntry;
import trabalho.events.EventType;
import trabalho.map.Room;
import trabalho.map.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsTest {

    @Test
    void addRoomAdicionaAoCaminho() {
        PlayerStats stats = new PlayerStats();
        Room r1 = new Room(1, "A", RoomType.NORMAL);
        Room r2 = new Room(2, "B", RoomType.NORMAL);

        stats.addRoom(r1);
        stats.addRoom(r2);

        assertEquals(2, stats.getPath().size());
    }

    @Test
    void addEventAdicionaAListaEventos() {
        PlayerStats stats = new PlayerStats();
        Room r = new Room(1, "A", RoomType.ENTRY);
        Player p = new Player("Jogador", r, new BotController());

        EventLogEntry ev = new EventLogEntry(p, null, EventType.EXTRA_TURN, "teste", 1);
        stats.addEvent(ev);

        assertEquals(1, stats.getEvents().size());
    }

    @Test
    void incrementosDeRiddlesFuncionam() {
        PlayerStats stats = new PlayerStats();
        stats.incCorrectRiddles();
        stats.incCorrectRiddles();
        stats.incWrongRiddles();

        assertEquals(2, stats.getCorrectRiddles());
        assertEquals(1, stats.getWrongRiddles());
    }
}
