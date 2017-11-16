import java.util.ArrayList;

/**
 * This class implements methods from the ChessPiece interface.
 * Instantiations of this class are stored in ChessBoard objects.
 * 
 * @author Devan
 */
public class Queen implements ChessPiece{
	
	private String color;
	private char name;
	
	private int startRow;
	private int startColumn;
	
	private ArrayList<MoveCommand> goodMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> noLossMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> takeOpponentPieceMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> opponentKingInCheckMovesList = new ArrayList<MoveCommand>();
	private ArrayList<MoveCommand> opponentInCheckMateMovesList = new ArrayList<MoveCommand>();

	/**
	 * This is the constructor for Queen objects.
	 * @param color [of the queen]
	 * @param row (current row of queen)
	 * @param column (current column of queen)
	 */
	Queen (String color, int row, int column) {
		this.color = color; 
		startRow = row;
		startColumn = column;
		name = color.equals("white") ? 'Q' : 'q';
	}
	
	@Override
	public ChessPiece copyOf() {
		Queen copy = new Queen(color, startRow, startColumn);
		return copy;
	}
	
	@Override
	public void generateGoodMoves(ChessBoard board) {
		
		ArrayList<MoveCommand> maybeGoodMoves = new ArrayList<MoveCommand>();
		ArrayList<MoveCommand> goodMoves = new ArrayList<MoveCommand>();
		
		for (int i = startRow, j = startColumn; i > 0 && j > 0; i--, j--) {
			if (board.get(i-1, j-1).getChar() == '-')
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, j-1));
			else if (!(board.get(i-1, j-1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i > 0 && j < 7; i--, j++) {
			if (board.get(i-1, j+1).getChar() == '-')
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, j+1));
			else if (!(board.get(i-1, j+1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, j+1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j > 0; i++, j--) {
			if (board.get(i+1, j-1).getChar() == '-')
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, j-1));
			else if (!(board.get(i+1, j-1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j < 7; i++, j++) {
			if (board.get(i+1, j+1).getChar() == '-') 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, j+1));
			else if (!(board.get(i+1, j+1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, j+1));
				break;
			}
			else
				break;
		}		
		for (int i = startRow; i > 0; i--) {
			if (board.get(i-1, startColumn).getChar() == '-') 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, startColumn));
			else if (!(board.get(i-1, startColumn).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i-1, startColumn));
				break;
			}
			else
				break;
			}
		for (int i = startRow; i < 7; i++) {
			if (board.get(i+1, startColumn).getChar() == '-') 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, startColumn));
			else if (!(board.get(i+1, startColumn).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, i+1, startColumn));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j > 0; j--) {
			if (board.get(startRow, j-1).getChar() == '-') 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow, j-1));
			else if (!(board.get(startRow, j-1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow, j-1));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j < 7; j++) {
			if (board.get(startRow, j+1).getChar() == '-') 
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow, j+1));
			else if (!(board.get(startRow, j+1).getColor().equals(color))) {
				maybeGoodMoves.add(new MoveCommand(startRow, startColumn, startRow, j+1));
				break;
			}
			else
				break;
		}		
		
		int tempKingRow = 0;
		int tempKingColumn = 0;
		
		goodMovesList = new ArrayList<MoveCommand>();
		for (int i = 0; i < maybeGoodMoves.size(); i++) {
			MoveCommand tempMove = maybeGoodMoves.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			ChessBoard temp = board.copyOf();
			//generateNoLossMoves(temp);
			//int before = noLossMovesList.size();			
			//System.out.println(tempMove.toString() + "\nbefore value for queen: " + before + " (" + noLossMovesList.toString() + ")");
			if (temp.executeMove(startRow, startColumn, endRow, endColumn) != false) {
				//generateNoLossMoves(temp);
				//int after = noLossMovesList.size();
				//System.out.println("after value for queen: " + after + " (" + noLossMovesList.toString() + ")\n");
				//System.out.println(temp.toString());
				if (color.equals("white")) {
				   tempKingRow = board.getPlayerWhite().getKingRow();
				   tempKingColumn = board.getPlayerWhite().getKingColumn();
			    }
				else if (color.equals("black")) {
				   tempKingRow = board.getPlayerBlack().getKingRow();
				   tempKingColumn = board.getPlayerBlack().getKingColumn();
			   }
			   if (!(temp.isInCheckState(tempKingRow, tempKingColumn, color))) {
				   //maybeGoodMoves.get(i).setDiffNumMovesBeforeAfterExec(after - before);
				   int val = (int)(Math.abs(3.5 - endRow) + Math.abs(3.5 - endColumn) / 2.0);
				   maybeGoodMoves.get(i).setAvgDistToCenter(val);
				   goodMoves.add(maybeGoodMoves.get(i));
			   }
		    }
		}
		goodMovesList = goodMoves;
	}

	@Override
	public void generateNoLossMoves(ChessBoard board) {
		ArrayList<MoveCommand> maybeNoLossMoves = new ArrayList<MoveCommand>();
		ArrayList<ArrayList<Integer>> playerPieceLocations = new ArrayList<ArrayList<Integer>>();
		playerPieceLocations = board.getPlayerPieces(color);

		for (int i = startRow, j = startColumn; i > 0 && j > 0; i--, j--) {
			if (board.get(i-1, j-1).getChar() == '-')
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, j-1));
			else if (!(board.get(i-1, j-1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i > 0 && j < 7; i--, j++) {
			if (board.get(i-1, j+1).getChar() == '-')
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, j+1));
			else if (!(board.get(i-1, j+1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, j+1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j > 0; i++, j--) {
			if (board.get(i+1, j-1).getChar() == '-')
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, j-1));
			else if (!(board.get(i+1, j-1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j < 7; i++, j++) {
			if (board.get(i+1, j+1).getChar() == '-') 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, j+1));
			else if (!(board.get(i+1, j+1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, j+1));
				break;
			}
			else
				break;
		}		
		for (int i = startRow; i > 0; i--) {
			if (board.get(i-1, startColumn).getChar() == '-') 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, startColumn));
			else if (!(board.get(i-1, startColumn).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i-1, startColumn));
				break;
			}
			else
				break;
			}
		for (int i = startRow; i < 7; i++) {
			if (board.get(i+1, startColumn).getChar() == '-') 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, startColumn));
			else if (!(board.get(i+1, startColumn).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, i+1, startColumn));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j > 0; j--) {
			if (board.get(startRow, j-1).getChar() == '-') 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow, j-1));
			else if (!(board.get(startRow, j-1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow, j-1));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j < 7; j++) {
			if (board.get(startRow, j+1).getChar() == '-') 
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow, j+1));
			else if (!(board.get(startRow, j+1).getColor().equals(color))) {
				maybeNoLossMoves.add(new MoveCommand(startRow, startColumn, startRow, j+1));
				break;
			}
			else
				break;
		}		
		
		noLossMovesList = new ArrayList<MoveCommand>();
		noLossMovesList = filteredMaybeNoLossMoves(board, playerPieceLocations, maybeNoLossMoves);
	}

	@Override
	public ArrayList<MoveCommand> filteredMaybeNoLossMoves(ChessBoard board, ArrayList<ArrayList<Integer>> playerPieceLocations, ArrayList<MoveCommand> maybeNoLossMoves) {
		ArrayList<MoveCommand> noLossMoves= new ArrayList<MoveCommand>();	
		
		int tempPieceRow = 0;
		int tempPieceColumn = 0;
		
		for (int i = 0; i < maybeNoLossMoves.size(); i++) {
			MoveCommand tempMove = maybeNoLossMoves.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			boolean addToNoLossMovesFlag = true;
			for (int j = 0; j < playerPieceLocations.size(); j++) {
				ChessBoard temp = board.copyOf();
				if (temp.get(playerPieceLocations.get(j).get(0), playerPieceLocations.get(j).get(1)).getChar() != 'P' && temp.get(playerPieceLocations.get(j).get(0), playerPieceLocations.get(j).get(1)).getChar() != 'p')
					if (temp.executeMove(startRow, startColumn, endRow, endColumn) != false) {
						if (startRow == playerPieceLocations.get(j).get(0) && startColumn == playerPieceLocations.get(j).get(1)) {
							tempPieceRow = endRow;
							tempPieceColumn = endColumn;
						}
						else {
							tempPieceRow = playerPieceLocations.get(j).get(0);
							tempPieceColumn = playerPieceLocations.get(j).get(1);
						}
						if (temp.isInCheckState(tempPieceRow, tempPieceColumn, color)) {
							addToNoLossMovesFlag = false;
							break;
						}
					}
			}
			if (addToNoLossMovesFlag)
				noLossMoves.add(maybeNoLossMoves.get(i));
		}
		return noLossMoves;
	}

	@Override
	public void generateTakeOpponentPieceMoves(ChessBoard board) {
		takeOpponentPieceMovesList = new ArrayList<MoveCommand>();
		ArrayList<MoveCommand> takeOpponentPieceMoves = new ArrayList<MoveCommand>();

		for (int i = startRow, j = startColumn; i > 0 && j > 0; i--, j--) {
			if (board.get(i-1, j-1).getChar() == '-')
				;
			else if (!(board.get(i-1, j-1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i-1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i > 0 && j < 7; i--, j++) {
			if (board.get(i-1, j+1).getChar() == '-')
				;
			else if (!(board.get(i-1, j+1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i-1, j+1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j > 0; i++, j--) {
			if (board.get(i+1, j-1).getChar() == '-')
				;
			else if (!(board.get(i+1, j-1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i+1, j-1));
				break;
			}
			else
				break;
		}
		for (int i = startRow, j = startColumn; i < 7 && j < 7; i++, j++) {
			if (board.get(i+1, j+1).getChar() == '-') 
				;
			else if (!(board.get(i+1, j+1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i+1, j+1));
				break;
			}
			else
				break;
		}		
		for (int i = startRow; i > 0; i--) {
			if (board.get(i-1, startColumn).getChar() == '-') 
				;
			else if (!(board.get(i-1, startColumn).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i-1, startColumn));
				break;
			}
			else
				break;
			}
		for (int i = startRow; i < 7; i++) {
			if (board.get(i+1, startColumn).getChar() == '-') 
				;
			else if (!(board.get(i+1, startColumn).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, i+1, startColumn));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j > 0; j--) {
			if (board.get(startRow, j-1).getChar() == '-') 
				;
			else if (!(board.get(startRow, j-1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, startRow, j-1));
				break;
			}
			else
				break;
		}
		for (int j = startColumn; j < 7; j++) {
			if (board.get(startRow, j+1).getChar() == '-') 
				;
			else if (!(board.get(startRow, j+1).getColor().equals(color))) {
				takeOpponentPieceMoves.add(new MoveCommand(startRow, startColumn, startRow, j+1));
				break;
			}
			else
				break;
		}		
		
		takeOpponentPieceMovesList = takeOpponentPieceMoves;
	}

	@Override
	public void generateOpponentKingInCheckMoves(ChessBoard board) {
		
		generateGoodMoves(board);	
		ArrayList<MoveCommand> maybeOpponentKingInCheckMovesList = goodMovesList;
		opponentKingInCheckMovesList = new ArrayList<MoveCommand>();
		
		int tempPieceRow = -1;
		int tempPieceColumn = -1;
		String tempColor = "";
		
		if (color.equals("white")) {
			tempPieceRow = board.getPlayerBlack().getKingRow();
			tempPieceColumn = board.getPlayerBlack().getKingColumn();
			tempColor = "black";
		}
		else if (color.equals("black")) {
			tempPieceRow = board.getPlayerWhite().getKingRow();
			tempPieceColumn = board.getPlayerWhite().getKingColumn();
			tempColor = "white";
		}
		
		for (int i = 0; i < maybeOpponentKingInCheckMovesList.size(); i++) {
			MoveCommand tempMove = maybeOpponentKingInCheckMovesList.get(i);
			int startRow = tempMove.getStartRow();
			int startColumn = tempMove.getStartColumn();
			int endRow = tempMove.getEndRow();
			int endColumn = tempMove.getEndColumn();
			ChessBoard temp = board.copyOf();
			if (temp.executeMove(startRow, startColumn, endRow, endColumn) != false) {
				if (temp.isInCheckState(tempPieceRow, tempPieceColumn, tempColor)) {
					opponentKingInCheckMovesList.add(tempMove);
				}
			}
		}
	}
	
	@Override
	public void generateOpponentInCheckMateMoves(ChessBoard board) {

		generateGoodMoves(board);	
		if (goodMovesList.size() != 0) {
			ArrayList<MoveCommand> maybeOpponentInCheckMateMovesList = goodMovesList;
			opponentInCheckMateMovesList = new ArrayList<MoveCommand>();

			int tempPieceRow = -1;
			int tempPieceColumn = -1;
			String tempColor = "";

			if (color.equals("white")) {
				tempPieceRow = board.getPlayerBlack().getKingRow();
				tempPieceColumn = board.getPlayerBlack().getKingColumn();
				tempColor = "black";
			}
			else if (color.equals("black")) {
				tempPieceRow = board.getPlayerWhite().getKingRow();
				tempPieceColumn = board.getPlayerWhite().getKingColumn();
				tempColor = "white";
			}

			for (int i = 0; i < maybeOpponentInCheckMateMovesList.size(); i++) {
				MoveCommand tempMove = maybeOpponentInCheckMateMovesList.get(i);
				int startRow = tempMove.getStartRow();
				int startColumn = tempMove.getStartColumn();
				int endRow = tempMove.getEndRow();
				int endColumn = tempMove.getEndColumn();
				ChessBoard temp = board.copyOf();
				if (temp.executeMove(startRow, startColumn, endRow, endColumn) != false) {
					ArrayList<MoveCommand> opponentGoodMoves = new ArrayList<MoveCommand>();
					if (color.equals("white"))
						opponentGoodMoves = board.getPlayerBlack().getGoodMoves(temp, tempColor);
					else if (color.equals("black"))
						opponentGoodMoves = board.getPlayerWhite().getGoodMoves(temp, tempColor);
					if (opponentGoodMoves.size() == 0)
						opponentInCheckMateMovesList.add(maybeOpponentInCheckMateMovesList.get(i));
				}
			}
		}
	}

	@Override
	public ArrayList<MoveCommand> getTakeOpponentPieceMoves() {	return takeOpponentPieceMovesList;	}

	@Override
	public ArrayList<MoveCommand> getOpponentKingInCheckMoves() { return opponentKingInCheckMovesList; }
	
	@Override
	public ArrayList<MoveCommand> getOpponentInCheckMateMoves() { return opponentInCheckMateMovesList; }
	
	@Override
	public char getChar() { return name; }
	
	@Override
	public String getColor() { return color; }
	
	@Override
	public ArrayList getGoodMoves() { return goodMovesList;	}
	
	@Override
	public int getStartRow() { return startRow;	}
	
	@Override
	public void setStartRow(int startRow) {	this.startRow = startRow; }
	
	@Override
	public int getStartColumn() { return startColumn; }
	
	@Override
	public void setStartColumn(int startColumn) { this.startColumn = startColumn; }
	
	@Override
	public ArrayList<MoveCommand> getNoLossMoves() { return noLossMovesList; }
}
