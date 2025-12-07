package Trabalho.Players;

import Estruturas.LinkedStack;
import interfaces.StackADT;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Trabalho.interfacesTrabalho.PlayerController;

public class Player {
    private final String name;
    private Room currentRoom;
    private final PlayerController controller;
    private int blockedTurns;
    private final PlayerStats stats;
    private final StackADT<Room> moveStack;
    private int minStackSize;

    public Player(String name, Room startRoom, PlayerController controller) {
        if (startRoom == null || startRoom.getType() != RoomType.ENTRY) {
            throw new IllegalArgumentException("A sala inicial do jogador " + name +
                    " tem de ser uma ENTRY.");
        }
        this.name = name;
        this.currentRoom = startRoom;
        this.controller = controller;
        this.blockedTurns = 0;
        this.stats = new PlayerStats();

        this.stats.addRoom(startRoom);

        this.moveStack = new LinkedStack<>();
        this.moveStack.push(startRoom);

        this.minStackSize = this.moveStack.size();
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
     * Marca a posição atual na stack como limite mínimo de recuo após um SWAP.
     * A partir daqui, o MOVE_BACK nunca pode reduzir a stack abaixo deste tamanho.
     */
    public void markSwapBoundary() {
        this.minStackSize = this.moveStack.size();
    }

    /**
     * Usado pelo evento MOVE_BACK: recua 'steps' passos no caminho.
     * Nunca recua para antes do limite definido por minStackSize.
     * - Antes de qualquer SWAP, esse limite é a sala inicial (stack size 1).
     * - Depois de um SWAP, esse limite passa a ser a posição da stack após o SWAP.
     */
    public void moveBack(int steps) {
        if (steps <= 0) {
            return;
        }

        int pops = 0;
        while (pops < steps && moveStack.size() > minStackSize) {
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
