public class Minimax {
    // Find the best possible outcome for originalPlayer
    public static <Move> double minimax(Board<Move> board, boolean maximizing, Piece originalPlayer, int maxDepth) {
        // Base case - terminal position or maximum depth reached
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }
        // Recursive case - maximize your gains or minimize the opponent's gains
        if (maximizing) {
            double bestEval = Double.NEGATIVE_INFINITY; // result must be higher
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), false, originalPlayer, maxDepth - 1);
                bestEval = Math.max(result, bestEval);
            }
            return bestEval;
        } else { // minimizing
            double worstEval = Double.POSITIVE_INFINITY; // result must be lower
            for (Move move : board.getLegalMoves()) {
                double result = minimax(board.move(move), true, originalPlayer, maxDepth - 1);
                worstEval = Math.min(result, worstEval);
            }
            return worstEval;
        }
    }

    // Find the best possible move in the current position
    // looking up to maxDepth ahead
    public static <Move> Move findBestMove(Board<Move> board, int maxDepth) {
        double bestEval = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for (var move : board.getLegalMoves()) {
            double result = alphabeta(board.move(move), false, board.getTurn(), maxDepth);
            if (result > bestEval) {
                bestEval = result;
                bestMove = move;
            }
        }
        return bestMove;
    }

    // Helper that sets alpha and beta for the first call
    public static <Move> double alphabeta(Board<Move> board, boolean maximizing, Piece originalPlayer,
            int maxDepth) {
        return alphabeta(board, maximizing, originalPlayer, maxDepth, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY);
    }

    // Evaluates a Board b
    private static <Move> double alphabeta(
            Board<Move> board,
            boolean maximizing,
            Piece originalPlayer,
            int maxDepth,
            double alpha,
            double beta) {
        // Base case - terminal position or maximum depth reached
        if (board.isWin() || board.isDraw() || maxDepth == 0) {
            return board.evaluate(originalPlayer);
        }

        // Recursive case - maximize your gains or minimize the opponent's gains
        if (maximizing) {
            for (Move m : board.getLegalMoves()) {
                alpha = Math.max(alpha, alphabeta(board.move(m), false, originalPlayer, maxDepth - 1, alpha, beta));
                if (beta <= alpha)
                    break; // check cutoff
            }
            return alpha;
        } else { // minimizing
            for (Move m : board.getLegalMoves()) {
                beta = Math.min(beta, alphabeta(board.move(m), true, originalPlayer, maxDepth - 1, alpha, beta));
                if (beta <= alpha)
                    break; // check cutoff
            }
            return beta;
        }
    }
}
