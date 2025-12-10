package trabalho.menu;

import Estruturas.ArrayUnorderedList;
import Estruturas.LinkedQueue;
import interfaces.UnorderedListADT;
import interfaces.QueueADT;
import org.json.simple.parser.ParseException;
import trabalho.io.MapLoader;
import trabalho.map.Corridor;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;
import trabalho.view.LabyrinthPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

public class MapValidationDemo {

    private static final String MAPS_FOLDER = "src/Mapas";

    public static void main(String[] args) {
        File folder = new File(MAPS_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Pasta \"" + MAPS_FOLDER + "\" não existe ou não é uma diretoria.");
            return;
        }

        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });

        if (files == null || files.length == 0) {
            System.out.println("Não foram encontrados ficheiros .json na pasta \"" + MAPS_FOLDER + "\".");
            return;
        }

        System.out.println("=== MapValidationDemo ===");
        System.out.println("A carregar e validar todos os mapas de " + MAPS_FOLDER + "...\n");

        for (File f : files) {
            String fullPath = MAPS_FOLDER + File.separator + f.getName();
            try {
                Labyrinth lab = MapLoader.loadFromJson(fullPath);
                boolean valido = validarMapaDemo(lab);

                String titulo = (valido ? "[VALIDO] " : "[INVALIDO] ") + f.getName();
                System.out.println(titulo);

                abrirJanelaMapa(lab, titulo);

            } catch (IOException | ParseException e) {
                System.out.println("[ERRO] " + f.getName() + " - " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("[INVALIDO] " + f.getName() + " - " + e.getMessage());
            }
        }
    }

    private static void abrirJanelaMapa(Labyrinth lab, String titulo) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(titulo);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            LabyrinthPanel panel = new LabyrinthPanel(lab);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }

    /**
     * Versão de validação usada apenas nesta demo:
     * - tem CENTER;
     * - tem pelo menos uma ENTRY;
     * - para cada ENTRY, verifica se consegue chegar ao CENTER
     *   respeitando locked/unlocked, mas assumindo que todas as salas
     *   com alavanca que sejam alcançáveis podem ser ativadas e, por isso,
     *   desbloqueiam todos os corredores ligados a essa sala.
     */
    private static boolean validarMapaDemo(Labyrinth lab) {
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
     * Verifica se uma ENTRY consegue chegar ao CENTER:
     * - começa com os corredores no estado atual (locked/unlocked);
     * - sempre que chega a uma sala com alavanca, marca essa sala como
     *   "alavanca ativada" e passa a poder usar todos os corredores
     *   ligados a essa sala, mesmo que estejam locked.
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
