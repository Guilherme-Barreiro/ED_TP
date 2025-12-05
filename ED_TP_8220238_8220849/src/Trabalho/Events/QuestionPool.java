package Trabalho.Events;

import Colecoes.Estruturas.LinkedQueue;
import Colecoes.interfaces.QueueADT;

public class QuestionPool {
    private QueueADT<Question> available;
    private QueueADT<Question> used;

    public QuestionPool() {
        this.available = new LinkedQueue<>();
        this.used = new LinkedQueue<>();
    }

    /**
     * Adiciona uma nova pergunta ao pool (vai para o fim da fila).
     */
    public void addQuestion(Question q) {
        if (q != null) {
            available.enqueue(q);
        }
    }

    /**
     * true se não houver perguntas nenhumas (nem disponíveis nem usadas).
     */
    public boolean isCompletelyEmpty() {
        return available.isEmpty() && used.isEmpty();
    }

    /**
     * Devolve uma pergunta aleatória do conjunto de disponíveis.
     * Quando esgotar as disponíveis, faz reset (move todas as usadas de volta para a fila de disponíveis)
     * e continua a sortear.
     *
     * Cada pergunta escolhida é removida de 'available' e colocada em 'used',
     * garantindo que não se repete até acabar o ciclo atual.
     */
    public Question getRandomQuestion() {
        if (available.isEmpty()) {
            reset();
        }
        if (available.isEmpty()) {
            return null;
        }

        QueueADT<Question> temp = new LinkedQueue<>();
        int size = 0;

        while (!available.isEmpty()) {
            Question q = available.dequeue();
            temp.enqueue(q);
            size++;
        }

        int randomIndex = (int) (Math.random() * size);

        Question selected = null;
        int i = 0;

        while (!temp.isEmpty()) {
            Question q = temp.dequeue();
            if (i == randomIndex) {
                selected = q;
                used.enqueue(q);
            } else {
                available.enqueue(q);
            }
            i++;
        }

        return selected;
    }


    /**
     * Volta a pôr todas as perguntas usadas na fila de disponíveis.
     */
    public void reset() {
        while (!used.isEmpty()) {
            available.enqueue(used.dequeue());
        }
    }
}
