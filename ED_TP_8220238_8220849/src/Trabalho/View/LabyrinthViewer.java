package Trabalho.View;

import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;

import javax.swing.*;
import java.awt.*;

public class LabyrinthViewer {

    // Versão antiga (layout automático)
    public static void show(Labyrinth lab) {
        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LabyrinthPanel panel = new LabyrinthPanel(lab);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // NOVO: versão com layout manual
    public static void show(Labyrinth lab, Room[] rooms, Point[] positions) {
        JFrame frame = new JFrame("Labirinto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LabyrinthPanel panel = new LabyrinthPanel(lab, rooms, positions);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
