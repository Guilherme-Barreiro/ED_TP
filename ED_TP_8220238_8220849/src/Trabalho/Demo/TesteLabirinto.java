package Trabalho.Demo;

import Trabalho.Map.*;

public class TesteLabirinto {
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();

        Room r1 = new Room(1, "Sala 1", RoomType.NORMAL);
        Room r2 = new Room(2, "Sala 2", RoomType.NORMAL);
        Room r3 = new Room(3, "Sala 3", RoomType.NORMAL);
        Room r4 = new Room(4, "Sala 4", RoomType.NORMAL);
        Room r5 = new Room(5, "Sala 5", RoomType.NORMAL);
        Room E = new Room(6, "Entrada 1", RoomType.ENTRY);
        Room C = new Room(7, "Centro", RoomType.CENTER);

        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);
        lab.addRoom(r4);
        lab.addRoom(r5);
        lab.addRoom(C);
        lab.addRoom(E);

        lab.addCorridor(r1, r2, 1.0, false);
        lab.addCorridor(r2, r3, 3.0, true);
        lab.addCorridor(r2, r4, 3.0, false);
        lab.addCorridor(r2, r5, 3.0, false);
        lab.addCorridor(r3, r4, 3.0, false);

        System.out.println("Vizinhos de r1: " + lab.getNeighbors(r1));
        System.out.println("Vizinhos de r2: " + lab.getNeighbors(r2));

        System.out.println("A desbloquear corredores de r2...");
        lab.unlockCorridorsFrom(r2);

        System.out.println("Vizinhos de r2 depois de unlock: " + lab.getNeighbors(r2));
    }
}
