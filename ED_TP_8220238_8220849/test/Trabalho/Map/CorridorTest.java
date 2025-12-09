package Trabalho.Map;

import Trabalho.Events.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorridorTest {

    @Test
    void getOtherSideFuncionaParaAmbosOsLados() {
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        Corridor c = new Corridor(a, b, 1.0, (Event) null, false);

        assertEquals(b, c.getOtherSide(a), "Deve devolver a outra extremidade do corredor");
        assertEquals(a, c.getOtherSide(b), "Deve devolver a outra extremidade do corredor");
    }

    @Test
    void getOtherSideDevolveNullSeSalaNaoPertence() {
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        Room cRoom = new Room(3, "C", RoomType.NORMAL);
        Corridor c = new Corridor(a, b, 1.0, (Event) null, false);

        assertNull(c.getOtherSide(cRoom), "Se a sala não pertence ao corredor, deve devolver null");
    }

    @Test
    void unlockDesbloqueiaCorredor() {
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        Corridor c = new Corridor(a, b, 1.0, (Event) null, true);

        assertTrue(c.isLocked(), "Corredor começa bloqueado");
        c.unlock();
        assertFalse(c.isLocked(), "Depois de unlock(), já não deve estar bloqueado");
    }
}
