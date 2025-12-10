package trabalho.map;

import trabalho.events.Lever;
import trabalho.events.Question;

import java.util.Objects;

/**
 * Representa uma sala do labirinto.
 * <p>
 * Cada sala tem:
 * <ul>
 *     <li>um ID único ({@link #id});</li>
 *     <li>um nome ({@link #name});</li>
 *     <li>um tipo ({@link #type});</li>
 *     <li>opcionalmente uma alavanca ({@link #lever}) ou um enigma ({@link #riddle}), mas nunca ambos em simultâneo.</li>
 * </ul>
 */
public class Room {
    private int id;
    private String name;
    private RoomType type;

    private Lever lever;
    private boolean hasLever;

    private Question riddle;
    private boolean hasRiddle;

    /**
     * Cria uma sala com o ID, nome e tipo indicados, sem alavanca nem enigma.
     *
     * @param id   identificador único da sala
     * @param name nome da sala
     * @param type tipo de sala
     */
    public Room(int id, String name, RoomType type) {
        this.id = id;
        this.name = name;
        this.type = type;

        this.lever = null;
        this.hasLever = false;

        this.riddle = null;
        this.hasRiddle = false;
    }

    /**
     * Devolve o ID da sala.
     *
     * @return ID da sala
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o nome da sala.
     *
     * @return nome da sala
     */
    public String getName() {
        return name;
    }

    /**
     * Devolve o tipo da sala.
     *
     * @return tipo da sala
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Devolve a alavanca associada à sala (se existir).
     *
     * @return alavanca ou {@code null} se não existir
     */
    public Lever getLever() {
        return lever;
    }

    /**
     * Associa uma alavanca à sala.
     * <p>
     * Se {@code lever} for não nula, remove automaticamente qualquer enigma
     * existente, garantindo que não há alavanca e enigma ao mesmo tempo.
     *
     * @param lever nova alavanca (pode ser {@code null})
     */
    public void setLever(Lever lever) {
        this.lever = lever;
        this.hasLever = (lever != null);

        if (lever != null) {
            this.riddle = null;
            this.hasRiddle = false;
        }
    }

    /**
     * Indica se a sala tem uma alavanca.
     *
     * @return {@code true} se tiver alavanca, {@code false} caso contrário
     */
    public boolean hasLever() {
        return hasLever;
    }

    /**
     * Atualiza o indicador de existência de alavanca.
     * <p>
     * Se passar para {@code false}, remove também a instância de {@link #lever}.
     *
     * @param hasLever novo valor para o indicador
     */
    public void setHasLever(boolean hasLever) {
        this.hasLever = hasLever;
        if (!hasLever) {
            this.lever = null;
        }
    }

    /**
     * Devolve o enigma associado à sala (se existir).
     *
     * @return enigma ou {@code null} se não existir
     */
    public Question getRiddle() {
        return riddle;
    }

    /**
     * Associa um enigma à sala.
     * <p>
     * Se {@code riddle} for não nulo, remove automaticamente qualquer alavanca
     * existente, garantindo que não há alavanca e enigma ao mesmo tempo.
     *
     * @param riddle novo enigma (pode ser {@code null})
     */
    public void setRiddle(Question riddle) {
        this.riddle = riddle;
        this.hasRiddle = (riddle != null);

        if (riddle != null) {
            this.lever = null;
            this.hasLever = false;
        }
    }

    /**
     * Indica se a sala tem um enigma ativo.
     *
     * @return {@code true} se tiver enigma, {@code false} caso contrário
     */
    public boolean hasRiddle() {
        return hasRiddle;
    }

    /**
     * Atualiza o indicador de existência de enigma.
     * <p>
     * Se passar para {@code false}, remove também a instância de {@link #riddle}.
     *
     * @param hasRiddle novo valor para o indicador
     */
    public void setHasRiddle(boolean hasRiddle) {
        this.hasRiddle = hasRiddle;
        if (!hasRiddle) {
            this.riddle = null;
        }
    }

    /**
     * Duas salas são consideradas iguais se tiverem o mesmo ID.
     *
     * @param o outro objeto
     * @return {@code true} se for uma sala com o mesmo ID, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id;
    }

    /**
     * HashCode baseado apenas no ID da sala.
     *
     * @return valor de hash
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Representação textual simples da sala (id, nome e tipo).
     *
     * @return string descritiva da sala
     */
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
