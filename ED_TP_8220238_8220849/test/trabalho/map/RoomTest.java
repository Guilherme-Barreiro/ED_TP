package trabalho.map;

import trabalho.events.Lever;
import trabalho.events.LeverPuzzle;
import trabalho.events.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void setLeverDeveRemoverRiddle() {
        Room room = new Room(1, "Sala", RoomType.NORMAL);
        Question q = new Question("Pergunta?", new String[]{"A", "B"}, 0);
        room.setRiddle(q);

        assertTrue(room.hasRiddle());
        assertFalse(room.hasLever());

        Lever lever = new Lever(new LeverPuzzle(0));
        room.setLever(lever);

        assertTrue(room.hasLever());
        assertNotNull(room.getLever());
        assertFalse(room.hasRiddle());
        assertNull(room.getRiddle());
    }

    @Test
    void setRiddleDeveRemoverLever() {
        Room room = new Room(1, "Sala", RoomType.NORMAL);
        Lever lever = new Lever(new LeverPuzzle(0));
        room.setLever(lever);

        assertTrue(room.hasLever());
        assertFalse(room.hasRiddle());

        Question q = new Question("Pergunta?", new String[]{"A", "B"}, 0);
        room.setRiddle(q);

        assertTrue(room.hasRiddle());
        assertNotNull(room.getRiddle());
        assertFalse(room.hasLever());
        assertNull(room.getLever());
    }

    @Test
    void setHasLeverFalseDeveLimparLever() {
        Room room = new Room(1, "Sala", RoomType.NORMAL);
        Lever lever = new Lever(new LeverPuzzle(0));
        room.setLever(lever);

        assertTrue(room.hasLever());
        assertNotNull(room.getLever());

        room.setHasLever(false);

        assertFalse(room.hasLever());
        assertNull(room.getLever());
    }

    @Test
    void setHasRiddleFalseDeveLimparRiddle() {
        Room room = new Room(1, "Sala", RoomType.NORMAL);
        Question q = new Question("Pergunta?", new String[]{"A", "B"}, 0);
        room.setRiddle(q);

        assertTrue(room.hasRiddle());
        assertNotNull(room.getRiddle());

        room.setHasRiddle(false);

        assertFalse(room.hasRiddle());
        assertNull(room.getRiddle());
    }

    @Test
    void equalsBaseadoEmId() {
        Room r1 = new Room(1, "A", RoomType.NORMAL);
        Room r2 = new Room(1, "B", RoomType.ENTRY);
        Room r3 = new Room(2, "C", RoomType.NORMAL);

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }
}
