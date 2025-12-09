package Trabalho.Menu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HallOfFameMenu {

    private static final String REPORTS_FOLDER = "src/Game Reports";

    public static void main(String[] args) {
        File folder = new File(REPORTS_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Não existe nenhuma pasta \"" + REPORTS_FOLDER + "\" com relatórios.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("Não foram encontrados relatórios de jogo.");
            return;
        }

        GlobalStats stats = new GlobalStats();

        JSONParser parser = new JSONParser();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            try (FileReader fr = new FileReader(f)) {
                Object rootObj = parser.parse(fr);
                if (!(rootObj instanceof JSONObject)) {
                    continue;
                }
                JSONObject root = (JSONObject) rootObj;

                stats.processReport(root, f.getName());

            } catch (IOException | ParseException e) {
                System.out.println("Erro ao ler relatório " + f.getName() + ": " + e.getMessage());
            }
        }

        stats.printSummary();
    }

    private static class GlobalStats {

        private static class PlayerGlobal {
            String name;
            int wins;
            int totalCorrect;
            int totalWrong;
            int games;

            PlayerGlobal(String name) {
                this.name = name;
            }

            double accuracy() {
                int total = totalCorrect + totalWrong;
                if (total == 0) return 0.0;
                return (double) totalCorrect / total;
            }
        }

        private PlayerGlobal[] players = new PlayerGlobal[0];
        private int playerCount = 0;

        private long longestGameTurns = -1;
        private String longestGameFile = null;

        private PlayerGlobal getOrCreatePlayer(String name) {
            if (name == null) return null;

            for (int i = 0; i < playerCount; i++) {
                if (players[i].name.equals(name)) {
                    return players[i];
                }
            }

            PlayerGlobal pg = new PlayerGlobal(name);

            PlayerGlobal[] newArr = new PlayerGlobal[playerCount + 1];
            for (int i = 0; i < playerCount; i++) {
                newArr[i] = players[i];
            }
            newArr[playerCount] = pg;
            players = newArr;
            playerCount++;

            return pg;
        }

        public void processReport(JSONObject root, String fileName) {
            Object turnsRaw = root.get("turns");
            long turns = turnsRaw instanceof Number ? ((Number) turnsRaw).longValue() : -1;
            if (turns > longestGameTurns) {
                longestGameTurns = turns;
                longestGameFile = fileName;
            }

            Object winnerRaw = root.get("winner");
            if (winnerRaw != null) {
                String winnerName = winnerRaw.toString();
                PlayerGlobal pgWinner = getOrCreatePlayer(winnerName);
                if (pgWinner != null) {
                    pgWinner.wins++;
                    pgWinner.games++;
                }
            }

            Object playersRaw = root.get("players");
            if (!(playersRaw instanceof JSONArray)) {
                return;
            }
            JSONArray playersArray = (JSONArray) playersRaw;

            for (int i = 0; i < playersArray.size(); i++) {
                Object obj = playersArray.get(i);
                if (!(obj instanceof JSONObject)) continue;
                JSONObject jp = (JSONObject) obj;

                String name = jp.get("name") != null ? jp.get("name").toString() : "Desconhecido";
                PlayerGlobal pg = getOrCreatePlayer(name);
                if (pg == null) continue;

                Object correctRaw = jp.get("correctRiddles");
                long correct = correctRaw instanceof Number ? ((Number) correctRaw).longValue() : 0;

                Object wrongRaw = jp.get("wrongRiddles");
                long wrong = wrongRaw instanceof Number ? ((Number) wrongRaw).longValue() : 0;

                pg.totalCorrect += (int) correct;
                pg.totalWrong += (int) wrong;

                if (winnerRaw == null || !name.equals(winnerRaw.toString())) {
                    pg.games++;
                }
            }
        }

        public void printSummary() {
            System.out.println("\n=== Hall of Fame ===");

            if (playerCount == 0) {
                System.out.println("Ainda não há dados suficientes.");
                return;
            }

            PlayerGlobal topWins = players[0];
            for (int i = 1; i < playerCount; i++) {
                if (players[i].wins > topWins.wins) {
                    topWins = players[i];
                }
            }

            PlayerGlobal bestRiddles = null;
            for (int i = 0; i < playerCount; i++) {
                int total = players[i].totalCorrect + players[i].totalWrong;
                if (total == 0) continue;
                if (bestRiddles == null || players[i].accuracy() > bestRiddles.accuracy()) {
                    bestRiddles = players[i];
                }
            }

            System.out.println("\nJogador com mais vitórias:");
            System.out.println("  " + topWins.name + " - " + topWins.wins + " vitória(s) em " + topWins.games + " jogo(s)");

            if (bestRiddles != null) {
                System.out.printf("\nMelhor taxa de enigmas (min 1 respondido):%n");
                System.out.printf("  %s - %.1f%% (%d certos, %d errados)%n",
                        bestRiddles.name,
                        bestRiddles.accuracy() * 100.0,
                        bestRiddles.totalCorrect,
                        bestRiddles.totalWrong);
            }

            if (longestGameTurns >= 0 && longestGameFile != null) {
                System.out.println("\nPartida mais longa:");
                System.out.println("  " + longestGameFile + " - " + longestGameTurns + " turno(s)");
            }

            System.out.println("\n--- Estatísticas por jogador ---");
            for (int i = 0; i < playerCount; i++) {
                PlayerGlobal pg = players[i];
                System.out.printf("  %s: %d vitórias, %d jogos, enigmas: %d certos / %d errados (%.1f%%)%n",
                        pg.name,
                        pg.wins,
                        pg.games,
                        pg.totalCorrect,
                        pg.totalWrong,
                        pg.accuracy() * 100.0);
            }
        }
    }
}
