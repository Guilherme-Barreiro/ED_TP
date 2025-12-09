package Trabalho.Map;

import Estruturas.ArrayUnorderedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthTest {

    @Test
    void addRoomAtualizaEntryECenter() {
        Labyrinth lab = new Labyrinth();
        Room r1 = new Room(1, "Entrada", RoomType.ENTRY);
        Room r2 = new Room(2, "Normal", RoomType.NORMAL);
        Room r3 = new Room(3, "Centro", RoomType.CENTER);

        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);

        assertEquals(r3, lab.getCenterRoom());
        assertEquals(1, lab.getEntryRoomCount());
    }

    @Test
    void addCorridorNaoPermiteDuplicadosMesmoAoContrario() {
        Labyrinth lab = new Labyrinth();
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        lab.addRoom(a);
        lab.addRoom(b);

        assertTrue(lab.addCorridor(a, b, 1.0, false));
        assertFalse(lab.addCorridor(a, b, 1.0, false));
        assertFalse(lab.addCorridor(b, a, 1.0, false));
    }

    @Test
    void getCorridorFuncionaNosDoisSentidos() {
        Labyrinth lab = new Labyrinth();
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        lab.addRoom(a);
        lab.addRoom(b);
        lab.addCorridor(a, b, 1.0, false);

        assertNotNull(lab.getCorridor(a, b));
        assertNotNull(lab.getCorridor(b, a));
    }

    @Test
    void getNeighborsIgnoraCorredoresBloqueados() {
        Labyrinth lab = new Labyrinth();
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        Room c = new Room(3, "C", RoomType.NORMAL);
        lab.addRoom(a);
        lab.addRoom(b);
        lab.addRoom(c);

        lab.addCorridor(a, b, 1.0, false);
        lab.addCorridor(a, c, 1.0, true);

        ArrayUnorderedList<Room> neighbors = lab.getNeighbors(a);

        assertEquals(1, neighbors.size(), "Só deve ter 1 vizinho acessível");
        Room found = null;
        var it = neighbors.iterator();
        while (it.hasNext()) {
            found = it.next();
        }
        assertEquals(b, found);
    }

    @Test
    void unlockCorridorsFromDesbloqueiaTodosLigados() {
        Labyrinth lab = new Labyrinth();
        Room a = new Room(1, "A", RoomType.NORMAL);
        Room b = new Room(2, "B", RoomType.NORMAL);
        Room c = new Room(3, "C", RoomType.NORMAL);
        lab.addRoom(a);
        lab.addRoom(b);
        lab.addRoom(c);

        lab.addCorridor(a, b, 1.0, true);
        lab.addCorridor(b, c, 1.0, true);

        lab.unlockCorridorsFrom(b);

        assertFalse(lab.getCorridor(a, b).isLocked());
        assertFalse(lab.getCorridor(b, c).isLocked());
    }

    @Test
    void findRoomByIdFuncionaOuLancaExcecao() {
        Labyrinth lab = new Labyrinth();
        Room a = new Room(1, "A", RoomType.NORMAL);
        lab.addRoom(a);

        assertEquals(a, lab.findRoomById(1));
        assertThrows(IllegalArgumentException.class, () -> lab.findRoomById(99));
    }
}
