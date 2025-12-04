package Trabalho.interfacesTrabalho;

import Colecoes.interfaces.UnorderedListADT;
import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Players.Player;

public interface PlayerController {
    /**
     * Escolhe o próximo movimento para o jogador.
     */
    Room chooseMove(Player player, Labyrinth labyrinth, GameState state);

    /**
     * Responde a uma pergunta de enigma.
     *
     * @return índice da opção escolhida (0, 1, 2, ...)
     */
    int answerQuestion(Question question);

    int chooseLever(Room room, int leverCount);

    Player chooseSwapTarget(Player current, UnorderedListADT<Player> allPlayers, GameState state);
}
