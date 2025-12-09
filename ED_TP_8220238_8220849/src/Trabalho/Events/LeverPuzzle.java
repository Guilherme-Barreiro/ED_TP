package Trabalho.Events;

/**
 * Representa o puzzle associado a uma alavanca ({@link Lever}),
 * composto por várias alavancas possíveis e um índice correto.
 * <p>
 * O jogador escolhe um índice (0..n-1) e o puzzle indica se
 * essa escolha corresponde à alavanca correta.
 */
public class LeverPuzzle {

    private final int leverCount;
    private final int correctIndex;

    /**
     * Cria um puzzle com um número de alavancas por omissão (3)
     * e o índice correto indicado.
     *
     * @param correctIndex índice da alavanca correta (0..2, no caso por omissão)
     * @throws IllegalArgumentException se o índice não for válido
     */
    public LeverPuzzle(int correctIndex) {
        this(correctIndex, 3);
    }

    /**
     * Cria um puzzle com o número de alavancas e o índice correto indicados.
     *
     * @param correctIndex índice da alavanca correta (0..leverCount-1)
     * @param leverCount   número total de alavancas (pelo menos 2)
     * @throws IllegalArgumentException se {@code leverCount} for &lt; 2
     *                                  ou se {@code correctIndex} estiver fora dos limites
     */
    public LeverPuzzle(int correctIndex, int leverCount) {
        if (leverCount < 2) {
            throw new IllegalArgumentException("O número de alavancas tem de ser pelo menos 2.");
        }
        if (correctIndex < 0 || correctIndex >= leverCount) {
            throw new IllegalArgumentException("Índice da alavanca correta tem de ser entre 0 e " + (leverCount - 1));
        }
        this.leverCount = leverCount;
        this.correctIndex = correctIndex;
    }

    /**
     * Devolve o número total de alavancas do puzzle.
     *
     * @return número de alavancas
     */
    public int getLeverCount() {
        return leverCount;
    }

    /**
     * Devolve o índice da alavanca correta.
     *
     * @return índice correto (0..leverCount-1)
     */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Verifica se o índice escolhido pelo jogador corresponde
     * à alavanca correta.
     *
     * @param choiceIndex índice escolhido
     * @return {@code true} se {@code choiceIndex} for igual a {@link #correctIndex},
     * {@code false} caso contrário
     */
    public boolean isCorrect(int choiceIndex) {
        return choiceIndex == correctIndex;
    }
}
