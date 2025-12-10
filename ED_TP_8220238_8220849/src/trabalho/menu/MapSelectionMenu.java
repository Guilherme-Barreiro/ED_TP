package trabalho.menu;

import trabalho.io.MapLoader;
import trabalho.map.Corridor;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;
import Estruturas.ArrayUnorderedList;
import Estruturas.LinkedQueue;
import interfaces.UnorderedListADT;
import interfaces.QueueADT;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Menu responsável pela seleção de mapas (ficheiros JSON) da pasta {@code src/Mapas}.
 * <p>
 * Funcionalidades:
 * <ul>
 *     <li>listar ficheiros .json disponíveis na pasta de mapas;</li>
 *     <li>permitir ao utilizador escolher um mapa por índice;</li>
 *     <li>carregar o mapa com {@link MapLoader};</li>
 *     <li>validar se todas as entradas (ENTRY) conseguem chegar à sala central (CENTER),
 *         ignorando o estado de bloqueio dos corredores.</li>
 * </ul>
 */
public class MapSelectionMenu {

    private static final String MAPS_FOLDER = "src/Mapas";

    /**
     * Menu que:
     * <ul>
     *   <li>lista os ficheiros .json na pasta Mapas;</li>
     *   <li>deixa o utilizador escolher um;</li>
     *   <li>tenta carregar com {@link MapLoader};</li>
     *   <li>valida se todas as ENTRY chegam ao CENTER (ignorando locked);</li>
     *   <li>repete até ter um mapa válido ou o utilizador desistir (Ctrl+C).</li>
     * </ul>
     *
     * @param in {@link Scanner} a usar para ler do teclado
     * @return um {@link Labyrinth} válido, ou {@code null} se não for possível
     * carregar/validar nenhum mapa
     */
    public static Labyrinth escolherMapa(Scanner in) {
        while (true) {
            File folder = new File(MAPS_FOLDER);
            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("Pasta \"" + MAPS_FOLDER + "\" não existe ou não é uma diretoria.");
                return null;
            }

            File[] files = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".json");
                }
            });

            if (files == null || files.length == 0) {
                System.out.println("Não foram encontrados ficheiros .json na pasta \"" + MAPS_FOLDER + "\".");
                return null;
            }

            System.out.println("\n=== Mapas disponíveis ===");
            for (int i = 0; i < files.length; i++) {
                System.out.println("  " + i + " - " + files[i].getName());
            }

            int choice = -1;
            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("Escolhe o índice do mapa: ");
                String line = in.nextLine().trim();
                try {
                    choice = Integer.parseInt(line);
                    if (choice >= 0 && choice < files.length) {
                        validChoice = true;
                    } else {
                        System.out.println("Índice inválido.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido.");
                }
            }

            File chosen = files[choice];
            String fullPath = MAPS_FOLDER + File.separator + chosen.getName();

            try {
                Labyrinth lab = MapLoader.loadFromJson(fullPath);
                if (validarMapa(lab)) {
                    System.out.println("Mapa \"" + chosen.getName() + "\" carregado e validado com sucesso.");
                    return lab;
                } else {
                    System.out.println("Mapa inválido: pelo menos uma ENTRY não consegue chegar ao CENTER considerando bloqueios e alavancas.");
                    System.out.println("Escolhe outro mapa.\n");
                }
            } catch (IOException | ParseException e) {
                System.out.println("Erro ao carregar o mapa: " + e.getMessage());
                System.out.println("Escolhe outro mapa.\n");
            } catch (IllegalArgumentException e) {
                System.out.println("Mapa inválido (" + e.getMessage() + "). Escolhe outro.\n");
            }
        }
    }

    /**
     * Valida um {@link Labyrinth}:
     * <ul>
     *   <li>tem CENTER não nulo;</li>
     *   <li>tem pelo menos uma ENTRY;</li>
     *   <li>todas as ENTRY conseguem chegar ao CENTER, através dos corredores,
     *       respeitando bloqueios, mas assumindo que o jogador consegue ativar
     *       todas as alavancas a que tenha acesso.</li>
     * </ul>
     *
     * @param lab labirinto a validar
     * @return {@code true} se o mapa for considerado válido, {@code false} caso contrário
     */
    private static boolean validarMapa(Labyrinth lab) {
        if (lab == null) {
            return false;
        }

        Room center = lab.getCenterRoom();
        if (center == null) {
            System.out.println("Mapa sem sala CENTER.");
            return false;
        }

        UnorderedListADT<Room> entryRooms = lab.getEntryRooms();
        if (entryRooms == null) {
            System.out.println("Mapa sem lista de ENTRY rooms.");
            return false;
        }

        int entryCount = 0;
        Iterator<Room> itEntriesCount = entryRooms.iterator();
        while (itEntriesCount.hasNext()) {
            Room r = itEntriesCount.next();
            if (r != null && r.getType() == RoomType.ENTRY) {
                entryCount++;
            }
        }

        if (entryCount == 0) {
            System.out.println("Mapa sem nenhuma sala ENTRY.");
            return false;
        }

        Iterator<Room> itEntries = entryRooms.iterator();
        while (itEntries.hasNext()) {
            Room entry = itEntries.next();
            if (entry != null && entry.getType() == RoomType.ENTRY) {
                boolean ok = canEntryReachCenterConsideringLevers(lab, entry, center);
                if (!ok) {
                    System.out.println(
                            "Entrada com id " + entry.getId() +
                                    " não tem caminho até ao CENTER, mesmo considerando alavancas."
                    );
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se uma dada sala ENTRY consegue chegar ao CENTER,
     * começando com os corredores no estado atual (locked/unlocked),
     * mas assumindo que, sempre que chegar a uma sala com alavanca,
     * consegue ativá-la e, na prática, desbloquear todos os corredores
     * ligados a essa sala.
     */
    private static boolean canEntryReachCenterConsideringLevers(Labyrinth lab, Room entry, Room center) {
        if (entry == null || center == null) {
            return false;
        }

        UnorderedListADT<Room> visitados = new ArrayUnorderedList<>();
        UnorderedListADT<Room> leverActivatedRooms = new ArrayUnorderedList<>();
        QueueADT<Room> fila = new LinkedQueue<>();

        visitados.addToRear(entry);
        fila.enqueue(entry);

        while (!fila.isEmpty()) {
            Room atual = fila.dequeue();

            if (atual == center) {
                return true;
            }

            if (atual.hasLever() && !contemSala(leverActivatedRooms, atual)) {
                leverActivatedRooms.addToRear(atual);
            }

            Iterator<Corridor> itCorr = lab.getCorridors().iterator();
            while (itCorr.hasNext()) {
                Corridor c = itCorr.next();

                Room outro = null;
                if (c.getFrom() == atual) {
                    outro = c.getTo();
                } else if (c.getTo() == atual) {
                    outro = c.getFrom();
                }

                if (outro == null) {
                    continue;
                }

                boolean extremidadeComLeverAtivada =
                        contemSala(leverActivatedRooms, c.getFrom()) ||
                                contemSala(leverActivatedRooms, c.getTo());

                if (c.isLocked() && !extremidadeComLeverAtivada) {
                    continue;
                }

                if (!contemSala(visitados, outro)) {
                    visitados.addToRear(outro);
                    fila.enqueue(outro);
                }
            }
        }

        return false;
    }

    /**
     * Procura uma sala numa {@link UnorderedListADT} usando comparação por referência.
     *
     * @param lista lista de salas
     * @param alvo  sala a procurar
     * @return {@code true} se a sala estiver presente, {@code false} caso contrário
     */
    private static boolean contemSala(UnorderedListADT<Room> lista, Room alvo) {
        Iterator<Room> it = lista.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r == alvo) {
                return true;
            }
        }
        return false;
    }
}
