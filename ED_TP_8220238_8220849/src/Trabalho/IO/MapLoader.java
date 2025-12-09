package Trabalho.IO;

import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lê um ficheiro JSON e constrói um Labyrinth
 * com salas, corredores e (opcionalmente) alavancas.
 * <p>
 * Formato esperado:
 * <p>
 * {
 * "rooms": [
 * {
 * "id": 1,
 * "name": "Entrada 1",
 * "type": "ENTRY",
 * "hasRiddle": false,
 * "lever": { "correctIndex": 1 },
 * "x": 220,
 * "y": 300
 * },
 * ...
 * ],
 * "corridors": [
 * { "from": 1, "to": 2, "weight": 1.0, "locked": false },
 * ...
 * ]
 * }
 */
public class MapLoader {

    private static Room[] lastRoomsWithLayout = null;
    private static Point[] lastPositions = null;
    private static boolean lastHasLayout = false;

    /**
     * Lê o ficheiro de mapa JSON e constrói um novo {@link Labyrinth}.
     * <p>
     * Também valida:
     * <ul>
     *     <li>que existe exatamente uma sala CENTER;</li>
     *     <li>que existe pelo menos uma sala ENTRY;</li>
     *     <li>que os corredores referem IDs de salas válidos.</li>
     * </ul>
     * Se todas as salas tiverem coordenadas (x,y), guarda essas informações
     * nos campos estáticos {@link #lastRoomsWithLayout} e
     * {@link #lastPositions}.
     *
     * @param filePath caminho do ficheiro JSON do mapa
     * @return labirinto construído
     * @throws IOException              se ocorrer um erro de leitura
     * @throws ParseException           se o JSON for inválido
     * @throws IllegalArgumentException se a estrutura do ficheiro não corresponder
     *                                  ao formato esperado
     */
    public static Labyrinth loadFromJson(String filePath)
            throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object rootObj = parser.parse(reader);
            if (!(rootObj instanceof JSONObject)) {
                throw new IllegalArgumentException("Root do ficheiro de mapa não é um objeto JSON.");
            }

            JSONObject root = (JSONObject) rootObj;

            Labyrinth lab = new Labyrinth();

            Object roomsRaw = root.get("rooms");
            if (!(roomsRaw instanceof JSONArray)) {
                throw new IllegalArgumentException("\"rooms\" não é um array no ficheiro de mapa.");
            }
            JSONArray roomsArray = (JSONArray) roomsRaw;

            int entryCount = 0;
            int centerCount = 0;

            int nRooms = roomsArray.size();
            Room[] layoutRooms = new Room[nRooms];
            Point[] layoutPositions = new Point[nRooms];
            boolean allHaveLayout = true;

            for (int i = 0; i < roomsArray.size(); i++) {
                Object rRaw = roomsArray.get(i);
                if (!(rRaw instanceof JSONObject)) {
                    throw new IllegalArgumentException("Entrada em rooms[" + i + "] não é um objeto.");
                }

                JSONObject rObj = (JSONObject) rRaw;

                Object idRaw = rObj.get("id");
                if (!(idRaw instanceof Long)) {
                    throw new IllegalArgumentException("Campo \"id\" em rooms[" + i + "] não é inteiro.");
                }
                int id = ((Long) idRaw).intValue();

                Object nameRaw = rObj.get("name");
                if (!(nameRaw instanceof String)) {
                    throw new IllegalArgumentException("Campo \"name\" em rooms[" + i + "] não é string.");
                }
                String name = (String) nameRaw;

                Object typeRaw = rObj.get("type");
                if (!(typeRaw instanceof String)) {
                    throw new IllegalArgumentException("Campo \"type\" em rooms[" + i + "] não é string.");
                }
                RoomType type = RoomType.valueOf((String) typeRaw);

                Room room = new Room(id, name, type);

                if (type == RoomType.ENTRY) {
                    entryCount++;
                } else if (type == RoomType.CENTER) {
                    centerCount++;
                }

                Object riddleRaw = rObj.get("hasRiddle");
                boolean hasRiddle = false;
                if (riddleRaw instanceof Boolean) {
                    hasRiddle = (Boolean) riddleRaw;
                }
                room.setHasRiddle(hasRiddle);

                Object leverRaw = rObj.get("lever");
                if (leverRaw instanceof JSONObject) {
                    JSONObject leverObj = (JSONObject) leverRaw;
                    Object correctRaw = leverObj.get("correctIndex");
                    if (correctRaw instanceof Long) {
                        int correctIndex = ((Long) correctRaw).intValue();
                        LeverPuzzle puzzle = new LeverPuzzle(correctIndex);
                        Lever lever = new Lever(puzzle);
                        room.setLever(lever);
                    }
                }

                Object xRaw = rObj.get("x");
                Object yRaw = rObj.get("y");
                if (xRaw instanceof Long && yRaw instanceof Long) {
                    int x = ((Long) xRaw).intValue();
                    int y = ((Long) yRaw).intValue();
                    layoutPositions[i] = new Point(x, y);
                } else {
                    layoutPositions[i] = null;
                    allHaveLayout = false;
                }

                layoutRooms[i] = room;

                lab.addRoom(room);
            }

