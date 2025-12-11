package trabalho.menu;

import trabalho.io.MapWriter;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;
import trabalho.view.LabyrinthViewer;
import interfaces.UnorderedListADT;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Editor interativo de mapas (labirintos).
 * <p>
 * Permite:
 * <ul>
 *     <li>criar salas (ENTRY, NORMAL, CENTER);</li>
 *     <li>atribuir enigmas ou alavancas a salas NORMAL;</li>
 *     <li>criar corredores entre salas;</li>
 *     <li>listar salas e corredores existentes;</li>
 *     <li>guardar o mapa em ficheiro JSON.</li>
 * </ul>
 */
public class MapEditorMenu {

    private static final Scanner in = new Scanner(System.in);
    private static int nextRoomId = 1;

    /**
     * Ciclo principal do editor de mapas.
     *
     * @param args não usado
     */
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();
        boolean running = true;

        SwingUtilities.invokeLater(() -> {
            LabyrinthViewer.resetLayout();
            LabyrinthViewer.show(lab);
        });

        while (running) {
            System.out.println("\n=== Editor de Mapas ===");
            System.out.println("1 - Criar sala");
            System.out.println("2 - Criar corredor");
            System.out.println("3 - Guardar mapa");
            System.out.println("4 - Listar salas");
            System.out.println("5 - Listar corredores");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String op = in.nextLine().trim();

            switch (op) {
                case "1" -> criarSala(lab);
                case "2" -> criarCorredor(lab);
                case "3" -> guardarMapa(lab);
                case "4" -> listarSalas(lab.getRooms());
                case "5" -> listarCorredores(lab);
                case "0" -> running = false;
                default -> System.out.println("Opção inválida.");
            }
        }

