import java.util.Arrays;

class FourInARow {

    private static final int BOARD_WIDTH = 8;

    public static void main(String[] args) {
        int[] board = createBoard();
        printBoard(board);
    }

    private static int[] createBoard() {
        int[] board = new int[BOARD_WIDTH * BOARD_WIDTH];
        Arrays.fill(board, 0);
        return board;
    }

    private static void printBoard(int[] board) {
        for (int i = 0; i < (BOARD_WIDTH * BOARD_WIDTH); i++) {
            int val = board[i];

            if (val == 0) System.out.print("_ ");
            else if (val == 1) System.out.print("X ");
            else if (val == -1) System.out.print("O ");

            if ( (i + 1) % BOARD_WIDTH == 0) System.out.println();
        }
    }
}