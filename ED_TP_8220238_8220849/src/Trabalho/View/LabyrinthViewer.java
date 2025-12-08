package Trabalho.View;

import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;

import javax.swing.*;
import java.awt.*;

public class LabyrinthViewer {

    private static JFrame currentFrame;

    // guardar último layout manual usado
    private static Room[] lastRooms;
    private static Point[] lastPositions;

    // layout automático (reutiliza layout manual se existir)
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

    // layout manual
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
