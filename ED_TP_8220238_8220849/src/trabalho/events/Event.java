package trabalho.events;

/**
 * Representa um evento aleatório que pode ocorrer num corredor do labirinto.
 * <p>
 * Cada evento tem:
 * <ul>
 *     <li>um {@link EventType tipo}, que define o efeito (ex.: avançar, recuar, trocar posições, perder turno, etc.);</li>
 *     <li>uma intensidade, que indica a “força” ou impacto desse efeito
 *     (por exemplo, número de casas, número de turnos, etc.).</li>
 * </ul>
 * Os eventos são normalmente criados pela {@link EventFactory} quando um jogador atravessa um corredor.
 */
public class Event {
    private EventType type;
    private int intensity;

    /**
     * Cria um novo evento com o tipo e a intensidade indicados.
     *
     * @param type      tipo do evento
     * @param intensity intensidade do evento
     */
    public Event(EventType type, int intensity) {
        this.type = type;
        this.intensity = intensity;
    }

    /**
     * Devolve o tipo deste evento.
     *
     * @return tipo do evento
     */
    public EventType getType() {
        return type;
    }

    /**
     * Atualiza o tipo deste evento.
     *
     * @param type novo tipo do evento
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * Devolve a intensidade deste evento.
     *
     * @return intensidade do evento
     */
    public int getIntensity() {
        return intensity;
    }

    /**
     * Atualiza a intensidade deste evento.
     *
     * @param intensity nova intensidade do evento
     */
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    /**
     * Devolve uma representação textual do evento,
     * útil para debug e registo em logs.
     *
     * @return string com o tipo e a intensidade do evento
     */
    @Override
    public String toString() {
        return "Event{" + "type=" + type + ", intensity=" + intensity + '}';
    }
}
