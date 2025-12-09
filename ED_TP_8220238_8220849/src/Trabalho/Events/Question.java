package Trabalho.Events;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;

import java.util.Iterator;

/**
 * Representa uma pergunta de quiz com várias opções de resposta.
 * <p>
 * Cada pergunta contém:
 * <ul>
 *     <li>o texto da pergunta ({@link #text});</li>
 *     <li>uma lista de opções de resposta ({@link #options});</li>
 *     <li>o índice da resposta correta ({@link #correctIndex}).</li>
 * </ul>
 */
public class Question {
    private String text;
    private UnorderedListADT<String> options;
    private int correctIndex;

    /**
     * Cria uma nova pergunta com texto, opções e índice da resposta correta.
     *
     * @param text         texto da pergunta
     * @param opts         array com as opções de resposta
     * @param correctIndex índice da opção correta no array {@code opts}
     * @throws IllegalArgumentException se o texto ou as opções forem nulos/vazios
     *                                  ou se o índice correto estiver fora dos limites
     */
    public Question(String text, String[] opts, int correctIndex) {
        if (text == null || opts == null || opts.length == 0) {
            throw new IllegalArgumentException("Texto e opções não podem ser nulos/vazios.");
        }
        if (correctIndex < 0 || correctIndex >= opts.length) {
            throw new IllegalArgumentException("Índice da resposta correta inválido.");
        }

        this.text = text;
        this.options = new ArrayUnorderedList<>();
        for (int i = 0; i < opts.length; i++) {
            options.addToRear(opts[i]);
        }
        this.correctIndex = correctIndex;
    }

    /**
     * Devolve o texto da pergunta.
     *
     * @return texto da pergunta
     */
    public String getText() {
        return text;
    }

    /**
     * Devolve o número de opções de resposta.
     *
     * @return quantidade de opções
     */
    public int getOptionsCount() {
        return options.size();
    }

    /**
     * Obtém o texto de uma opção pelo índice (0..n-1),
     * percorrendo a lista através de um {@link Iterator}.
     *
     * @param index índice da opção
     * @return texto da opção no índice dado
     * @throws IndexOutOfBoundsException se o índice estiver fora dos limites
     * @throws IllegalStateException     se ocorrer algum problema ao percorrer a lista
     */
    public String getOption(int index) {
        if (index < 0 || index >= options.size()) {
            throw new IndexOutOfBoundsException("Índice de opção inválido: " + index);
        }

        Iterator<String> it = options.iterator();
        int i = 0;
        while (it.hasNext()) {
            String opt = it.next();
            if (i == index) {
                return opt;
            }
            i++;
        }
        throw new IllegalStateException("Estado inconsistente em Question.getOption");
    }

    /**
     * Devolve o índice da opção correta.
     *
     * @return índice da resposta correta
     */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Verifica se o índice de resposta fornecido corresponde
     * à opção correta.
     *
     * @param answerIndex índice respondido pelo jogador
     * @return {@code true} se a resposta estiver correta,
     * {@code false} caso contrário
     */
    public boolean isCorrect(int answerIndex) {
        return answerIndex == correctIndex;
    }

    /**
     * Devolve uma representação textual resumida da pergunta,
     * incluindo apenas o texto e o índice correto.
     *
     * @return string representando a pergunta
     */
    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", correctIndex=" + correctIndex +
                '}';
    }
}
