package trabalho.view;

import trabalho.map.Labyrinth;
import trabalho.map.Room;

import javax.swing.*;
import java.awt.*;

/**
 * Janela utilitária para visualizar graficamente um {@link Labyrinth}
 * usando um {@link LabyrinthPanel}.
 * <p>
 * Mantém uma referência à última janela aberta para a fechar antes de
 * abrir uma nova, e guarda o último layout (salas + posições) para
 * ser reutilizado quando apenas o estado lógico do labirinto muda.
 */
public class LabyrinthViewer {

    private static JFrame currentFrame;

    private static Room[] lastRooms;
    private static Point[] lastPositions;

    /**
     * Mostra o labirinto numa nova janela.
     * <p>
     * Se existir um layout previamente guardado ({@link #lastRooms} e
     * {@link #lastPositions}), esse layout é reutilizado; caso contrário,
     * o {@link LabyrinthPanel} calcula automaticamente as posições em círculo.
     * <p>
     * Se já existir uma janela aberta, ela é fechada antes de abrir a nova.
     *
     * @param lab labirinto a visualizar
     */
    public static void show(Labyrinth lab) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LabyrinthPanel panel;
        if (lastRooms != null && lastPositions != null && lastRooms.length == lastPositions.length) {
            panel = new LabyrinthPanel(lab, lastRooms, lastPositions);
        } else {
            panel = new LabyrinthPanel(lab);
        }

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        currentFrame = frame;
    }

    /**
     * Limpa qualquer layout guardado (salas + posições) da última visualização.
     * <p>
     * Depois de chamar este método, a próxima chamada a
     * {@link #show(Labyrinth)} volta a calcular automaticamente as posições
     * das salas, em vez de reutilizar o layout anterior.
     */
    public static void resetLayout() {
        lastRooms = null;
        lastPositions = null;
    }

    /**
     * Mostra o labirinto numa nova janela com um layout específico,
     * fornecido através dos arrays de salas e posições.
     * <p>
     * Este layout é guardado em {@link #lastRooms} e {@link #lastPositions}
     * para que possa ser reutilizado em chamadas futuras de
     * {@link #show(Labyrinth)}.
     * <p>
     * Se já existir uma janela aberta, ela é fechada antes de abrir a nova.
     *
     * @param lab       labirinto a visualizar
     * @param rooms     array de salas (na mesma ordem que as posições)
     * @param positions array de posições para cada sala
     */
    public static void show(Labyrinth lab, Room[] rooms, Point[] positions) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        lastRooms = rooms;
        lastPositions = positions;

        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LabyrinthPanel panel = new LabyrinthPanel(lab, rooms, positions);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        currentFrame = frame;
    }
}
