package Trabalho.Events;

public class Event {
    private EventType type;
    private int intensity;

    public Event(EventType type, int intensity) {
        this.type = type;
        this.intensity = intensity;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "Event{" + "type=" + type + ", intensity=" + intensity + '}';
    }
}
