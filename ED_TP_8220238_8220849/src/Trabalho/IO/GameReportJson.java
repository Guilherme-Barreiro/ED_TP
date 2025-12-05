package Trabalho.IO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import Trabalho.Game.GameState;
import Trabalho.Players.Player;
import Trabalho.Map.Room;
import Trabalho.Events.EventLogEntry;

import java.util.Iterator;

public class GameReportJson {

    /**
     * Gera uma string JSON com o relatório do jogo,
     * a partir do GameState.
     */
    public static String generate(GameState state) {
        JSONObject root = new JSONObject();

        root.put("turns", state.getCurrentTurn() - 1);

        Player winner = state.getWinner();
        if (winner != null) {
            root.put("winner", winner.getName());
        } else {
            root.put("winner", null);
        }

        JSONArray playersArray = new JSONArray();
        Iterator<Player> itPlayers = state.getPlayers().iterator();
        while (itPlayers.hasNext()) {
            Player p = itPlayers.next();

            JSONObject jp = new JSONObject();
            jp.put("name", p.getName());

            Room currentRoom = p.getCurrentRoom();
            if (currentRoom != null) {
                jp.put("currentRoom", currentRoom.getId());
            } else {
                jp.put("currentRoom", null);
            }

            jp.put("correctRiddles", p.getStats().getCorrectRiddles());
            jp.put("wrongRiddles", p.getStats().getWrongRiddles());

            JSONArray pathArray = new JSONArray();
            Iterator<Room> itPath = p.getStats().getPath().iterator();
            while (itPath.hasNext()) {
                Room r = itPath.next();
                pathArray.add(r.getId());
            }
            jp.put("path", pathArray);

            JSONArray eventsArray = new JSONArray();
            Iterator<EventLogEntry> itEvents = p.getStats().getEvents().iterator();
            while (itEvents.hasNext()) {
                EventLogEntry ev = itEvents.next();

                JSONObject je = new JSONObject();
                je.put("turn", ev.getTurnNumber());
                je.put("type", ev.getType().name());
                je.put("source", ev.getSource() != null ? ev.getSource().getName() : null);
                je.put("target", ev.getTarget() != null ? ev.getTarget().getName() : null);
                je.put("description", ev.getDescription());

                eventsArray.add(je);
            }
            jp.put("events", eventsArray);

            playersArray.add(jp);
        }

        root.put("players", playersArray);

        return root.toJSONString();
    }
}


