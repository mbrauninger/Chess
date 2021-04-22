class Main {
    public static void main(String[] args) {
        int[][] test1 = new int[][]
        {
        {0, 3, 0, -5, 6, 0, 2, 0},
        {0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, -4, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 4, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, -4, 0, 0, 0, 0, 0},
        {0, -3, -2, -5, 0, 0, -2, 0}
        };

        int[][] defaultBoard = new int[][]
        {
        {2, 3, 4, 5, 6, 4, 3, 2},
        {1, 1, 1, 1, 1, 1, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {-1, -1, -1, -1, -1, -1, -1, -1},
        {-2, -3, -4, -5, -6, -4, -3, -2}
        };
        ChessBoard test = generateCustomBoard(test1);
        GamePlay host = new GamePlay(test);

        host.playGame();
    }
    static ChessBoard generateCustomBoard(int[][] inputArray) {
        Piece[][] testBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (inputArray[i][j] == 1) {
                    testBoard[i][j] = new Piece("White", "Pawn");
                }
                if (inputArray[i][j] == -1) {
                    testBoard[i][j] = new Piece("Black", "Pawn");
                }
                if (inputArray[i][j] == 2) {
                    testBoard[i][j] = new Piece("White", "Rook");
                }
                if (inputArray[i][j] == -2) {
                    testBoard[i][j] = new Piece("Black", "Rook");
                }
                if (inputArray[i][j] == 3) {
                    testBoard[i][j] = new Piece("White", "Knight");
                }
                if (inputArray[i][j] == -3) {
                    testBoard[i][j] = new Piece("Black", "Knight");
                }
                if (inputArray[i][j] == 4) {
                    testBoard[i][j] = new Piece("White", "Bishop");
                }
                if (inputArray[i][j] == -4) {
                    testBoard[i][j] = new Piece("Black", "Bishop");
                }
                if (inputArray[i][j] == 5) {
                    testBoard[i][j] = new Piece("White", "Queen");
                }
                if (inputArray[i][j] == -5) {
                    testBoard[i][j] = new Piece("Black", "Queen");
                }
                if (inputArray[i][j] == 6) {
                    testBoard[i][j] = new Piece("White", "King");
                }
                if (inputArray[i][j] == -6) {
                    testBoard[i][j] = new Piece("Black", "King");
                }
            }
        }
        ChessBoard output = new ChessBoard(testBoard);
        return output;
    }
}