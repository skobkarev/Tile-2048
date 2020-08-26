package task3513;

public class MoveEfficiency implements Comparable <MoveEfficiency> {
    //

    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }


    public Move getMove() {
        return move;
    }


    @Override
    public int compareTo(MoveEfficiency o) {

        int betterEmpty = Integer.compare(this.numberOfEmptyTiles, o.numberOfEmptyTiles);
        if(betterEmpty != 0) return betterEmpty;
        else return Integer.compare(this.score, o.score);
    }


}
