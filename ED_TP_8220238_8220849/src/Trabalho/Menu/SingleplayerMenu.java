package Trabalho.Menu;

import Estruturas.ArrayUnorderedList;
import Trabalho.IO.GameReportJson;
import Trabalho.View.LabyrinthViewer;
import interfaces.UnorderedListADT;
import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Events.Question;
import Trabalho.Events.QuestionPool;
import Trabalho.Game.GameMode;
import Trabalho.Game.GameState;
import Trabalho.IO.MapLoader;
import Trabalho.IO.QuestionLoader;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Trabalho.Players.HumanController;
import Trabalho.Players.Player;
import Trabalho.interfacesTrabalho.PlayerController;
import org.json.simple.parser.ParseException;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class SingleplayerMenu {

    public static void main(String[] args) throws IOException, ParseException {

        Scanner in = new Scanner(System.in);

        Labyrinth lab = MapSelectionMenu.escolherMapa(in);
        if (lab == null) {
            System.out.println("\nNão foi possível selecionar um mapa válido. A sair do Singleplayer.");
            return;
        }

        // Se as rooms do mapa tiverem posições sao usadas, senao é usado o layout circular.
        if (MapLoader.hasLastLayout()) {
            Room[] rooms = MapLoader.getLastRoomsWithLayout();
            Point[] positions = MapLoader.getLastPositions();
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab, rooms, positions));
        } else {
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab));
        }

        // caregar questions do JSON
        QuestionPool pool;
        try {
            pool = QuestionLoader.loadFromJson("src/resources/questions.json");

        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar src/resources/questions.json: " + e.getMessage());
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

        GameState state = new GameState(lab, players, pool, GameMode.MANUAL);

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

    // Helper: escolher uma sala de entrada (ENTRY) pelo ID
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
