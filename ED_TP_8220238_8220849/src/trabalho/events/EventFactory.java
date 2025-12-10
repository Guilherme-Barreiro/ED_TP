package trabalho.events;

import trabalho.game.Difficulty;

import java.util.Random;

/**
 * Fábrica responsável por criar eventos aleatórios associados a um corredor,
 * com base no peso desse corredor.
 * <p>
 * A probabilidade de surgir um evento e o tipo de evento gerado
 * dependem do peso do corredor:
 * <ul>
 *     <li>Corredores leves (peso baixo) têm menor probabilidade de evento
 *     e eventos mais “leves” (ex.: turno extra, recuar pouco).</li>
 *     <li>Corredores pesados (peso alto) têm maior probabilidade de evento
 *     e eventos mais fortes ou caóticos
 *     (ex.: trocar jogadores, baralhar todos, saltar turnos).</li>
 * </ul>
 * A dificuldade (EASY, NORMAL, HARD) pode atenuar ou reforçar
 * alguns destes efeitos.
 */
public class EventFactory {
    private static final Random random = new Random();

    /**
     * Lógica base de geração de um evento em função do peso do corredor,
     * sem considerar dificuldade.
     * <p>
     * Passos principais:
     * <ol>
     *     <li>Calcula a probabilidade de disparo do evento com base no peso.</li>
     *     <li>Faz um lançamento aleatório; se falhar, devolve {@code null}.</li>
     *     <li>Se disparar, escolhe um tipo de evento e intensidade apropriados
     *     para a faixa de peso.</li>
     * </ol>
     *
     * @param weight peso do corredor (por exemplo, 1..5)
     * @return instância de {@link Event} ou {@code null} se não acontecer nada
     */
    private static Event baseMaybeCreateEvent(double weight) {

        double triggerProb;

        if (weight <= 1.0) {
            triggerProb = 0.10;
        } else if (weight <= 2.0) {
            triggerProb = 0.25;
        } else if (weight <= 3.0) {
            triggerProb = 0.40;
        } else if (weight <= 4.0) {
            triggerProb = 0.60;
        } else {
            triggerProb = 0.80;
        }

        double roll = random.nextDouble();
        if (roll > triggerProb) {
            return null;
        }

        double t = random.nextDouble();
        EventType type;
        int intensity;

        if (weight <= 2.0) {
            if (t < 0.7) {
                type = EventType.EXTRA_TURN;
                intensity = 1;
            } else {
                type = EventType.MOVE_BACK;
                intensity = 1;
            }
        } else if (weight <= 4.0) {
            if (t < 0.4) {
                type = EventType.MOVE_BACK;
                intensity = 1 + random.nextInt(2);
            } else if (t < 0.8) {
                type = EventType.SKIP_TURNS;
                intensity = 1;
            } else {
                type = EventType.EXTRA_TURN;
                intensity = 1;
            }
        } else {
            if (t < 0.3) {
                type = EventType.SWAP_PLAYER;
                intensity = 1;
            } else if (t < 0.6) {
                type = EventType.SKIP_TURNS;
                intensity = 1 + random.nextInt(2);
            } else {
                type = EventType.SHUFFLE_ALL;
                intensity = 1;
            }
        }

        return new Event(type, intensity);
    }

    /**
     * Cria, de forma aleatória, um evento com base apenas no peso do corredor,
     * usando a lógica base (dificuldade não é considerada).
     *
     * @param weight peso do corredor
     * @return evento criado ou {@code null} se não houver evento
     */
    public static Event maybeCreateEvent(double weight) {
        return baseMaybeCreateEvent(weight);
    }

    /**
     * Cria, de forma aleatória, um evento com base no peso do corredor
     * e na dificuldade de jogo.
     * <p>
     * A dificuldade afeta a probabilidade e/ou o tipo de certos eventos
     *
     * @param weight     peso do corredor
     * @param difficulty dificuldade do jogo
     * @return evento criado (possivelmente alterado pela dificuldade) ou {@code null}
     * se não houver evento
     */
    public static Event maybeCreateEvent(double weight, Difficulty difficulty) {
        if (difficulty == null) {
            return baseMaybeCreateEvent(weight);
        }

        Event base = baseMaybeCreateEvent(weight);

        if (difficulty == Difficulty.NORMAL) {
            return base;
        }

        if (difficulty == Difficulty.EASY) {
            if (base == null) {
                if (random.nextDouble() < 0.15) {
                    return new Event(EventType.EXTRA_TURN, 1);
                }
                return null;
            }

            switch (base.getType()) {
                case MOVE_BACK:
                case SKIP_TURNS:
                case SHUFFLE_ALL:
                case SWAP_PLAYER:
                    if (random.nextDouble() < 0.6) {
                        return new Event(EventType.EXTRA_TURN, 1);
                    }
                    return base;
                default:
                    return base;
            }
        }

        if (difficulty == Difficulty.HARD) {
            if (base == null) {
                if (random.nextDouble() < 0.15) {
                    int intensity = 1 + random.nextInt(2);
                    return new Event(EventType.SKIP_TURNS, intensity);
                }
                return null;
            }

            if (base.getType() == EventType.EXTRA_TURN) {
                if (random.nextDouble() < 0.6) {
                    int intensity = base.getIntensity();
                    if (intensity <= 0) intensity = 1;
                    return new Event(EventType.SKIP_TURNS, intensity);
                }
            }

            return base;
        }

        return base;
    }
}
