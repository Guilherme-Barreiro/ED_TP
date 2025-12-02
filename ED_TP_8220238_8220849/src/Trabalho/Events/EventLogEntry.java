package Trabalho.Events;

import Trabalho.Players.Player;

public class EventLogEntry {

    private Player source;
    private Player target;
    private EventType type;
    private String description;
    private int turnNumber;

    public EventLogEntry(Player source, Player target, EventType type, String description, int turnNumber) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.description = description;
        this.turnNumber = turnNumber;
    }

    public Player getSource() {
        return source;
    }

    public Player getTarget() {
        return target;
    }

    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

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
