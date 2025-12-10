package trabalho.io;

import trabalho.events.Question;
import trabalho.events.QuestionPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * Responsável por carregar perguntas de um ficheiro JSON
 * e construir um QuestionPool.
 *
 * Formato esperado:
 *
 * {
 *   "questions": [
 *     {
 *       "text": "2 + 2?",
 *       "options": ["3", "4", "5"],
 *       "correctIndex": 1
 *     },
 *     ...
 *   ]
 * }
 */
public class QuestionLoader {

    /**
     * Lê um ficheiro JSON com perguntas e constrói um {@link QuestionPool}
     * contendo todas as perguntas válidas.
     *
     * @param filePath caminho do ficheiro JSON de perguntas
     * @return pool preenchido com todas as perguntas lidas
     * @throws IOException           se ocorrer um erro de leitura
     * @throws ParseException        se o JSON for inválido
     * @throws IllegalArgumentException se a estrutura do ficheiro não corresponder
     *                                  ao formato esperado
     */
    public static QuestionPool loadFromJson(String filePath)
            throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object rootObj = parser.parse(reader);

            if (!(rootObj instanceof JSONObject)) {
                throw new IllegalArgumentException("Root do ficheiro de perguntas não é um objeto JSON.");
            }

            JSONObject root = (JSONObject) rootObj;

            Object questionsRaw = root.get("questions");
            if (!(questionsRaw instanceof JSONArray)) {
                throw new IllegalArgumentException("\"questions\" não é um array no ficheiro JSON.");
            }

            JSONArray questionsArray = (JSONArray) questionsRaw;

            QuestionPool pool = new QuestionPool();

            for (int i = 0; i < questionsArray.size(); i++) {
                Object qRaw = questionsArray.get(i);
                if (!(qRaw instanceof JSONObject)) {
                    throw new IllegalArgumentException("Entrada de pergunta na posição " + i + " não é um objeto.");
                }

                JSONObject qObj = (JSONObject) qRaw;

                Object textRaw = qObj.get("text");
                if (!(textRaw instanceof String)) {
                    throw new IllegalArgumentException("Campo \"text\" em questions[" + i + "] não é string.");
                }
                String text = (String) textRaw;

                Object optsRaw = qObj.get("options");
                if (!(optsRaw instanceof JSONArray)) {
                    throw new IllegalArgumentException("Campo \"options\" em questions[" + i + "] não é array.");
                }
                JSONArray optsArray = (JSONArray) optsRaw;
                if (optsArray.isEmpty()) {
                    throw new IllegalArgumentException("Campo \"options\" em questions[" + i + "] está vazio.");
                }

                String[] opts = new String[optsArray.size()];
                for (int j = 0; j < optsArray.size(); j++) {
                    Object optRaw = optsArray.get(j);
                    if (optRaw == null) {
                        opts[j] = "";
                    } else {
                        opts[j] = optRaw.toString();
                    }
                }

                Object idxRaw = qObj.get("correctIndex");
                if (!(idxRaw instanceof Long)) {
                    throw new IllegalArgumentException("Campo \"correctIndex\" em questions[" + i + "] não é inteiro.");
                }
                int correctIndex = ((Long) idxRaw).intValue();

                Question q = new Question(text, opts, correctIndex);

                pool.addQuestion(q);
            }

            return pool;
        }
    }
}
