import java.util.Scanner;
import sensehat.api.SenseHat;
import sensehat.api.led.LEDMatrix;
import sensehat.api.led.Color;

public class ConnectFour {
    private char[][] board;

    public ConnectFour() {
        board = new char[8][8];
        prepareBoard();
    }

    public void prepareBoard() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = ' ';
            }
        }
    }

    public void printBoard() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                System.out.print("|" + board[r][c]);
            }
            System.out.println("|");
        }
        for (int i = 1; i <= 8; i++) {
            System.out.print(" " + i);
        }
        System.out.println("\n");
    }

    public void makeMove(int player, int column) throws InvalidMoveException, ColumnFullException {
        if (column < 0 || column >= 8) {
            throw new InvalidMoveException("Move must be between 1 and 8");
        }
        if (isColumnFull(column)) {
            throw new ColumnFullException("Column is full");
        }

        for (int r = 7; r >= 0; r--) {
            if (board[r][column] == ' ') {
                board[r][column] = (player == 1) ? 'X' : 'O';
                break;
            }
        }
        updateLEDMatrix();
    }

    public boolean isColumnFull(int column) {
        return board[0][column] != ' ';
    }

    public int checkGameOver() {
        if (checkWinner()) return 1;
        if (boardIsFull()) return -1;
        return 0;
    }

    private boolean checkWinner() {
        return horizontalWinner() || verticalWinner() || diagonalWinner();
    }

    private boolean horizontalWinner() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 5; c++) {
                char token = board[r][c];
                if (token != ' ' && token == board[r][c+1] && token == board[r][c+2] && token == board[r][c+3]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verticalWinner() {
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 5; r++) {
                char token = board[r][c];
                if (token != ' ' && token == board[r+1][c] && token == board[r+2][c] && token == board[r+3][c]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean diagonalWinner() {
        // Bottom-left to top-right
        for (int r = 3; r < 8; r++) {
            for (int c = 0; c < 5; c++) {
                char token = board[r][c];
                if (token != ' ' && token == board[r-1][c+1] && token == board[r-2][c+2] && token == board[r-3][c+3]) {
                    return true;
                }
            }
        }
        // Top-left to bottom-right
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                char token = board[r][c];
                if (token != ' ' && token == board[r+1][c+1] && token == board[r+2][c+2] && token == board[r+3][c+3]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean boardIsFull() {
        for (int c = 0; c < 8; c++) {
            if (board[0][c] == ' ') return false;
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void updateLEDMatrix() {
        SenseHat senseHat = new SenseHat();
        LEDMatrix leds = senseHat.getLedMatrix();

        Color[][] pixels = new Color[8][8];

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char token = board[r][c];
                if (token == 'X') {
                    pixels[r][c] = Color.RED;
                } else if (token == 'O') {
                    pixels[r][c] = Color.BLUE;
                } else {
                    pixels[r][c] = Color.BLACK;
                }
            }
        }

        leds.setPixels(pixels);
    }
} 
