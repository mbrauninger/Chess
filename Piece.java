class Piece {

    String rank;
    String color;
    boolean firstMove = true;
    
    Piece(String color, String rank) {
        this.rank = rank;
        this.color = color;
    }
    @Override
    public String toString()
    {
        return this.color + this.rank;
    }
}