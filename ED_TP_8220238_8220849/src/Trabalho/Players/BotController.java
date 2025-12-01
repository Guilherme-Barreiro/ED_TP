package Trabalho.Players;

import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.interfacesTrabalho.PlayerController;

import java.util.Random;

public class BotController implements PlayerController {
    private final Random random;

    public BotController() {
        this.random = new Random();
    }

    @Override
    public Room chooseMove(Player player, Labyrinth labyrinth, GameState state) {
        /**
        // obtém as salas vizinhas da sala atual do jogador
        ArrayUnorderedList<Room> neighbors =
                labyrinth.getNeighbors(player.getCurrentRoom());

        Iterator<Room> it = neighbors.iterator();
        if (!it.hasNext()) {
            // sem movimentos possíveis, fica onde está
            return player.getCurrentRoom();
        }

        // copiar vizinhos para uma lista "options"
        ArrayUnorderedList<Room> options = new ArrayUnorderedList<>();
        while (it.hasNext()) {
            options.addToRear(it.next());
        }

        // escolher índice aleatório
        int size = options.size();
        int choiceIndex = random.nextInt(size);

        // devolver a room correspondente ao índice escolhido
        int idx = 0;
        it = options.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (idx == choiceIndex) {
                return r;
            }
            idx++;
        }

        // fallback (não deve acontecer)
         */
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
}
