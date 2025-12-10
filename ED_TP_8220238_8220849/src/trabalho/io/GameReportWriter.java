package trabalho.io;

import trabalho.game.GameState;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utilitário simples para escrever um relatório de jogo em JSON
 * para um ficheiro com caminho especificado pelo utilizador.
 * <p>
 * Internamente delega a criação do JSON em {@link GameReportJson}.
 */
public class GameReportWriter {
    /**
     * Gera o JSON do relatório de jogo e grava-o no ficheiro indicado.
     *
     * @param state    estado do jogo a partir do qual gerar o relatório
     * @param filePath caminho do ficheiro de saída
     * @throws IOException se ocorrer um erro ao escrever o ficheiro
     */
    public static void saveToFile(GameState state, String filePath) throws IOException {
        String json = GameReportJson.generate(state);

        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(json);
        }
    }
}
