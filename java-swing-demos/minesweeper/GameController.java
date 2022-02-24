import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class GameController implements MouseListener, ActionListener, WindowListener {
    private final Score score;
    private boolean playing;
    private Board board;
    private View view;

    public GameController() {
        score = new Score();
        board = new Board(10, 9, 9);
        // view = new View(new UI(board.rows, board.cols, board.numMines), this);
        view = new View(new MainUI(board.rows, board.cols, board.numMines), this);
        this.playing = false;
    }

    private void checkGame() {
        if (isFinished())
            gameWon();
    }

    public void newGame() {
        this.playing = false;
        board = new Board(10, 9, 9);
        view.newGame(board.numMines);
    }

    public void restartGame() {
        this.playing = false;
        board.resetBoard();
        view.restartGame(board.numMines);
    }

    private void endGame() {
        playing = false;
        showAll();
    }

    public void gameWon() {
        score.incCurrentStreak();
        score.incCurrentWinningStreak();
        score.incGamesWon();
        score.incGamesPlayed();
        view.gui.interruptTimer();
        endGame();
        view.showGameWon(score, () -> newGame(), () -> windowClosing(null));
    }

    public void gameLost() {
        score.decCurrentStreak();
        score.incCurrentLosingStreak();
        score.incGamesPlayed();
        view.gui.interruptTimer();
        endGame();
        view.showGameLost(score, () -> newGame(), () -> restartGame(), () -> windowClosing(null));
    }

    public void showScore() {
        view.showScore(score);
    }

    private void showAll() {
        String cellSolution;
        var cells = board.cells;
        JButton[][] buttons = view.gui.getButtons();
        for (int x = 0; x < board.cols; x++) {
            for (int y = 0; y < board.rows; y++) {
                cellSolution = cells[x][y].content;
                // Is the cell still unrevealed
                if (cellSolution.equals("")) {
                    buttons[x][y].setIcon(null);
                    // Get Neighbours
                    cellSolution = Integer.toString(cells[x][y].surroundingMines);
                    // Is it a mine?
                    if (cells[x][y].isMine) {
                        cellSolution = "M";
                        // mine
                        buttons[x][y].setIcon(view.gui.getIconMine());
                        buttons[x][y].setBackground(Color.lightGray);
                    } else {
                        if (cellSolution.equals("0")) {
                            buttons[x][y].setText("");
                            buttons[x][y].setBackground(Color.lightGray);
                        } else {
                            buttons[x][y].setBackground(Color.lightGray);
                            buttons[x][y].setText(cellSolution);
                            view.gui.setTextColor(buttons[x][y]);
                        }
                    }
                } else if (cellSolution.equals("F")) {
                    if (!cells[x][y].isMine)
                        buttons[x][y].setBackground(Color.orange);
                    else
                        buttons[x][y].setBackground(Color.green);
                }
            }
        }
    }

    public boolean isFinished() {
        var isFinished = true;
        String cellSolution;
        var cells = board.cells;
        for (int x = 0; x < board.cols; x++) {
            for (int y = 0; y < board.rows; y++) {
                cellSolution = Integer.toString(cells[x][y].surroundingMines);
                if (cells[x][y].isMine)
                    cellSolution = "F";
                if (!cells[x][y].content.equals(cellSolution)) {
                    isFinished = false;
                    break;
                }
            }
        }
        return isFinished;
    }

    private void clearCellsRecursive(int xCo, int yCo) {
        int neighbours;
        var cells = board.cells;
        var buttons = view.gui.getButtons();
        for (int x = makeValidCoordinateX(xCo - 1); x <= makeValidCoordinateX(xCo + 1); x++) {
            for (int y = makeValidCoordinateY(yCo - 1); y <= makeValidCoordinateY(yCo + 1); y++) {
                // Only unrevealed cells need to be revealed.
                if (cells[x][y].content.equals("")) {
                    // Get the neighbours of the current (neighbouring) cell.
                    neighbours = cells[x][y].surroundingMines;
                    // Reveal the neighbours of the current (neighbouring) cell
                    cells[x][y].content = (Integer.toString(neighbours));
                    if (!cells[x][y].isMine)
                        buttons[x][y].setIcon(null);
                    // Is this (neighbouring) cell a "zero" cell itself?
                    buttons[x][y].setBackground(Color.lightGray);
                    if (neighbours == 0) {
                        buttons[x][y].setText("");
                        clearCellsRecursive(x, y);
                    } else {
                        buttons[x][y].setText(Integer.toString(neighbours));
                        view.gui.setTextColor(buttons[x][y]);
                    }
                }
            }
        }
    }

    private int makeValidCoordinateX(int x) {
        return Math.max(0, Math.min(x, board.cells.length - 1));
    }

    private int makeValidCoordinateY(int y) {
        return Math.max(0, Math.min(y, board.cells[0].length - 1));
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (playing) {
            var question = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("question.png")));
            Object[] options = { "Quit", "Cancel" };
            int quit = JOptionPane.showOptionDialog(
                    null, "What do you want to do with the game in progress?", "New Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, question, options, options[1]);
            switch (quit) {
                case JOptionPane.YES_OPTION -> {
                    score.incGamesPlayed();
                    System.exit(0);
                }
            }
        } else
            System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JMenuItem) e.getSource()).getName()) {
            case "New Game" -> {
                if (playing) {
                    switch (view.showNewGame()) {
                        case JOptionPane.YES_OPTION -> {
                            newGame();
                            score.incGamesPlayed();
                        }
                        case JOptionPane.NO_OPTION -> {
                            score.incGamesPlayed();
                            restartGame();
                        }
                    }
                }
            }
            case "Exit" -> windowClosing(null);
            default -> showScore();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!playing) {
            view.gui.startTimer();
            playing = true;
        }
        if (playing) {
            var button = (JButton) e.getSource();
            var coordinates = button.getName().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            // Get cell information.
            boolean isMine = board.cells[x][y].isMine;
            int neighbours = board.cells[x][y].surroundingMines;
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (!board.cells[x][y].content.equals("F")) {
                    button.setIcon(null);
                    if (isMine) {
                        button.setIcon(view.gui.getIconRedMine());
                        button.setBackground(Color.red);
                        board.cells[x][y].content = "M";
                        gameLost();
                    } else {
                        board.cells[x][y].content = (Integer.toString(neighbours));
                        button.setText(Integer.toString(neighbours));
                        view.gui.setTextColor(button);
                        button.setBackground(Color.lightGray);
                        if (neighbours == 0) {
                            button.setText("");
                            clearCellsRecursive(x, y);
                        }
                    }
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (board.cells[x][y].content.equals("F")) {
                    board.cells[x][y].content = "";
                    button.setText("");
                    button.setBackground(new Color(0, 110, 140));
                    // simple blue
                    button.setIcon(view.gui.getIconTile());
                    view.gui.incMines();
                } else if (board.cells[x][y].content.equals("")) {
                    board.cells[x][y].content = "F";
                    button.setBackground(Color.blue);
                    button.setIcon(view.gui.getIconFlag());
                    view.gui.decMines();
                }
            }
            checkGame();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
}
