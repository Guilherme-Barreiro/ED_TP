package Trabalho.Demo;

import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;

import java.util.Scanner;

public class TesteAlavancas {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 1) Criar um puzzle com 3 alavancas, por exemplo a correta é a 1
        LeverPuzzle puzzle = new LeverPuzzle(1); // índices: 0, 1, 2
        Lever lever = new Lever(puzzle);

        System.out.println("=== Teste de Alavanca ===");
        System.out.println("Há " + puzzle.getLeverCount() + " alavancas (0, 1, 2).");
        System.out.println("Uma delas é a correta. Tenta adivinhar.");
        System.out.println("(Para debug: correta = " + puzzle.getCorrectIndex() + ")");
        System.out.println();

        while (true) {
            if (lever.isActivated()) {
                System.out.println("A alavanca já está ativada. Não podes voltar a ativá-la.");
                break;
            }

            System.out.print("Escolhe uma alavanca (0.." + (puzzle.getLeverCount() - 1) + "), ou -1 para sair: ");
            String line = in.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Input inválido, tenta outra vez.");
                continue;
            }

            if (choice == -1) {
                System.out.println("A sair sem ativar a alavanca.");
                break;
            }

            if (choice < 0 || choice >= puzzle.getLeverCount()) {
                System.out.println("Índice fora de range. Tem de ser entre 0 e " + (puzzle.getLeverCount() - 1) + ".");
                continue;
            }

            boolean success = lever.tryActivate(choice);
            if (success) {
                System.out.println("Acertaste! A alavanca foi ativada.");
                System.out.println("Estado final: isActivated = " + lever.isActivated());
                break;
            } else {
                System.out.println("Falhaste. A alavanca continua desativada.");
                System.out.println("isActivated = " + lever.isActivated());
            }
        }

        in.close();
        System.out.println("Fim do teste.");
    }
}
