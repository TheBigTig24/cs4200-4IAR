import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class FourInARow {

    private static final int BOARD_WIDTH = 8;
    private static final int WIN_LEN = 4;

    // Rule Book
    private static final int FOUR = 676767;
    private static final int THREE = 100;
    private static final int TWO = 10;
    private static final int ONE = 1;
    private static final int N_THREE = 467;

    public static void main(String[] args) {
        int[] board = createBoard();
        printBoard(board);


    }

    private static int[] performMove(int[] board) {
        List<int[]> possibleMoves = getNextMoves(board, -1);
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        List<int[]> tiedBestMoves = new ArrayList<>();

        for (int[] move : possibleMoves) {
            int score = doMinimax(move, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        }

        return null;
    }

    private static int doMinimax(int[] board, int depth, int alpha, int beta, int isMax) {
        if (depth == 0 || isTerminal(board)) {
            return evaluate(board);
        }
    }

    private static int evaluate(List<int[]> possibleMoves) {
        int totalScore = 0;

        for (int i = 0; i < possibleMoves.size(); i++) {
            int vl = 0, ht = 0;
            while (vl < possibleMoves.get(i).length - WIN_LEN) {
                int xCount = 0, oCount = 0;
                for (int vr = vl; vr < WIN_LEN; vr++) {
                    if (possibleMoves.get(i)[vr] == 1) xCount++;
                    else if (possibleMoves.get(i)[vr] == -1) oCount++;
                }

                if (xCount == 0 && oCount == 0) totalScore += ONE;
                else if (xCount == 1) totalScore += TWO;
                else if (xCount == 2) totalScore += THREE;
                else if (xCount == 3) totalScore += FOUR;
                else if (oCount == 3) totalScore += N_THREE;
                
                if ( (vl + 4) % 8 == 0) vl += 4;
                else vl++;
            }

            while (ht < (possibleMoves.get(i).length * 3) / 4) {
                int xCount = 0, oCount = 0;
                for (int hb = ht; hb < WIN_LEN; hb += 8) {
                    if (possibleMoves.get(i)[hb] == 1) xCount++;
                    else if (possibleMoves.get(i)[hb] == -1) oCount++;
                }

                if (xCount == 0 && oCount == 0) totalScore += ONE;
                else if (xCount == 1) totalScore += TWO;
                else if (xCount == 2) totalScore += THREE;
                else if (xCount == 3) totalScore += FOUR;
                else if (oCount == 3) totalScore += N_THREE;
                
                ht++;
            }
        }

        return totalScore;
    }

    private static boolean checkIfWin(int[] board, int player) {
        for (int r = 0; r < BOARD_WIDTH; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                int idx = r * BOARD_WIDTH + c;

                if (board[idx] != player) continue;

                if (c <= BOARD_WIDTH - WIN_LEN) {
                    if (board[idx + 1] == player &&
                        board[idx + 2] == player &&
                        board[idx + 3] == player
                    ) {
                        return true;
                    }
                }

                if (r <= BOARD_WIDTH - WIN_LEN) {
                    if (board[idx + BOARD_WIDTH] == player &&
                        board[idx + BOARD_WIDTH * 2] == player &&
                        board[idx + BOARD_WIDTH * 3] == player
                    ) {
                        return true;
                    }
                }


            }
        }
        return false;
    }

    /**
     * Gets all next possible moves
     * @param board
     * @param turn
     * @return List<int[]> List of all board positions with possible move
     */
    private static List<int[]> getNextMoves(int[] board, int turn) {
        List<int[]> moves = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                int[] copy = board.clone();
                if (turn > 0) copy[i] = 1;
                else if (turn < 0) copy[i] = -1;
                moves.add(copy);
            }
        }

        return moves;
    }

    /**
     * Creates the board.
     * @return int[] board
     */
    private static int[] createBoard() {
        int[] board = new int[BOARD_WIDTH * BOARD_WIDTH];
        Arrays.fill(board, 0);
        return board;
    }

    /**
     * Prints the board.
     * @param board
     */
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