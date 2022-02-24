import javax.swing.*;

public class MenuBarPanel extends JMenuBar {
    private final JMenuItem newGame;
    private final JMenuItem statistics;
    private final JMenuItem exit;

    public MenuBarPanel() {
        JMenu gameMenu = new JMenu("Game");
        newGame = new JMenuItem("   New Game");
        statistics = new JMenuItem("   Statistics");
        exit = new JMenuItem("   Exit");
        newGame.setName("New Game");
        statistics.setName("Statistics");
        exit.setName("Exit");
        gameMenu.add(newGame);
        gameMenu.add(statistics);
        gameMenu.add(exit);
        add(gameMenu);
    }

    public JMenuItem getNewGameMenuItem() {
        return newGame;
    }

    public JMenuItem getStatisticsMenuItem() {
        return statistics;
    }

    public JMenuItem getExitMenuItem() {
        return exit;
    }
}
