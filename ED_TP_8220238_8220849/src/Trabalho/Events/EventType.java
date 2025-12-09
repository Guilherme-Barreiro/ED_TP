package Trabalho.Events;

/**
 * Tipos de eventos que podem ocorrer no jogo ao atravessar corredores
 * ou em outras situações especiais.
 * <p>
 * Cada tipo corresponde a um efeito diferente na jogabilidade.
 */
public enum EventType {
    EXTRA_TURN,
    SWAP_PLAYER,
    MOVE_BACK,
    SKIP_TURNS,
    SHUFFLE_ALL
}