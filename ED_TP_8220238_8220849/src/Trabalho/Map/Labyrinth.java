package Trabalho.Map;

import Estruturas.ArrayUnorderedList;
import Estruturas.NetworkList;
import interfaces.NetworkADT;
import interfaces.UnorderedListADT;
import Trabalho.Events.Event;
import Trabalho.Events.EventFactory;

import java.util.Iterator;

public class Labyrinth {
    private NetworkADT<Room> map;
    private UnorderedListADT<Room> rooms;
    private UnorderedListADT<Room> entryRooms;
    private UnorderedListADT<Corridor> corridors;
    private Room centerRoom;

    public Labyrinth() {
        this.map = new NetworkList<>();
        this.rooms = new ArrayUnorderedList<>();
        this.entryRooms = new ArrayUnorderedList<>();
        this.corridors = new ArrayUnorderedList<>();
        this.centerRoom = null;
    }

    public void addRoom(Room room) {
        if (room == null) return;

        rooms.addToRear(room);
        map.addVertex(room);

        if (room.getType() == RoomType.ENTRY) {
            entryRooms.addToRear(room);
        } else if (room.getType() == RoomType.CENTER) {
            centerRoom = room;
        }
    }

    public UnorderedListADT<Room> getRooms() {
        return rooms;
    }

    public UnorderedListADT<Room> getEntryRooms() {
        return entryRooms;
    }

    public Room getCenterRoom() {
        return centerRoom;
    }

    /**
     * Cria um corredor entre a e b com o peso dado.
     * Gera um Event com base no peso (ou null) através do EventFactory.
     */
    public boolean addCorridor(Room a, Room b, double weight, boolean locked) {
        if (a == null || b == null) return false;

        if (corridorExists(a, b)) {
            return false;
        }

        map.addEdge(a, b, weight);

        Event e = null;

        Corridor c = new Corridor(a, b, weight, e, locked);
        corridors.addToRear(c);
        return true;
    }

    private boolean corridorExists(Room a, Room b) {
        Iterator<Corridor> it = corridors.iterator();

        while (it.hasNext()) {
            Corridor c = it.next();

            Room r1 = c.getFrom();
            Room r2 = c.getTo();

            boolean sameDirection =
                    r1.equals(a) && r2.equals(b);
            boolean oppositeDirection =
                    r1.equals(b) && r2.equals(a);

            if (sameDirection || oppositeDirection) {
                return true;
            }
        }

        return false;
    }

    /**
     * Devolve uma lista das salas vizinhas acessíveis a partir de 'room'
     * (ignorando corredores bloqueados).
     */
    public ArrayUnorderedList<Room> getNeighbors(Room room) {
        ArrayUnorderedList<Room> neighbors = new ArrayUnorderedList<>();
        if (room == null) return neighbors;

        Iterator<Corridor> it = corridors.iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            if (c.isLocked()) continue;

            Room other = c.getOtherSide(room);
            if (other != null) {
                neighbors.addToRear(other);
            }
        }

        return neighbors;
    }

    /**
     * Devolve o corredor entre duas salas, se existir.
     */
    public Corridor getCorridor(Room a, Room b) {
        if (a == null || b == null) return null;

        Iterator<Corridor> it = corridors.iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            Room from = c.getFrom();
            Room to = c.getTo();

            if ((from == a && to == b) || (from == b && to == a)) {
                return c;
            }
        }
        return null;
    }

    /**
     * desbloquear todos os corredores ligados a uma certa sala.
     */
    public void unlockCorridorsFrom(Room room) {
        if (room == null) return;

        Iterator<Corridor> it = corridors.iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            if (c.getFrom() == room || c.getTo() == room) {
                c.unlock();
            }
        }
    }

    public UnorderedListADT<Corridor> getCorridors() {
        return corridors;
    }

    public int getEntryRoomCount() {
        int count = 0;
        Iterator<Room> it = entryRooms.iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * Procura uma sala pelo id, percorrendo a lista de rooms do Labyrinth.
     */
    public Room findRoomById(int id) {
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.getId() == id) {
                return r;
            }
        }
        throw new IllegalArgumentException("Não foi encontrada sala com id " + id + " no labirinto.");
    }

}
