import java.util.Random;

public class Board {
    public final int numMines;
    public Cell[][] cells;
    public final int rows;
    public final int cols;

    public Board(int numMines, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        createCells(rows, cols, numMines);
    }

    private void createCells(int rows, int cols, int numMines) {
        cells = new Cell[cols][rows];
        var mineMap = setMines(numMines);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                cells[x][y] = new Cell();
                cells[x][y].isMine = mineMap[x][y];
            }
        }

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                cells[x][y].surroundingMines = neighbourMines(x, y);
            }
        }
    }

    private boolean[][] setMines(int numMines) {
        return new Random().ints(0, cols * rows) // Generate random integers in the range [0, cols*rows)
                .distinct() // Ensure uniqueness of positions
                .limit(numMines) // Limit to numMines positions
                .mapToObj(i -> new int[] { i / cols, i % cols }) // Map to coordinates (x, y)
                .collect(() -> new boolean[rows][cols], // Collect into boolean[][] array
                        (matrix, pos) -> matrix[pos[0]][pos[1]] = true,
                        (a, b) -> {
                        }); // No combi
    }

    public int neighbourMines(int x, int y) {
        int mines = 0;

        if (isValidCell(x - 1, y - 1, cells) && cells[x - 1][y - 1].isMine)
            mines++;
        if (isValidCell(x - 1, y, cells) && cells[x - 1][y].isMine)
            mines++;
        if (isValidCell(x - 1, y + 1, cells) && cells[x - 1][y + 1].isMine)
            mines++;
        if (isValidCell(x, y - 1, cells) && cells[x][y - 1].isMine)
            mines++;
        if (isValidCell(x, y + 1, cells) && cells[x][y + 1].isMine)
            mines++;
        if (isValidCell(x + 1, y - 1, cells) && cells[x + 1][y - 1].isMine)
            mines++;
        if (isValidCell(x + 1, y, cells) && cells[x + 1][y].isMine)
            mines++;
        if (isValidCell(x + 1, y + 1, cells) && cells[x + 1][y + 1].isMine)
            mines++;

        return mines;
    }

    private boolean isValidCell(int x, int y, Cell[][] cells) {
        return x >= 0 && x < cells.length && y >= 0 && y < cells[0].length;
    }

    public void resetBoard() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++)
                cells[x][y].content = "";
        }
    }
}
