package Trabalho.Demo;

import Trabalho.Map.*;

public class TesteLabirinto {
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();

        Room r1 = new Room(1, "Entrada 1", RoomType.ENTRY);
        Room r2 = new Room(2, "Sala A", RoomType.NORMAL);
        Room r3 = new Room(3, "Centro", RoomType.CENTER);

        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);

        lab.addCorridor(r1, r2, 1.0, false);
        lab.addCorridor(r2, r3, 3.0, true); // este começa bloqueado

        System.out.println("Vizinhos de r1: " + lab.getNeighbors(r1));
        System.out.println("Vizinhos de r2: " + lab.getNeighbors(r2));

        System.out.println("A desbloquear corredores de r2...");
        lab.unlockCorridorsFrom(r2);

        System.out.println("Vizinhos de r2 depois de unlock: " + lab.getNeighbors(r2));
    }
}
