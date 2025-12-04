package Trabalho.Events;

public class LeverPuzzle {

    private static final int LEVER_COUNT = 3;
    private final int correctIndex;

    public LeverPuzzle(int correctIndex) {
        if (correctIndex < 0 || correctIndex >= LEVER_COUNT) {
            throw new IllegalArgumentException("Índice da alavanca correta tem de ser entre 0 a " + (LEVER_COUNT - 1));
        }
        this.correctIndex = correctIndex;
    }

    public int getLeverCount() {
        return LEVER_COUNT;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public boolean isCorrect(int choiceIndex) {
        return choiceIndex == correctIndex;
    }
}

