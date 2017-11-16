import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This is the driver class for my Chess game.
 * 
 * Users can specify the chess board state they want to start with. If a file is not entered,
 * the default chess board start state will be used. Information about move sets each turn are
 * initially turned off, but can be printed to the console by entering '1' or '2' as the second
 * command line argument.
 * 
 * @author Devan
 *
 */
public class StartGame {

	static ChessBoard board;

	/**
	 * This is the main method. The chess game program starts here.
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		Player playerWhite = new Player("white");
		Player playerBlack = new Player("black");
		int outputMode = 0;
		if (args.length > 0)
			board = new ChessBoard(playerWhite, playerBlack, args[0]);
		else
			board = new ChessBoard(playerWhite, playerBlack);
		if (args.length > 1) {
			try {
				outputMode = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("invalid command line arguments");
				System.out.println("usage: <fileName> <outputMode>");
				System.out.println("info: name of text file with chess board configuration,\n      output mode in console (0, 1, or 2)");
				System.exit(0);
			}
		}
		Random rand = new Random();
		String moveInfo = "";
		boolean boardInCheckMateState = false;
		MoveCommand playerBlackMove = null;

		System.out.println("Welcome to Chess!\n\nto start the game select a move from the list below\n");
		
		while (boardInCheckMateState == false) {

			ArrayList<MoveCommand> acceptableMoves = new ArrayList<MoveCommand>();
			acceptableMoves = board.getPlayerWhite().getGoodMoves(board, "white");

			// looking for check mate state for player white
			if (acceptableMoves.size() == 0) {
				System.out.println(board.toString());
				System.out.println("check mate! playerBlack wins!\nno valid moves were found for playerWhite...");
				System.exit(0);
			}

			// printing information about player black's move
			if (playerBlack.getMoveIsValid()) {
				System.out.println(moveInfo);
				System.out.println("playerBlack moved " + board.get(playerBlackMove.getEndRow(), playerBlackMove.getEndColumn()).getChar()
						+ " from:\nrow " + playerBlackMove.getStartRow() + " column " + playerBlackMove.getStartColumn() + ", to row " +
						playerBlackMove.getEndRow() + ", column " + playerBlackMove.getEndColumn() + "\n");
			}
			ChessPiece tileTaken = null;
			playerWhite.setMoveIsValid(false);
			
			int subValue = 66; // used for ascii value comparisons
			
			System.out.println("current game state:");
			System.out.println(board.toString());
			
			// optional print out for information regarding player white's possible moves
			if (outputMode == 0 || outputMode == 1 || outputMode == 2)
				System.out.println("All " + acceptableMoves.size() + " possible moves for playerWhite: \n" + board.getPlayerWhite().printPossibleMoves(board, "white"));
			MoveCommand playerWhiteMove;
			
			// repeatedly ask player white to enter a move until a valid move is selected
			do {
				System.out.print("select a move playerWhite: ");
				playerWhiteMove = new MoveCommand(scan.nextLine().toLowerCase());
				if (!(playerWhiteMove.hasValidSyntax(playerWhiteMove.toString(), board, playerWhite.getColor()))) {
					;
				}
				else
					playerWhite.setMoveIsValid(true);
			} while (playerWhite.getMoveIsValid() == false);

			acceptableMoves = new ArrayList<MoveCommand>();
			acceptableMoves = board.getPlayerWhite().getGoodMoves(board, "white");

//++++++++++// new code for making good AI playing random AI
//			playerWhiteMove = acceptableMoves.get(rand.nextInt(acceptableMoves.size()));
//			System.out.println("select a move playerWhite: " + playerWhiteMove.toString());
//			try {
//				TimeUnit.MILLISECONDS.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//++++++++++// end of new code
			
			boolean found = false;
			for (int i = 0; i < acceptableMoves.size() && !found; i++) {
				if (acceptableMoves.get(i).toString().equals(playerWhiteMove.toString()))
					found = true;
			}

			if (found == false) {
				System.out.println("\nselected move not found in acceptableMoves list");
				continue;
			}

			// execute move here for player white
			int startRow = playerWhiteMove.getStartRow();
			int startColumn = playerWhiteMove.getStartColumn();
			int endRow = playerWhiteMove.getEndRow();
			int endColumn = playerWhiteMove.getEndColumn();
			ChessPiece pieceToMove = board.get(startRow, startColumn);
			tileTaken = board.get(endRow, endColumn);
			board.get(startRow, startColumn).setStartRow(endRow);
			board.get(startRow, startColumn).setStartColumn(endColumn);
			board.setBoard(endRow, endColumn, board.get(startRow, startColumn));
			board.setBoard(startRow, startColumn, new Hyphen());
			if (board.getEnPassentRow() != endRow || board.getEnPassentColumn() != endColumn)
				board.resetThePassent();
			if (board.pawnWasMovedTwoSpaces() == true) {
				if (endRow == board.getEnPassentRow() && endColumn == board.getEnPassentColumn()) {
					System.out.println("enPassent capture taking place");
					board.setBoard(board.getEnPassentRow()+1, board.getEnPassentColumn(), new Hyphen());
					System.out.println("'p' captured by whitePlayer!\n");
				}
			}
			if (board.get(endRow, endColumn).getChar() == 'K') {
				board.getPlayerWhite().setKingRow(endRow);
				board.getPlayerWhite().setKingColumn(endColumn);		
				if (board.getPlayerWhite().canDoQSC() == true) {
					if (endRow == 7 && endColumn == 2) {
						board.setBoard(7, 0, new Hyphen());
						board.setBoard(7, 3, new Rook("white", 7, 3));
						System.out.println("Queen side castle done by white player!\n");
					}
				}
				if (board.getPlayerWhite().canDoKSC()) {
					if (endRow == 7 && endColumn == 6) {
						board.setBoard(7, 7, new Hyphen());
						board.setBoard(7, 5, new Rook("white", 7, 5));
						System.out.println("King side castle done by white player!\n");
					}
				}
			}
			if (board.get(endRow, endColumn).getChar() == 'P' && Math.abs(endRow - startRow) == 2) {
				board.setPawnWasMovedTwoSpaces(true);
				board.setEnPassentRow(endRow + 1);
				board.setEnPassentColumn(endColumn);
			}
			if (board.get(endRow, endColumn).getChar() == 'P' && endRow == 0) {
				System.out.println("white player upgraded pawn to queen");
				board.setBoard(endRow, endColumn, new Queen("white", endRow, endColumn));
			}
			if (board.get(endRow, endColumn).getChar() == 'K') {				   
				board.getPlayerWhite().setCanDoQSC(false);
				board.getPlayerWhite().setCanDoKSC(false);
			}
			if (board.get(endRow, endColumn).getChar() == 'R') {
				if (startColumn == 0)
					board.getPlayerWhite().setCanDoQSC(false);
				if (startColumn == 7)
					board.getPlayerWhite().setCanDoKSC(false);
			}

			// looking for check mate state for player black
			acceptableMoves = new ArrayList<MoveCommand>();
			acceptableMoves = board.getPlayerBlack().getGoodMoves(board, "black");
			if (acceptableMoves.size() == 0) {
				System.out.println(board.toString());
				System.out.println("check mate! playerWhite wins!\nno valid moves were found for playerBlack...");
				System.exit(0);
			}

			//System.out.println("\ngame state for playerBlack:");
			//System.out.println(board.toString());
			
			// optional info for player black move sets put together
			String aIMoveSets = "";
			aIMoveSets += "\nAll acceptable moves for playerBlack: \n" + board.getPlayerBlack().printPossibleMoves(board, "black") + "\n";

			ArrayList<MoveCommand> noLossMoves = new ArrayList<MoveCommand>();
			noLossMoves = board.getPlayerBlack().getNoLossMoves(board, "black");
			aIMoveSets += "All no potential loss moves for playerBlack: \n" + board.getPlayerBlack().printNoLossMoves(board, "black") + "\n";

			ArrayList<MoveCommand> takeOpponentPieceMoves = new ArrayList<MoveCommand>();
			takeOpponentPieceMoves = board.getPlayerBlack().getTakeOpponentPieceMoves(board, "black");
			aIMoveSets += "All take opponent piece moves for playerBlack: \n" + board.getPlayerBlack().printTakeOpponentPieceMoves(board, "black") + "\n";
			
			ArrayList<MoveCommand> opponentKingInCheckMoves = new ArrayList<MoveCommand>();
			opponentKingInCheckMoves = board.getPlayerBlack().getOpponentKingInCheckMoves(board, "black");
			aIMoveSets += "All opponent king in check moves for playerBlack: \n" + board.getPlayerBlack().printOpponentKingInCheckMoves(board, "black") + "\n";
			
			ArrayList<MoveCommand> opponentInCheckMateMoves = board.getPlayerBlack().getOpponentInCheckMateMoves(board, "black");
			aIMoveSets += "All opponent in check mate moves for playerBlack: \n" + board.getPlayerBlack().printOpponentInCheckMateMoves(board, "black");
			
			if (outputMode == 2)
				System.out.println(aIMoveSets);
			// printing info about previously taken piece if applicable
			if (tileTaken.getChar() != '-')
				System.out.println("'" + tileTaken.getChar() + "' was taken by playerWhite!");
			
			//+++++++++++++++this+is+the+code+for+the+AI+choosing+a+move+++++++++++++++++++++++++++
			// a move is selected from a non-empty intersection of move sets, if all intersections
			// are empty the default move set to use is the list  of all acceptable moves
			playerBlackMove = new MoveCommand();
			if (acceptableMoves.size() == 1)
				playerBlackMove = acceptableMoves.get(0);
			if (playerBlackMove.toString().length() == 0) {			
				if (opponentInCheckMateMoves.size() > 0)
					playerBlackMove = opponentInCheckMateMoves.get(0);
			}
// priority 1
			if (playerBlackMove.toString().length() == 0) {
				if (noLossMoves.size() > 0 && takeOpponentPieceMoves.size() > 0 && opponentKingInCheckMoves.size() > 0) {
					ArrayList<MoveCommand> noLossTakePieceOpponentKingInCheckMoves = new ArrayList<MoveCommand>(); 
					for (int i = 0; i < noLossMoves.size(); i++) {
						for (int j = 0; j < takeOpponentPieceMoves.size(); j++) {
							for (int k = 0; k < opponentKingInCheckMoves.size(); k++) {
								if (takeOpponentPieceMoves.get(j).toString().equals(noLossMoves.get(i).toString()) && takeOpponentPieceMoves.get(j).toString().equals(opponentKingInCheckMoves.get(k).toString())) {
									for (int x = 0; x < acceptableMoves.size(); x++) {
										if (acceptableMoves.get(x).toString().equals(noLossMoves.get(i).toString())) {
											noLossTakePieceOpponentKingInCheckMoves.add(acceptableMoves.get(x));
										}
									}
								}
							}
						}
					}
					if (noLossTakePieceOpponentKingInCheckMoves.size() > 0) {
						playerBlackMove = returnGreatest(noLossTakePieceOpponentKingInCheckMoves);  
						moveInfo = ("\nplayerBlack selected a noLossTakePieceOpponentKingInCheck move");
					}
				}
			}
// priority 2
			if (playerBlackMove.toString().length() == 0) {
				if (noLossMoves.size() > 0 && takeOpponentPieceMoves.size() > 0) {
					ArrayList<MoveCommand> takePieceOpponentKingInCheckMoves = new ArrayList<MoveCommand>(); 
					for (int i = 0; i < noLossMoves.size(); i++) {
						for (int j = 0; j < takeOpponentPieceMoves.size(); j++) {
							if (takeOpponentPieceMoves.get(j).toString().equals(noLossMoves.get(i).toString())) {
								for (int k = 0; k < acceptableMoves.size(); k++) {
									if (acceptableMoves.get(k).toString().equals(noLossMoves.get(i).toString())) {
										takePieceOpponentKingInCheckMoves.add(acceptableMoves.get(k));
									}
								}
							}
						}
					}
					if (takePieceOpponentKingInCheckMoves.size() > 0) {
						playerBlackMove = returnGreatest(takePieceOpponentKingInCheckMoves);  
						moveInfo = ("\nplayerBlack selected a noLossTakePiece move");
					}
				}
			}
// priority 3
			if (playerBlackMove.toString().length() == 0) {
				if (noLossMoves.size() > 0 && opponentKingInCheckMoves.size() > 0) {
					ArrayList<MoveCommand> noLossOpponentKingInCheckMoves = new ArrayList<MoveCommand>(); 
					for (int i = 0; i < noLossMoves.size(); i++) {
						for (int j = 0; j < opponentKingInCheckMoves.size(); j++) {
							if (opponentKingInCheckMoves.get(j).toString().equals(noLossMoves.get(i).toString())) {
								for (int k = 0; k < acceptableMoves.size(); k++) {
									if (acceptableMoves.get(k).toString().equals(noLossMoves.get(i).toString())) {
										noLossOpponentKingInCheckMoves.add(acceptableMoves.get(k));
									}
								}
							}
						}
					}
					if (noLossOpponentKingInCheckMoves.size() > 0) {
						playerBlackMove = returnGreatest(noLossOpponentKingInCheckMoves);  
						moveInfo = ("\nplayerBlack selected a noLossOpponentKingInCheck move12");
					}
				}
			}
// priority 4
			if (playerBlackMove.toString().length() == 0) {
				if (noLossMoves.size() > 0) {
					ArrayList<MoveCommand> acceptableNoLossMoves = new ArrayList<MoveCommand>();
					for (int i = 0; i < noLossMoves.size(); i++) {
						for (int j = 0; j < acceptableMoves.size(); j++) {
							if (noLossMoves.get(i).toString().equals(acceptableMoves.get(j).toString()))
								acceptableNoLossMoves.add(acceptableMoves.get(j));
						}
					}
					if (acceptableNoLossMoves.size() > 0) {
						playerBlackMove = returnGreatest(acceptableNoLossMoves);  
						moveInfo = ("\nplayerBlack selected a noLoss move");
					}
				}
			}
// priority 5
			if (playerBlackMove.toString().length() == 0) {
				if (opponentKingInCheckMoves.size() > 0 && takeOpponentPieceMoves.size() > 0) {
					ArrayList<MoveCommand> takePieceOpponentKingInCheckMoves = new ArrayList<MoveCommand>(); 
					for (int i = 0; i < opponentKingInCheckMoves.size(); i++) {
						for (int j = 0; j < takeOpponentPieceMoves.size(); j++) {
							if (takeOpponentPieceMoves.get(j).toString().equals(opponentKingInCheckMoves.get(i).toString())) {
								for (int k = 0; k < acceptableMoves.size(); k++) {
									if (acceptableMoves.get(k).toString().equals(opponentKingInCheckMoves.get(i).toString())) {
										takePieceOpponentKingInCheckMoves.add(acceptableMoves.get(k));
									}
								}
							}
						}
					}
					if (takePieceOpponentKingInCheckMoves.size() > 0) {
						playerBlackMove = returnGreatest(takePieceOpponentKingInCheckMoves);  
						moveInfo = ("\nplayerBlack selected a takePieceOpponentKingInCheck move");
					}
				}
			}			
// priority 6
			if (playerBlackMove.toString().length() == 0) {
				if (takeOpponentPieceMoves.size() > 0) {
					ArrayList<MoveCommand> acceptableTakeOpponentPieceMoves = new ArrayList<MoveCommand>();
					for (int i = 0; i < takeOpponentPieceMoves.size(); i++) {
						for (int j = 0; j < acceptableMoves.size(); j++) {
							if (takeOpponentPieceMoves.get(i).toString().equals(acceptableMoves.get(j).toString()))
								acceptableTakeOpponentPieceMoves.add(acceptableMoves.get(j));
						}
					}
					if (acceptableTakeOpponentPieceMoves.size() > 0) {
						playerBlackMove = returnGreatest(acceptableTakeOpponentPieceMoves);  
						moveInfo = ("\nplayerBlack selected a takeOpponentPiece move");
					}
				}
			}			
// priority 7
			if (playerBlackMove.toString().length() == 0) {
				if (opponentKingInCheckMoves.size() > 0) {
					ArrayList<MoveCommand> acceptableOpponentKingInCheckMoves = new ArrayList<MoveCommand>();
					for (int i = 0; i < opponentKingInCheckMoves.size(); i++) {
						for (int j = 0; j < acceptableMoves.size(); j++) {
							if (opponentKingInCheckMoves.get(i).toString().equals(acceptableMoves.get(j).toString()))
								acceptableOpponentKingInCheckMoves.add(acceptableMoves.get(j));
						}
					}
					if (acceptableOpponentKingInCheckMoves.size() > 0) {
						playerBlackMove = returnGreatest(acceptableOpponentKingInCheckMoves);  
						moveInfo = ("\nplayerBlack selected an opponentKingInCheck move");
					}
				}
			}
// priority 8
			if (playerBlackMove.toString().length() == 0) {
				int x = rand.nextInt(acceptableMoves.size());
				playerBlackMove = acceptableMoves.get(x);
				moveInfo = ("\nplayerBlack selected an acceptable move");
			}
			// at this point the AI has selected a move to execute
			//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
			// executing player black move here
			playerBlack.setMoveIsValid(false);
			subValue = 98;

			if (!(playerBlackMove.hasValidSyntax(playerBlackMove.toString(), board, playerBlack.getColor()))) {
				System.out.println("invalid command syntax: " + playerBlackMove.toString());
				System.exit(0);
			}

			found = false;
			for (int i = 0; i < acceptableMoves.size() && !found; i++) {
				if (acceptableMoves.get(i).toString().equals(playerBlackMove.toString()))
					found = true;
			}

			if (found == false) {
				System.out.println("invalid command syntax: " + playerBlackMove.toString());
				System.exit(0);
			}

			startRow = playerBlackMove.getStartRow();
			startColumn = playerBlackMove.getStartColumn();
			endRow = playerBlackMove.getEndRow();
			endColumn = playerBlackMove.getEndColumn();
			pieceToMove = board.get(startRow, startColumn);
			tileTaken = board.get(endRow, endColumn);
			board.get(startRow, startColumn).setStartRow(endRow);
			board.get(startRow, startColumn).setStartColumn(endColumn);
			board.setBoard(endRow, endColumn, board.get(startRow, startColumn));
			board.setBoard(startRow, startColumn, new Hyphen());
			board.getPlayerBlack().getGoodMoves(board, "black");
			if (tileTaken.getChar() != '-')
				System.out.println("'" + tileTaken.getChar() + "' was taken by playerBlack!");
			if (board.pawnWasMovedTwoSpaces() == true) {
				if (endRow == board.getEnPassentRow() && endColumn == board.getEnPassentColumn()) {
					System.out.println("enPassent capture taking place");
					board.setBoard(board.getEnPassentRow()-1, board.getEnPassentColumn(), new Hyphen());
					System.out.println("'P' captured by playerBlack!\n");
				}
			}
			if ((pieceToMove.getChar() != 'P' || pieceToMove.getChar() != 'p'))
				board.resetThePassent();
			if (board.get(endRow, endColumn).getChar() == 'k') {
				board.getPlayerBlack().setKingRow(endRow);
				board.getPlayerBlack().setKingColumn(endColumn);		
				if (board.getPlayerBlack().canDoQSC() == true) {
					if (endRow == 0 && endColumn == 2) {
						board.setBoard(0, 0, new Hyphen());
						board.setBoard(0, 3, new Rook("black", 0, 3));

						System.out.println("Queen side castle done by black player!\n");
					}
				}
				if (board.getPlayerBlack().canDoKSC() == true) {
					if (endRow == 0 && endColumn == 6) {
						board.setBoard(0, 7, new Hyphen());
						board.setBoard(0, 5, new Rook("black", 0, 5));

						System.out.println("King side castle done by black player!\n");
					}
				}
			}
			if (board.get(endRow, endColumn).getChar() == 'p' && Math.abs(endRow - startRow) == 2) {
				board.setPawnWasMovedTwoSpaces(true);
				board.setEnPassentRow(endRow - 1); 
				board.setEnPassentColumn(endColumn); 
			}
			if (board.get(endRow, endColumn).getChar() == 'p' && endRow == 7) {
				System.out.println("black player upgraded pawn to queen");
				board.setBoard(endRow, endColumn, new Queen("black", endRow, endColumn));
			}
			if (board.get(endRow, endColumn).getChar() == 'k') {				   
				board.getPlayerBlack().setCanDoQSC(false);
				board.getPlayerBlack().setCanDoKSC(false);
			}
			if (board.get(endRow, endColumn).getChar() == 'r') {
				if (startColumn == 0)
					board.getPlayerBlack().setCanDoQSC(false);
				if (startColumn == 7)
					board.getPlayerBlack().setCanDoKSC(false);
			}
			board.getPlayerBlack().setMoveIsValid(true);
		}
	}

	/**
	 * This method decides if a random move should be selected or if the MoveCommand with the smallest 
	 * value should be selected. This is to prevent the AI from having the same behavior all the time
	 * @param input
	 * @return retVal
	 */
	public static MoveCommand returnGreatest(ArrayList<MoveCommand> input){
		Random rand = new Random();
		MoveCommand retVal = input.get(rand.nextInt(input.size()));
		if (rand.nextBoolean()) {
			if (input.size() > 1) {
				for (int i = 1; i < input.size(); i++) {
					if (input.get(i).getAvgDistToCenter() < retVal.getAvgDistToCenter())
						retVal = input.get(i);
					if (input.get(i).getAvgDistToCenter() == retVal.getAvgDistToCenter()) {
						if (rand.nextInt(2) == 0) {
							retVal = input.get(i);
						}
					}
				}
			}
		}
		return retVal;
	}
	
}
	
