package Trabalho.Players;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;
import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.interfacesTrabalho.PlayerController;

import java.util.Iterator;
import java.util.Random;

/**
 * Implementação de {@link PlayerController} para jogadores controlados por bot.
 * <p>
 * As decisões são tomadas de forma aleatória:
 * <ul>
 *     <li>movimento: escolhe aleatoriamente uma sala vizinha;</li>
 *     <li>perguntas de enigma: escolhe uma opção aleatória;</li>
 *     <li>alavancas: escolhe uma alavanca aleatória;</li>
 *     <li>SWAP_PLAYER: escolhe aleatoriamente outro jogador para trocar.</li>
 * </ul>
 */
public class BotController implements PlayerController {
    private final Random random;

    /**
     * Cria um controlador de bot com um gerador de números aleatórios.
     */
    public BotController() {
        this.random = new Random();
    }

    /**
     * Escolhe um movimento aleatório entre as salas vizinhas acessíveis.
     * <p>
     * Se não existirem vizinhos, permanece na sala atual.
     */
    @Override
    public Room chooseMove(Player player, Labyrinth labyrinth, GameState state) {
        ArrayUnorderedList<Room> neighbors =
                labyrinth.getNeighbors(player.getCurrentRoom());

        Iterator<Room> it = neighbors.iterator();
        if (!it.hasNext()) {
            return player.getCurrentRoom();
        }

        ArrayUnorderedList<Room> options = new ArrayUnorderedList<>();
        while (it.hasNext()) {
            options.addToRear(it.next());
        }

        int size = options.size();
        int choiceIndex = random.nextInt(size);

        int idx = 0;
        it = options.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (idx == choiceIndex) {
                return r;
            }
            idx++;
        }
        return player.getCurrentRoom();
    }

    /**
     * Responde a uma pergunta escolhendo aleatoriamente um dos índices das opções.
     *
     * @return índice aleatório no intervalo [0, número de opções[
     */
    @Override
    public int answerQuestion(Question question) {
        int options = question.getOptionsCount();
        if (options <= 0) {
            return 0;
        }
        return random.nextInt(options);
    }

    /**
     * Escolhe aleatoriamente uma alavanca entre {@code 0} e {@code leverCount-1}.
     */
    @Override
    public int chooseLever(Room room, int leverCount) {
        return random.nextInt(leverCount);
    }

    /**
     * Escolhe aleatoriamente um jogador diferente de {@code current}
     * para o evento SWAP_PLAYER.
     * <p>
     * Se não houver mais jogadores, devolve o próprio {@code current}.
     */
    @Override
    public Player chooseSwapTarget(Player current, UnorderedListADT<Player> allPlayers, GameState state) {
        int count = 0;
        Iterator<Player> itCount = allPlayers.iterator();
        while (itCount.hasNext()) {
            Player p = itCount.next();
            if (p != current) {
                count++;
            }
        }

        if (count == 0) {
            return current;
        }

        Player[] candidates = new Player[count];
        int idx = 0;
        Iterator<Player> it = allPlayers.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            if (p != current) {
                candidates[idx] = p;
                idx++;
            }
        }

        int choice = random.nextInt(count);
        return candidates[choice];
    }

}
