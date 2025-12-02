package Trabalho.Players;

import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.interfacesTrabalho.PlayerController;

public class Player {
    private final String name;
    private Room currentRoom;
    private final PlayerController controller;
    private int blockedTurns;
    private final PlayerStats stats;

    public Player(String name, Room startRoom, PlayerController controller) {
        this.name = name;
        this.currentRoom = startRoom;
        this.controller = controller;
        this.blockedTurns = 0;
        this.stats = new PlayerStats();

        this.stats.addRoom(startRoom);
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
        this.stats.addRoom(currentRoom);
    }

    public PlayerController getController() {
        return controller;
    }

    public int getBlockedTurns() {
        return blockedTurns;
    }

    public void setBlockedTurns(int blockedTurns) {
        this.blockedTurns = blockedTurns;
    }

    public void decrementBlockedTurns() {
        if (blockedTurns > 0) {
            blockedTurns--;
        }
    }

    public PlayerStats getStats() {
        return stats;
    }

    /**
     * Pergunta ao controller qual o próximo movimento.
     */
    public Room chooseNextMove(Labyrinth labyrinth, GameState state) {
        if (blockedTurns > 0) {
            return currentRoom;
        }
        return controller.chooseMove(this, labyrinth, state);
    }


    public boolean isBot() {
        return !(controller instanceof HumanController);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", currentRoom=" + (currentRoom != null ? currentRoom.getId() : "null") +
                ", blockedTurns=" + blockedTurns +
                '}';
    }
}
