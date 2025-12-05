package Trabalho.Events;

public class LeverPuzzle {

    private final int leverCount;
    private final int correctIndex;

    //se nao for enviado levercount, fica predefinido com 3
    public LeverPuzzle(int correctIndex) {
        this(correctIndex, 3);
    }

    public LeverPuzzle(int correctIndex, int leverCount) {
        if (leverCount < 2) {
            throw new IllegalArgumentException("O número de alavancas tem de ser pelo menos 2.");
        }
        if (correctIndex < 0 || correctIndex >= leverCount) {
            throw new IllegalArgumentException("Índice da alavanca correta tem de ser entre 0 e " + (leverCount - 1));
        }
        this.leverCount = leverCount;
        this.correctIndex = correctIndex;
    }

    public int getLeverCount() {
        return leverCount;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public boolean isCorrect(int choiceIndex) {
        return choiceIndex == correctIndex;
    }
}
