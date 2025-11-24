// -------------------------------
// TicTacToeClient.java
// -------------------------------
// Connects to the TicTacToeServer and interacts using simple text messages.
// Reads server messages, shows board updates, and allows user to make moves.

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TicTacToeClient {

    public static void main(String[] args) {
        // Address of the server
        String serverHost = ""; // enter ip address of device running the server file
        int port = 5000; // must match the server's port

        // try-with-resources ensures all resources close automatically
        try (Socket socket = new Socket(serverHost, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server.");

            // Read messages from the server line by line
            String line;
            while ((line = in.readLine()) != null) {
                // ---- ROLE ASSIGNMENT ----
                if (line.startsWith("ROLE")) {
                    System.out.println("You are: " + line);
                }

                // ---- BOARD DISPLAY ----
                else if (line.equals("BOARD")) {
                    System.out.println("Current board:");
                    // The server will send exactly 3 lines for the board
                    for (int i = 0; i < 3; i++) {
                        String row = in.readLine();
                        System.out.println(row);
                    }
                }

                // ---- TURN INFO ----
                else if (line.startsWith("TURN")) {
                    System.out.println("Turn info: " + line);
                }

                // ---- YOUR TURN TO MOVE ----
                else if (line.equals("YOUR_MOVE")) {
                    // Ask user for input
                    System.out.print("Enter row (0-2): ");
                    int r = scanner.nextInt();
                    System.out.print("Enter col (0-2): ");
                    int c = scanner.nextInt();
                    scanner.nextLine(); // consume leftover newline
                    // Send move to server
                    out.println("MOVE " + r + " " + c);
                }

                // ---- FEEDBACK ----
                else if (line.equals("OK")) {
                    System.out.println("Move accepted.");
                } else if (line.equals("INVALID")) {
                    System.out.println("Invalid move, try again when prompted.");
                }

                // ---- GAME RESULTS ----
                else if (line.startsWith("WIN")) {
                    System.out.println(line);
                    break; // exit loop after game over
                } else if (line.equals("DRAW")) {
                    System.out.println("Game ended in a draw.");
                    break;
                }

                // ---- ANY OTHER SERVER MESSAGE ----
                else {
                    System.out.println("Server: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // show connection errors
        }
    }
}


