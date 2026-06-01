
/*

0. Understand the question and make yourself familair with problem..
    TicTacToe
    --> 3*3 
    Symbols: X,O
    Players

1. Requirements:
    1. Functional Requirments:
        - should be able to take turns and put their symbol on the board
        - Board containing 3x3
        - turn alternation should be there
        - if there is a match [diagonal, row, col] then declare the winner
        - if there is a draw, if number of pieces are 9 but no winner
    
    2. Non-Functional:
        - skipped
    
2. Indentify the core entities
    - enum Symbol:
        X, O
    - Board
        board[][]
        movesCount
        winner
        size
        - hasWinner(i,j,Symbol)
            --> 
        - isDraw()
        - placeSymbol(i,j)
            -- checkIfValid()

        - 
    - Game
        --> orchastrator class running the game and turns
        List<Player>
        Board
        start()
            --> turn = 1 - turn; // 
            Player player = players.get(turn);

            int valid = placeSymbol(i,j)
            if(valid != -1)
                continue;

            if(hasWinner())
                break;
            
    - Player
        --> name
        --> Symbol
    

3. Write Code


4. Discuss extensibility..
    --> thread saf





*/

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
        return symbol;
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
    private int size;
    private int movesCount;

    public Board(int size) {
        this.size = size;
        this.board = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public boolean placeSymbol(int row, int col, Symbol symbol) {
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

    public boolean hasWinner(int row, int col, Symbol symbol) {

        boolean rowMatch = true;
        for (int i = 0; i < size; i++) {
            if (board[row][i].getSymbol() != symbol) {
                rowMatch = false;
                break;
            }
        }

        boolean colMatch = true;
        for (int i = 0; i < size; i++) {
            if (board[i][col].getSymbol() != symbol) {
                colMatch = false;
                break;
            }
        }

        boolean diagMatch = false;
        if (row == col) {
            diagMatch = true;
            for (int i = 0; i < size; i++) {
                if (board[i][i].getSymbol() != symbol) {
                    diagMatch = false;
                    break;
                }
            }
        }
        
        boolean antiDiagMatch = false;
        if (row + col == size - 1) {
            antiDiagMatch = true;
            for (int i = 0; i < size; i++) {
                if (board[i][size - i - 1].getSymbol() != symbol) {
                    antiDiagMatch = false;
                    break;
                }
            }
        }

        return rowMatch || colMatch || diagMatch || antiDiagMatch;
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

class Game {
    private List<Player> players;
    private Board board;
    private int turn;

    public Game (List<Player> players, int size) {
        this.players = players;
        this.board = new Board(size);
        this.turn = 0;
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

            if (board.hasWinner(row, col, player.getSymbol())) {
                board.printBoard();
                System.out.println(player.getName() + " won the game!");
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

public class TicTacToe {
    public static void main(String[] args) {
        Player p1 = new Player("Vish", Symbol.X);
        Player p2 = new Player("Rahul", Symbol.O);

        Game game = new Game(List.of(p1, p2),3);
        game.start();
    }
}
