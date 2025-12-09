package Trabalho.Menu;

import java.util.Scanner;

/**
 * Menu principal da aplicação.
 * <p>
 * Permite ao utilizador escolher:
 * <ul>
 *     <li>modo Singleplayer;</li>
 *     <li>modo Multiplayer;</li>
 *     <li>modo Bots;</li>
 *     <li>editor de mapas;</li>
 *     <li>visualização de relatórios de jogos anteriores;</li>
 *     <li>sair do jogo.</li>
 * </ul>
 */
public class MainMenu {
    /**
     * Ciclo principal do menu.
     * <p>
     * Mostra as opções e despacha para o menu correspondente
     * até o utilizador escolher sair.
     *
     * @param args não usado
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Singleplayer");
            System.out.println("2 - Multiplayer");
            System.out.println("3 - Bot a jogar");
            System.out.println("4 - Criar mapa");
            System.out.println("5 - Ver relatórios de jogos anteriores");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String op = in.nextLine().trim();

            switch (op) {
                case "1" -> iniciarSingleplayer();
                case "2" -> iniciarMultiplayer();
                case "3" -> iniciarBotGame();
                case "4" -> criarMapa();
                case "5" -> verRelatorios();
                case "0" -> {
                    System.out.println("A sair do jogo...");
                    running = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }

        in.close();
    }

    /**
     * Arranca o menu de jogo Singleplayer.
     * Apanha exceções e mostra mensagem de erro se algo correr mal.
     */
    private static void iniciarSingleplayer() {
        System.out.println("\n--- Singleplayer ---");
        try {
            SingleplayerMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar modo Singleplayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Arranca o menu de jogo Multiplayer.
     */
    private static void iniciarMultiplayer() {
        System.out.println("\n--- Multiplayer ---");
        try {
            MultiplayerMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar modo Multiplayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Arranca o modo em que apenas bots jogam.
     */
    private static void iniciarBotGame() {
        System.out.println("\n--- Bot a jogar ---");
        try {
            BotGameMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar modo Bot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Arranca o editor de mapas.
     */
    private static void criarMapa() {
        System.out.println("\n--- Editor de Mapas ---");
        try {
            MapEditorMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o editor de mapas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre o menu de visualização de relatórios de jogos anteriores.
     */
    private static void verRelatorios() {
        System.out.println("\n--- Relatórios de jogos anteriores ---");
        try {
            GameReportsMenu.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Erro ao abrir relatórios: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
