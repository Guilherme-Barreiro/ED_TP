package Trabalho.Demo;

import Trabalho.Events.*;

public class TestePerguntas {
    public static void main(String[] args) {
        QuestionPool pool = new QuestionPool();

        pool.addQuestion(new Question("2+2?", new String[]{"3","4","5"}, 1));
        pool.addQuestion(new Question("Capital de Portugal?", new String[]{"Madrid","Lisboa"}, 1));

        for (int i = 0; i < 4; i++) {
            Question q = pool.getRandomQuestion();
            if (q == null) {
                System.out.println("Sem perguntas.");
            } else {
                System.out.println("Pergunta: " + q.getText());
            }
        }
    }
}
