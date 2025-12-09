package Trabalho.interfacesTrabalho;

import interfaces.UnorderedListADT;
import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Players.Player;

/**
 * Controlador responsável por tomar decisões em nome de um {@link Player}.
 * <p>
 * Implementações desta interface podem representar:
 * <ul>
 *     <li>um jogador humano (controlado por input do utilizador);</li>
 *     <li>um bot / IA (decisões automáticas);</li>
 *     <li>ou outras estratégias de controlo.</li>
 * </ul>
 */
public interface PlayerController {
    /**
     * Escolhe o próximo movimento para o jogador.
     *
     * @param player    jogador que está a jogar o turno
     * @param labyrinth labirinto onde o jogador se encontra
     * @param state     estado global do jogo
     * @return sala de destino escolhida (pode ser a sala atual ou {@code null}
     * se não se quiser/for possível mover)
     */
    Room chooseMove(Player player, Labyrinth labyrinth, GameState state);

    /**
     * Responde a uma pergunta de enigma.
     *
     * @param question pergunta apresentada ao jogador
     * @return índice da opção escolhida (0, 1, 2, ...)
     */
    int answerQuestion(Question question);

    /**
     * Escolhe uma alavanca (índice) numa sala que contém um puzzle de alavancas.
     *
     * @param room       sala onde se encontra a alavanca
     * @param leverCount número total de alavancas possíveis (0..leverCount-1)
     * @return índice da alavanca escolhida
     */
    int chooseLever(Room room, int leverCount);

    /**
     * Escolhe outro jogador para trocar de posição no evento SWAP_PLAYER.
     *
     * @param current    jogador atual (que está a escolher o alvo)
     * @param allPlayers lista de todos os jogadores na partida
     * @param state      estado global do jogo
     * @return jogador alvo com quem se pretende trocar, ou {@code null}
     * se a implementação decidir não efetuar troca
     */
    Player chooseSwapTarget(Player current, UnorderedListADT<Player> allPlayers, GameState state);
}
