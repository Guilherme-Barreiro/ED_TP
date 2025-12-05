package Trabalho.Demo;

import Trabalho.IO.MapWriter;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Colecoes.interfaces.UnorderedListADT;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class MapEditorMain {

    private static final Scanner in = new Scanner(System.in);
    private static int nextRoomId = 1;

    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();
        boolean running = true;

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

    // ===================== OPÇÃO 1: CRIAR SALA =====================

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

        // validações básicas de regra de jogo
        if (type == RoomType.CENTER && lab.getCenterRoom() != null) {
            System.out.println("Já existe uma sala CENTER. Não podes criar outra.");
            return;
        }

        if (type == RoomType.ENTRY && contarEntradas(lab) >= 3) {
            System.out.println("Já existem 3 entradas. Limite atingido.");
            return;
        }

        Room room = new Room(nextRoomId++, name, type);
        lab.addRoom(room);
        System.out.println("Sala criada: " + room);
    }

    private static int contarEntradas(Labyrinth lab) {
        int count = 0;
        Iterator<Room> it = lab.getEntryRooms().iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    // ===================== OPÇÃO 2: CRIAR CORREDOR =====================

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

        lab.addCorridor(from, to, weight, locked);
        System.out.println("Corredor criado entre " + from.getName() + " e " + to.getName()
                + " (locked=" + locked + ").");
    }

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

    private static Room encontrarSalaPorId(UnorderedListADT<Room> rooms, int id) {
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.getId() == id) return r;
        }
        return null;
    }

    // ===================== OPÇÃO 3: GUARDAR MAPA =====================

    private static void guardarMapa(Labyrinth lab) {
        System.out.println("\n--- Guardar mapa ---");
        System.out.print("Nome do ficheiro (ex: mapa1.json): ");
        String filename = in.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Nome de ficheiro inválido.");
            return;
        }

        try {
            MapWriter.saveToJson(lab, filename);
            System.out.println("Mapa guardado em: " + filename);
        } catch (IOException e) {
            System.out.println("Erro ao guardar mapa: " + e.getMessage());
        }
    }

    // ===================== OPÇÃO 4: LISTAR SALAS =====================

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

    // ===================== OPÇÃO 5: LISTAR CORREDORES =====================

    private static void listarCorredores(Labyrinth lab) {
        System.out.println("\n--- Corredores ---");

        Iterator<Trabalho.Map.Corridor> it = lab.getCorridors().iterator();
        if (!it.hasNext()) {
            System.out.println("(nenhum corredor criado)");
            return;
        }

        while (it.hasNext()) {
            Trabalho.Map.Corridor c = it.next();
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

    // ===================== HELPERS DE INPUT =====================

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
