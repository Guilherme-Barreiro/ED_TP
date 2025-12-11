package trabalho.players;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;
import trabalho.events.Question;
import trabalho.game.GameState;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.interfacestrabalho.PlayerController;

import java.util.Iterator;
import java.util.Scanner;

/**
 * Implementação de {@link PlayerController} para um jogador humano.
 * <p>
 * Todas as decisões (movimento, respostas a enigmas, alavancas e SWAP_PLAYER)
 * são pedidas ao utilizador via consola.
 */
public class HumanController implements PlayerController {
    private final Scanner in;

    /**
     * Cria um controlador humano associado a um {@link Scanner}
     * para ler input do utilizador.
     *
     * @param in scanner usado para ler da consola
     */
    public HumanController(Scanner in) {
        this.in = in;
    }

    /**
     * Mostra as salas vizinhas acessíveis e pede ao utilizador que escolha
     * o índice do próximo movimento.
     * <p>
     * Se o {@link GameState} permitir ficar parado neste turno,
     * é adicionada a opção de permanecer na sala atual.
     */
    @Override
    public Room chooseMove(Player player, Labyrinth labyrinth, GameState state) {
        ArrayUnorderedList<Room> neighbors =
                labyrinth.getNeighbors(player.getCurrentRoom());

        Iterator<Room> it = neighbors.iterator();
        if (!it.hasNext()) {
            System.out.println("Não há movimentos possíveis. Vais ficar na sala atual.");
            return player.getCurrentRoom();
        }

        boolean canStay = (state != null && state.isStayAllowedThisTurn());

        System.out.println("Jogador: " + player.getName());
        System.out.println("Estás em: " + player.getCurrentRoom());
        System.out.println("Podes mover para:");

        int index = 0;
        ArrayUnorderedList<Room> options = new ArrayUnorderedList<Room>();
        while (it.hasNext()) {
            Room r = it.next();
            System.out.println("  " + index + " - " + r);
            options.addToRear(r);
            index++;
        }

        Integer stayIndex = null;
        if (canStay) {
            stayIndex = index;
            System.out.println("  " + stayIndex + " - Ficar na sala atual");
        }

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Escolhe o índice: ");
            String line = in.nextLine();
            try {
                choice = Integer.parseInt(line);

                if (canStay) {
                    if (choice >= 0 && choice <= stayIndex) {
                        valid = true;
                    } else {
                        System.out.println("Índice inválido.");
                    }
                } else {
                    if (choice >= 0 && choice < options.size()) {
                        valid = true;
                    } else {
                        System.out.println("Índice inválido.");
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }

        if (canStay && stayIndex != null && choice == stayIndex) {
            return player.getCurrentRoom();
        }
        int i = 0;
        it = options.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (i == choice) {
                return r;
            }
            i++;
        }

        return player.getCurrentRoom();
    }

    /**
     * Mostra o texto e as opções da pergunta e pede ao utilizador
     * que introduza o índice da resposta.
     *
     * @return índice escolhido (entre 0 e número de opções - 1)
     */
    @Override
    public int answerQuestion(Question question) {
        System.out.println("Pergunta: " + question.getText());
        for (int i = 0; i < question.getOptionsCount(); i++) {
            System.out.println("  " + i + " - " + question.getOption(i));
        }

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Resposta (índice): ");
            String line = in.nextLine();
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < question.getOptionsCount()) {
                    valid = true;
                } else {
                    System.out.println("Índice inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }
        return choice;
    }

    /**
     * Mostra o número de alavancas disponíveis na sala e pede ao utilizador
     * que escolha o índice de uma alavanca.
     *
     * @param room       sala atual
     * @param leverCount número de alavancas disponíveis
     * @return índice de alavanca escolhido (0..leverCount-1)
     */
    @Override
    public int chooseLever(Room room, int leverCount) {
        System.out.println("Estás na sala: " + room.getName());
        System.out.println("Há " + leverCount + " alavancas. Escolhe uma:");

        for (int i = 0; i < leverCount; i++) {
            System.out.println("  " + i + " - Alavanca " + (i + 1));
        }

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Escolhe o índice da alavanca (0-" + (leverCount - 1) + "): ");
            String line = in.nextLine();
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < leverCount) {
                    valid = true;
                } else {
                    System.out.println("Índice inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }
        return choice;
    }

    /**
     * Mostra a lista de outros jogadores e pede ao utilizador que escolha
     * qual será o alvo do evento SWAP_PLAYER.
     *
     * @return jogador escolhido para troca; se não houver outros, devolve o próprio
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
            System.out.println("Não há outros jogadores para trocar.");
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

        System.out.println("Escolhe um jogador para trocar de posição:");
        for (int i = 0; i < candidates.length; i++) {
            Player p = candidates[i];
            System.out.println("  " + i + " - " + p.getName() +
                    " (" + (p.getCurrentRoom() != null ? p.getCurrentRoom().getName() : "null") + ")");
        }

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Índice do jogador: ");
            String line = in.nextLine();
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < candidates.length) {
                    valid = true;
                } else {
                    System.out.println("Índice inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }

        return candidates[choice];
    }

}