package Trabalho.Players;

import Colecoes.Estruturas.ArrayUnorderedList;
import Trabalho.Events.Question;
import Trabalho.Game.GameState;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.interfacesTrabalho.PlayerController;

import java.util.Iterator;
import java.util.Scanner;

public class HumanController implements PlayerController {
    private final Scanner in;

    public HumanController(Scanner in) {
        this.in = in;
    }

    @Override
    public Room chooseMove(Player player, Labyrinth labyrinth, GameState state) {
        ArrayUnorderedList<Room> neighbors =
                labyrinth.getNeighbors(player.getCurrentRoom());

        Iterator<Room> it = neighbors.iterator();
        if (!it.hasNext()) {
            System.out.println("Não há movimentos possíveis.");
            return player.getCurrentRoom();
        }

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

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Escolhe o índice: ");
            String line = in.nextLine();
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < options.size()) {
                    valid = true;
                } else {
                    System.out.println("Índice inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }

        index = 0;
        it = options.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (index == choice) {
                return r;
            }
            index++;
        }
        return player.getCurrentRoom();
    }

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

}
