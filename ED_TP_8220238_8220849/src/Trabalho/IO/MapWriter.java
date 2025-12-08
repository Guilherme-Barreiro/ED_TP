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

public class MapWriter {

    /**
     * guarda apenas a estrutura lógic sem posiçoes das salas
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
     * Nova versão: guarda também o layout visual.
     * rooms[] e positions[] devem estar alinhados (mesmo índice = mesma sala).
     * Se algum id de sala do lab não existir em rooms[], simplesmente
     * não leva coordenadas no JSON (continua válido).
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
     * Pesquisa linear: encontra a posição associada à sala r,
     * usando arrays rooms[] e positions[].
     * Não usa Map / HashMap.
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
