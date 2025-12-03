package Trabalho.Map;

import Trabalho.Events.Event;

public class Corridor {

    private Room from;
    private Room to;
    private double weight;
    private Event event;
    private boolean locked;

    public Corridor(Room from, Room to, double weight, Event event, boolean locked) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.event = event;
        this.locked = locked;
    }

    public Room getFrom() {
        return from;
    }

    public Room getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        this.locked = false;
    }

    /**
     * Dado um lado do corredor, devolve o outro lado.
     * Se 'room' não for nem from nem to, devolve null.
     */
    public Room getOtherSide(Room room) {
        if (room == from) return to;
        if (room == to) return from;
        return null;
    }

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
