package Trabalho.IO;

import Trabalho.Game.GameState;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameReportWriter {

    public static void saveToFile(GameState state, String filePath) throws IOException {
        String json = GameReportJson.generate(state);

        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(json);
        }
    }
}
