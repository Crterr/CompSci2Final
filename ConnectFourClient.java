import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConnectFourClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("SERVER_IP_HERE", 6789);
             Scanner in = new Scanner(socket.getInputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner kb = new Scanner(System.in)) {

            ConnectFour game = new ConnectFour();
            int player = 2;
            System.out.println("Connected to server! You are Player 2 (O).");

            while (true) {
                System.out.println("Waiting for Player 1's move...");
                int serverMove = Integer.parseInt(in.nextLine());
                game.makeMove(1, serverMove);
                game.printBoard();

                System.out.println("Your move, Player 2 (1-8):");
                int move = -1;
                boolean valid = false;

                while (!valid) {
                    if (kb.hasNextInt()) {
                        move = kb.nextInt() - 1;
                        kb.nextLine(); // Clear buffer
                        try {
                            game.makeMove(2, move);
                            valid = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Enter a number between 1-8.");
                        kb.next();
                    }
                }

                out.println(move); // send move to server

                int state = game.checkGameOver();
                if (state != 0) break;
            }

            game.printBoard();
            int result = game.checkGameOver();
            if (result == -1) System.out.println("Tie game!");
            else System.out.println("Player " + player + " wins!");

        } catch (IOException e) {
            System.err.println("Could not connect to server.");
        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
}