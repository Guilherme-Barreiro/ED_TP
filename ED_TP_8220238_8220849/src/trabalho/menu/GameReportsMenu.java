package trabalho.menu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Menu responsável por listar e visualizar relatórios de jogos anteriores
 * guardados em ficheiros JSON na pasta {@code src/Game Reports}.
 * <p>
 * Permite ao utilizador:
 * <ul>
 *     <li>ver a lista de relatórios disponíveis;</li>
 *     <li>escolher um relatório pelo índice;</li>
 *     <li>imprimir para a consola um resumo dos turnos, vencedor,
 *         percurso e eventos de cada jogador.</li>
 * </ul>
 */
public class GameReportsMenu {
    /**
     * Ponto de entrada do menu de relatórios.
     *
     * @param args não usado
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        File folder = new File("src/Game Reports");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Não existe nenhuma pasta \"Game Reports\" com relatórios.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("Não foram encontrados relatórios de jogo.");
            return;
        }

        System.out.println("\n=== Relatórios disponíveis ===");
        for (int i = 0; i < files.length; i++) {
            System.out.println(i + " - " + files[i].getName());
        }

        int choice = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print("Escolhe o índice do relatório: ");
            String line = in.nextLine().trim();
            try {
                choice = Integer.parseInt(line);
                if (choice >= 0 && choice < files.length) {
                    valid = true;
                } else {
                    System.out.println("Índice inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        }

        File selected = files[choice];
        mostrarRelatorio(selected);
    }

    /**
     * Lê um ficheiro JSON de relatório de jogo e imprime um resumo
     * legível na consola (turnos, vencedor, percurso e eventos
     * de cada jogador).
     *
     * @param file ficheiro de relatório a mostrar
     */
    private static void mostrarRelatorio(File file) {
        System.out.println("\n=== Relatório: " + file.getName() + " ===");
        JSONParser parser = new JSONParser();

        try (FileReader fr = new FileReader(file)) {
            Object rootObj = parser.parse(fr);
            if (!(rootObj instanceof JSONObject)) {
                System.out.println("Ficheiro JSON inválido.");
                return;
            }

            JSONObject root = (JSONObject) rootObj;

            Object turnsRaw = root.get("turns");
            long turns = turnsRaw instanceof Number ? ((Number) turnsRaw).longValue() : -1;

            Object winnerRaw = root.get("winner");
            String winner = winnerRaw != null ? winnerRaw.toString() : null;

            System.out.println("Turnos: " + (turns >= 0 ? turns : "?"));
            System.out.println("Vencedor: " + (winner != null ? winner : "nenhum"));

            Object playersRaw = root.get("players");
            if (!(playersRaw instanceof JSONArray)) {
                System.out.println("Estrutura de jogadores inválida no relatório.");
                return;
            }

            JSONArray players = (JSONArray) playersRaw;
            for (Object obj : players) {
                if (!(obj instanceof JSONObject)) {
                    continue;
                }
                JSONObject jp = (JSONObject) obj;

                Object nameRaw = jp.get("name");
                String name = nameRaw != null ? nameRaw.toString() : "Desconhecido";

                Object correctRaw = jp.get("correctRiddles");
                long correct = correctRaw instanceof Number ? ((Number) correctRaw).longValue() : 0;

                Object wrongRaw = jp.get("wrongRiddles");
                long wrong = wrongRaw instanceof Number ? ((Number) wrongRaw).longValue() : 0;

                Object pathRaw = jp.get("path");
                StringBuilder pathStr = new StringBuilder();
                if (pathRaw instanceof JSONArray) {
                    JSONArray pathArr = (JSONArray) pathRaw;
                    for (int i = 0; i < pathArr.size(); i++) {
                        Object v = pathArr.get(i);
                        if (v instanceof Number) {
                            pathStr.append(((Number) v).longValue());
                        } else {
                            pathStr.append(v);
                        }
                        if (i < pathArr.size() - 1) {
                            pathStr.append(" -> ");
                        }
                    }
                }

                System.out.println("\nJogador: " + name);
                System.out.println("  Enigmas: certos=" + correct + ", errados=" + wrong);
                if (pathStr.length() > 0) {
                    System.out.println("  Caminho: " + pathStr);
                }

                Object eventsRaw = jp.get("events");
                if (eventsRaw instanceof JSONArray) {
                    JSONArray eventsArr = (JSONArray) eventsRaw;
                    if (!eventsArr.isEmpty()) {
                        System.out.println("  Eventos:");
                        for (Object eObj : eventsArr) {
                            if (!(eObj instanceof JSONObject)) {
                                continue;
                            }
                            JSONObject je = (JSONObject) eObj;

                            Object turnRaw = je.get("turn");
                            long turn = turnRaw instanceof Number ? ((Number) turnRaw).longValue() : -1;

                            Object typeRaw = je.get("type");
                            String type = typeRaw != null ? typeRaw.toString() : "?";

                            Object sourceRaw = je.get("source");
                            String source = sourceRaw != null ? sourceRaw.toString() : "null";

                            Object targetRaw = je.get("target");
                            String target = targetRaw != null ? targetRaw.toString() : "null";

                            Object descRaw = je.get("description");
                            String desc = descRaw != null ? descRaw.toString() : "";

                            System.out.println("    [turno " + turn + "] " +
                                    type + " | source=" + source +
                                    " | target=" + target +
                                    " | " + desc);
                        }
                    }
                }
            }

        } catch (IOException | ParseException e) {
            System.out.println("Erro ao ler relatório: " + e.getMessage());
        }
    }

}
