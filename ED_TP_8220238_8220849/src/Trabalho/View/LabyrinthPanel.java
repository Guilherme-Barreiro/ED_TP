package Trabalho.View;

import Colecoes.interfaces.UnorderedListADT;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;
import Colecoes.Estruturas.ArrayUnorderedList;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Iterator;

public class LabyrinthPanel extends JPanel {

    private final Labyrinth labyrinth;
    private Room[] roomsArray;
    private Point[] positionsArray;

    // construtor antigo (layout automático)
    public LabyrinthPanel(Labyrinth labyrinth) {
        this(labyrinth, null, null);
    }

    // NOVO: construtor com layout manual
    public LabyrinthPanel(Labyrinth labyrinth, Room[] rooms, Point[] positions) {
        this.labyrinth = labyrinth;

        if (rooms != null && positions != null &&
                rooms.length == positions.length && rooms.length > 0) {
            // layout definido pelo utilizador
            this.roomsArray = rooms;
            this.positionsArray = positions;
            setPreferredSize(new Dimension(800, 600));
        } else {
            // fallback: layout automático (círculo)
            computeAutoLayout();
        }

        setBackground(Color.WHITE);
    }

    // layout automático (círculo), só usado se não houver layout manual
    private void computeAutoLayout() {
        UnorderedListADT<Room> rooms = labyrinth.getRooms();
        int n = rooms.size();

        roomsArray = new Room[n];
        positionsArray = new Point[n];

        Iterator<Room> it = rooms.iterator();
        int idx = 0;
        while (it.hasNext() && idx < n) {
            roomsArray[idx] = it.next();
            idx++;
        }

        int width = 800;
        int height = 600;
        setPreferredSize(new Dimension(width, height));

        if (n == 0) return;

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2 - 80;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            positionsArray[i] = new Point(x, y);
        }
    }

    private int indexOf(Room r) {
        for (int i = 0; i < roomsArray.length; i++) {
            if (roomsArray[i] == r) return i; // comparação por referência
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (labyrinth == null || roomsArray == null || positionsArray == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int nodeRadius = 20;

        // 1) arestas (corridores não bloqueados)
        for (int i = 0; i < roomsArray.length; i++) {
            Room r = roomsArray[i];
            Point p1 = positionsArray[i];

            ArrayUnorderedList<Room> neighbors = labyrinth.getNeighbors(r);
            Iterator<Room> it = neighbors.iterator();
            while (it.hasNext()) {
                Room n = it.next();
                int j = indexOf(n);
                if (j == -1) continue; // se a sala não estiver no layout, ignora

                if (r.getId() < n.getId()) {
                    Point p2 = positionsArray[j];
                    g2.setColor(Color.BLACK);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        // 2) nós (salas)
        for (int i = 0; i < roomsArray.length; i++) {
            Room r = roomsArray[i];
            Point p = positionsArray[i];

            int x = p.x - nodeRadius;
            int y = p.y - nodeRadius;

            // cor por tipo
            if (r.getType() == RoomType.ENTRY) {
                g2.setColor(new Color(135, 206, 235)); // entrada
            } else if (r.getType() == RoomType.CENTER) {
                g2.setColor(new Color(255, 215, 0));   // centro
            } else {
                g2.setColor(new Color(211, 211, 211)); // normal
            }

            g2.fillOval(x, y, 2 * nodeRadius, 2 * nodeRadius);
            g2.setColor(Color.BLACK);
            g2.drawOval(x, y, 2 * nodeRadius, 2 * nodeRadius);

            String label = r.getName();
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(label);
            int th = fm.getAscent();
            g2.drawString(label, p.x - tw / 2, p.y + th / 2);
        }
    }
}
