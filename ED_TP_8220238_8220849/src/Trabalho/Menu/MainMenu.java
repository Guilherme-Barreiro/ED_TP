package Trabalho.Menu;

import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Singleplayer");
            System.out.println("2 - Multiplayer");
            System.out.println("3 - Bot a jogar");
            System.out.println("4 - Criar mapa");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String op = in.nextLine().trim();

            switch (op) {
                case "1" -> iniciarSingleplayer();
                case "2" -> iniciarMultiplayer();
                case "3" -> iniciarBotGame();
                case "4" -> criarMapa();
                case "0" -> {
                    System.out.println("A sair do jogo...");
                    running = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }

        in.close();
    }

    private static void iniciarSingleplayer() { // 80% funcional
        System.out.println("\n--- Singleplayer ---");
        try {
            SingleplayerMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar modo Singleplayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void iniciarMultiplayer() {
        System.out.println("\n--- Multiplayer ---");
        System.out.println("Modo ainda não implementado.");
        // Aqui no futuro vamos montar:
        // - criação de vários jogadores humanos
        // - escolha das entradas
        // - criação do GameState com vários Player
    }

    private static void iniciarBotGame() {
        System.out.println("\n--- Bot a jogar ---");
        System.out.println("Modo ainda não implementado.");
        // Aqui no futuro:
        // - criar jogadores bot (BotController)
        // - eventualmente permitir um humano a observar / misto humano + bots
    }

    private static void criarMapa() { // 100% funcional (penso)
        System.out.println("\n--- Editor de Mapas ---");
        try {
            MapEditorMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o editor de mapas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
