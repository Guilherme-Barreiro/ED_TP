package Trabalho.Players;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;
import Trabalho.Events.EventLogEntry;
import Trabalho.Map.Room;

import java.util.Iterator;

public class PlayerStats {
    private final UnorderedListADT<Room> path;
    private int correctRiddles;
    private int wrongRiddles;
    private final UnorderedListADT<EventLogEntry> events;

    public PlayerStats() {
        this.path = new ArrayUnorderedList<Room>();
        this.events = new ArrayUnorderedList<EventLogEntry>();
        this.correctRiddles = 0;
        this.wrongRiddles = 0;
    }

    public void addRoom(Room room) {
        if (room != null) {
            path.addToRear(room);
        }
    }

    public void addEvent(EventLogEntry event) {
        if (event != null) {
            events.addToRear(event);
        }
    }

    public void incCorrectRiddles() {
        correctRiddles++;
    }

    public void incWrongRiddles() {
        wrongRiddles++;
    }

    public int getCorrectRiddles() {
        return correctRiddles;
    }

    public int getWrongRiddles() {
        return wrongRiddles;
    }

    public UnorderedListADT<Room> getPath() {
        return path;
    }

    public UnorderedListADT<EventLogEntry> getEvents() {
        return events;
    }

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
        sb.append("]}");
        return sb.toString();
    }
}
