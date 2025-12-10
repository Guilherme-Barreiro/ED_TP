package trabalho.players;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;
import trabalho.events.EventLogEntry;
import trabalho.map.Room;

import java.util.Iterator;

/**
 * Armazena estatísticas de um jogador ao longo da partida.
 * <p>
 * Guarda:
 * <ul>
 *     <li>percurso (lista de salas visitadas);</li>
 *     <li>número de enigmas certos e errados;</li>
 *     <li>lista de eventos relevantes ({@link EventLogEntry}).</li>
 * </ul>
 */
public class PlayerStats {
    private final UnorderedListADT<Room> path;
    private int correctRiddles;
    private int wrongRiddles;
    private final UnorderedListADT<EventLogEntry> events;

    /**
     * Cria um objeto de estatísticas vazio.
     */
    public PlayerStats() {
        this.path = new ArrayUnorderedList<Room>();
        this.events = new ArrayUnorderedList<EventLogEntry>();
        this.correctRiddles = 0;
        this.wrongRiddles = 0;
    }

    /**
     * Regista uma nova sala no percurso do jogador.
     *
     * @param room sala visitada
     */
    public void addRoom(Room room) {
        if (room != null) {
            path.addToRear(room);
        }
    }

    /**
     * Adiciona um evento à lista de eventos do jogador.
     *
     * @param event evento a registar
     */
    public void addEvent(EventLogEntry event) {
        if (event != null) {
            events.addToRear(event);
        }
    }

    /**
     * Incrementa o contador de enigmas bem respondidos.
     */
    public void incCorrectRiddles() {
        correctRiddles++;
    }

    /**
     * Incrementa o contador de enigmas falhados.
     */
    public void incWrongRiddles() {
        wrongRiddles++;
    }

    /**
     * Devolve o número de enigmas respondidos corretamente.
     */
    public int getCorrectRiddles() {
        return correctRiddles;
    }

    /**
     * Devolve o número de enigmas respondidos erradamente.
     */
    public int getWrongRiddles() {
        return wrongRiddles;
    }

    /**
     * Devolve o percurso (lista de salas visitadas) do jogador.
     */
    public UnorderedListADT<Room> getPath() {
        return path;
    }

    /**
     * Devolve a lista de eventos registados para este jogador.
     */
    public UnorderedListADT<EventLogEntry> getEvents() {
        return events;
    }

    /**
     * Devolve uma representação textual resumida das estatísticas:
     * número de enigmas certos/errados, percurso e eventos.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PlayerStats{");
        sb.append("correct=").append(correctRiddles)
                .append(", wrong=").append(wrongRiddles)
                .append(", path=[");

        Iterator<Room> it = path.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            sb.append(r.getId());
            if (it.hasNext()) sb.append(" -> ");
        }
        sb.append("]");

        sb.append(", events=[");

        Iterator<EventLogEntry> itEv = events.iterator();
        while (itEv.hasNext()) {
            EventLogEntry e = itEv.next();
            sb.append("{turn=")
                    .append(e.getTurnNumber())
                    .append(", type=")
                    .append(e.getType())
                    .append(", source=")
                    .append(e.getSource() != null ? e.getSource().getName() : "null")
                    .append(", target=")
                    .append(e.getTarget() != null ? e.getTarget().getName() : "null")
                    .append("}");

            if (itEv.hasNext()) sb.append(", ");
        }

        sb.append("]}");
        return sb.toString();
    }

}
