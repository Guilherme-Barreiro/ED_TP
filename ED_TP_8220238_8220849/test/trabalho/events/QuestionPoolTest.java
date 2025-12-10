package trabalho.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionPoolTest {

    @Test
    void isCompletelyEmptyQuandoNaoHaPerguntas() {
        QuestionPool pool = new QuestionPool();
        assertTrue(pool.isCompletelyEmpty());
        assertNull(pool.getRandomQuestion());
    }

    @Test
    void getRandomQuestionDevolveUmaDasPerguntasAdicionadas() {
        QuestionPool pool = new QuestionPool();
        Question q1 = new Question("P1", new String[]{"A"}, 0);
        Question q2 = new Question("P2", new String[]{"B"}, 0);

        pool.addQuestion(q1);
        pool.addQuestion(q2);

        Question q = pool.getRandomQuestion();
        assertNotNull(q);
        assertTrue(q == q1 || q == q2);
    }

    @Test
    void resetMovePerguntasUsadasDeVoltaParaDisponiveis() {
        QuestionPool pool = new QuestionPool();
        Question q1 = new Question("P1", new String[]{"A"}, 0);
        pool.addQuestion(q1);

        Question q = pool.getRandomQuestion();
        assertNotNull(q);

        pool.reset();
        Question qAgain = pool.getRandomQuestion();
        assertNotNull(qAgain);
    }

    @Test
    void addQuestionIgnoraNull() {
        QuestionPool pool = new QuestionPool();
        pool.addQuestion(null);
        assertTrue(pool.isCompletelyEmpty());
    }
}
