package trabalho.game;

/**
 * Modos de funcionamento do jogo.
 * <p>
 * Determina a forma como os turnos e movimentos são controlados:
 * <ul>
 *     <li>{@link #MANUAL} – jogadas decididas pelo utilizador;</li>
 *     <li>{@link #AUTOMATIC} – jogadas decididas automaticamente (bots/simulação).</li>
 * </ul>
 */
public enum GameMode {
    MANUAL,
    AUTOMATIC
}
