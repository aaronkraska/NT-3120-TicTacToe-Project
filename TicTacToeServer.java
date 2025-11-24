// -------------------------------
// TicTacToeServer.java
// -------------------------------
// A simple networked Tic-Tac-Toe server using plain TCP sockets.
// Waits for two clients, assigns roles (X and O), and controls the game flow.

import java.io.*;
import java.net.*;

public class TicTacToeServer {

    // Port number that both clients will connect to
    private static final int PORT = 5000;

    public static void main(String[] args) {
        // The try-with-resources block automatically closes the ServerSocket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            // ---- STEP 1: WAIT FOR TWO PLAYERS ----
            System.out.println("Waiting for Player X...");
            Socket playerXSocket = serverSocket.accept(); // blocks until client connects
            System.out.println("Player X connected.");

            System.out.println("Waiting for Player O...");
            Socket playerOSocket = serverSocket.accept();
            System.out.println("Player O connected.");

            // ---- STEP 2: PREPARE STREAMS ----
            // Each socket has input and output streams.
            // BufferedReader for reading lines; PrintWriter for writing lines.
            BufferedReader inX = new BufferedReader(new InputStreamReader(playerXSocket.getInputStream()));
            PrintWriter outX = new PrintWriter(playerXSocket.getOutputStream(), true);

            BufferedReader inO = new BufferedReader(new InputStreamReader(playerOSocket.getInputStream()));
            PrintWriter outO = new PrintWriter(playerOSocket.getOutputStream(), true);

            // ---- STEP 3: INFORM PLAYERS OF THEIR ROLES ----
            outX.println("ROLE X");
            outO.println("ROLE O");

            // ---- STEP 4: INITIALIZE GAME STATE ----
            char[][] board = new char[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = '.'; // '.' means an empty cell
                }
            }

            char currentPlayer = 'X'; // X always starts
            boolean gameOver = false;

            // ---- STEP 5: MAIN GAME LOOP ----
            while (!gameOver) {
                // Send the current board to both clients
                sendBoard(outX, board);
                sendBoard(outO, board);

                // Tell both players whose turn it is
                outX.println("TURN " + currentPlayer);
                outO.println("TURN " + currentPlayer);

                // Pick the input/output pair for the current player
                PrintWriter currentOut = (currentPlayer == 'X') ? outX : outO;
                BufferedReader currentIn = (currentPlayer == 'X') ? inX : inO;

                int row, col;

                // ---- STEP 6: GET A VALID MOVE ----
                while (true) {
                    // Prompt the current player to make a move
                    currentOut.println("YOUR_MOVE");

                    // Read a line from the client (expected format: "MOVE r c")
                    String line = currentIn.readLine();
                    if (line == null) {
                        // If the client disconnects, terminate the game
                        System.out.println("Player disconnected, ending game.");
                        return;
                    }

                    // Parse and validate move command
                    String[] parts = line.split(" ");
                    if (parts.length == 3 && parts[0].equals("MOVE")) {
                        row = Integer.parseInt(parts[1]);
                        col = Integer.parseInt(parts[2]);

                        // Check if the move is valid (inside board and empty)
                        if (isValidMove(board, row, col)) {
                            board[row][col] = currentPlayer; // place X or O
                            currentOut.println("OK"); // acknowledge
                            break; // move accepted
                        } else {
                            currentOut.println("INVALID"); // tell client to retry
                        }
                    } else {
                        currentOut.println("INVALID"); // bad command format
                    }
                }

                // ---- STEP 7: CHECK GAME OUTCOME ----
                if (hasWon(board, currentPlayer)) {
                    // Send final board and result to both players
                    sendBoard(outX, board);
                    sendBoard(outO, board);
                    outX.println("WIN " + currentPlayer);
                    outO.println("WIN " + currentPlayer);
                    gameOver = true;
                } else if (isBoardFull(board)) {
                    // Board filled with no winner → draw
                    sendBoard(outX, board);
                    sendBoard(outO, board);
                    outX.println("DRAW");
                    outO.println("DRAW");
                    gameOver = true;
                } else {
                    // Switch turn to the other player
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            }

            // ---- STEP 8: CLOSE CONNECTIONS ----
            playerXSocket.close();
            playerOSocket.close();

        } catch (IOException e) {
            e.printStackTrace(); // print any socket or I/O errors
        }
    }

    // Helper method: send the board to a client in text form
    private static void sendBoard(PrintWriter out, char[][] board) {
        out.println("BOARD");
        for (int i = 0; i < 3; i++) {
            out.println(board[i][0] + " " + board[i][1] + " " + board[i][2]);
        }
    }

    // Helper method: checks whether a move is within range and cell is empty
    private static boolean isValidMove(char[][] board, int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) return false;
        return board[row][col] == '.';
    }

    // Helper method: determines if a player has won
    private static boolean hasWon(char[][] b, char p) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (b[i][0] == p && b[i][1] == p && b[i][2] == p) return true;
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (b[0][j] == p && b[1][j] == p && b[2][j] == p) return true;
        }
        // Check diagonals
        if (b[0][0] == p && b[1][1] == p && b[2][2] == p) return true;
        if (b[0][2] == p && b[1][1] == p && b[2][0] == p) return true;
        return false;
    }

    // Helper method: checks if there are any empty cells left
    private static boolean isBoardFull(char[][] b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b[i][j] == '.') return false;
            }
        }
        return true;
    }
}