        System.out.println("A sair do editor.");
    }

    /**
     * Cria uma nova sala no labirinto, pedindo o nome, tipo
     * e (no caso de NORMAL) o conteúdo especial (nada, enigma, alavanca).
     *
     * @param lab labirinto onde adicionar a sala
     */
    private static void criarSala(Labyrinth lab) {
        System.out.println("\n--- Criar sala ---");

        System.out.print("Nome da sala: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Nome não pode ser vazio.");
            return;
        }

        System.out.println("Tipo da sala:");
        System.out.println("1 - ENTRY (entrada)");
        System.out.println("2 - NORMAL");
        System.out.println("3 - CENTER");
        System.out.print("Opção: ");
        String opt = in.nextLine().trim();

        RoomType type;
        switch (opt) {
            case "1" -> type = RoomType.ENTRY;
            case "2" -> type = RoomType.NORMAL;
            case "3" -> type = RoomType.CENTER;
            default -> {
                System.out.println("Tipo inválido.");
                return;
            }
        }

        if (type == RoomType.CENTER && lab.getCenterRoom() != null) {
            System.out.println("Já existe uma sala CENTER. Não podes criar outra.");
            return;
        }

        if (type == RoomType.ENTRY && contarEntradas(lab) >= 3) {
            System.out.println("Já existem 3 entradas. Limite atingido.");
            return;
        }

        Room room = new Room(nextRoomId++, name, type);

        if (type == RoomType.NORMAL) {
            System.out.println("Sala NORMAL – conteúdo especial?");
            System.out.println("1 - Nada (sala normal)");
            System.out.println("2 - Sala com ENIGMA");
            System.out.println("3 - Sala com ALAVANCA");
            System.out.print("Opção: ");
            String extra = in.nextLine().trim();

            switch (extra) {
                case "2" -> {
                    trabalho.events.Question dummyQ =
                            new trabalho.events.Question(
                                    "ENIGMA_POR_DEFINIR",
                                    new String[]{"A", "B", "C"},
                                    0
                            );
                    room.setRiddle(dummyQ);
                }
                case "3" -> {
                    int correctLeverIndex;
                    while (true) {
                        System.out.print("Qual a alavanca correta (1 a 3): ");
                        String line = in.nextLine().trim();
                        try {
                            int choice = Integer.parseInt(line);
                            if (choice >= 1 && choice <= 3) {
                                correctLeverIndex = choice - 1;
                                break;
                            } else {
                                System.out.println("Tem de ser um número entre 1 e 3.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Número inválido, tenta outra vez.");
                        }
                    }

                    trabalho.events.LeverPuzzle puzzle =
                            new trabalho.events.LeverPuzzle(correctLeverIndex);
                    trabalho.events.Lever lever =
                            new trabalho.events.Lever(puzzle);
                    room.setLever(lever);
                }
                default -> {
                }
            }

        }

        lab.addRoom(room);
        System.out.println("Sala criada: " + room);

        SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab));
    }

    /**
     * Conta quantas salas ENTRY existem no labirinto.
     *
     * @param lab labirinto
     * @return número de salas de entrada
     */
    private static int contarEntradas(Labyrinth lab) {
        int count = 0;
        Iterator<Room> it = lab.getEntryRooms().iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * Cria um novo corredor entre duas salas escolhidas pelo utilizador,
     * com peso e estado de bloqueio configuráveis.
     *
     * @param lab labirinto onde criar o corredor
     */
    private static void criarCorredor(Labyrinth lab) {
        System.out.println("\n--- Criar corredor ---");

        if (!temPeloMenosNSalas(lab, 2)) {
            System.out.println("Precisas de pelo menos duas salas para criar um corredor.");
            return;
        }

        UnorderedListADT<Room> rooms = lab.getRooms();
        listarSalas(rooms);

        System.out.print("ID da sala de origem: ");
        int fromId = lerInt();
        System.out.print("ID da sala de destino: ");
        int toId = lerInt();

        if (fromId == toId) {
            System.out.println("Origem e destino não podem ser a mesma sala.");
            return;
        }

        Room from = encontrarSalaPorId(rooms, fromId);
        Room to = encontrarSalaPorId(rooms, toId);

        if (from == null || to == null) {
            System.out.println("ID de sala inválido.");
            return;
        }

        System.out.print("Peso do corredor (double, ex: 1.0) [Enter para 1.0]: ");
        double weight = lerDoubleComDefault(1.0);

        System.out.print("Corredor começa bloqueado? (s/n): ");
        String resp = in.nextLine().trim().toLowerCase();
        boolean locked = resp.startsWith("s");

        boolean created = lab.addCorridor(from, to, weight, locked);
        if (created) {
            System.out.println("Corredor criado entre " + from.getName() + " e " + to.getName()
                    + " (locked=" + locked + ").");
        } else {
            System.out.println("Já existe um corredor entre essas salas.");
        }

        SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab));
    }

    /**
     * Verifica se o labirinto tem pelo menos {@code n} salas.
     */
    private static boolean temPeloMenosNSalas(Labyrinth lab, int n) {
        int count = 0;
        Iterator<Room> it = lab.getRooms().iterator();
        while (it.hasNext()) {
            it.next();
            count++;
            if (count >= n) return true;
        }
        return false;
    }

    /**
     * Procura uma sala por ID numa lista de salas.
     *
     * @param rooms lista de salas
     * @param id    identificador da sala
     * @return sala correspondente, ou {@code null} se não existir
     */
    private static Room encontrarSalaPorId(UnorderedListADT<Room> rooms, int id) {
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.getId() == id) return r;
        }
        return null;
    }

    /**
     * Pede um nome de ficheiro e guarda o mapa em JSON na pasta {@code src/Mapas}.
     *
     * @param lab labirinto a guardar
     */
    private static void guardarMapa(Labyrinth lab) {
        System.out.println("\n--- Guardar mapa ---");
        System.out.print("Nome do ficheiro (ex: mapa1.json): ");
        String filename = in.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Nome de ficheiro inválido.");
            return;
        }

        String folder = "src/Mapas";

        java.io.File dir = new java.io.File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = folder + "/" + filename;

        try {
            MapWriter.saveToJson(lab, filePath);
            System.out.println("Mapa guardado em: " + filePath);
        } catch (IOException e) {
            System.out.println("Erro ao guardar mapa: " + e.getMessage());
        }
    }

    /**
     * Lista todas as salas existentes.
     *
     * @param rooms lista de salas
     */
    private static void listarSalas(UnorderedListADT<Room> rooms) {
        System.out.println("\n--- Salas ---");
        Iterator<Room> it = rooms.iterator();
        if (!it.hasNext()) {
            System.out.println("(nenhuma sala criada)");
            return;
        }
        while (it.hasNext()) {
            Room r = it.next();
            System.out.println("ID=" + r.getId() + " | " + r.getName() + " | " + r.getType());
        }
    }

    /**
     * Lista todos os corredores existentes no labirinto.
     *
     * @param lab labirinto
     */
    private static void listarCorredores(Labyrinth lab) {
        System.out.println("\n--- Corredores ---");

        Iterator<trabalho.map.Corridor> it = lab.getCorridors().iterator();
        if (!it.hasNext()) {
            System.out.println("(nenhum corredor criado)");
            return;
        }

        while (it.hasNext()) {
            trabalho.map.Corridor c = it.next();
            System.out.println(
                    "from=" + c.getFrom().getId() +
                            " (" + c.getFrom().getName() + ")" +
                            " -> to=" + c.getTo().getId() +
                            " (" + c.getTo().getName() + ")" +
                            " | weight=" + c.getWeight() +
                            " | locked=" + c.isLocked()
            );
        }
    }

    /**
     * Lê um inteiro da consola, repetindo até o utilizador introduzir
     * um valor válido.
     *
     * @return inteiro lido
     */
    private static int lerInt() {
        while (true) {
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido, tenta outra vez: ");
            }
        }
    }

    /**
     * Lê um double da consola. Se o utilizador carregar apenas ENTER,
     * devolve o valor por defeito.
     *
     * @param defaultValue valor por defeito
     * @return valor introduzido ou o valor por defeito em caso de erro
     */
    private static double lerDoubleComDefault(double defaultValue) {
        String line = in.nextLine().trim();
        if (line.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, a usar valor por defeito: " + defaultValue);
            return defaultValue;
        }
    }
}
