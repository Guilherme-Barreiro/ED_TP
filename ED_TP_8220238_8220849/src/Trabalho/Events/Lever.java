package Trabalho.Events;

public class Lever {
    private boolean activated;
    private final LeverPuzzle puzzle;

    /**
     * Construtor principal: a alavanca tem um puzzle com uma alavanca correta (0..2).
     */
    public Lever(LeverPuzzle puzzle) {
        if (puzzle == null) {
            throw new IllegalArgumentException("LeverPuzzle não pode ser null.");
        }
        this.puzzle = puzzle;
        this.activated = false;
    }

    /**
     * Construtor por omissão: alavanca correta é a 0.
     * (Podes nem usar este se fores sempre criar com um LeverPuzzle explícito.)
     */
    public Lever() {
        this(new LeverPuzzle(0));
    }

    public boolean isActivated() {
        return activated;
    }

    public LeverPuzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Tenta ativar a alavanca escolhendo um dos índices 0..leverCount-1.
     *
     * @param choiceIndex índice escolhido pelo jogador (0,1,2,...)
     * @return true se escolheu a alavanca correta e esta foi ativada
     */
    public boolean tryActivate(int choiceIndex) {
        if (activated) {
            return false;
        }

        if (puzzle.isCorrect(choiceIndex)) {
            activated = true;
            return true;
        }

        return false;
    }
}
