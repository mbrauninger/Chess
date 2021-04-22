import java.util.Arrays;

class ChessBoard implements Cloneable {
    Piece grid[][];
    int numWhitePieces = 16;
    int numBlackPieces = 16;
    boolean gameOver = false;
    String currentPlayer = "White";
    //int squaresOccupied[][] = new int[32][2];

    ChessBoard(Piece[][] grid) {
        this.grid = grid;
    }

    // Used to create independent copies of the current game state
    ChessBoard cloneBoard() {
        Piece[][] clonedGrid = new Piece[8][8];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                if (this.grid[i][j] != null) {
                    clonedGrid[i][j] = new Piece(this.grid[i][j].color, this.grid[i][j].rank);
                }
            }
        }
        ChessBoard clonedBoard = new ChessBoard(clonedGrid);
        return clonedBoard;
    }

    // Removes a piece from a unit on the board, positioned at i, j
    void deletePiece(int i, int j) {
        this.grid[i][j] = null;
    }

    // Arguments received from Gameplay class
    // Used in Gameplay class to facilitate the execution of a player's decisions
    // Moves piece from current position to intended position, i is rows, j is columns
    // Capable of throwing exceptions if user makes an illegal move, to be received in Gameplay.playGame()
    // Function will only be called if input is 5 characters and formatted correctly and within the board's dimensions (a1 to h8)
    void movePiece(int iInitial, int jInitial, int iFinal, int jFinal) {
        // Checks index on board to be moved
        Piece tempPiece = this.grid[iInitial][jInitial];

        // If index is empty, throw an exception
        if (tempPiece == null) {
            throw new NullPointerException("Selected square contained a null value.");
        }

        // If wrong color is chosen based on whose turn it is, throw an exception
        if ((tempPiece.color == "Black" && this.currentPlayer == "White") || tempPiece.color == "White" && this.currentPlayer == "Black") {
            throw new ArithmeticException("Incorrect color piece chosen.");
        }

        // Valid moves are generated for the piece at the chosen index
        // If final destination index matches one of these valid moves, flag is set to true
        // Otherwise, an exception is thrown
        int[][] validMoves = this.getValidMovesForPiece(iInitial, jInitial);
        boolean moveIsValid = false;
        int[] intendedDestination = new int[] {iFinal, jFinal};
        for (int i = 0; i < validMoves.length; i++) {
            if (Arrays.equals(validMoves[i], intendedDestination)) {
                moveIsValid = true;
                break;
            }
        }
        if (moveIsValid == false) {
            throw new ArithmeticException("Destination is invalid.");
        }

        // If move is determined to be valid from the above code, move the piece
        // De-increments the piece count if a color's piece is taken
        // Switches the current player after move is made, and always sets the piece that was moved to firstMove = false
        this.deletePiece(iInitial, jInitial);
        if (this.grid[iFinal][jFinal] != null) {
            if (this.grid[iFinal][jFinal].color == "White") {
                this.numWhitePieces -= 1;
            }
            else {
                this.numBlackPieces -= 1;
            }
        }
        this.grid[iFinal][jFinal] = tempPiece;
        this.currentPlayer = this.currentPlayer == "White" ? "Black" : "White";
        tempPiece.firstMove = false;
    }

    int[][] getValidPawnMoves(int i, int j) {
        int[][] validMoves = new int[0][2];
        Piece tempPiece = this.grid[i][j];
        int[] tempArray;
        if (tempPiece.color == "White") {
            if (i < this.grid.length - 1) { // so program doesn't crash if pawn is at the other end of the board
                // Case where pawn moves forward 2 on first move
                if (tempPiece.firstMove && this.grid[i+1][j] == null && this.grid[i+2][j] == null) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+2, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                // Case where pawn moves forward 1
                if (this.grid[i+1][j] == null) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                // Case where pawn captures piece to the left
                if (j > 0) {
                    if (this.grid[i+1][j-1] != null) {
                        if (this.grid[i+1][j-1].color == "Black") {
                            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                            tempArray = new int[] {i+1, j-1};
                            validMoves[validMoves.length - 1] = tempArray;
                        }
                    }
                }
                // Case where pawn captures piece to the right
                if (j < this.grid.length - 1) {
                    if (this.grid[i+1][j+1] != null) {
                        if (this.grid[i+1][j+1].color == "Black") {
                            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                            tempArray = new int[] {i+1, j+1};
                            validMoves[validMoves.length - 1] = tempArray;
                        }
                    }
                }
            }
        }
        else {
            if (i > 0) { // so program doesn't crash if pawn is at the other end of the board
                // Case where pawn moves forward 2 on first move
                if (tempPiece.firstMove && this.grid[i-1][j] == null && this.grid[i-2][j] == null) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-2, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                // Case where pawn moves forward 1
                if (this.grid[i-1][j] == null) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                // Case where pawn captures piece to the left
                if (j < this.grid.length - 1) {
                    if (this.grid[i-1][j+1] != null) {
                        if (this.grid[i-1][j+1].color == "White") {
                            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                            tempArray = new int[] {i-1, j+1};
                            validMoves[validMoves.length - 1] = tempArray;
                        }
                    }
                }
                // Case where pawn captures piece to the right
                if (j > 0) {
                    if (this.grid[i-1][j-1] != null) {
                        if (this.grid[i-1][j-1].color == "White") {
                            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                            tempArray = new int[] {i-1, j-1};
                            validMoves[validMoves.length - 1] = tempArray;
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    int[][] getValidKnightMoves(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        Piece tempPiece = this.grid[i][j];

        // From prespective of white knight: piece moves up 2 and to the left 1
        if (i < this.grid.length - 2 && j > 0) {
            if (this.grid[i+2][j-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+2, j-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+2][j-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+2, j-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves up 2 and to the right 1
        if (i < this.grid.length - 2 && j < this.grid.length - 1) {
            if (this.grid[i+2][j+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+2, j+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+2][j+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+2, j+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves down 2 and to the left 1
        if (i > 1 && j > 0) {
            if (this.grid[i-2][j-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-2, j-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-2][j-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-2, j-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves down 2 and to the right 1
        if (i > 1 && j < this.grid.length - 1) {
            if (this.grid[i-2][j+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-2, j+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-2][j+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-2, j+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves up 1 and to the left 2
        if (i < this.grid.length - 1 && j > 1) {
            if (this.grid[i+1][j-2] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+1, j-2};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+1][j-2].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j-2};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves up 1 and to the right 2
        if (i < this.grid.length - 1 && j < this.grid.length - 2) {
            if (this.grid[i+1][j+2] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+1, j+2};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+1][j+2].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j+2};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves down 1 and to the left 2
        if (i > 0 && j > 1) {
            if (this.grid[i-1][j-2] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-1, j-2};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-1][j-2].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j-2};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From prespective of white knight: piece moves down 1 and to the right 2
        if (i > 0 && j < this.grid.length - 2) {
            if (this.grid[i-1][j+2] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-1, j+2};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-1][j+2].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j+2};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        return validMoves;
    }
    int[][] getValidDiagonals(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        Piece tempPiece = this.grid[i][j];
        int iTemp;
        int jTemp;

        // From perspective of white bishop: piece moves up and to the left
        iTemp = i;
        jTemp = j;
        while (iTemp < this.grid.length - 1 && jTemp > 0) {
            if (this.grid[iTemp+1][jTemp-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp+1, jTemp-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp+1][jTemp-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp+1, jTemp-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp += 1;
            jTemp -= 1;
        }
        // From perspective of white bishop: piece moves up and to the right
        iTemp = i;
        jTemp = j;
        while (iTemp < this.grid.length - 1 && jTemp < this.grid.length - 1) {
            if (this.grid[iTemp+1][jTemp+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp+1, jTemp+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp+1][jTemp+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp+1, jTemp+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp += 1;
            jTemp += 1;
        }
        // From perspective of white bishop: piece moves down and to the left
        iTemp = i;
        jTemp = j;
        while (iTemp > 0 && jTemp > 0) {
            if (this.grid[iTemp-1][jTemp-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp-1, jTemp-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp-1][jTemp-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp-1, jTemp-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp -= 1;
            jTemp -= 1;
        }
        // From perspective of white bishop: piece moves down and to the right
        iTemp = i;
        jTemp = j;
        while (iTemp > 0 && jTemp < this.grid.length - 1) {
            if (this.grid[iTemp-1][jTemp+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp-1, jTemp+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp-1][jTemp+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp-1, jTemp+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp -= 1;
            jTemp += 1;
        }
        return validMoves;
    }

    int[][] getValidRookMoves(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        Piece tempPiece = this.grid[i][j];
        int iTemp;
        int jTemp;

        // From perspective of white rook: piece moves up
        iTemp = i;
        while (iTemp < this.grid.length - 1) {
            if (this.grid[iTemp+1][j] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp+1, j};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp+1][j].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp+1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp += 1;
        }
        // From perspective of white rook: piece moves down
        iTemp = i;
        while (iTemp > 0) {
            if (this.grid[iTemp-1][j] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {iTemp-1, j};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[iTemp-1][j].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {iTemp-1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            iTemp -= 1;
        }
        // From perspective of white rook: piece moves left
        jTemp = j;
        while (jTemp > 0) {
            if (this.grid[i][jTemp-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i, jTemp-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i][jTemp-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i, jTemp-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            jTemp -= 1;
        }
        // From perspective of white rook: piece moves right
        jTemp = j;
        while (jTemp < this.grid.length - 1) {
            if (this.grid[i][jTemp+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i, jTemp+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i][jTemp+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i, jTemp+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
                break;
            }
            jTemp += 1;
        }
        return validMoves;
    }

    int[][] getValidQueenMoves(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        int[][] bishopMoves = this.getValidDiagonals(i, j);
        int[][] rookMoves = this.getValidRookMoves(i, j);

        for (int k = 0; k < bishopMoves.length; k++) {
            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
            tempArray = new int[] {bishopMoves[k][0], bishopMoves[k][1]};
            validMoves[validMoves.length - 1] = tempArray;
        }
        for (int k = 0; k < rookMoves.length; k++) {
            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
            tempArray = new int[] {rookMoves[k][0], rookMoves[k][1]};
            validMoves[validMoves.length - 1] = tempArray;
        }
        return validMoves;
    }

    int[][] getValidKingMoves(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        Piece tempPiece = this.grid[i][j];

        // From perspective of white king: piece to the right
        if (j < this.grid.length - 1) {
            if (this.grid[i][j+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i, j+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i][j+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i, j+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }

        // From perspective of white king: piece to the left
        if (j > 0) {
            if (this.grid[i][j-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i, j-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i][j-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i, j-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece above
        if (i < this.grid.length - 1) {
            if (this.grid[i+1][j] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+1, j};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+1][j].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece below
        if (i > 0) {
            if (this.grid[i-1][j] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-1, j};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-1][j].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece above and to the right
        if (i < this.grid.length - 1 && j < this.grid.length - 1) {
            if (this.grid[i+1][j+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+1, j+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+1][j+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece above and to the left
        if (i < this.grid.length - 1 && j >  0) {
            if (this.grid[i+1][j-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i+1, j-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i+1][j-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i+1, j-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece below and to the right
        if (i > 0 && j < this.grid.length - 1) {
            if (this.grid[i-1][j+1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-1, j+1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-1][j+1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j+1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        // From perspective of white king: piece below and to the left
        if (i > 0 && j >  0) {
            if (this.grid[i-1][j-1] == null) {
                validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                tempArray = new int[] {i-1, j-1};
                validMoves[validMoves.length - 1] = tempArray;
            }
            else {
                if (this.grid[i-1][j-1].color != tempPiece.color) {
                    validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
                    tempArray = new int[] {i-1, j-1};
                    validMoves[validMoves.length - 1] = tempArray;
                }
            }
        }
        return validMoves;
    }

    // Arguments received from movePiece()
    int[][] getValidMovesForPiece(int i, int j) {
        int[][] validMoves = new int[0][2];
        int[] tempArray;
        int[][] moves = null;
        Piece tempPiece = this.grid[i][j];
        if (tempPiece == null) {
            throw new NullPointerException("Selected square contained a null value.");
        }
        if (tempPiece.rank == "Pawn") {
            moves = this.getValidPawnMoves(i, j);
        }
        if (tempPiece.rank == "Knight") {
            moves = this.getValidKnightMoves(i, j);
        }
        if (tempPiece.rank == "Bishop") {
            moves = this.getValidDiagonals(i, j);
        }
        if (tempPiece.rank == "Rook") {
            moves = this.getValidRookMoves(i, j);
        }
        if (tempPiece.rank == "Queen") {
            moves = this.getValidQueenMoves(i, j);
        }
        if (tempPiece.rank == "King") {
            moves = this.getValidKingMoves(i, j);
        }
        for (int k = 0; k < moves.length; k++) {
            validMoves = Arrays.copyOf(validMoves, validMoves.length + 1);
            tempArray = new int[] {moves[k][0], moves[k][1]};
            validMoves[validMoves.length - 1] = tempArray;
        }
        return validMoves;
    }
    /*
    Map<int[],int[][]> successors(String currentColor) {
        ChessBoard tempBoard = this.cloneBoard();
    }
    */
    void prettyPrint(String color) {
        System.out.println();
        for (int j = 0; j < this.grid.length; j++) {
            if (j == 0) {
                System.out.print("  ");
            }
            System.out.print("________");
        }
        for (int i = color == "White" ? this.grid.length - 1 : 0; color == "White" ? i >= 0 : i < this.grid.length; i += color == "White" ? -1 : 1) {
            System.out.println();
            for (int k = 0; k < 3; k++) {
                for (int j = color == "White" ? 0 : this.grid.length - 1; color == "White" ? j < this.grid.length : j >= 0; j+= color == "White" ? 1 : -1) {
                    if (color == "White" ? j == 0 : j == this.grid.length - 1) {
                        if (k == 1) {
                            System.out.print(i + 1 + " ");
                        }
                        else {
                            System.out.print("  ");
                        }
                    }
                    System.out.print("|   ");
                    
                    if (this.grid[i][j] != null && k == 1) {
                        char printChar;
                        if (this.grid[i][j].color == "White") {
                            printChar = this.grid[i][j].rank.charAt(0);
                        }
                        else {
                            printChar = Character.toLowerCase(this.grid[i][j].rank.charAt(0));
                        }
                        System.out.print(printChar + "   ");
                    }
                    else {
                        System.out.print("    ");
                    }
                    
                }
                System.out.print("|   ");
                System.out.println();
            }
            for (int j = 0; j < this.grid.length; j++) {
                if (j == 0) {
                    System.out.print("  ");
                }
                System.out.print("--------");
            }
        }
        System.out.println();
        if (color == "White") {
            System.out.println("     a       b       c       d       e       f       g       h");
        }
        else {
            System.out.println("     h       g       f       e       d       c       b       a");
        }
        System.out.println();
    }
}