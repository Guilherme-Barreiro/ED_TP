package Trabalho.Events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void construtorValidaArgumentos() {
        assertThrows(IllegalArgumentException.class,
                () -> new Question(null, new String[]{"A"}, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Question("Pergunta", null, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Question("Pergunta", new String[]{}, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Question("Pergunta", new String[]{"A", "B"}, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new Question("Pergunta", new String[]{"A", "B"}, 2));
    }

    @Test
    void getOptionFuncionaEValidaIndices() {
        Question q = new Question("Pergunta", new String[]{"A", "B", "C"}, 1);

        assertEquals("A", q.getOption(0));
        assertEquals("B", q.getOption(1));
        assertEquals("C", q.getOption(2));

        assertThrows(IndexOutOfBoundsException.class, () -> q.getOption(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> q.getOption(3));
    }

    @Test
    void isCorrectVerificaIndiceCorreto() {
        Question q = new Question("Pergunta", new String[]{"A", "B", "C"}, 2);

        assertFalse(q.isCorrect(0));
        assertFalse(q.isCorrect(1));
        assertTrue(q.isCorrect(2));
    }
}
