package Trabalho.Events;

import Trabalho.Players.Player;

/**
 * Representa um registo (entrada de log) de um evento ocorrido durante o jogo.
 * <p>
 * Cada entrada guarda informação sobre:
 * <ul>
 *     <li>o jogador que originou o evento ({@link #source});</li>
 *     <li>o jogador afetado pelo evento ({@link #target}), se existir;</li>
 *     <li>o tipo de evento ({@link #type});</li>
 *     <li>uma descrição textual ({@link #description});</li>
 *     <li>o número do turno em que ocorreu ({@link #turnNumber}).</li>
 * </ul>
 * Estas entradas podem ser usadas para mostrar um histórico de eventos
 * ao jogador ou para gerar relatórios (JSON, por exemplo).
 */
public class EventLogEntry {

    private Player source;
    private Player target;
    private EventType type;
    private String description;
    private int turnNumber;

    /**
     * Cria uma nova entrada de log de evento.
     *
     * @param source      jogador que originou o evento (pode ser {@code null})
     * @param target      jogador afetado pelo evento (pode ser {@code null})
     * @param type        tipo de evento
     * @param description descrição textual do evento
     * @param turnNumber  número do turno em que o evento ocorreu
     */
    public EventLogEntry(Player source, Player target, EventType type, String description, int turnNumber) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.description = description;
        this.turnNumber = turnNumber;
    }

    /**
     * Devolve o jogador que originou o evento.
     *
     * @return jogador de origem, ou {@code null} se não existir
     */
    public Player getSource() {
        return source;
    }

    /**
     * Devolve o jogador afetado pelo evento.
     *
     * @return jogador alvo, ou {@code null} se não existir
     */
    public Player getTarget() {
        return target;
    }

    /**
     * Devolve o tipo do evento.
     *
     * @return tipo do evento
     */
    public EventType getType() {
        return type;
    }

    /**
     * Devolve a descrição textual do evento.
     *
     * @return descrição do evento
     */
    public String getDescription() {
        return description;
    }

    /**
     * Devolve o número do turno em que o evento ocorreu.
     *
     * @return número do turno
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Devolve uma representação textual da entrada de log,
     * incluindo nomes dos jogadores, tipo, turno e descrição.
     *
     * @return string com os dados principais do evento
     */
    @Override
    public String toString() {
        return "EventLogEntry{" +
                "source=" + (source != null ? source.getName() : "null") +
                ", target=" + (target != null ? target.getName() : "null") +
                ", type=" + type +
                ", turnNumber=" + turnNumber +
                ", description='" + description + '\'' +
                '}';
    }
}
