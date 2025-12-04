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
     * Devolve a próxima pergunta da fila.
     * Quando esgotar as disponíveis, faz reset (volta a meter as usadas na fila)
     * e continua.
     *
     * O nome fica "getRandomQuestion" para bater com o UML, mas aqui a ordem é FIFO.
     */
    public Question getRandomQuestion() {
        if (available.isEmpty()) {
            reset();
        }
        if (available.isEmpty()) {
            return null;
        }

        Question q = available.dequeue();
        used.enqueue(q);

        return q;
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
