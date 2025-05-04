import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConnectFourServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6789);
             Socket socket = serverSocket.accept();
             Scanner in = new Scanner(socket.getInputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner kb = new Scanner(System.in)) {

            ConnectFour game = new ConnectFour();
            int player = 1;
            System.out.println("Client connected! You are Player 1 (X).");

            while (true) {
                game.printBoard();
                System.out.println("Your move, Player 1 (1-8):");

                int move = -1;
                boolean valid = false;
                while (!valid) {
                    if (kb.hasNextInt()) {
                        move = kb.nextInt() - 1;
                        kb.nextLine(); // Clear buffer
                        try {
                            game.makeMove(1, move);
                            valid = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Enter a number between 1-8.");
                        kb.next();
                    }
                }

                out.println(move); // send move to client

                int state = game.checkGameOver();
                if (state != 0) break;

                System.out.println("Waiting for Player 2...");
                int clientMove = Integer.parseInt(in.nextLine());
                game.makeMove(2, clientMove);

                if (game.checkGameOver() != 0) break;
            }

            game.printBoard();
            int result = game.checkGameOver();
            if (result == -1) System.out.println("Tie game!");
            else System.out.println("Player " + player + " wins!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
}