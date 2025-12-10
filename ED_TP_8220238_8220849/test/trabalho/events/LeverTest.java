package trabalho.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeverTest {

    @Test
    void tryActivateAtivaNaAlavancaCorreta() {
        LeverPuzzle puzzle = new LeverPuzzle(2, 3);
        Lever lever = new Lever(puzzle);

        assertFalse(lever.isActivated());

        assertFalse(lever.tryActivate(0));
        assertFalse(lever.isActivated());

        assertTrue(lever.tryActivate(2));
        assertTrue(lever.isActivated());
    }

    @Test
    void tryActivateNaoReativaDepoisDeAtivada() {
        LeverPuzzle puzzle = new LeverPuzzle(0, 3);
        Lever lever = new Lever(puzzle);

        assertTrue(lever.tryActivate(0));
        assertTrue(lever.isActivated());

        assertFalse(lever.tryActivate(0));
        assertTrue(lever.isActivated());
    }

    @Test
    void construtorNaoAceitaPuzzleNull() {
        assertThrows(IllegalArgumentException.class, () -> new Lever(null));
    }
}
