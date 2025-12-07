package Trabalho.Events;

import Estruturas.ArrayUnorderedList;
import interfaces.UnorderedListADT;

import java.util.Iterator;

public class Question {
    private String text;
    private UnorderedListADT<String> options;
    private int correctIndex;

    /**
     * @param text         texto da pergunta
     * @param opts         array com as opções
     * @param correctIndex índice da opção correta no array/ordem dada
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

    public String getText() {
        return text;
    }

    public int getOptionsCount() {
        return options.size();
    }

    /**
     * Obtém a opção pelo índice (0..n-1) percorrendo com o iterator.
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

    public int getCorrectIndex() {
        return correctIndex;
    }

    public boolean isCorrect(int answerIndex) {
        return answerIndex == correctIndex;
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", correctIndex=" + correctIndex +
                '}';
    }
}
