package Trabalho.Game;

import Trabalho.View.LabyrinthViewer;
import interfaces.UnorderedListADT;
import interfaces.QueueADT;
import Estruturas.LinkedQueue;
import Trabalho.Events.*;
import Trabalho.Map.Corridor;
import Trabalho.Map.Labyrinth;
import Trabalho.Map.Room;
import Trabalho.Players.Player;

import javax.swing.*;
import java.util.Iterator;
import java.util.Random;

/**
 * Representa o estado global de uma partida.
 * Gere:
 * - ordem dos turnos (queue de jogadores),
 * - movimentos,
 * - aplicação de eventos nos corredores,
 * - enigmas e alavancas nas salas,
 * - condições de vitória,
 */
public class GameState {

    private final Labyrinth labyrinth;
    private final UnorderedListADT<Player> players;
    private final QueueADT<Player> turnQueue;
    private final QuestionPool questionPool;
    private final GameMode mode;

    private boolean gameOver;
    private Player winner;
    private int currentTurn;
    private boolean extraTurnThisTurn;

    private final Random random;
    /**
     * Indica se, neste turno, o jogador tem a opção de ficar parado
     * (apenas quando começou o turno numa sala com alavanca, tentou
     * uma alavanca e falhou).
     */
    private boolean allowStayThisTurn;

