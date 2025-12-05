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

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * Lê um ficheiro JSON e constrói um Labyrinth
 * com salas, corredores e (opcionalmente) alavancas.
 *
 * Formato esperado:
 *
 * {
 *   "rooms": [
 *     {
 *       "id": 1,
 *       "name": "Entrada 1",
 *       "type": "ENTRY",
 *       "hasRiddle": false,
 *       "lever": { "correctIndex": 1 } // ou null
 *     },
 *     ...
 *   ],
 *   "corridors": [
 *     { "from": 1, "to": 2, "weight": 1.0, "locked": false },
 *     ...
 *   ]
 * }
 */
public class MapLoader {

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

                lab.addRoom(room);
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

                    Room fromRoom = findRoomById(lab, fromId);
                    Room toRoom = findRoomById(lab, toId);

                    lab.addCorridor(fromRoom, toRoom, weight, locked);
                }
            }

            return lab;
        }
    }

    /**
     * Procura uma sala pelo id, percorrendo a lista de rooms do Labyrinth.
     */
    private static Room findRoomById(Labyrinth lab, int id) {
        Iterator<Room> it = lab.getRooms().iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.getId() == id) {
                return r;
            }
        }
        throw new IllegalArgumentException("Não foi encontrada sala com id " + id + " no labirinto.");
    }
}
