package trabalho.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeverPuzzleTest {

    @Test
    void construtorValidaLimites() {
        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(0, 1));

        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(-1, 3));

        assertThrows(IllegalArgumentException.class,
                () -> new LeverPuzzle(3, 3));
    }

    @Test
    void isCorrectFuncionaBem() {
        LeverPuzzle puzzle = new LeverPuzzle(1, 3);

        assertFalse(puzzle.isCorrect(0));
        assertTrue(puzzle.isCorrect(1));
        assertFalse(puzzle.isCorrect(2));
    }
}
