package trabalho.menu;

import Estruturas.ArrayUnorderedList;
import trabalho.game.Difficulty;
import trabalho.io.GameReportJson;
import trabalho.view.LabyrinthViewer;
import interfaces.UnorderedListADT;
import trabalho.events.QuestionPool;
import trabalho.game.GameMode;
import trabalho.game.GameState;
import trabalho.io.MapLoader;
import trabalho.io.QuestionLoader;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;
import trabalho.players.HumanController;
import trabalho.players.Player;
import trabalho.interfacestrabalho.PlayerController;
import org.json.simple.parser.ParseException;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Menu para configuração e execução de um jogo em modo Singleplayer
 * (um único jogador humano).
 * <p>
 * Passos principais:
 * <ul>
 *     <li>escolha do mapa;</li>
 *     <li>carregamento das perguntas de enigmas;</li>
 *     <li>pedido do nome do jogador;</li>
 *     <li>escolha da sala de entrada;</li>
 *     <li>início do jogo em modo {@link GameMode#MANUAL};</li>
 *     <li>no fim, mostra estatísticas e gera relatório JSON.</li>
 * </ul>
 */
public class SingleplayerMenu {
    /**
     * Ponto de entrada do modo Singleplayer.
     *
     * @param args não usado
     * @throws IOException    se ocorrer erro com ficheiros
     * @throws ParseException se ocorrer erro ao ler JSON
     */
    public static void main(String[] args) throws IOException, ParseException {

        Scanner in = new Scanner(System.in);

        Labyrinth lab = MapSelectionMenu.escolherMapa(in);
        if (lab == null) {
            System.out.println("\nNão foi possível selecionar um mapa válido. A sair do Singleplayer.");
            return;
        }

        if (MapLoader.hasLastLayout()) {
            Room[] rooms = MapLoader.getLastRoomsWithLayout();
            Point[] positions = MapLoader.getLastPositions();
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab, rooms, positions));
        } else {
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab));
        }

        Difficulty difficulty = escolherDificuldade(in);

        String questionsFile;
        switch (difficulty) {
            case EASY -> questionsFile = "src/Questions/questions_easy.json";
            case HARD -> questionsFile = "src/Questions/questions_hard.json";
            default -> questionsFile = "src/Questions/questions_normal.json";
        }

        QuestionPool pool;
        try {
            pool = QuestionLoader.loadFromJson(questionsFile);
        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar " + questionsFile + ": " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.print("\nNome do jogador: ");
        String playerName = in.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Jogador 1";
        }

        Room startRoom = escolherEntrada(lab, in);
        if (startRoom == null) {
            System.out.println("Não foi possível escolher sala de entrada. A sair.");
            return;
        }

        PlayerController controller = new HumanController(in);
        Player player = new Player(playerName, startRoom, controller);

        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        players.addToRear(player);

        GameState state = new GameState(lab, players, pool, GameMode.MANUAL, difficulty);

        System.out.println("\n=== Início do jogo ===");
        System.out.println("Objetivo: chegar à sala central (" + lab.getCenterRoom().getName() + ").");

        while (!state.isGameOver()) {
            System.out.println("\nPressiona ENTER para jogar o próximo turno...");
            in.nextLine();
            state.nextTurn();
        }

        System.out.println("\n=== Fim do jogo ===");
        Player winner = state.getWinner();
        if (winner != null) {
            System.out.println("Vencedor: " + winner.getName());
            System.out.println("Estatísticas do vencedor:");
            System.out.println(winner.getStats());
        } else {
            System.out.println("O jogo terminou sem vencedor definido.");
        }

        try {
            String reportFile = GameReportJson.generateAndSave(state);
            System.out.println("Relatório do jogo guardado em: " + reportFile);
        } catch (IOException e) {
            System.out.println("Erro ao guardar relatório do jogo: " + e.getMessage());
        }
    }

    /**
     * Pede interativamente ao utilizador que escolha
     * um nível de dificuldade para o jogo.
     *
     * @param in {@link Scanner} já aberto, usado para ler a opção do utilizador
     * @return dificuldade escolhida
     */
    private static Difficulty escolherDificuldade(Scanner in) {
        while (true) {
            System.out.println("\nEscolhe a dificuldade:");
            System.out.println("1 - Fácil");
            System.out.println("2 - Normal");
            System.out.println("3 - Difícil");
            System.out.print("Opção: ");
            String op = in.nextLine().trim();
            switch (op) {
                case "1":
                    return Difficulty.EASY;
                case "2":
                    return Difficulty.NORMAL;
                case "3":
                    return Difficulty.HARD;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Permite ao jogador escolher qual a sala de entrada (ENTRY)
     * onde quer começar.
     *
     * @param lab labirinto
     * @param in  scanner para ler do teclado
     * @return sala de entrada escolhida ou {@code null} se não existirem entradas
     */
    private static Room escolherEntrada(Labyrinth lab, Scanner in) {
        UnorderedListADT<Room> entries = lab.getEntryRooms();
        Iterator<Room> it = entries.iterator();

        if (!it.hasNext()) {
            System.out.println("Não existem salas de entrada no labirinto.");
            return null;
        }

        System.out.println("\nSalas de entrada disponíveis:");
        while (it.hasNext()) {
            Room r = it.next();
            System.out.println("  ID=" + r.getId() + " | " + r.getName());
        }

        Room chosen = null;
        while (chosen == null) {
            System.out.print("Escolhe o ID da sala de entrada: ");
            String line = in.nextLine().trim();
            int id;
            try {
                id = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                continue;
            }

            try {
                Room candidate = lab.findRoomById(id);
                if (candidate.getType() != RoomType.ENTRY) {
                    System.out.println("A sala com ID " + id + " não é uma entrada.");
                } else {
                    chosen = candidate;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return chosen;
    }
}
