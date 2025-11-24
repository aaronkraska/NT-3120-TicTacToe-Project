README – Networked Tic-Tac-Toe
Overview

This project demonstrates a simple client–server network game using plain TCP sockets in Java.
The server manages the game state and enforces rules; two clients connect and play interactively from separate hosts or VMs.

Files
File	Description
TicTacToeServer.java	Handles player connections, validates moves, manages board, and sends updates to both clients.
TicTacToeClient.java	Connects to the server, displays the board, and sends moves entered by the user.
How It Works

Protocol messages between server and clients:

From	Message	Meaning
Server → Client	ROLE X / ROLE O	Assigns player symbol.
Server → Client	BOARD + 3 lines	Sends current 3×3 grid.
Server → Client	TURN X	Indicates whose turn it is.
Server → Client	YOUR_MOVE	Prompts that player for a move.
Client → Server	MOVE r c	Sends chosen row and column.
Server → Client	OK / INVALID	Accepts or rejects the move.
Server → Client	WIN X / WIN O / DRAW	Game outcome.

The game continues until one player wins or all spaces are filled.

Running Locally (Single Machine)

Compile both files:

javac TicTacToeServer.java TicTacToeClient.java


Start the server:

java TicTacToeServer


Open two terminals and start two clients:

java TicTacToeClient
java TicTacToeClient

Running Across Two Machines or VMs

On the server machine, find its IP address:

ip addr    # Linux
ipconfig   # Windows


Start the server:

java TicTacToeServer


On each client machine, edit this line in TicTacToeClient.java:

String serverHost = "192.168.x.x";  // server’s IP


Recompile and run the client:

javac TicTacToeClient.java
java TicTacToeClient

Notes

The server must be started before clients connect.

Use the same PORT number (default 5000) everywhere.

If clients can’t connect, check firewall rules and ensure both are on the same network.

The program uses only Java’s standard library (no external dependencies).

Example Gameplay
Server: Waiting for Player X...
Server: Player X connected.
Server: Player O connected.
Player X terminal:
  ROLE X
  Current board:
  . . .
  . . .
  . . .
  TURN X
  YOUR_MOVE
  Enter row (0-2): 0
  Enter col (0-2): 0
  Move accepted.
