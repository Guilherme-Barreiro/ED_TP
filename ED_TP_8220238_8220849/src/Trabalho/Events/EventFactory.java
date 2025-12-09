package Trabalho.Events;

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
 */
public class EventFactory {
    private static final Random random = new Random();

    /**
     * Decide se há evento neste corredor, e se sim qual.
     *
     * @param weight peso do corredor (por ex. 1..5)
     * @return Event ou null (se não acontecer nada)
     */
    public static Event maybeCreateEvent(double weight) {

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
}
