package Trabalho.IO;

import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Map.Corridor;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class MapWriter {

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
            jr.put("hasRiddle", r.hasEnigma());

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
}
