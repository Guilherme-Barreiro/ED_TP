package Trabalho.Map;

import Trabalho.Events.Lever;

public class Room {
    private int id;
    private String name;
    private RoomType type;
    private Lever lever;
    private boolean hasRiddle;


    public Room(int id, String name, RoomType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lever = null;
        this.hasRiddle = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoomType getType() {
        return type;
    }

    public Lever getLever() {
        return lever;
    }

    public void setLever(Lever lever) {
        this.lever = lever;
    }

    public boolean hasLever() { return lever != null; }

    public boolean hasEnigma() { return hasRiddle; }

    public void setHasRiddle(boolean hasRiddle) {
        this.hasRiddle = hasRiddle;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name='" + name + '\'' + ", type=" + type + '}';
    }
}
