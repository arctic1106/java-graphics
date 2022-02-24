import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class View {
    public UI gui;
    public MainUI gui2;

    public View(UI gui, GameController gameController) {
        this.gui = gui;
        gui.setButtonListeners(gameController);
        gui.setVisible(true);
        gui.setIcons();
        gui.hideAll();
    }

    public View(MainUI gui, GameController controller) {
        this.gui2 = gui;
        gui.setVisible(true);
    }

    public void newGame(int numberOfMines) {
        gui.interruptTimer();
        gui.resetTimer();
        gui.initGame();
        gui.setMines(numberOfMines);
    }

    public void restartGame(int numberOfMines) {
        gui.interruptTimer();
        gui.resetTimer();
        gui.initGame();
        gui.setMines(numberOfMines);
    }

    public void showGameWon(Score score, Runnable newGame, Runnable windowClosing) {
        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);
        JLabel message = new JLabel("Congratulations, you won the game!", SwingConstants.CENTER);
        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(6, 1, 0, 10));

        ArrayList<Time> bTimes = score.bestTimes;
        if (bTimes.isEmpty() || (bTimes.get(0).time > gui.getTimePassed()))
            statistics.add(new JLabel("    You have the fastest time for this difficulty level!    "));

        score.addTime(gui.getTimePassed(), new Date(System.currentTimeMillis()));
        JLabel time = new JLabel("  Time:  " + Integer.toString(gui.getTimePassed()) + " seconds            Date:  "
                + new Date(System.currentTimeMillis()));
        JLabel bestTime = new JLabel();
        if (bTimes.isEmpty())
            bestTime.setText("  Best Time:  ---                  Date:  ---");
        else
            bestTime.setText(
                    "  Best Time:  " + bTimes.get(0).time + " seconds            Date:  " + bTimes.get(0).date);

        JLabel gPlayed = new JLabel("  Games Played:  " + score.gamesPlayed);
        JLabel gWon = new JLabel("  Games Won:  " + score.gamesWon);
        JLabel gPercentage = new JLabel("  Win Percentage:  " + score.getWinPercentage() + "%");

        statistics.add(time);
        statistics.add(bestTime);
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        statistics.setBorder(loweredetched);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2, 10, 0));

        JButton exit = new JButton("Exit");
        JButton playAgain = new JButton("Play Again");

        exit.addActionListener(e -> {
            dialog.dispose();
            windowClosing.run();
        });

        playAgain.addActionListener(e -> {
            dialog.dispose();
            newGame.run();
        });

        buttons.add(exit);
        buttons.add(playAgain);

        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20, 20));
        c.add(message, BorderLayout.NORTH);
        c.add(statistics, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dialog.dispose();
                newGame.run();
            }
        });

        dialog.setTitle("Game Won");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);
    }

    public void showGameLost(Score score, Runnable newGame, Runnable restartGame, Runnable windowClosing) {
        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);
        JLabel message = new JLabel("Sorry, you lost this game. Better luck next time!", SwingConstants.CENTER);
        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(5, 1, 0, 10));
        JLabel time = new JLabel("  Time:  " + Integer.toString(gui.getTimePassed()) + " seconds");
        JLabel bestTime = new JLabel();
        ArrayList<Time> bTimes = score.bestTimes;

        if (bTimes.isEmpty())
            bestTime.setText("                        ");
        else
            bestTime.setText(
                    "  Best Time:  " + bTimes.get(0).time + " seconds            Date:  " + bTimes.get(0).date);

        JLabel gPlayed = new JLabel("  Games Played:  " + score.gamesPlayed);
        JLabel gWon = new JLabel("  Games Won:  " + score.gamesWon);
        JLabel gPercentage = new JLabel("  Win Percentage:  " + score.getWinPercentage() + "%");

        statistics.add(time);
        statistics.add(bestTime);
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        statistics.setBorder(loweredetched);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 2, 0));

        JButton exit = new JButton("Exit");
        JButton restart = new JButton("Restart");
        JButton playAgain = new JButton("Play Again");

        exit.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            windowClosing.run();
        });
        restart.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            restartGame.run();
        });
        playAgain.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            newGame.run();
        });
        buttons.add(exit);
        buttons.add(restart);
        buttons.add(playAgain);

        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20, 20));
        c.add(message, BorderLayout.NORTH);
        c.add(statistics, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);

        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dialog.dispose();
                newGame.run();
            }
        });

        dialog.setTitle("Game Lost");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);
    }

    public void showScore(Score score) {
        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);
        JPanel bestTimes = new JPanel();
        bestTimes.setLayout(new GridLayout(5, 1));

        ArrayList<Time> bTimes = score.bestTimes;

        for (Time bTime : bTimes) {
            JLabel t = new JLabel("  " + bTime.time + "           " + bTime.date);
            bestTimes.add(t);
        }
        if (bTimes.isEmpty()) {
            JLabel t = new JLabel("                               ");
            bestTimes.add(t);
        }
        TitledBorder b = BorderFactory.createTitledBorder("Best Times");
        b.setTitleJustification(TitledBorder.LEFT);
        bestTimes.setBorder(b);

        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(6, 1, 0, 10));
        JLabel gPlayed = new JLabel("  Games Played:  " + score.gamesPlayed);
        JLabel gWon = new JLabel("  Games Won:  " + score.gamesWon);
        JLabel gPercentage = new JLabel("  Win Percentage:  " + score.getWinPercentage() + "%");
        JLabel lWin = new JLabel("  Longest Winning Streak:  " + score.longestWinningStreak);
        JLabel lLose = new JLabel("  Longest Losing Streak:  " + score.longestLosingStreak);
        JLabel currentStreak = new JLabel("  Current Streak:  " + score.currentStreak);
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);
        statistics.add(lWin);
        statistics.add(lLose);
        statistics.add(currentStreak);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        statistics.setBorder(loweredetched);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2, 10, 0));
        JButton close = new JButton("Close");
        JButton reset = new JButton("Reset");

        close.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });

        reset.addActionListener((ActionEvent e) -> {
            ImageIcon question = new ImageIcon(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("question.png")));
            int option = JOptionPane.showOptionDialog(null, "Do you want to reset all your statistics to zero?",
                    "Reset Statistics", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, question, null, null);
            switch (option) {
                case JOptionPane.YES_OPTION -> {
                    score.resetScore();
                    dialog.dispose();
                    showScore(score);
                }
            }
        });
        buttons.add(close);
        buttons.add(reset);
        if (score.gamesPlayed == 0)
            reset.setEnabled(false);
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20, 20));
        c.add(bestTimes, BorderLayout.WEST);
        c.add(statistics, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dialog.setTitle("Minesweeper Statistics - Haris Muneer");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);
    }

    public int showNewGame() {
        ImageIcon question = new ImageIcon(
                Objects.requireNonNull(getClass().getClassLoader().getResource("question.png")));
        Object[] options = { "Quit and Start a New Game", "Restart", "Keep Playing" };
        return JOptionPane.showOptionDialog(null, "What do you want to do with the game in progress?",
                "New Game",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, question, options, options[2]);
    }
}