    /**
     * @param labyrinth    labirinto do jogo
     * @param players      lista de jogadores (na ordem inicial)
     * @param questionPool pool de perguntas para os enigmas
     * @param mode         modo de jogo (MANUAL / AUTOMATIC)
     */
    public GameState(Labyrinth labyrinth,
                     UnorderedListADT<Player> players,
                     QuestionPool questionPool,
                     GameMode mode) {

        if (labyrinth == null || players == null || questionPool == null || mode == null) {
            throw new IllegalArgumentException("Parâmetros do GameState não podem ser nulos.");
        }

        this.labyrinth = labyrinth;
        this.players = players;
        this.questionPool = questionPool;
        this.mode = mode;

        this.turnQueue = new LinkedQueue<Player>();
        this.gameOver = false;
        this.winner = null;
        this.currentTurn = 1;
        this.extraTurnThisTurn = false;
        this.random = new Random();
        this.allowStayThisTurn = false;

        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            turnQueue.enqueue(it.next());
        }
    }

    public Labyrinth getLabyrinth() {
        return labyrinth;
    }

    public UnorderedListADT<Player> getPlayers() {
        return players;
    }

    public QuestionPool getQuestionPool() {
        return questionPool;
    }

    public GameMode getMode() {
        return mode;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean hasPlayersInQueue() {
        return !turnQueue.isEmpty();
    }

    /**
     * Indica se, neste turno, o jogador pode escolher ficar parado
     * na sala atual (opção extra no HumanController).
     */
    public boolean isStayAllowedThisTurn() {
        return allowStayThisTurn;
    }

    /**
     * Executa um turno para o jogador que está à frente da queue.
     * - Se estiver bloqueado, apenas gasta 1 turno de bloqueio e roda a fila.
     * - Caso contrário:
     * 1) tenta resolver enigma se começou o turno numa sala com enigma,
     * 2) tenta resolver alavanca se começou o turno numa sala com alavanca,
     * 3) escolhe movimento,
     * 4) move o jogador,
     * 5) aplica evento do corredor (se existir),
     * 6) trata alavanca / enigma da sala onde chegou,
     * 7) verifica vitória,
     * 8) roda fila (ou dá extra turn).
     */
    public void nextTurn() {
        if (gameOver) {
            System.out.println("O jogo já terminou.");
            return;
        }

        if (turnQueue.isEmpty()) {
            System.out.println("Não há jogadores na fila de turnos.");
            return;
        }

        allowStayThisTurn = false;

        Player current = turnQueue.first();
        System.out.println("=== Turno " + currentTurn + " - Jogador: " + current.getName() + " ===");

        if (current.getCurrentRoom() == labyrinth.getCenterRoom()) {
            declareWinner(current);
            return;
        }

        if (current.getBlockedTurns() > 0) {
            System.out.println(current.getName() + " está bloqueado. Faltam " +
                    current.getBlockedTurns() + " turnos.");
            current.decrementBlockedTurns();

            Player p = turnQueue.dequeue();
            turnQueue.enqueue(p);

            currentTurn++;
            return;
        }

        //Se falhar enigma, o turno acaba aqui (sem movimento).
        if (!handleRiddleAtTurnStart(current)) {
            Player p = turnQueue.dequeue();
            turnQueue.enqueue(p);
            currentTurn++;
            return;
        }

        handleLeverAtTurnStart(current);

        Room from = current.getCurrentRoom();
        Room to = current.getController().chooseMove(current, labyrinth, this);

        if (to == null || to == from) {
            System.out.println(current.getName() + " não se moveu.");
            Player p = turnQueue.dequeue();
            turnQueue.enqueue(p);
            currentTurn++;
            return;
        }

        Corridor corridor = labyrinth.getCorridor(from, to);
        if (corridor == null || corridor.isLocked()) {
            System.out.println("Corredor inválido ou bloqueado entre " + from + " e " + to +
                    ". Movimento cancelado.");
            Player p = turnQueue.dequeue();
            turnQueue.enqueue(p);
            currentTurn++;
            return;
        }

        current.setCurrentRoom(to);
        System.out.println(current.getName() + " moveu-se para " + to);

        extraTurnThisTurn = false;
        Event e = EventFactory.maybeCreateEvent(corridor.getWeight());
        applyCorridorEvent(e, current, corridor);

        handleLeverIfAny(current, to);
        handleRiddleIfAny(current, to);

        if (to == labyrinth.getCenterRoom()) {
            declareWinner(current);
            return;
        }

        if (!extraTurnThisTurn) {
            Player p = turnQueue.dequeue();
            turnQueue.enqueue(p);
        } else {
            extraTurnThisTurn = false;
        }

        currentTurn++;
    }

    private void declareWinner(Player winner) {
        this.gameOver = true;
        this.winner = winner;
        System.out.println(">>> VITÓRIA! O jogador " + winner.getName() +
                " chegou à sala central no turno " + currentTurn + ". <<<");
    }

    private void applyCorridorEvent(Event event, Player current, Corridor corridor) {
        if (event == null) {
            return;
        }

        System.out.println("Evento no corredor: " + event);

        switch (event.getType()) {
            case MOVE_BACK:
                handleMoveBack(event, current);
                break;
            case SKIP_TURNS:
                handleSkipTurns(event, current);
                break;
            case EXTRA_TURN:
                handleExtraTurn(event, current);
                break;
            case SWAP_PLAYER:
                handleSwapPlayer(event, current);
                break;
            case SHUFFLE_ALL:
                handleShuffleAll(event);
                break;
        }

        current.getStats().addEvent(
                new EventLogEntry(
                        current,
                        null,
                        event.getType(),
                        "Evento no corredor: " + corridor.toString(),
                        currentTurn
                )
        );
    }

    private void handleMoveBack(Event event, Player current) {
        int steps = event.getIntensity();
        if (steps <= 0) return;

        System.out.println(current.getName() + " recua " + steps + " casas.");
        current.moveBack(steps);
    }

    private void handleSkipTurns(Event event, Player current) {
        int turnsToSkip = event.getIntensity();
        if (turnsToSkip <= 0) return;

        System.out.println(current.getName() + " vai perder " + turnsToSkip + " turno(s).");
        current.setBlockedTurns(current.getBlockedTurns() + turnsToSkip);
    }

    private void handleExtraTurn(Event event, Player current) {
        System.out.println(current.getName() + " ganhou um turno extra!");
        extraTurnThisTurn = true;
    }

    /**
     * SWAP_PLAYER: o jogador escolhe outro jogador para trocar de posição.
     * Requer que o PlayerController implemente chooseSwapTarget(...).
     */
    private void handleSwapPlayer(Event event, Player current) {
        int total = getPlayersCount();
        if (total <= 1) {
            System.out.println("SWAP_PLAYER: não há outros jogadores.");
            return;
        }

        Player target = current.getController().chooseSwapTarget(current, players, this);
        if (target == null || target == current) {
            System.out.println("SWAP_PLAYER: escolha inválida, não há troca.");
            return;
        }

        Room roomCurrent = current.getCurrentRoom();
        Room roomTarget = target.getCurrentRoom();

        current.setCurrentRoom(roomTarget);
        target.setCurrentRoom(roomCurrent);

        current.markSwapBoundary();
        target.markSwapBoundary();

        System.out.println("SWAP_PLAYER: " + current.getName() +
                " trocou de posição com " + target.getName() + ".");

        current.getStats().addEvent(
                new EventLogEntry(current, target, EventType.SWAP_PLAYER,
                        "Trocou de posição com " + target.getName(), currentTurn)
        );
        target.getStats().addEvent(
                new EventLogEntry(target, current, EventType.SWAP_PLAYER,
                        "Trocou de posição com " + current.getName(), currentTurn)
        );
    }

    /**
     * SHUFFLE_ALL: baralha as posições de todos os jogadores.
     */
    private void handleShuffleAll(Event event) {
        int n = getPlayersCount();
        if (n <= 1) {
            System.out.println("SHUFFLE_ALL: só há um jogador, nada a baralhar.");
            return;
        }

        Player[] playerArray = new Player[n];
        Room[] rooms = new Room[n];

        int i = 0;
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            playerArray[i] = p;
            rooms[i] = p.getCurrentRoom();
            i++;
        }

        for (int k = n - 1; k > 0; k--) {
            int j = random.nextInt(k + 1);
            Room tmp = rooms[k];
            rooms[k] = rooms[j];
            rooms[j] = tmp;
        }

        for (int k = 0; k < n; k++) {
            playerArray[k].setCurrentRoom(rooms[k]);
        }

        System.out.println("SHUFFLE_ALL: posições dos jogadores baralhadas.");
    }

    private int getPlayersCount() {
        int count = 0;
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * Se o jogador começou o turno numa sala com alavanca ainda não ativada,
     * é obrigado a escolher logo uma alavanca antes de poder mover-se.
     * Se falhar, este turno fica com a opção de permanecer na sala.
     */
    private void handleLeverAtTurnStart(Player current) {
        Room room = current.getCurrentRoom();
        if (room == null || !room.hasLever()) {
            return;
        }

        Lever lever = room.getLever();
        if (lever == null || lever.isActivated()) {
            return;
        }

        LeverPuzzle puzzle = lever.getPuzzle();
        int leverCount = (puzzle != null) ? puzzle.getLeverCount() : 3;

        System.out.println(current.getName() + " está numa sala com alavanca (" + room.getId() + ").");
        System.out.println("Antes de te moveres, tens de escolher uma das " + leverCount + " alavancas...");

        int choice = current.getController().chooseLever(room, leverCount);

        boolean ok = lever.tryActivate(choice);

        if (ok) {
            System.out.println("Alavanca correta! Corredores a partir da sala " + room.getId() + " foram desbloqueados.");
            labyrinth.unlockCorridorsFrom(room);
            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(labyrinth));
            allowStayThisTurn = false;

        } else {
            System.out.println("\nEscolheste a alavanca errada. Podes tentar outra noutra jogada ou mover-te por outro caminho.");
            allowStayThisTurn = true;
        }
    }

    /**
     * Alavanca encontrada APÓS um movimento (primeira vez que entra na sala).
     * Aqui não é dado o direito de ficar parado neste turno: a escolha da alavanca
     * é um “bónus” em cima do movimento já feito.
     */
    private void handleLeverIfAny(Player current, Room room) {
        if (room == null || !room.hasLever()) {
            return;
        }

        Lever lever = room.getLever();
        if (lever == null || lever.isActivated()) {
            return;
        }

        LeverPuzzle puzzle = lever.getPuzzle();
        int leverCount = (puzzle != null) ? puzzle.getLeverCount() : 3;

        System.out.println(current.getName() + " encontrou uma alavanca na sala " + room.getId() + ".");
        System.out.println("Terás de escolher uma das " + leverCount + " alavancas...");

        int choice = current.getController().chooseLever(room, leverCount);

        boolean ok = lever.tryActivate(choice);

        if (ok) {
            System.out.println("Alavanca correta! Corredores a partir da sala " + room.getId() + " foram desbloqueados.");
            labyrinth.unlockCorridorsFrom(room);

            SwingUtilities.invokeLater(() -> LabyrinthViewer.show(labyrinth));
        } else {
            System.out.println("Escolheste a alavanca errada. Nada acontece... tenta noutra jogada.");
        }
    }

    /**
     * Se o jogador COMEÇA o turno numa sala com ENIGMA ativo:
     * - tenta responder logo no início;
     * - se falhar, o turno termina e não se pode mover;
     * - se acertar, o enigma é removido e pode mover-se normalmente.
     *
     * @return true se o jogador estiver livre para se mover neste turno;
     *         false se falhou o enigma e o turno termina.
     */
    private boolean handleRiddleAtTurnStart(Player current) {
        Room room = current.getCurrentRoom();
        if (room == null || !room.hasRiddle()) {
            return true; // sem enigma -> pode mover-se
        }

        if (questionPool.isCompletelyEmpty()) {
            System.out.println("Não há mais perguntas disponíveis. O enigma é ignorado.");
            room.setHasRiddle(false);
            return true;
        }

        Question q = questionPool.getRandomQuestion();
        if (q == null) {
            System.out.println("Falha ao obter pergunta. O enigma é ignorado.");
            room.setHasRiddle(false);
            return true;
        }

        System.out.println("Enigma na sala " + room.getId() + ":");
        int answerIndex = current.getController().answerQuestion(q);

        if (q.isCorrect(answerIndex)) {
            System.out.println("Resposta correta!");
            current.getStats().incCorrectRiddles();
            room.setHasRiddle(false);
            return true; // libertado, pode mover-se
        } else {
            System.out.println("Resposta errada. Ficas preso nesta sala até acertares no enigma.");
            current.getStats().incWrongRiddles();
            return false; // turno termina, sem movimento
        }
    }

    /**
     * Enigma encontrado APÓS um movimento (quando entra pela primeira vez na sala).
     * Se falhar aqui, o turno já está “gasto” de qualquer forma; a partir do próximo
     * turno o handleRiddleAtTurnStart(...) vai obrigá-lo a continuar a tentar.
     */
    private void handleRiddleIfAny(Player current, Room room) {
        if (room == null || !room.hasRiddle()) {
            return;
        }

        if (questionPool.isCompletelyEmpty()) {
            System.out.println("Não há mais perguntas disponíveis. O enigma é ignorado.");
            room.setHasRiddle(false);
            return;
        }

        Question q = questionPool.getRandomQuestion();
        if (q == null) {
            System.out.println("Falha ao obter pergunta. O enigma é ignorado.");
            room.setHasRiddle(false);
            return;
        }

        System.out.println("Enigma na sala " + room.getId() + ":");
        int answerIndex = current.getController().answerQuestion(q);

        if (q.isCorrect(answerIndex)) {
            System.out.println("Resposta correta!");
            current.getStats().incCorrectRiddles();
            room.setHasRiddle(false);
        } else {
            System.out.println("Resposta errada. Terás de tentar novamente noutra jogada.");
            current.getStats().incWrongRiddles();
        }
    }

}
