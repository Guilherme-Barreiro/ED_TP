package Trabalho.Players;

import Colecoes.Estruturas.LinkedStack;
import Colecoes.interfaces.StackADT;
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
    private final StackADT<Room> moveStack;

    public Player(String name, Room startRoom, PlayerController controller) {
        this.name = name;
        this.currentRoom = startRoom;
        this.controller = controller;
        this.blockedTurns = 0;
        this.stats = new PlayerStats();

        this.stats.addRoom(startRoom);

        this.moveStack = new LinkedStack<>();
        this.moveStack.push(startRoom);
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
        this.moveStack.push(currentRoom);
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

    /**
     * Usado pelo evento MOVE_BACK: recua 'steps' passos no caminho.
     * Mantemos sempre pelo menos uma sala na stack (a inicial).
     */
    public void moveBack(int steps) {
        if (steps <= 0) {
            return;
        }

        int pops = 0;
        while (pops < steps && moveStack.size() > 1) {
            moveStack.pop();
            pops++;
        }

        Room newTop = moveStack.peek();
        this.currentRoom = newTop;
        this.stats.addRoom(newTop);
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
