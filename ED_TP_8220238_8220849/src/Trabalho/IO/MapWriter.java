package Trabalho.IO;

import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Map.Corridor;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Responsável por serializar um {@link Labyrinth} para ficheiros JSON.
 * <p>
 * Suporta:
 * <ul>
 *     <li>guardar apenas a estrutura lógica (salas, corredores, enigmas, alavancas);</li>
 *     <li>guardar também o layout visual (coordenadas das salas), se fornecido.</li>
 * </ul>
 */
public class MapWriter {

    /**
     * Guarda apenas a estrutura lógica do labirinto (sem posições das salas).
     *
     * @param lab      labirinto a exportar
     * @param filePath caminho do ficheiro JSON de saída
     * @throws IOException se ocorrer algum erro de escrita
     */
    public static void saveToJson(Labyrinth lab, String filePath) throws IOException {
        JSONObject root = new JSONObject();

        JSONArray roomsArray = new JSONArray();
        Iterator<Room> itRooms = lab.getRooms().iterator();
        while (itRooms.hasNext()) {
            Room r = itRooms.next();

            JSONObject jr = new JSONObject();
            jr.put("id", r.getId());
            jr.put("name", r.getName());
            jr.put("type", r.getType().name());
            jr.put("hasRiddle", r.hasRiddle());

            if (r.hasLever() && r.getLever() != null && r.getLever().getPuzzle() != null) {
                Lever lever = r.getLever();
                LeverPuzzle puzzle = lever.getPuzzle();

                JSONObject jl = new JSONObject();
                jl.put("correctIndex", puzzle.getCorrectIndex());

                jr.put("lever", jl);
            } else {
                jr.put("lever", null);
            }

            roomsArray.add(jr);
        }
        root.put("rooms", roomsArray);

        JSONArray corridorsArray = new JSONArray();
        Iterator<Corridor> itCorr = lab.getCorridors().iterator();
        while (itCorr.hasNext()) {
            Corridor c = itCorr.next();

            JSONObject jc = new JSONObject();
            jc.put("from", c.getFrom().getId());
            jc.put("to", c.getTo().getId());
            jc.put("weight", c.getWeight());
            jc.put("locked", c.isLocked());

            corridorsArray.add(jc);
        }
        root.put("corridors", corridorsArray);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(root.toJSONString());
        }
    }

    /**
     * Guarda a estrutura lógica do labirinto e também o layout visual.
     * <p>
     * Os arrays {@code rooms} e {@code positions} devem estar alinhados:
     * mesmo índice = mesma sala (com a respetiva posição).
     * <p>
     * Se um ID de sala do labirinto não aparecer em {@code rooms}, essa sala
     * é exportada sem coordenadas (continua a ser válida).
     *
     * @param lab       labirinto a exportar
     * @param rooms     array de salas com layout conhecido
     * @param positions array de posições correspondentes às salas do array rooms
     * @param filePath  caminho do ficheiro JSON de saída
     * @throws IOException              se ocorrer algum erro de escrita
     * @throws IllegalArgumentException se os arrays forem nulos ou tiverem tamanhos diferentes
     */
    public static void saveToJson(Labyrinth lab,
                                  Room[] rooms,
                                  Point[] positions,
                                  String filePath) throws IOException {

        if (rooms == null || positions == null || rooms.length != positions.length) {
            throw new IllegalArgumentException("rooms e positions têm de ter o mesmo tamanho e não podem ser null.");
        }

        JSONObject root = new JSONObject();

        JSONArray roomsArray = new JSONArray();
        Iterator<Room> itRooms = lab.getRooms().iterator();
        while (itRooms.hasNext()) {
            Room r = itRooms.next();

            JSONObject jr = new JSONObject();
            jr.put("id", r.getId());
            jr.put("name", r.getName());
            jr.put("type", r.getType().name());
            jr.put("hasRiddle", r.hasRiddle());

            if (r.hasLever() && r.getLever() != null && r.getLever().getPuzzle() != null) {
                Lever lever = r.getLever();
                LeverPuzzle puzzle = lever.getPuzzle();

                JSONObject jl = new JSONObject();
                jl.put("correctIndex", puzzle.getCorrectIndex());

                jr.put("lever", jl);
            } else {
                jr.put("lever", null);
            }

            Point p = findPositionForRoom(r, rooms, positions);
            if (p != null) {
                jr.put("x", p.x);
                jr.put("y", p.y);
            }

            roomsArray.add(jr);
        }
        root.put("rooms", roomsArray);

        JSONArray corridorsArray = new JSONArray();
        Iterator<Corridor> itCorr = lab.getCorridors().iterator();
        while (itCorr.hasNext()) {
            Corridor c = itCorr.next();

            JSONObject jc = new JSONObject();
            jc.put("from", c.getFrom().getId());
            jc.put("to", c.getTo().getId());
            jc.put("weight", c.getWeight());
            jc.put("locked", c.isLocked());

            corridorsArray.add(jc);
        }
        root.put("corridors", corridorsArray);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(root.toJSONString());
        }
    }

    /**
     * Pesquisa linear que encontra a posição associada à sala {@code r},
     * usando os arrays {@code rooms} e {@code positions}.
     * <p>
     * Não utiliza {@code Map} / {@code HashMap}, por restrição do enunciado.
     *
     * @param r         sala para a qual se pretende obter a posição
     * @param rooms     array de salas com layout conhecido
     * @param positions array de posições correspondentes
     * @return posição encontrada ou {@code null} se a sala não existir no array
     */
    private static Point findPositionForRoom(Room r, Room[] rooms, Point[] positions) {
        int id = r.getId();
        for (int i = 0; i < rooms.length; i++) {
            Room candidate = rooms[i];
            if (candidate != null && candidate.getId() == id) {
                return positions[i];
            }
        }
        return null;
    }
}
