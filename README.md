# 🎮 Networked Tic-Tac-Toe (Java Client-Server Game)

A simple **network-based Tic-Tac-Toe** game implemented in Java using **TCP sockets**.  
The project demonstrates how a server and multiple clients can communicate over a network, maintain shared game state, and enforce rules in real time — using only Java’s standard library.

---

## 🚀 Features

- **Client–Server Architecture:** Two clients connect to a central server.  
- **Turn-Based Gameplay:** Server enforces turn order and validates moves.  
- **Real-Time Updates:** The board state is sent to both clients after every move.  
- **Text-Based Protocol:** Simple human-readable message exchange (`BOARD`, `MOVE`, `WIN`, etc.).  
- **Cross-Machine Play:** Clients can connect from different VMs or physical hosts.

---

## 🧩 File Overview

| File | Description |
|------|--------------|
| **`TicTacToeServer.java`** | Runs the game server. Waits for two players, assigns roles (X/O), manages the game board, and sends updates to clients. |
| **`TicTacToeClient.java`** | Connects to the server, displays the board, prompts user input, and sends moves. |

---

## 🖥️ Requirements

- **Java 17+** (any modern version works)
- Two or more terminals (or separate VMs)
- TCP connectivity between the client and server machines

---

## 🧠 How It Works

**Communication Protocol**

| Direction | Message | Description |
|------------|----------|-------------|
| Server → Client | `ROLE X` / `ROLE O` | Assigns a player symbol. |
| Server → Client | `BOARD` + 3 lines | Sends the 3×3 game board. |
| Server → Client | `TURN X` / `TURN O` | Indicates whose turn it is. |
| Server → Client | `YOUR_MOVE` | Prompts the player for input. |
| Client → Server | `MOVE r c` | Sends chosen row & column (0-2). |
| Server → Client | `OK` / `INVALID` | Confirms or rejects the move. |
| Server → Client | `WIN X` / `WIN O` / `DRAW` | Announces game result. |

The server is the **source of truth** for the board state.  
Clients simply render updates and send their moves.

---

## 🧱 Project Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/aaronkraska/NT-3120-TicTacToe-Project.git
cd NT-3120-TicTacToe-Project

2️⃣ Compile the Code
javac TicTacToeServer.java TicTacToeClient.java

3️⃣ Run the Server
java TicTacToeServer


You should see:

Server listening on port 5000
Waiting for Player X...

4️⃣ Run Two Clients

Open two more terminals (or two VMs):

java TicTacToeClient


The first client to connect becomes Player X.

The second becomes Player O.

🌐 Running Across Machines or VMs

On the server machine, find its IP:

ip addr   # Linux
ipconfig  # Windows


In the client code, change the line:

String serverHost = "";


to:

String serverHost = "192.168.x.x"; // Replace with server's IP


Recompile and run clients again:

javac TicTacToeClient.java
java TicTacToeClient


Make sure both machines are on the same network and that port 5000 is allowed through the firewall.

🧮 Example Gameplay
Server listening on port 5000
Waiting for Player X...
Player X connected.
Waiting for Player O...
Player O connected.

--- Client X ---
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

--- Client O ---
ROLE O
Current board:
X . .
. . .
. . .
TURN O
YOUR_MOVE
