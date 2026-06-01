import java.util.List;
import java.util.Scanner;

enum Symbol { X, O };

class Player {
    private String name;
    private Symbol symbol;

    public Player (String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}

class Cell {
    private Symbol symbol;

    public Symbol getSymbol() {
        return this.symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public boolean isEmpty() {
        return symbol == null;
    }
}

class Board {
    private Cell[][] board;
    private int movesCount;
    private int size;

    public Board(int size) {
        this.size = size;
        board = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public Symbol getSymbol(int row, int col) {
        return board[row][col].getSymbol();
    }

    public int getSize() {
        return this.size;
    }

    public boolean placeSymbol (int row, int col, Symbol symbol) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            return false;
        }

        if (!board[row][col].isEmpty()) {
            return false;
        }

        board[row][col].setSymbol(symbol);
        movesCount++;
        return true;
    }

    public boolean isDraw() {
        return movesCount == size * size;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Symbol symbol = board[i][j].getSymbol();
                System.out.print(symbol == null ? "-" : symbol);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

interface WinningStrategy {
    boolean checkWinner(Board board, int row, int col, Symbol symbol);
}

class RowWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, int row, int col, Symbol symbol) {
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            if (board.getSymbol(row, i) != symbol) {
                return false;
            }
        }
        return true;
    }

}

class ColumnWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, int row, int col, Symbol symbol) {
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            if (board.getSymbol(i, col) != symbol) {
                return false;
            }
        }
        return true;
    }

}

class DiagonalWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, int row, int col, Symbol symbol) {
        int size = board.getSize();

        if (row == col) {
            boolean match = true;

            for (int i = 0; i < size; i++) {
                if (board.getSymbol(i, i) != symbol) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return true;
            }
        }

        if (row + col == size - 1) {
            boolean match = true;

            for (int i = 0; i < size; i++) {
                if (board.getSymbol(i, size - i - 1) != symbol) {
                    match = false;
                    break;
                }
            }

            return match;
        }

        return false;
    }

}

class Game {
    private List<Player> players;
    private Board board;
    private int turn;
    private List<WinningStrategy> winningStrategies;

    public Game(List<Player> players, int size) {
        this.players = players;
        this.board = new Board(size);
        this.turn = 0;

        this.winningStrategies = List.of(
            new RowWinningStrategy(),
            new ColumnWinningStrategy(),
            new DiagonalWinningStrategy()
        );
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            Player player = players.get(turn);
            board.printBoard();
            System.out.println(player.getName() + " enter row and col:");
            int row = sc.nextInt();
            int col = sc.nextInt();

            boolean placed = board.placeSymbol(row, col, player.getSymbol());
            if (!placed) {
                System.out.println("Invalid move");
                continue;
            }

            boolean hasWinner = false;

            for(WinningStrategy strategy: winningStrategies) {
                if(strategy.checkWinner(board, row, col, player.getSymbol())) {
                    hasWinner = true;
                    break;
                }
            }

            if(hasWinner) {
                board.printBoard();
                System.out.println(player.getName()+ " won the game!");
                break;
            }

            if (board.isDraw()) {
                board.printBoard();
                System.out.println("Match Draw");
                break;
            }

            turn = 1 - turn;
        }
    }
}


public class TicTacToe2 {
    public static void main(String[] args) {
        Player p1 = new Player("Vish", Symbol.X);
        Player p2 = new Player("Rahul", Symbol.O);

        Game game = new Game(List.of(p1, p2),3);
        game.start();
    }
}
