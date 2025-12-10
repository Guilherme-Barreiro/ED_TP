package trabalho.map;

import trabalho.events.Event;

/**
 * Representa um corredor (ligação) entre duas salas do labirinto.
 * <p>
 * Cada corredor tem:
 * <ul>
 *     <li>uma sala de origem ({@link #from}) e de destino ({@link #to});</li>
 *     <li>um peso ({@link #weight}), que pode representar risco ou dificuldade;</li>
 *     <li>um evento associado ({@link #event}), que pode ser {@code null};</li>
 *     <li>um estado de bloqueio ({@link #locked}).</li>
 * </ul>
 */
public class Corridor {

    private Room from;
    private Room to;
    private double weight;
    private Event event;
    private boolean locked;

    /**
     * Cria um corredor entre duas salas com o peso, evento e estado de bloqueio dados.
     *
     * @param from   sala de origem
     * @param to     sala de destino
     * @param weight peso do corredor
     * @param event  evento inicial associado (pode ser {@code null})
     * @param locked {@code true} se o corredor começar bloqueado
     */
    public Corridor(Room from, Room to, double weight, Event event, boolean locked) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.event = event;
        this.locked = locked;
    }

    /**
     * Devolve a sala de origem do corredor.
     *
     * @return sala de origem
     */
    public Room getFrom() {
        return from;
    }

    /**
     * Devolve a sala de destino do corredor.
     *
     * @return sala de destino
     */
    public Room getTo() {
        return to;
    }

    /**
     * Devolve o peso do corredor.
     *
     * @return peso
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Devolve o evento associado ao corredor.
     *
     * @return evento ou {@code null} se não existir
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Atualiza o evento associado ao corredor.
     *
     * @param event novo evento (pode ser {@code null})
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Indica se o corredor está bloqueado.
     *
     * @return {@code true} se estiver bloqueado, {@code false} caso contrário
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Desbloqueia o corredor.
     */
    public void unlock() {
        this.locked = false;
    }

    /**
     * Dado um lado do corredor, devolve o outro lado.
     * <p>
     * Se o argumento não corresponder nem a {@link #from} nem a {@link #to},
     * devolve {@code null}.
     *
     * @param room sala de referência
     * @return a outra sala do corredor ou {@code null} se não pertencer a este corredor
     */
    public Room getOtherSide(Room room) {
        if (room == from) return to;
        if (room == to) return from;
        return null;
    }

    /**
     * Representação textual do corredor, com IDs das salas, peso, estado
     * de bloqueio e evento.
     *
     * @return string descritiva do corredor
     */
    @Override
    public String toString() {
        return "Corridor{" +
                "from=" + (from != null ? from.getId() : "null") +
                ", to=" + (to != null ? to.getId() : "null") +
                ", weight=" + weight +
                ", locked=" + locked +
                ", event=" + event +
                '}';
    }
}
