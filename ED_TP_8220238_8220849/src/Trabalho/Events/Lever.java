package Trabalho.Events;

/**
 * Representa uma alavanca presente numa sala, associada a um puzzle
 * de alavancas ({@link LeverPuzzle}).
 * <p>
 * A alavanca só pode ser ativada se o jogador escolher o índice correto
 * definido no puzzle. Uma vez ativada com sucesso, permanece ativa e
 * não pode ser ativada novamente.
 */
public class Lever {
    private boolean activated;
    private final LeverPuzzle puzzle;

    /**
     * Construtor principal: cria uma alavanca associada ao puzzle indicado.
     *
     * @param puzzle puzzle de alavancas associado
     * @throws IllegalArgumentException se o {@code puzzle} for {@code null}
     */
    public Lever(LeverPuzzle puzzle) {
        if (puzzle == null) {
            throw new IllegalArgumentException("LeverPuzzle não pode ser null.");
        }
        this.puzzle = puzzle;
        this.activated = false;
    }

    /**
     * Construtor por omissão: cria uma alavanca com um {@link LeverPuzzle}
     * em que a alavanca correta tem índice 0.
     * <p>
     * Útil como valor por defeito quando não se pretende configurar um
     * puzzle específico.
     */
    public Lever() {
        this(new LeverPuzzle(0));
    }

    /**
     * Verifica se a alavanca já foi ativada.
     *
     * @return {@code true} se a alavanca já foi ativada, {@code false} caso contrário
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Devolve o puzzle associado a esta alavanca.
     *
     * @return puzzle de alavancas
     */
    public LeverPuzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Tenta ativar a alavanca escolhendo um dos índices válidos do puzzle
     * (por exemplo, 0, 1, 2, ...).
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
