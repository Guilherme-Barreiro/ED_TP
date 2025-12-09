package Trabalho.Map;

import Estruturas.ArrayUnorderedList;
import Estruturas.NetworkList;
import interfaces.NetworkADT;
import interfaces.UnorderedListADT;
import Trabalho.Events.Event;
import Trabalho.Events.EventFactory;

import java.util.Iterator;

/**
 * Representa o labirinto do jogo, composto por salas e corredores.
 * <p>
 * Internamente mantém:
 * <ul>
 *     <li>um grafo de salas ({@link #map});</li>
 *     <li>uma lista de todas as salas ({@link #rooms});</li>
 *     <li>uma lista de salas de entrada ({@link #entryRooms});</li>
 *     <li>uma lista de corredores ({@link #corridors});</li>
 *     <li>a sala central ({@link #centerRoom}).</li>
 * </ul>
 */
public class Labyrinth {
    private NetworkADT<Room> map;
    private UnorderedListADT<Room> rooms;
    private UnorderedListADT<Room> entryRooms;
    private UnorderedListADT<Corridor> corridors;
    private Room centerRoom;

    /**
     * Cria um labirinto vazio, sem salas nem corredores.
     */
    public Labyrinth() {
        this.map = new NetworkList<>();
        this.rooms = new ArrayUnorderedList<>();
        this.entryRooms = new ArrayUnorderedList<>();
        this.corridors = new ArrayUnorderedList<>();
        this.centerRoom = null;
    }

    /**
     * Adiciona uma sala ao labirinto e ao grafo interno.
     * <p>
     * Se a sala for do tipo ENTRY, é também guardada em {@link #entryRooms}.
     * Se for do tipo CENTER, atualiza {@link #centerRoom}.
     *
     * @param room sala a adicionar (ignorada se for {@code null})
     */
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

    /**
     * Devolve a lista de todas as salas do labirinto.
     *
     * @return lista de salas
     */
    public UnorderedListADT<Room> getRooms() {
        return rooms;
    }

    /**
     * Devolve a lista de salas de entrada (ENTRY).
     *
     * @return lista de salas de entrada
     */
    public UnorderedListADT<Room> getEntryRooms() {
        return entryRooms;
    }

    /**
     * Devolve a sala central (CENTER).
     *
     * @return sala central ou {@code null} se ainda não existir
     */
    public Room getCenterRoom() {
        return centerRoom;
    }

    /**
     * Cria um corredor entre duas salas com o peso e estado de bloqueio dados.
     * <p>
     * Não permite corredores duplicados (em nenhuma direção).
     *
     * @param a      sala A
     * @param b      sala B
     * @param weight peso do corredor
     * @param locked {@code true} se o corredor começar bloqueado
     * @return {@code true} se o corredor foi criado, {@code false} se já existia
     * ou se algum dos parâmetros era {@code null}
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

    /**
     * Verifica se já existe um corredor entre duas salas
     * (em qualquer direção).
     *
     * @param a sala A
     * @param b sala B
     * @return {@code true} se o corredor existir, {@code false} caso contrário
     */
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
     * Devolve uma lista das salas vizinhas acessíveis a partir de {@code room},
     * ignorando corredores bloqueados.
     *
     * @param room sala de referência
     * @return lista de vizinhos acessíveis (pode estar vazia, mas nunca é {@code null})
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
     * Devolve o corredor entre duas salas, se existir (em qualquer direção).
     *
     * @param a sala A
     * @param b sala B
     * @return corredor correspondente ou {@code null} se não existir
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
     * Desbloqueia todos os corredores diretamente ligados a uma determinada sala.
     *
     * @param room sala cujos corredores devem ser desbloqueados
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

    /**
     * Devolve a lista de todos os corredores do labirinto.
     *
     * @return lista de corredores
     */
    public UnorderedListADT<Corridor> getCorridors() {
        return corridors;
    }

    /**
     * Devolve o número de salas de entrada (ENTRY) no labirinto.
     *
     * @return quantidade de salas ENTRY
     */
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
     * Procura uma sala pelo ID, percorrendo a lista de salas.
     *
     * @param id identificador da sala
     * @return sala com o ID indicado
     * @throws IllegalArgumentException se não existir nenhuma sala com esse ID
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
