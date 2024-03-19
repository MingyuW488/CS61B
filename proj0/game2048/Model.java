package game2048;

import javax.swing.*;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        if( side == Side.NORTH){
            changed = equalNorth();
        }
        else if(side == Side.EAST){
            board.setViewingPerspective(Side.EAST);
            changed = equalNorth();
            board.setViewingPerspective(Side.NORTH);
        }
        else if(side == Side.WEST){
            board.setViewingPerspective(Side.WEST);
            changed = equalNorth();
            board.setViewingPerspective(Side.NORTH);
        }
        else if(side == Side.SOUTH){
            board.setViewingPerspective(Side.SOUTH);
            changed = equalNorth();
            board.setViewingPerspective(Side.NORTH);
        }


        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }
    private boolean equalNorth(){
        boolean changed = false;

        for (int c = 0 ; c < board.size() ; c++ ){


            int mergedAlready = 0; // 查看当前行上方是否已经进行过合并操作，用来判断 2/2/4/0类情况
            int canMerge = 0; // move 至少需要两个tile,找到一个设置为1，找到两个若能合并则设置为
            int emptyTile = 0;//遍历行用来计null数量
            int foundTile = 0;//行遍历过程中找到一个不为空的tile时重新赋值为1
            for (int r = board.size() - 1 ; r >= 0 ; r--){

                Tile t = board.tile(c,r);
                if(board.tile(c,r) == null) //为空则空tile计数+1
                    emptyTile ++;
                //不为空则判断上方是否有非空tile
                else {
                    //foundTile == 0 说明上方全为null或者为row3,直接move（c,r + null数量 ，t )
                    if(foundTile == 0){
                        //若为row3,则不需要设定changed = true,board没有发生改变
                        if (r == 3){
                            //保持当前位置
                        }else{
                            //向上移到null处
                            board.move(c,r + emptyTile,t);
                            changed = true ;
                        }
                        //遇到非空没有合并操作canMove设定为1
                        canMerge = 1;
                    }
                    //上方有非空tile，此时两种情况，已经进行过合并操作，或者没有，如果进行过了那就要注意 2/2/4/2 这种情，若没有则进行值的比对,进行合并或者move操作
                    else {
                        //未合并操作
                        if(mergedAlready == 0) {
                            //对比值，相同则合并，并加分
                            if(t.value() == board.tile(c,r+emptyTile + 1).value()){
                                board.move(c,r + emptyTile +1,t);
                                score += 2 * t.value();
                                changed = true;
                                //进行合并操作，mergedAlready设定为1
                                mergedAlready = 1;
                                //因为进行合并操作，说明已经遍历过两个非空tile了，设定canMove为0， 用于 2/2/4/0 例子
                                canMerge = 0;
                            }
                            //若值不同,若上方有null则可以move，若没有则不需要
                            else{
                                //上方没有null，无需操作
                                if(emptyTile == 0) {
                                    //保持当前位置
                                }
                                else{
                                    board.move(c, r + emptyTile ,t );
                                    changed = true;
                                }
                                //因为没有合并操作，所以设定为1，若下一个遍历的能与当前tile 进行合并，则可以用于判断
                                canMerge = 1;
                            }

                        }
                        //上方已经进行合并操作，说明emptyTile 没有 +1 ，但是又多了一个null，所以进行move操作时row 要多 +1
                        else{
                            //说明已经进行过合并，当前tile是不能进行合并操作的，所以只能move到null处
                            if(canMerge == 0){
                                //因为合并产生null，row 多+1
                                board.move(c,r+emptyTile+1,t);
                                //重新设定为1
                                canMerge = 1;
                                changed = true;
                            }
                            // 能进行合并操作，所以进行值得对比，相同则合并，不同则move到null处
                            else{
                                if(t.value() == board.tile(c,r+emptyTile + 1 + 1 ).value()){
                                    board.move(c,r+emptyTile + 1 + 1,t);
                                    score += 2 * t.value();
                                    mergedAlready = 1;
                                    canMerge = 0;
                                    changed = true;
                                }
                            }
                        }

                    }

                    foundTile = 1 ;
                }
            }
        }
        return changed;
    }


    private boolean noMergeUp(int c, int r, Tile t ){
        board.move(c,r,t);
        return true;
    }


    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0 ; i < b.size() ; i++ ){
            for(int j = 0 ; j < b.size() ; j++){
                if( b.tile(i,j) == null )
                    return true;
            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0 ; i < b.size() ; i++){
            for(int j = 0 ;j < b.size() ; j++ ){

                if(b.tile(i,j) != null){
                    if(b.tile(i,j).value() == MAX_PIECE)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if(emptySpaceExists(b))
            return true;
        else {
            for ( int i = 0; i < b.size() ; i++ ){
                for( int j = 0 ; j < b.size() ; j++ ){
                    if(i + 1 <= 3 && b.tile(i,j).value() == b.tile(i+1,j).value())
                        return true;
                    else if( i-1 >= 0 && b.tile(i,j).value() == b.tile(i-1,j).value())
                        return true;
                    else if( j + 1 <= 3 && b.tile(i,j).value() == b.tile(i,j+1).value())
                        return true;
                    else if( j - 1 >= 0 && b.tile(i,j).value() == b.tile(i,j-1).value())
                        return true;
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
