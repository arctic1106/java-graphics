import java.util.Scanner;

public class Conecta4CLI {
    private final Scanner scanner = new Scanner(System.in);
    private C4Board board = new C4Board();

    public static void main(String[] args) {
        new Conecta4CLI().runGame();
    }

    private Integer getPlayerMove() {
        int playerMove = -1;
        while (!board.getLegalMoves().contains(playerMove)) {
            System.out.println("Enter a legal column (0-6):");
            playerMove = scanner.nextInt();
        }
        return playerMove;
    }

    private void runGame() {
        while (true) {
            var humanMove = getPlayerMove();
            board = board.move(humanMove);
            if (board.isWin()) {
                System.out.println("Human wins!");
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
            var computerMove = Minimax.findBestMove(board, 7);
            System.out.println("Computer move is " + computerMove);
            board = board.move(computerMove);
            System.out.println(board);
            if (board.isWin()) {
                System.out.println("Computer wins!");
                break;
            } else if (board.isDraw()) {
                System.out.println("Draw!");
                break;
            }
        }
    }
}
