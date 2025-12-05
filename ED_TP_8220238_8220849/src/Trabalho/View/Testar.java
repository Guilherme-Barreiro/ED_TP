
package Trabalho.View;

import Trabalho.Events.Lever;
import Trabalho.Events.LeverPuzzle;
import Trabalho.Events.Question;
import Trabalho.Events.QuestionPool;

import Trabalho.IO.QuestionLoader;
import java.io.IOException;

import Trabalho.Map.*;

import javax.swing.SwingUtilities;
import java.awt.Point;
import org.json.simple.parser.ParseException;

public class Testar {
    public static void main(String[] args) throws IOException, ParseException {
        Labyrinth lab = new Labyrinth();

        Room E1  = new Room(1, "Entrada 1", RoomType.ENTRY);
        Room E2  = new Room(2, "Entrada 2", RoomType.ENTRY);
        Room E3  = new Room(3, "Entrada 3", RoomType.ENTRY);
        Room C  = new Room(4, "Centro", RoomType.CENTER);
        Room r1 = new Room(5, "Sala 1", RoomType.NORMAL);
        Room r2 = new Room(6, "Sala 2", RoomType.NORMAL);
        Room r3 = new Room(7, "Sala 3", RoomType.NORMAL);
        Room r4 = new Room(8, "Sala 4", RoomType.NORMAL);
        Room r5 = new Room(9, "Sala 5", RoomType.NORMAL);
        Room r6 = new Room(10, "Sala 6", RoomType.NORMAL);
        Room r7 = new Room(11, "Sala 7", RoomType.NORMAL);
        Room r8 = new Room(12, "Sala 8", RoomType.NORMAL);
        Room r9 = new Room(13, "Sala 9", RoomType.NORMAL);
        Room r10 = new Room(14, "Sala 10", RoomType.NORMAL);
        Room r11 = new Room(15, "Sala 11", RoomType.NORMAL);
        Room r12 = new Room(16, "Sala 12", RoomType.NORMAL);
        Room r13 = new Room(17, "Sala 13", RoomType.NORMAL);


        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);
        lab.addRoom(r4);
        lab.addRoom(r5);
        lab.addRoom(r6);
        lab.addRoom(r7);
        lab.addRoom(r8);
        lab.addRoom(r9);
        lab.addRoom(r10);
        lab.addRoom(r11);
        lab.addRoom(r12);
        lab.addRoom(r13);
        lab.addRoom(C);
        lab.addRoom(E1);
        lab.addRoom(E2);
        lab.addRoom(E3);

        lab.addCorridor(E1, r1, 1.0, false);
        lab.addCorridor(E2, r6, 3.0, false);
        lab.addCorridor(E3, r9, 3.0, false);
        lab.addCorridor(E3, r13, 3.0, false);
        lab.addCorridor(r1, r2, 1.0, false);
        lab.addCorridor(r2, r3, 3.0, false);
        lab.addCorridor(r4, r5, 3.0, false);
        lab.addCorridor(r3, r4, 3.0, false);
        lab.addCorridor(r5, C, 3.0, false);
        lab.addCorridor(r1, r6, 3.0, false);
        lab.addCorridor(r6, r7, 3.0, false);
        lab.addCorridor(r7, r8, 3.0, false);
        lab.addCorridor(r4, r8, 3.0, false);
        lab.addCorridor(r7, r11, 3.0, false);
        lab.addCorridor(r9, r10, 3.0, false);
        lab.addCorridor(r12, r13, 3.0, false);
        lab.addCorridor(r9, r13, 3.0, false);
        lab.addCorridor(r10, r8, 3.0, false);
        lab.addCorridor(r10, r12, 3.0, false);
        lab.addCorridor(r7, r12, 3.0, false);


        Room[] rooms = { E1, E2, E3, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, C };

        Point[] positions = new Point[] {
                new Point(110, 200),  // E1
                new Point(110, 400),  // E2
                new Point(660, 400),  // E3
                new Point(220, 200),  // r1
                new Point(330, 200),  // r2
                new Point(440, 100),  // r3
                new Point(440, 200),  // r4
                new Point(550, 300),  // r5
                new Point(220, 400),  // r6
                new Point(330, 400),  // r7
                new Point(330, 300),  // r8
                new Point(550, 400),  // r9
                new Point(440, 400),  // r10
                new Point(330, 500),  // r11
                new Point(440, 500),  // r12
                new Point(550, 500),  // r13
                new Point(440, 300)   // C
        };
        SwingUtilities.invokeLater(() -> LabyrinthViewer.show(lab, rooms, positions));


        //criar questions -----------------------------------------------------------------------------------------------------------
        QuestionPool pool = null;
        try {
            pool = QuestionLoader.loadFromJson("resources/questions.json");
            System.out.println("QuestionPool carregado com sucesso.");

            Question q = pool.getRandomQuestion();
            System.out.println("Exemplo de pergunta: " + q.getText());
        } catch (IOException | ParseException e) {
            System.out.println("Erro ao carregar resources/questions.json: " + e.getMessage());
            e.printStackTrace();
        }

        if (pool != null) {
            Question riddleR5 = pool.getRandomQuestion();
            r5.setRiddle(riddleR5);

            System.out.println("Sala com enigma: " + r5.getName());
            System.out.println("hasEnigma = " + r5.hasRiddle());
            System.out.println("Texto do enigma: " + r5.getRiddle().getText());
        }



        //criar levers -----------------------------------------------------------------------------------------------------------
        LeverPuzzle puzzle = new LeverPuzzle(1);
        Lever lever = new Lever(puzzle);
        r8.setLever(lever);

        System.out.println("Sala com alavanca: " + r8.getName());
        System.out.println("hasLever = " + r8.hasLever());
        System.out.println("Lever correta = " + r8.getLever().getPuzzle().getCorrectIndex());


    }
}
