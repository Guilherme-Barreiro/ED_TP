package Trabalho.View;

import Trabalho.Map.Corridor;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Map.RoomType;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LabyrinthPanel extends JPanel {

    private final Labyrinth lab;
    private final Room[] rooms;
    private final Point[] positions;
    private final Map<Room, Point> roomPositions;

    private static final int NODE_RADIUS = 20;
    private static final int PADDING = 60;

    // layout automático
    public LabyrinthPanel(Labyrinth lab) {
        this.lab = lab;
        java.util.List<Room> list = new ArrayList<>();
        Iterator<Room> it = lab.getRooms().iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        this.rooms = list.toArray(new Room[0]);
        this.positions = computeCircularPositions(rooms.length, 400, 300, 200);
        this.roomPositions = new HashMap<>();
        for (int i = 0; i < rooms.length; i++) {
            roomPositions.put(rooms[i], positions[i]);
        }
        setBackground(Color.WHITE);
        setPreferredSize(computePreferredSize(positions));
    }

    // layout manual
    public LabyrinthPanel(Labyrinth lab, Room[] rooms, Point[] positions) {
        if (rooms.length != positions.length) {
            throw new IllegalArgumentException("rooms e positions têm de ter o mesmo tamanho.");
        }
        this.lab = lab;
        this.rooms = rooms;
        this.positions = positions;
        this.roomPositions = new HashMap<>();
        for (int i = 0; i < rooms.length; i++) {
            roomPositions.put(rooms[i], positions[i]);
        }
        setBackground(Color.WHITE);
        setPreferredSize(computePreferredSize(positions));
    }

    private Point[] computeCircularPositions(int n, int cx, int cy, int radius) {
        Point[] pts = new Point[n];
        if (n == 0) return pts;
        double step = 2 * Math.PI / n;
        for (int i = 0; i < n; i++) {
            double angle = i * step;
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            pts[i] = new Point(x, y);
        }
        return pts;
    }

    private Dimension computePreferredSize(Point[] pts) {
        int maxX = 0;
        int maxY = 0;
        for (Point p : pts) {
            if (p == null) continue;
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        }
        int w = maxX + NODE_RADIUS + PADDING;
        int h = maxY + NODE_RADIUS + PADDING;

        w = Math.max(w, 400);
        h = Math.max(h, 300);
        return new Dimension(w, h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (lab == null || rooms == null || positions == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawCorridors(g2);
        drawRooms(g2);
    }

    private void drawCorridors(Graphics2D g2) {
        Iterator<Corridor> it = lab.getCorridors().iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            Room from = c.getFrom();
            Room to = c.getTo();

            Point p1 = roomPositions.get(from);
            Point p2 = roomPositions.get(to);
            if (p1 == null || p2 == null) continue;

            if (c.isLocked()) {
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(Color.RED);
            } else {
                g2.setStroke(new BasicStroke(1.0f));
                g2.setColor(Color.LIGHT_GRAY);
            }

            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void drawRooms(Graphics2D g2) {
        Font normalFont = g2.getFont();

        for (int i = 0; i < rooms.length; i++) {
            Room r = rooms[i];
            Point p = positions[i];
            int x = p.x;
            int y = p.y;

            int diameter = NODE_RADIUS * 2;
            int topLeftX = x - NODE_RADIUS;
            int topLeftY = y - NODE_RADIUS;

            Color fill = Color.LIGHT_GRAY;
            if (r.getType() == RoomType.ENTRY) {
                fill = new Color(180, 230, 180);
            } else if (r.getType() == RoomType.CENTER) {
                fill = new Color(255, 235, 0);
            }

            if (r.hasRiddle()) {
                fill = new Color(200, 180, 240);
            }

            if (r.hasLever()) {
                fill = new Color(180, 210, 250);
            }

            g2.setColor(fill);
            g2.fillOval(topLeftX, topLeftY, diameter, diameter);

            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawOval(topLeftX, topLeftY, diameter, diameter);

            g2.setFont(normalFont);
            g2.setColor(Color.BLACK);
            String label = r.getName();
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            int labelX = x - labelWidth / 2;
            int labelY = y + fm.getAscent() / 2;
            g2.drawString(label, labelX, labelY);

            if (r.hasRiddle()) {
                g2.setFont(normalFont.deriveFont(Font.BOLD, 14f));
                g2.setColor(new Color(80, 0, 120));
                g2.drawString("?", x - 4, y - NODE_RADIUS - 4);
            }

            if (r.hasLever()) {
                g2.setFont(normalFont.deriveFont(Font.BOLD, 14f));
                g2.setColor(new Color(0, 70, 140));
                g2.drawString("L", x + NODE_RADIUS - 10, y - NODE_RADIUS - 4);
            }
        }

        g2.setFont(normalFont);
    }


}
