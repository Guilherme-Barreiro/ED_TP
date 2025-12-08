package Trabalho.Menu;

import Estruturas.ArrayUnorderedList;
import Trabalho.View.LabyrinthViewer;
import interfaces.UnorderedListADT;
import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Events.Question;
import Trabalho.Events.QuestionPool;
import Trabalho.Game.GameMode;
import Trabalho.Game.GameState;
import Trabalho.IO.QuestionLoader;
import Trabalho.Map.*;
import Trabalho.Players.HumanController;
import Trabalho.Players.Player;
import Trabalho.interfacesTrabalho.PlayerController;
import org.json.simple.parser.ParseException;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class SingleplayerMenu {

    public static void main(String[] args) throws IOException, ParseException {
        Labyrinth lab = new Labyrinth();

        Room r1  = new Room(1, "Sala 1", RoomType.NORMAL);
        Room r2  = new Room(2, "Sala 2", RoomType.NORMAL);
        Room r3  = new Room(3, "Sala 3", RoomType.NORMAL);
        Room r4  = new Room(4, "Sala 4", RoomType.NORMAL);
        Room r5  = new Room(5, "Sala 5", RoomType.NORMAL);
        Room r6  = new Room(6, "Sala 6", RoomType.NORMAL);
        Room r7  = new Room(7, "Sala 7", RoomType.NORMAL);
        Room r8  = new Room(8, "Sala 8", RoomType.NORMAL);
        Room r9  = new Room(9, "Sala 9", RoomType.NORMAL);
        Room r10 = new Room(10, "Sala 10", RoomType.NORMAL);
        Room r11 = new Room(11, "Sala 11", RoomType.NORMAL);
        Room r12 = new Room(12, "Sala 12", RoomType.NORMAL);
        Room r13 = new Room(13, "Sala 13", RoomType.NORMAL);
        Room r14 = new Room(14, "Sala 14", RoomType.NORMAL);
        Room E1  = new Room(15, "Entrada 1", RoomType.ENTRY);
        Room E2  = new Room(16, "Entrada 2", RoomType.ENTRY);
        Room E3  = new Room(17, "Entrada 3", RoomType.ENTRY);
        Room C   = new Room(18, "Centro", RoomType.CENTER);

        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);
        lab.addRoom(r4);
        lab.addRoom(r5);
        lab.addRoom(r6);
        lab.addRoom(r7);
        lab.addRoom(r8);
        lab.addRoom(r9);
        lab.addRoom(r10);
        lab.addRoom(r11);
        lab.addRoom(r12);
        lab.addRoom(r13);
        lab.addRoom(r14);
        lab.addRoom(C);
        lab.addRoom(E1);
        lab.addRoom(E2);
        lab.addRoom(E3);

        lab.addCorridor(E1, r1, 1.0, false);
        lab.addCorridor(E2, r6, 0.0, false);
        lab.addCorridor(E3, r9, 3.0, true);
        lab.addCorridor(E3, r13, 3.0, false);
        lab.addCorridor(r1, r2, 1.0, false);
        lab.addCorridor(r2, r3, 3.0, true);
        lab.addCorridor(r4, r5, 0.0, false);
        lab.addCorridor(r3, r4, 3.0, false);
        lab.addCorridor(r5, C, 10.0, false);
        lab.addCorridor(r1, r6, 3.0, false);
        lab.addCorridor(r6, r7, 0.0, false);
        lab.addCorridor(r7, r8, 0.0, false);
        lab.addCorridor(r4, r8, 0.0, true);
        lab.addCorridor(r7, r11, 3.0, false);
        lab.addCorridor(r9, r10, 3.0, false);
        lab.addCorridor(r12, r13, 3.0, false);
        lab.addCorridor(r9, r13, 3.0, false);
        lab.addCorridor(r10, r8, 3.0, true);
        lab.addCorridor(r10, r12, 3.0, false);
        lab.addCorridor(r7, r12, 3.0, false);
        lab.addCorridor(E2, r14, 8.0, false);
        lab.addCorridor(r6, r14, 8.0, false);
        lab.addCorridor(r11, r14, 8.0, false);

        Room[] rooms = { E1, E2, E3, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, C };

        Point[] positions = new Point[] {
                new Point(110, 200),  // E1
                new Point(110, 400),  // E2
                new Point(660, 400),  // E3
                new Point(220, 200),  // r1
                new Point(330, 200),  // r2
                new Point(440, 100),  // r3
                new Point(440, 200),  // r4
                new Point(550, 300),  // r5
                new Point(220, 400),  // r6
                new Point(330, 400),  // r7
                new Point(330, 300),  // r8
                new Point(550, 400),  // r9
                new Point(440, 400),  // r10
                new Point(330, 500),  // r11
                new Point(440, 500),  // r12
                new Point(550, 500),  // r13
                new Point(220, 500),  // r14
                new Point(440, 300)   // C
        };

        SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab, rooms, positions));

        // 2) Carregar perguntas do ficheiro JSON
        QuestionPool pool;
        try {
            pool = QuestionLoader.loadFromJson("resources/questions.json");

        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar resources/questions.json: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3) Associar um enigma e uma alavanca a salas (exemplo)
        if (!pool.isCompletelyEmpty()) {
            Question riddleR5 = pool.getRandomQuestion();
            r5.setRiddle(riddleR5);
        }

        LeverPuzzle puzzle = new LeverPuzzle(1);
        Lever lever = new Lever(puzzle);
        r8.setLever(lever);


        // 4) Criar jogador humano
        Scanner in = new Scanner(System.in);

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

        // 5) Criar GameState em modo MANUAL
        GameState state = new GameState(lab, players, pool, GameMode.MANUAL);


        // 6) Loop de jogo: um turno de cada vez até alguém chegar ao centro
        System.out.println("\n=== Início do jogo ===");
        System.out.println("Objetivo: chegar à sala central (" + C.getName() + ").");

        while (!state.isGameOver()) {
            System.out.println("\nPressiona ENTER para jogar o próximo turno...");
            in.nextLine();
            state.nextTurn();
        }

        // 7) Fim do jogo: mostrar vencedor e estatísticas
        System.out.println("\n=== Fim do jogo ===");
        Player winner = state.getWinner();
        if (winner != null) {
            System.out.println("Vencedor: " + winner.getName());
            System.out.println("Estatísticas do vencedor:");
            System.out.println(winner.getStats());
        } else {
            System.out.println("O jogo terminou sem vencedor definido.");
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
