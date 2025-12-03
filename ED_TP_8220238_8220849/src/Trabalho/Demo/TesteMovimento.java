package Trabalho.Demo;

import Trabalho.Map.*;
import Trabalho.Players.*;
import Trabalho.interfacesTrabalho.PlayerController;

import java.util.Scanner;

public class TesteMovimento {
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();
        Room r1 = new Room(1, "Entrada", RoomType.ENTRY);
        Room r2 = new Room(2, "Sala A", RoomType.NORMAL);
        Room r3 = new Room(3, "Sala B", RoomType.NORMAL);
        lab.addRoom(r1);
        lab.addRoom(r2);
        lab.addRoom(r3);

        lab.addCorridor(r1, r2, 1.0, false);
        lab.addCorridor(r1, r3, 2.0, false);

        Scanner sc = new Scanner(System.in);
        PlayerController ctrl = new HumanController(sc);
        Player p = new Player("Jogador 1", r1, ctrl);

        Room next = p.chooseNextMove(lab, null); // GameState ainda pode ser null por agora
        System.out.println("Escolheste mover para: " + next);
    }
}
