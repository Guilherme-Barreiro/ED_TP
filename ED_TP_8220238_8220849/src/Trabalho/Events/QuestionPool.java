package Trabalho.Events;

import Estruturas.LinkedQueue;
import interfaces.QueueADT;

/**
 * Conjunto (pool) de perguntas de quiz, geridas por filas para evitar repetições
 * até que todas as perguntas tenham sido usadas.
 * <p>
 * Usa duas filas:
 * <ul>
 *     <li>{@link #available}: perguntas disponíveis para serem sorteadas;</li>
 *     <li>{@link #used}: perguntas que já saíram no ciclo atual.</li>
 * </ul>
 * Quando esgotam as disponíveis, faz-se um {@link #reset()} que move todas as
 * perguntas de {@code used} de volta para {@code available}.
 */
public class QuestionPool {
    private QueueADT<Question> available;
    private QueueADT<Question> used;

    /**
     * Cria um pool de perguntas vazio.
     * As filas {@code available} e {@code used} começam vazias.
     */
    public QuestionPool() {
        this.available = new LinkedQueue<>();
        this.used = new LinkedQueue<>();
    }

    /**
     * Adiciona uma nova pergunta ao pool (vai para o fim da fila).
     *
     * @param q pergunta a adicionar (ignorada se for {@code null})
     */
    public void addQuestion(Question q) {
        if (q != null) {
            available.enqueue(q);
        }
    }

    /**
     * Verifica se não existem perguntas nenhumas no pool,
     * nem disponíveis nem usadas.
     *
     * @return {@code true} se ambas as filas estiverem vazias,
     * {@code false} caso contrário
     */
    public boolean isCompletelyEmpty() {
        return available.isEmpty() && used.isEmpty();
    }

    /**
     * Devolve uma pergunta aleatória do conjunto de disponíveis.
     * Quando esgotar as disponíveis, faz reset (move todas as usadas de volta para a fila de disponíveis)
     * e continua a sortear.
     * <p>
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
