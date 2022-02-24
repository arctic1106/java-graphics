import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe implements ActionListener {
    int step = 0;
    JFrame frame = new JFrame();
    JPanel titlePanel = new JPanel();
    JLabel text_field = new JLabel();
    JLabel player_turn = new JLabel();
    JPanel buttonPanel = new JPanel();
    JButton[] buttons = new JButton[9];
    JPanel resetPanel = new JPanel();
    JButton resetButton = new JButton();
    boolean player1_turn;
    Random random = new Random();
    Color defButtonColor = new Color(33, 40, 69);
    Color yellowButton = new Color(248, 211, 32);

    TicTacToe() {
        frame.setTitle("Tres En Raya");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 500);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        text_field.setText("Tic Tac Toe");
        text_field.setFont(new Font("Segoe UI", Font.BOLD, 32));
        text_field.setBackground(defButtonColor);
        text_field.setForeground(new Color(255, 255, 255));
        text_field.setHorizontalAlignment(JLabel.CENTER);
        text_field.setOpaque(true);
        player_turn.setForeground(new Color(248, 211, 32));
        player_turn.setBackground(new Color(33, 40, 69));
        player_turn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        player_turn.setHorizontalAlignment(JLabel.CENTER);
        player_turn.setOpaque(true);
        titlePanel.setLayout(new GridLayout(2, 0));
        titlePanel.setBounds(0, 0, 800, 100);
        titlePanel.add(text_field);
        titlePanel.add(player_turn);
        frame.add(titlePanel, BorderLayout.NORTH);

        buttonPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 60));
            buttons[i].setBackground(defButtonColor);
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }
        frame.add(buttonPanel, BorderLayout.CENTER);
        resetPanel.setLayout(new GridLayout(1, 1));
        resetPanel.add(resetButton);
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 32));
        resetButton.setText("Reset");
        resetButton.setFocusable(false);
        resetButton.setBackground(yellowButton);
        resetButton.addActionListener(this);
        frame.add(resetPanel, BorderLayout.SOUTH);
        randomTurn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton)
            reset();
        else {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == buttons[i]) {
                    if (player1_turn && (buttons[i].getText().isEmpty())) {
                        step++;
                        buttons[i].setForeground(Color.WHITE);
                        buttons[i].setText("X");
                        player1_turn = false;
                        player_turn.setText("O turn");
                        check();
                    } else if ((!player1_turn) && (buttons[i].getText().isEmpty())) {
                        step++;
                        buttons[i].setForeground(Color.WHITE);
                        buttons[i].setText("O");
                        player1_turn = true;
                        player_turn.setText("X turn");
                        check();
                    }
                }
            }
        }

    }

    void randomTurn() {
        if (random.nextInt(2) == 1) {
            player1_turn = true;
            player_turn.setText("X turn");
        } else {
            player1_turn = false;
            player_turn.setText("O turn");
        }
    }

    public void check() {
        if (step == 9)
            player_turn.setText("Draw");
        if ((buttons[0].getText().equals("X")) && (buttons[1].getText().equals("X"))
                && (buttons[2].getText().equals("X")))
            xWins(0, 1, 2);
        if ((buttons[3].getText().equals("X")) && (buttons[4].getText().equals("X"))
                && (buttons[5].getText().equals("X")))
            xWins(3, 4, 5);
        if ((buttons[6].getText().equals("X")) && (buttons[7].getText().equals("X"))
                && (buttons[8].getText().equals("X")))
            xWins(6, 7, 8);
        if ((buttons[0].getText().equals("X")) && (buttons[3].getText().equals("X"))
                && (buttons[6].getText().equals("X")))
            xWins(0, 3, 6);
        if ((buttons[1].getText().equals("X")) && (buttons[4].getText().equals("X"))
                && (buttons[7].getText().equals("X")))
            xWins(1, 4, 7);
        if ((buttons[2].getText().equals("X")) && (buttons[5].getText().equals("X"))
                && (buttons[8].getText().equals("X")))
            xWins(2, 5, 8);
        if ((buttons[0].getText().equals("X")) && (buttons[4].getText().equals("X"))
                && (buttons[8].getText().equals("X")))
            xWins(0, 4, 8);
        if ((buttons[2].getText().equals("X")) && (buttons[4].getText().equals("X"))
                && (buttons[6].getText().equals("X")))
            xWins(2, 4, 6);
        // check O win conditions
        if ((buttons[0].getText().equals("O")) && (buttons[1].getText().equals("O"))
                && (buttons[2].getText().equals("O")))
            oWins(0, 1, 2);
        if ((buttons[3].getText().equals("O")) && (buttons[4].getText().equals("O"))
                && (buttons[5].getText().equals("O")))
            oWins(3, 4, 5);
        if ((buttons[6].getText().equals("O")) && (buttons[7].getText().equals("O"))
                && (buttons[8].getText().equals("O")))
            oWins(6, 7, 8);
        if ((buttons[0].getText().equals("O")) && (buttons[3].getText().equals("O"))
                && (buttons[6].getText().equals("O")))
            oWins(0, 3, 6);
        if ((buttons[1].getText().equals("O")) && (buttons[4].getText().equals("O"))
                && (buttons[7].getText().equals("O")))
            oWins(1, 4, 7);
        if ((buttons[2].getText().equals("O")) && (buttons[5].getText().equals("O"))
                && (buttons[8].getText().equals("O")))
            oWins(2, 5, 8);
        if ((buttons[0].getText().equals("O")) && (buttons[4].getText().equals("O"))
                && (buttons[8].getText().equals("O")))
            oWins(0, 4, 8);
        if ((buttons[2].getText().equals("O")) && (buttons[4].getText().equals("O"))
                && (buttons[6].getText().equals("O")))
            oWins(2, 4, 6);
    }

    public void xWins(int a, int b, int c) {
        buttons[a].setBackground(yellowButton);
        buttons[b].setBackground(yellowButton);
        buttons[c].setBackground(yellowButton);
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty())
                buttons[i].setEnabled(false);
        }
        player_turn.setText("X win ");
    }

    public void oWins(int a, int b, int c) {
        buttons[a].setBackground(yellowButton);
        buttons[b].setBackground(yellowButton);
        buttons[c].setBackground(yellowButton);
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty())
                buttons[i].setEnabled(false);
        }
        player_turn.setText("O win ");
    }

    void reset() {
        step = 0;
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setBackground(defButtonColor);
            buttons[i].setEnabled(true);
        }
        randomTurn();
    }
}