            if (centerCount != 1) {
                throw new IllegalArgumentException(
                        "O mapa tem de ter exatamente uma sala CENTER, mas tem " + centerCount);
            }

            if (entryCount == 0) {
                throw new IllegalArgumentException(
                        "O mapa tem de ter pelo menos uma sala ENTRY.");
            }

            Object corridorsRaw = root.get("corridors");
            if (corridorsRaw instanceof JSONArray) {
                JSONArray corridorsArray = (JSONArray) corridorsRaw;

                for (int i = 0; i < corridorsArray.size(); i++) {
                    Object cRaw = corridorsArray.get(i);
                    if (!(cRaw instanceof JSONObject)) {
                        throw new IllegalArgumentException("Entrada em corridors[" + i + "] não é um objeto.");
                    }
                    JSONObject cObj = (JSONObject) cRaw;

                    Object fromRaw = cObj.get("from");
                    Object toRaw = cObj.get("to");
                    if (!(fromRaw instanceof Long) || !(toRaw instanceof Long)) {
                        throw new IllegalArgumentException("Campos \"from\"/\"to\" em corridors[" + i + "] não são inteiros.");
                    }
                    int fromId = ((Long) fromRaw).intValue();
                    int toId = ((Long) toRaw).intValue();

                    Object wRaw = cObj.get("weight");
                    double weight = 1.0;
                    if (wRaw instanceof Number) {
                        weight = ((Number) wRaw).doubleValue();
                    }

                    Object lockedRaw = cObj.get("locked");
                    boolean locked = false;
                    if (lockedRaw instanceof Boolean) {
                        locked = (Boolean) lockedRaw;
                    }

                    Room fromRoom = lab.findRoomById(fromId);
                    Room toRoom = lab.findRoomById(toId);

                    if (fromRoom == null || toRoom == null) {
                        throw new IllegalArgumentException(
                                "Corridor [" + i + "] refere salas inexistentes: from=" +
                                        fromId + ", to=" + toId);
                    }

                    lab.addCorridor(fromRoom, toRoom, weight, locked);
                }
            }

            if (allHaveLayout) {
                lastRoomsWithLayout = layoutRooms;
                lastPositions = layoutPositions;
                lastHasLayout = true;
            } else {
                lastRoomsWithLayout = null;
                lastPositions = null;
                lastHasLayout = false;
            }

            return lab;
        }
    }

    /**
     * Indica se o último mapa carregado tinha layout visual completo
     * (ou seja, todas as salas com coordenadas x,y definidas).
     *
     * @return {@code true} se existir layout completo, {@code false} caso contrário
     */
    public static boolean hasLastLayout() {
        return lastHasLayout;
    }

    /**
     * Devolve o array de salas do último layout carregado.
     * <p>
     * Só é significativo se {@link #hasLastLayout()} for {@code true}.
     *
     * @return array de salas do layout ou {@code null} se não existir
     */
    public static Room[] getLastRoomsWithLayout() {
        return lastRoomsWithLayout;
    }

    /**
     * Devolve o array de posições do último layout carregado.
     * <p>
     * Cada índice corresponde à sala no mesmo índice de
     * {@link #getLastRoomsWithLayout()}.
     *
     * @return array de pontos (x,y) ou {@code null} se não existir layout
     */
    public static Point[] getLastPositions() {
        return lastPositions;
    }
}
