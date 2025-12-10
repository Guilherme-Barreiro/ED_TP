package trabalho.players;

import Estruturas.LinkedStack;
import interfaces.StackADT;
import trabalho.game.GameState;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;
import trabalho.interfacestrabalho.PlayerController;

/**
 * Representa um jogador (humano ou bot) no jogo.
 * <p>
 * Mantém:
 * <ul>
 *     <li>nome do jogador;</li>
 *     <li>sala atual;</li>
 *     <li>controller responsável pelas decisões;</li>
 *     <li>turnos bloqueados (para eventos SKIP_TURNS);</li>
 *     <li>estatísticas ({@link PlayerStats});</li>
 *     <li>uma stack com o percurso de salas para suportar o evento MOVE_BACK;</li>
 *     <li>um limite mínimo na stack (para não recuar para antes de um SWAP).</li>
 * </ul>
 */
public class Player {
    private final String name;
    private Room currentRoom;
    private final PlayerController controller;
    private int blockedTurns;
    private final PlayerStats stats;
    private final StackADT<Room> moveStack;
    private int minStackSize;

    /**
     * Cria um novo jogador com nome, sala inicial e controller dados.
     * <p>
     * A sala inicial tem de ser do tipo {@link RoomType#ENTRY}.
     *
     * @param name       nome do jogador
     * @param startRoom  sala inicial (tem de ser ENTRY)
     * @param controller controlador responsável pelas ações do jogador
     * @throws IllegalArgumentException se a sala inicial não for ENTRY
     */
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

    /**
     * Devolve o nome do jogador.
     */
    public String getName() {
        return name;
    }

    /**
     * Devolve a sala atual do jogador.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Atualiza a sala atual do jogador.
     * <p>
     * Também regista a sala nas estatísticas e na stack de movimentos.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
        this.stats.addRoom(currentRoom);
        this.moveStack.push(currentRoom);
    }

    /**
     * Devolve o {@link PlayerController} associado a este jogador.
     */
    public PlayerController getController() {
        return controller;
    }

    /**
     * Devolve o número de turnos em que o jogador ainda ficará bloqueado.
     */
    public int getBlockedTurns() {
        return blockedTurns;
    }

    /**
     * Atualiza o número de turnos em que o jogador ficará bloqueado.
     */
    public void setBlockedTurns(int blockedTurns) {
        this.blockedTurns = blockedTurns;
    }

    /**
     * Decrementa em 1 o número de turnos bloqueados, se for maior que 0.
     */
    public void decrementBlockedTurns() {
        if (blockedTurns > 0) {
            blockedTurns--;
        }
    }

    /**
     * Devolve as estatísticas acumuladas deste jogador.
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     * Pergunta ao controller qual o próximo movimento.
     * <p>
     * Se o jogador estiver bloqueado, permanece na sala atual.
     */
    public Room chooseNextMove(Labyrinth labyrinth, GameState state) {
        if (blockedTurns > 0) {
            return currentRoom;
        }
        return controller.chooseMove(this, labyrinth, state);
    }

    /**
     * Indica se este jogador é controlado por um bot.
     *
     * @return {@code true} se o controller não for {@link HumanController}
     */
    public boolean isBot() {
        return !(controller instanceof HumanController);
    }

    /**
     * Marca a posição atual na stack como limite mínimo de recuo após um SWAP.
     * <p>
     * A partir deste momento, o evento MOVE_BACK nunca pode reduzir a stack
     * para menos entradas do que este limite.
     */
    public void markSwapBoundary() {
        this.minStackSize = this.moveStack.size();
    }

    /**
     * Usado pelo evento MOVE_BACK: recua {@code steps} passos no caminho.
     * <p>
     * Nunca recua para antes do limite definido por {@link #minStackSize}:
     * <ul>
     *   <li>antes de qualquer SWAP, esse limite é a sala inicial (stack size 1);</li>
     *   <li>depois de um SWAP, esse limite passa a ser a posição da stack
     *       após o SWAP.</li>
     * </ul>
     *
     * @param steps número de passos a recuar
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

    /**
     * Representação textual simples do jogador (nome, sala e turnos bloqueados).
     */
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", currentRoom=" + (currentRoom != null ? currentRoom.getId() : "null") +
                ", blockedTurns=" + blockedTurns +
                '}';
    }
}
