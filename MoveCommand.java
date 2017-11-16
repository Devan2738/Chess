
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

	/**
	 * This method checks the validity of a move by looking for failing cases.
	 * 
	 * @param playerMove
	 * @param board
	 * @param playerColor
	 * @return true/false
	 */
	public boolean hasValidSyntax(String playerMove, ChessBoard board, String playerColor) {
		boolean retVal = true;
		playerMove = playerMove.toLowerCase();
		int subValue = 0;
		if (playerColor.equals("white"))
			subValue = 66;
		else if (playerColor.equals("black"))
			subValue = 98;
		else {
			System.out.println("error determining player color\n");
			System.exit(0); }
		if (playerMove.length() != 5) {
			System.out.println("invalid move command length\n");
			return false;}
		else if (playerMove.charAt(0) < 97 || playerMove.charAt(0) > 104) {
			System.out.println("invalid start column selected\n");
			return false; }
		else if (playerMove.charAt(3) < 97 || playerMove.charAt(3) > 104) {
			System.out.println("invalid end column selected\n");
			return false; }
		else if (playerMove.charAt(1) < 49 || playerMove.charAt(1) > 56) {
			System.out.println("invalid start row selected\n");
			return false; }
		else if (playerMove.charAt(4) < 49 || playerMove.charAt(4) > 56) {
			System.out.println("invalid end row selected\n");
			return false; }
		startRow = 8 - Integer.parseInt(playerMove.substring(1, 2));
		startColumn = playerMove.charAt(0) - 97;
		if (board.get(startRow, startColumn).getChar() == '-') {
			System.out.println("empty tile selected\n");
			return false; }
		endRow = 8 - Integer.parseInt(playerMove.substring(4, 5));
		endColumn = playerMove.charAt(3) - 97;
		if ((startColumn == endColumn) && (startRow == endRow)) {
			System.out.println("starting and ending position are equal\n");
			return false; }
		if (playerColor.equals("white"))
		{ 
			if (board.get(startRow, startColumn).getChar() > 82) {
				System.out.println("white player selected black piece\n");
				return false; }
		}
		else if (playerColor.equals("black"))
		{
			if (board.get(startRow, startColumn).getChar() < 98) {
				System.out.println("black player selected white piece\n");
				return false; }
		}
		else {
			System.out.println("error determining player color\n");
			System.exit(0); }
		if (playerColor.equals("white"))
		{
			if (board.get(endRow, endColumn).getChar() > 65 && board.get(endRow, endColumn).getChar() < 83) {
				System.out.println("white player has selected end tile containing white piece\n");
				return false; }
		}
		else if (playerColor.equals("black"))
		{
			if (board.get(endRow, endColumn).getChar() > 97 && board.get(endRow, endColumn).getChar() < 117) {
				System.out.println("black player has selected end tile containing black piece\n");
				return false; }
		}
		return retVal;
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

