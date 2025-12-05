package Trabalho.Map;

import Trabalho.Events.Lever;
import Trabalho.Events.Question;

public class Room {
    private int id;
    private String name;
    private RoomType type;

    private Lever lever;
    private boolean hasLever;

    private Question riddle;
    private boolean hasRiddle;

    public Room(int id, String name, RoomType type) {
        this.id = id;
        this.name = name;
        this.type = type;

        this.lever = null;
        this.hasLever = false;

        this.riddle = null;
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
        this.hasLever = (lever != null);

        if (lever != null) {
            this.riddle = null;
            this.hasRiddle = false;
        }
    }

    public boolean hasLever() {
        return hasLever;
    }

    public void setHasLever(boolean hasLever) {
        this.hasLever = hasLever;
        if (!hasLever) {
            this.lever = null;
        }
    }

    public Question getRiddle() {
        return riddle;
    }

    public void setRiddle(Question riddle) {
        this.riddle = riddle;
        this.hasRiddle = (riddle != null);

        if (riddle != null) {
            this.lever = null;
            this.hasLever = false;
        }
    }

    public boolean hasRiddle() {
        return hasRiddle;
    }

    public void setHasRiddle(boolean hasRiddle) {
        this.hasRiddle = hasRiddle;
        if (!hasRiddle) {
            this.riddle = null;
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
