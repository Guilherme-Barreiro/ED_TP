package Trabalho.Menu;

import Estruturas.ArrayUnorderedList;
import Trabalho.Game.GameMode;
import Trabalho.Game.GameState;
import Trabalho.IO.GameReportJson;
import Trabalho.IO.MapLoader;
import Trabalho.IO.QuestionLoader;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Trabalho.Players.BotController;
import Trabalho.Players.Player;
import Trabalho.View.LabyrinthViewer;
import Trabalho.Events.QuestionPool;
import interfaces.UnorderedListADT;
import org.json.simple.parser.ParseException;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * Menu responsável por configurar e executar um jogo
 * completamente automático, controlado apenas por bots.
 * <p>
 * Passos principais:
 * <ul>
 *     <li>pede ao utilizador que escolha um mapa válido;</li>
 *     <li>carrega as perguntas de enigmas;</li>
 *     <li>cria um número de bots escolhido pelo utilizador;</li>
 *     <li>inicia o jogo em modo {@link GameMode#AUTOMATIC};</li>
 *     <li>no fim, mostra estatísticas e gera um relatório JSON.</li>
 * </ul>
 */
public class BotGameMenu {
    /**
     * Ponto de entrada do modo “Bot a jogar”.
     *
     * @param args não usado
     * @throws IOException    se ocorrer erro de leitura/escrita de ficheiros
     * @throws ParseException se houver erro ao fazer parse de JSON
     */
    public static void main(String[] args) throws IOException, ParseException {
        Scanner in = new Scanner(System.in);

        Labyrinth lab = MapSelectionMenu.escolherMapa(in);
        if (lab == null) {
            System.out.println("\nNão foi possível selecionar um mapa válido. A sair do modo Bot.");
            return;
        }

        if (MapLoader.hasLastLayout()) {
            Room[] rooms = MapLoader.getLastRoomsWithLayout();
            Point[] positions = MapLoader.getLastPositions();
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab, rooms, positions));
        } else {
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab));
        }

        QuestionPool pool;
        try {
            pool = QuestionLoader.loadFromJson("src/resources/questions.json");
        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar src/resources/questions.json: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        int numBots = 0;
        while (numBots <= 0) {
            System.out.print("\nNúmero de bots (>=1): ");
            String line = in.nextLine().trim();
            try {
                numBots = Integer.parseInt(line);
                if (numBots <= 0) {
                    System.out.println("Tem de ser pelo menos 1 bot.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }

        UnorderedListADT<Player> players = new ArrayUnorderedList<>();
        Random rnd = new Random();

        for (int i = 1; i <= numBots; i++) {
            Room startRoom = escolherEntradaAleatoria(lab, rnd);
            if (startRoom == null) {
                System.out.println("Erro: não foi possível encontrar sala de entrada para o bot " + i + ".");
                return;
            }

            String botName = "Bot " + i;
            Player botPlayer = new Player(botName, startRoom, new BotController());
            players.addToRear(botPlayer);

            System.out.println("Criado " + botName + " a começar na entrada " +
                    startRoom.getId() + " (" + startRoom.getName() + ").");
        }

        GameState state = new GameState(lab, players, pool, GameMode.AUTOMATIC);

        System.out.println("\n=== Início do jogo (Bots) ===");
        System.out.println("Objetivo: chegar à sala central (" + lab.getCenterRoom().getName() + ").");
        System.out.println("A partir de agora os bots jogam sozinhos...\n");

        while (!state.isGameOver()) {
            state.nextTurn();
        }

        System.out.println("\n=== Fim do jogo (Bots) ===");
        Player winner = state.getWinner();
        if (winner != null) {
            System.out.println("Vencedor: " + winner.getName());
        } else {
            System.out.println("O jogo terminou sem vencedor definido.");
        }

        System.out.println("\n--- Estatísticas dos bots ---");
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            System.out.println("\n" + p.getName() + ":");
            System.out.println(p.getStats());
        }

        try {
            String reportFile = GameReportJson.generateAndSave(state);
            System.out.println("Relatório do jogo guardado em: " + reportFile);
        } catch (IOException e) {
            System.out.println("Erro ao guardar relatório do jogo: " + e.getMessage());
        }

    }

    /**
     * Escolhe aleatoriamente uma sala de entrada (ENTRY) do labirinto.
     *
     * @param lab labirinto onde procurar entradas
     * @param rnd gerador de números aleatórios
     * @return uma sala do tipo ENTRY, ou {@code null} se não houver entradas
     */
    private static Room escolherEntradaAleatoria(Labyrinth lab, Random rnd) {
        UnorderedListADT<Room> entries = lab.getEntryRooms();
        if (entries == null) {
            return null;
        }

        int count = 0;
        Iterator<Room> itCount = entries.iterator();
        while (itCount.hasNext()) {
            Room r = itCount.next();
            if (r != null && r.getType() == RoomType.ENTRY) {
                count++;
            }
        }

        if (count == 0) {
            return null;
        }

        int targetIndex = rnd.nextInt(count);

        int idx = 0;
        Iterator<Room> it = entries.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r != null && r.getType() == RoomType.ENTRY) {
                if (idx == targetIndex) {
                    return r;
                }
                idx++;
            }
        }

        return null;
    }
}
