package trabalho.view;

import trabalho.map.Corridor;
import trabalho.map.Labyrinth;
import trabalho.map.Room;
import trabalho.map.RoomType;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * Painel Swing responsável por desenhar graficamente um {@link Labyrinth}.
 * <p>
 * Mostra:
 * <ul>
 *     <li>sala de entrada (ENTRY) a verde;</li>
 *     <li>sala central (CENTER) a amarelo;</li>
 *     <li>salas com enigma a roxo;</li>
 *     <li>salas com alavanca a azul;</li>
 *     <li>corredores normais a cinzento claro;</li>
 *     <li>corredores bloqueados a vermelho.</li>
 * </ul>
 * Suporta:
 * <ul>
 *     <li>cálculo automático de posições em círculo;</li>
 *     <li>layout baseado em posições fornecidas via ficheiro de mapa.</li>
 * </ul>
 */
public class LabyrinthPanel extends JPanel {

    private final Labyrinth lab;
    private final Room[] rooms;
    private final Point[] positions;

    private static final int NODE_RADIUS = 20;
    private static final int PADDING = 60;

    /**
     * Cria um painel para um labirinto, calculando automaticamente
     * as posições das salas em círculo.
     *
     * @param lab labirinto a desenhar
     */
    public LabyrinthPanel(Labyrinth lab) {
        this.lab = lab;

        Iterator<Room> itCount = lab.getRooms().iterator();
        int count = 0;
        while (itCount.hasNext()) {
            itCount.next();
            count++;
        }

        Room[] roomsArray = new Room[count];
        Iterator<Room> itRooms = lab.getRooms().iterator();
        int idx = 0;
        while (itRooms.hasNext()) {
            roomsArray[idx] = itRooms.next();
            idx++;
        }

        this.rooms = roomsArray;
        this.positions = computeCircularPositions(rooms.length, 400, 300, 200);

        setBackground(Color.WHITE);
        setPreferredSize(computePreferredSize(positions));
    }

    /**
     * Cria um painel para um labirinto com um layout fixo,
     * fornecido externamente (por exemplo, carregado de um ficheiro).
     *
     * @param lab       labirinto a desenhar
     * @param rooms     array de salas (na mesma ordem que as posições)
     * @param positions array de posições para cada sala
     * @throws IllegalArgumentException se o número de salas não coincidir com o número de posições
     */
    public LabyrinthPanel(Labyrinth lab, Room[] rooms, Point[] positions) {
        if (rooms.length != positions.length) {
            throw new IllegalArgumentException("rooms e positions têm de ter o mesmo tamanho.");
        }
        this.lab = lab;
        this.rooms = rooms;
        this.positions = positions;

        setBackground(Color.WHITE);
        setPreferredSize(computePreferredSize(positions));
    }

    /**
     * Calcula posições em círculo para {@code n} elementos, centradas em (cx, cy)
     * com o raio indicado.
     *
     * @param n      número de posições a gerar
     * @param cx     coordenada X do centro
     * @param cy     coordenada Y do centro
     * @param radius raio do círculo
     * @return array de pontos com as posições calculadas
     */
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

    /**
     * Calcula o tamanho preferido do painel com base nas posições
     * das salas, adicionando uma margem.
     *
     * @param pts posições das salas
     * @return dimensão preferida do painel
     */
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

    /**
     * Método Swing responsável por desenhar o conteúdo do painel.
     *
     * @param g contexto gráfico
     */
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

    /**
     * Encontra a posição associada a uma determinada sala,
     * percorrendo o array {@link #rooms}.
     *
     * @param room sala a procurar
     * @return posição correspondente, ou {@code null} se não for encontrada
     */
    private Point findPosition(Room room) {
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == room) {
                return positions[i];
            }
        }
        return null;
    }

    /**
     * Desenha todos os corredores do labirinto.
     * <p>
     * Corredores bloqueados são a vermelho; os restantes a cinzento claro.
     *
     * @param g2 contexto gráfico 2D
     */
    private void drawCorridors(Graphics2D g2) {
        Iterator<Corridor> it = lab.getCorridors().iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            Room from = c.getFrom();
            Room to = c.getTo();

            Point p1 = findPosition(from);
            Point p2 = findPosition(to);
            if (p1 == null || p2 == null) continue;

            if (c.isLocked()) {
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(Color.RED);
            } else {
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(Color.LIGHT_GRAY);
            }

            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    /**
     * Desenha todas as salas do labirinto, com cores diferentes conforme o tipo
     * e com marcadores para enigmas e alavancas.
     *
     * @param g2 contexto gráfico 2D
     */
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
