import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter your name here: ");
        String playerName = scnr.nextLine();

        int[] board = createBoard();
        boolean isPlayerTurn = true;

        while (!isTerminal(board)) {
            if (isPlayerTurn) {
                System.out.println("\n" + playerName + "'s Turn:");
                System.out.println("Enter your move in this format ([a-h][1-8]):");
                String playerMove = scnr.nextLine();

                while (!isValidMove(board, playerMove)) {
                    System.out.println("Enter your move in this format ([a-h][1-8]):");
                    playerMove = scnr.nextLine();
                }

                int row = playerMove.charAt(0) - 'a';
                int col = Character.getNumericValue(playerMove.charAt(1)) - 1;

                board[(col * BOARD_WIDTH) + row] = 1;
                isPlayerTurn = false;

                printBoard(board);
            } else {
                System.out.println("\nCPU's Turn:");
                board = performMove(board);
                isPlayerTurn = true;

                printBoard(board);
            }
        }

        int winner = checkWhoWon(board);
        switch (winner) {
            case 1:
                System.out.println("\n" + playerName + " won the game!");
                break;

            case -1:
                System.out.println("\nCPU won the game!");
                break;

            case 0:
                System.out.println("Draw.");
                break;

            default:
                System.out.println("ion think the game ended");
        }

        System.out.println("Ending game...");
        scnr.close();
        System.exit(1);
    }

    private static int[] performMove(int[] board) {
        long startTime = System.currentTimeMillis();
        long deadline = startTime + 5000;

        List<int[]> possibleMoves = getNextMoves(board, -1);
        int bestScore = Integer.MAX_VALUE;
        List<int[]> tiedBestMoves = new ArrayList<>();

        for (int[] move : possibleMoves) {
            int score = doMinimax(move, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, deadline);

            if (score < bestScore) {
                bestScore = score;
                tiedBestMoves.clear();
                tiedBestMoves.add(move);
            } else if (score == bestScore) {
                tiedBestMoves.add(move);
            }
        }

        return tiedBestMoves.get((int) (Math.random() * tiedBestMoves.size()));
    }

    private static int doMinimax(int[] board, int depth, int alpha, int beta, boolean isMax, long deadline) {
        if (System.currentTimeMillis() >= deadline) {
            return evaluate(board);
        }

        if (depth == 0 || isTerminal(board)) {
            return evaluate(board);
        }

        if (isMax) {
            int maxEval = Integer.MIN_VALUE;

            for (int[] move : getNextMoves(board, 1)) {
                int eval = doMinimax(move, depth - 1, alpha, beta, false, deadline);
                maxEval = Math.max(maxEval, eval);

                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (int[] move: getNextMoves(board, -1)) {
                int eval = doMinimax(move, depth - 1, alpha, beta, true, deadline);
                minEval = Math.min(minEval, eval);

                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }

            return minEval;
        }
    }

    private static int evaluate(int[] board) {
        int totalScore = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c <= (8 - WIN_LEN); c++) {
                int start = r * 8 + c;
                int xCount = 0, oCount = 0;

                for (int i = 0; i < WIN_LEN; i++) {
                    int val = board[start + i];
                    if (val == 1) xCount++;
                    else if (val == -1) oCount++;
                }
                totalScore += scoreWindow(xCount, oCount);
            }
        }

        for (int c = 0; c < 8; c++) {
            for (int r = 0; r <= (8 - WIN_LEN); r++) {
                int start = r * 8 + c;
                int xCount = 0, oCount = 0;

                for (int i = 0; i < WIN_LEN; i++) {
                    int val = board[start + (i * 8)];
                    if (val == 1) xCount++;
                    else if (val == -1) oCount++;
                }
                totalScore += scoreWindow(xCount, oCount);
            }
        }

        return totalScore;
    }

    private static int scoreWindow(int x, int o) {
        if (x > 0 && o > 0) return 0;

        if (x == 4) return FOUR;
        if (o == 4) return -FOUR;

        if (o == 0) {
            if (x == 3) return THREE;
            if (x == 2) return TWO;
            if (x == 1) return ONE;
        } else {
            if (o == 3) return -N_THREE;
        }

        return 0;
    }

    private static boolean isTerminal(int[] board) {
        if (checkIfWin(board, 1)) return true;
        if (checkIfWin(board, -1 )) return true;

        for (int cell : board) if (cell == 0) return false;

        return true;
    }

    private static int checkWhoWon(int[] board) {
        if (checkIfWin(board, 1)) return 1;
        if (checkIfWin(board, -1)) return -1;

        for (int cell : board) if (cell == 0) return 0;

        return 0;
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

    private static boolean isValidMove(int[] board, String move) {
        if (move.length() != 2) return false;
        if (!Character.isLetter(move.charAt(0))) return false;
        if (!Character.isDigit(move.charAt(1))) return false;

        int row = move.charAt(0) - 'a';
        int col = Character.getNumericValue(move.charAt(1)) - 1;

        if (board[(col * BOARD_WIDTH) + row] != 0) return false;
        else return true; 
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