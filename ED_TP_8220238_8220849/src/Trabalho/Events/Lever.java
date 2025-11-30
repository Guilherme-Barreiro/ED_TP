package Trabalho.Events;

import Trabalho.Players.Player;

public class Lever {
    private boolean activated;

    public Lever() {
        this.activated = false;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean tryActivate(Player p) {
        if (activated == false) {
            activated = true;
            return true;
        }
        return false;
    }
}
