import java.util.Scanner;  // Import the Scanner class
import java.util.Map;
import java.util.HashMap;

class GamePlay {
    // Initialization
    ChessBoard gameBoard;
    Agent p1 = new Agent("White", "Human");
    Agent p2 = new Agent("Black", "Human");
    Agent currentPlayer = p1;
    Map<String, Integer> letterMap = new HashMap<String, Integer>();
    Map<Integer, String> numberMap = new HashMap<Integer, String>();
    String validLetters = "abcdefgh";
    boolean printBoard = true;

    GamePlay(ChessBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    void initialize() {
        this.letterMap.put("a", 0); this.letterMap.put("b", 1); this.letterMap.put("c", 2); this.letterMap.put("d", 3);
        this.letterMap.put("e", 4); this.letterMap.put("f", 5); this.letterMap.put("g", 6); this.letterMap.put("h", 7);
        this.numberMap.put(0, "a"); this.numberMap.put(1, "b"); this.numberMap.put(2, "c"); this.numberMap.put(3, "d");
        this.numberMap.put(4, "e"); this.numberMap.put(5, "f"); this.numberMap.put(6, "g"); this.numberMap.put(7, "h");
        
    }
    void playGame() {
        this.initialize();
        while (this.gameBoard.gameOver == false) {
            this.gameBoard.prettyPrint(this.currentPlayer.color);
            Scanner reader = new Scanner(System.in); 
            System.out.print("Please select your move (Example: \"c3 c4\" moves a piece from c3 to c4): ");
            String input = reader.nextLine();
            while (true) {
                if (input.length() != 5) {
                    System.out.println("Please format input correctly.");
                    System.out.print("Please select your move (Example: \"c3 c4\" moves a piece from c3 to c4)): ");
                    input = reader.nextLine();
                    continue;
                }
                String beginningLetter = Character.toString(input.charAt(0));
                String endingLetter = Character.toString(input.charAt(3));
                int beginningNumber = Integer.parseInt(Character.toString(input.charAt(1)));
                int endingNumber = Integer.parseInt(Character.toString(input.charAt(4)));
                if (validLetters.contains(beginningLetter) == false || validLetters.contains(endingLetter) == false || beginningNumber >= 9 || endingNumber >= 9) {
                    System.out.println("Please format input correctly.");
                    System.out.print("Please select your move (Example: \"c3 c4\" moves a piece from c3 to c4)): ");
                    input = reader.nextLine();
                    continue;
                }
                else {
                    try {
                        this.gameBoard.movePiece(beginningNumber - 1, this.letterMap.get(beginningLetter), endingNumber - 1, this.letterMap.get(endingLetter));
                        currentPlayer = currentPlayer == p1 ? p2 : p1;
                        this.printBoard = true;
                    }
                    catch (NullPointerException e) {
                        System.out.println("Invalid move. Please re-enter.");
                        this.printBoard = false;
                    }
                    catch (ArithmeticException e) {
                        System.out.println("Invalid move. Please re-enter.");
                        this.printBoard = false;
                    }
                    break;
                }
            }
        }
    }
}