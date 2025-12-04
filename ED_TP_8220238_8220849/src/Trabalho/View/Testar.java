
package Trabalho.View;

import Trabalho.Events.Question;
import Trabalho.Events.QuestionPool;
import Trabalho.Map.*;
import javax.swing.SwingUtilities;
import java.awt.Point;

public class Testar {
    public static void main(String[] args) {
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


        //Criar enigmas
        QuestionPool pool = new QuestionPool();
        if(true){
            pool.addQuestion(new Question("Quem foi o primeiro Presidente da República Portuguesa?", new String[]{"Teófilo Braga","Manuel de Arriaga","Sidónio Pais"}, 1));
            pool.addQuestion(new Question("Em que ano ocorreu a Revolução de 25 de Abril em Portugal?", new String[]{"1968","1974","1982"}, 1));
            pool.addQuestion(new Question("Qual é a capital do Canadá?", new String[]{"Toronto","Ottawa","Vancouver"}, 1));
            pool.addQuestion(new Question("Qual é o maior oceano da Terra?", new String[]{"Atlântico","Pacífico","Índico"}, 1));
            pool.addQuestion(new Question("Qual é o símbolo químico do ouro?", new String[]{"Ag","Au","Pt"}, 1));
            pool.addQuestion(new Question("Quem escreveu \"Os Lusíadas\"?", new String[]{"Camilo Castelo Branco","Eça de Queirós","Luís de Camões"}, 2));
            pool.addQuestion(new Question("Qual é o planeta com mais luas conhecidas no Sistema Solar?", new String[]{"Júpiter","Saturno","Urano"}, 0));
            pool.addQuestion(new Question("Qual é o maior rio da Europa?", new String[]{"Danúbio","Volga","Reno"}, 1));
            pool.addQuestion(new Question("Qual é o maior órgão interno do corpo humano?", new String[]{"Fígado","Pulmão","Coração"}, 0));
            pool.addQuestion(new Question("Qual é a língua oficial de Angola?", new String[]{"Português","Inglês","Francês"}, 0));

            pool.addQuestion(new Question("Em que país se originou o sushi?", new String[]{"China","Japão","Coreia do Sul"}, 1));
            pool.addQuestion(new Question("Quem foi o primeiro homem a pisar a Lua?", new String[]{"Buzz Aldrin","Yuri Gagarin","Neil Armstrong"}, 2));
            pool.addQuestion(new Question("A unidade 'newton' mede que grandeza física?", new String[]{"Energia","Força","Pressão"}, 1));
            pool.addQuestion(new Question("Quantos jogadores de cada equipa estão em campo num jogo oficial de futebol?", new String[]{"9","10","11"}, 2));
            pool.addQuestion(new Question("Quem pintou a Mona Lisa?", new String[]{"Leonardo da Vinci","Michelangelo","Rafael"}, 0));
            pool.addQuestion(new Question("Qual é o menor número primo?", new String[]{"1","2","3"}, 1));
            pool.addQuestion(new Question("Que continente tem mais países reconhecidos?", new String[]{"África","Ásia","Europa"}, 0));
            pool.addQuestion(new Question("Quem escreveu \"Ensaio sobre a Cegueira\"?", new String[]{"José Saramago","Miguel Torga","Fernando Pessoa"}, 0));
            pool.addQuestion(new Question("Qual é o gás mais abundante na atmosfera terrestre?", new String[]{"Oxigénio","Azoto (Nitrogénio)","Dióxido de carbono"}, 1));
            pool.addQuestion(new Question("Qual é o maior deserto quente do mundo?", new String[]{"Deserto de Gobi","Deserto do Saara","Deserto de Kalahari"}, 1));

            pool.addQuestion(new Question("Qual destes instrumentos tem teclas, pedais e cordas?", new String[]{"Violino","Piano","Flauta"}, 1));
            pool.addQuestion(new Question("Como se chama o principal osso do braço, entre o ombro e o cotovelo?", new String[]{"Fémur","Úmero","Tíbia"}, 1));
            pool.addQuestion(new Question("Qual é a moeda oficial do Japão?", new String[]{"Yuan","Won","Iene"}, 2));
            pool.addQuestion(new Question("Qual é a capital de Moçambique?", new String[]{"Beira","Maputo","Luanda"}, 1));
            pool.addQuestion(new Question("Que cientista formulou a teoria da relatividade geral?", new String[]{"Isaac Newton","Albert Einstein","Niels Bohr"}, 1));
            pool.addQuestion(new Question("Qual é a fórmula química da água?", new String[]{"H2O","CO2","O2"}, 0));
            pool.addQuestion(new Question("Que gás é o principal responsável pelo aquecimento global devido à atividade humana?", new String[]{"Oxigénio","Dióxido de carbono","Hélio"}, 1));
            pool.addQuestion(new Question("Quem escreveu \"Dom Quixote\"?", new String[]{"Miguel de Cervantes","Gabriel García Márquez","Jorge Luis Borges"}, 0));
            pool.addQuestion(new Question("Qual destes países NÃO pertence à União Europeia?", new String[]{"Noruega","Espanha","Polónia"}, 0));
            pool.addQuestion(new Question("Em que cidade se encontra a Torre Eiffel?", new String[]{"Roma","Paris","Londres"}, 1));

            pool.addQuestion(new Question("O número romano XIV corresponde a que número decimal?", new String[]{"12","14","16"}, 1));
            pool.addQuestion(new Question("Quem é tradicionalmente considerado o inventor do telefone?", new String[]{"Alexander Graham Bell","Nikola Tesla","Thomas Edison"}, 0));
            pool.addQuestion(new Question("Qual é o maior planeta do Sistema Solar?", new String[]{"Júpiter","Saturno","Neptuno"}, 0));
            pool.addQuestion(new Question("Em música ocidental, quantos semitons (meios tons) há numa oitava?", new String[]{"7","8","12"}, 2));
            pool.addQuestion(new Question("Que cordilheira separa a França de Espanha?", new String[]{"Alpes","Apeninos","Pirenéus"}, 2));
            pool.addQuestion(new Question("Que órgão é responsável por bombear o sangue pelo corpo?", new String[]{"Coração","Pulmão","Rim"}, 0));
            pool.addQuestion(new Question("Como se chama o processo pelo qual as plantas produzem alimento usando luz solar?", new String[]{"Respiração","Fotossíntese","Fermentação"}, 1));
            pool.addQuestion(new Question("Em que país se encontra a cidade de Marraquexe?", new String[]{"Tunísia","Marrocos","Argélia"}, 1));
            pool.addQuestion(new Question("Qual é o nome da teoria mais famosa de Charles Darwin?", new String[]{"Evolução das espécies","Relatividade","Heliocentrismo"}, 0));
            pool.addQuestion(new Question("Qual é o número atómico do carbono?", new String[]{"4","6","12"}, 1));

            pool.addQuestion(new Question("Qual é a língua oficial predominante no Brasil?", new String[]{"Português","Espanhol","Inglês"}, 0));
            pool.addQuestion(new Question("Em informática, o que significa a sigla CPU?", new String[]{"Central Processing Unit","Computer Personal Unit","Control Program Utility"}, 0));
            pool.addQuestion(new Question("Quem escreveu o romance \"1984\"?", new String[]{"Aldous Huxley","George Orwell","Ray Bradbury"}, 1));
            pool.addQuestion(new Question("O símbolo químico Na corresponde a que elemento?", new String[]{"Sódio","Prata","Nitrogénio"}, 0));
            pool.addQuestion(new Question("Que desporto está associado ao torneio de Wimbledon?", new String[]{"Rugby","Ténis","Críquete"}, 1));
            pool.addQuestion(new Question("Que mar separa o sul da Europa do norte de África?", new String[]{"Mar do Norte","Mar Mediterrâneo","Mar Báltico"}, 1));
            pool.addQuestion(new Question("Que elemento químico é essencial na hemoglobina para o transporte de oxigénio?", new String[]{"Cálcio","Ferro","Potássio"}, 1));
            pool.addQuestion(new Question("Qual é a unidade de medida da resistência eléctrica no SI?", new String[]{"Volt","Ampere","Ohm"}, 2));
            pool.addQuestion(new Question("Que império construiu o Coliseu em Roma?", new String[]{"Império Romano","Império Otomano","Império Bizantino"}, 0));
            pool.addQuestion(new Question("Qual é a capital da República Checa?", new String[]{"Bratislava","Praga","Budapeste"}, 1));

        }

        //Criar enigmas
        if(true) {

        }
    }
}
