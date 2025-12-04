package Trabalho.Players;

import Colecoes.Estruturas.ArrayUnorderedList;
import Colecoes.interfaces.UnorderedListADT;
import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.interfacesTrabalho.PlayerController;

import java.util.Iterator;
import java.util.Random;

public class BotController implements PlayerController {
    private final Random random;

    public BotController() {
        this.random = new Random();
    }

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

    @Override
    public int answerQuestion(Question question) {
        int options = question.getOptionsCount();
        if (options <= 0) {
            return 0;
        }
        return random.nextInt(options);
    }

    @Override
    public int chooseLever(Room room, int leverCount) {
        return random.nextInt(leverCount);
    }

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
