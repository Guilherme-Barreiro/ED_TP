package Trabalho.Map;

import Colecoes.Estruturas.ArrayUnorderedList;
import Colecoes.Estruturas.Network;
import Trabalho.Events.Event;
import Trabalho.Events.EventFactory;

import java.util.Iterator;

public class Labyrinth {
    private Network<Room> map;
    private ArrayUnorderedList<Room> rooms;
    private ArrayUnorderedList<Room> entryRooms;
    private ArrayUnorderedList<Corridor> corridors;
    private Room centerRoom;

    public Labyrinth() {
        this.map = new Network<>();
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

    public ArrayUnorderedList<Room> getRooms() {
        return rooms;
    }

    public ArrayUnorderedList<Room> getEntryRooms() {
        return entryRooms;
    }

    public Room getCenterRoom() {
        return centerRoom;
    }

    /**
     * Cria um corredor entre a e b com o peso dado.
     * Gera um Event com base no peso (ou null) através do EventFactory.
     */
    public void addCorridor(Room a, Room b, double weight, boolean locked) {
        if (a == null || b == null) return;

        map.addEdge(a, b, weight);

        Event e = EventFactory.maybeCreateEvent(weight);

        Corridor c = new Corridor(a, b, weight, e, locked);
        corridors.addToRear(c);
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
}
