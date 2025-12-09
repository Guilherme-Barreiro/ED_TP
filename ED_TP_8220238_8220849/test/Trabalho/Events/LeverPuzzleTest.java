package Trabalho.Events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeverPuzzleTest {

    @Test
    void construtorValidaLimites() {
        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(0, 1)); // menos de 2 alavancas

        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(-1, 3));

        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(3, 3)); // índices válidos: 0,1,2
    }

    @Test
    void isCorrectFuncionaBem() {
        LeverPuzzle puzzle = new LeverPuzzle(1, 3);

        assertFalse(puzzle.isCorrect(0));
        assertTrue(puzzle.isCorrect(1));
        assertFalse(puzzle.isCorrect(2));
    }
}
