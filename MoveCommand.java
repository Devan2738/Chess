
/**
 * MoveCommand objects are used to contain information about where a ChessPiece object moves from
 * and where it moves to. Lists of moves are made up of many MoveCommand objects in a common data
 * structure. These lists are used to look for check mate states among other things.
 * 
 * @author Devan Karsann
 */
public class MoveCommand {

	private String move = ""; // format consists of five characters: <a-h><1-8><space><a-h><1-8>
    private int startRow; // refers to the start row of a move
    private int startColumn;
    private int endRow; // refers to the end row of a move
    private int endColumn;
    private int diffNumMovesBeforeAfterExec; // this variable stores the difference in the number of no potential loss moves before and after this move is executed
    private int avgDistToCenter;
    private int takePriorityLevel;
    
	/**
	 * This is a constructor for MoveCommand objects used by the AI. An initial location and an 
	 * end location are entered according to their row and column indices.
	 *  
	 * @param startRow
	 * @param startColumn
	 * @param endRow
	 * @param endColumn
	 */
    MoveCommand(int startRow, int startColumn, int endRow, int endColumn) {
    	move += (char)(startColumn + 97);
    	move += 8 - startRow; 
    	move += " ";	
    	move += (char)(endColumn + 97);
    	move += 8 - endRow;	
    	this.startRow = startRow;
    	this.startColumn = startColumn;
    	this.endRow = endRow;
    	this.endColumn = endColumn;
    }
    
    /**
     * This is an alternate constructor for MoveCommand objects. It is used to make MoveCommand
     * objects based on the input of the user. After MoveCommand objects are made with this constructor,
     * their are verified in the hasValidSyntax method below. 
     * @param playerMove
     */
    MoveCommand(String playerMove) {
    	move = playerMove;
    }

	/**
	 * This constructor is used to reset the values in a MoveCommand object.
	 */
    MoveCommand() {
    	;
    }

	public void setTakePriorityLevel(int input) {
		takePriorityLevel = input;
	}
	
	public int getTakePriorityLevel() {
		return takePriorityLevel;
	}
	
	/**
	 * sets the difference in number of no potential loss moves before and after this move is executed
	 * @param val
	 */
	public void setAvgDistToCenter(int val) { avgDistToCenter = val; }
	
	/**
	 * returns the difference in number of no potential loss moves before and after this move is executed
	 * @return diffNumMovesBeforeAfterexec
	 */
	public int getAvgDistToCenter() { return avgDistToCenter; }
	
	/**
	 * sets the difference in number of no potential loss moves before and after this move is executed
	 * @param val
	 */
	public void setDiffNumMovesBeforeAfterExec(int val) { diffNumMovesBeforeAfterExec = val; }
	
	/**
	 * returns the difference in number of no potential loss moves before and after this move is executed
	 * @return diffNumMovesBeforeAfterexec
	 */
	public int getDiffNumMovesBeforeAfterExec() { return diffNumMovesBeforeAfterExec; }
	
	/**
	 * returns startRow integer variable
	 * @return startRow
	 */
	public int getStartRow() { return startRow; }

	/**
	 * returns startColumn integer variable
	 * @return startColumn
	 */
	public int getStartColumn() { return startColumn; }

	/**
	 * returns endRow integer variable
	 * @return endRow
	 */
	public int getEndRow() { return endRow; }

	/**
	 * returns endColumn integer variable
	 * @return endColumn
	 */
	public int getEndColumn() {	return endColumn; }

	/**
	 * returns move string variable
	 * @return move
	 */
	public String toString() { return move; }
	
}

