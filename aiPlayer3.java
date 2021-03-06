import java.util.ArrayList;
import java.util.Random;

/**
 * Currently every instantiation of this class would react the same to a given board state. Future updates to this class will
 * use parameters to alter the metrics used by an AI to decide on a move. Additionally, multiple instantiations of this class will
 * play each another in a tournament-style battle for self-improvement. 
 * 
 * @author Devan Karsann
 */
public class aiPlayer3 {

	MoveCommand aiMove;
	String moveInfo = "";
	Random rand = new Random();
	ArrayList<MoveCommand> noLossMoves = new ArrayList<MoveCommand>();
	ArrayList<MoveCommand> takeOpponentPieceMoves = new ArrayList<MoveCommand>();
	ArrayList<MoveCommand> opponentKingInCheckMoves = new ArrayList<MoveCommand>();
	ArrayList<MoveCommand> acceptableMoves = new ArrayList<MoveCommand>();
	ArrayList<MoveCommand> opponentInCheckMateMoves = new ArrayList<MoveCommand>();
	ArrayList<MoveCommand> canTakePieceAfterwardsMoves = new ArrayList<MoveCommand>();
	
	/**
	 * This is the constructor for aiPlayer objects. A new aiPlayer is instantiated each
	 * time a decision is made. This is to reinforce the idea that the AI uses greedy
	 * best first search. 
	 * 
	 * @param board
	 */
	public aiPlayer3(ChessBoard board) {
		
		// looking for check mate state for player black
		acceptableMoves = board.getPlayerBlack().getGoodMoves(board, "black");
		
		// optional print out info for player black move sets put together
		String aIMoveSets = "";
		aIMoveSets += "\nAll acceptable moves for playerBlack: \n" + board.getPlayerBlack().printPossibleMoves(board, "black") + "\n";

		noLossMoves = board.getPlayerBlack().getNoLossMoves(board, "black");
		aIMoveSets += "All no potential loss moves for playerBlack: \n" + board.getPlayerBlack().printNoLossMoves(board, "black") + "\n";

		takeOpponentPieceMoves = board.getPlayerBlack().getTakeOpponentPieceMoves(board, "black");
		aIMoveSets += "All take opponent piece moves for playerBlack: \n" + board.getPlayerBlack().printTakeOpponentPieceMoves(board, "black") + "\n";
		
		opponentKingInCheckMoves = board.getPlayerBlack().getOpponentKingInCheckMoves(board, "black");
		aIMoveSets += "All opponent king in check moves for playerBlack: \n" + board.getPlayerBlack().printOpponentKingInCheckMoves(board, "black") + "\n";
		
		aIMoveSets += "All opponent in check mate moves for playerBlack: \n" + board.getPlayerBlack().printOpponentInCheckMateMoves(board, "black" + "\n");
        opponentInCheckMateMoves = board.getPlayerBlack().getOpponentInCheckMateMoves(board, "black");
		
        aIMoveSets += "\nAll can take piece afterwards moves for playerBlack: \n" + board.getPlayerBlack().printCanTakePieceAfterwardsMoves(board, "black");
        canTakePieceAfterwardsMoves = board.getPlayerBlack().getCanTakePieceAfterwardsMoves(board, "black");
        
		System.out.println(aIMoveSets);
		
		//+++++++++++++++++++THIS++IS++WHERE++THE++AI++PICKS++A++MOVE+++++++++++++++++++++++++++
		// a move is selected from a non-empty intersection of move sets, if all intersections
		// are empty the default move set to use is the list  of all acceptable moves
		aiMove = new MoveCommand();
		if (acceptableMoves.size() == 1)
			aiMove = acceptableMoves.get(0);
		if (aiMove.toString().length() == 0) {			
			if (opponentInCheckMateMoves.size() > 0)
				aiMove = opponentInCheckMateMoves.get(0);
		}
        
		// new code for simplified decision making
		boolean keepChecking = true;
		while (keepChecking){
			MoveCommand temp = checkFourLists(noLossMoves, takeOpponentPieceMoves, canTakePieceAfterwardsMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, takeOpponentPiece, canTakePieceAfterwards, opponentKingInCheckMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkThreeLists(noLossMoves, takeOpponentPieceMoves, canTakePieceAfterwardsMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, takeOpponentPiece, canTakePieceAfterwardsMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkThreeLists(noLossMoves, takeOpponentPieceMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, takeOpponentPiece, opponentKingInCheckMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkTwoLists(noLossMoves, takeOpponentPieceMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, takeOpponentPieceMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkThreeLists(noLossMoves, canTakePieceAfterwardsMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, canTakePieceAfterwards, opponentKingInCheckMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkTwoLists(noLossMoves, canTakePieceAfterwardsMoves);
			if (temp != null) {
				System.out.println("I selected a noLoss, canTakePieceAfterwardsMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			temp = checkTwoLists(noLossMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a noLoss, opponentKingInCheckMove");
				aiMove = temp;
				keepChecking = false;
				break;
			}
			if (noLossMoves.size() > 0) {
				System.out.println("I selected a noLossMove");
				aiMove = noLossMoves.get(0);
				for (int m = 0; m < acceptableMoves.size(); m++){
					if (aiMove.equals(acceptableMoves.get(m))){
						System.out.println("noLossMove was used");
						keepChecking = false; 
						break;
					}
				}
				if (!keepChecking)
					aiMove = null;
			}
			temp = checkThreeLists(takeOpponentPieceMoves, canTakePieceAfterwardsMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a takeOpponentPiece, canTakePieceAfterwards, opponentKingInCheckMove");
				aiMove = temp; 
				keepChecking = false; 
				break;
			}
			temp = checkTwoLists(takeOpponentPieceMoves, canTakePieceAfterwardsMoves);
			if (temp != null){
				System.out.println("I selected a takeOpponentPiece, canTakePieceAfterwardsMove");
				aiMove = temp; 
				keepChecking = false; 
				break;
			}
			temp = checkTwoLists(takeOpponentPieceMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a takeOpponentPiece, opponentKingInCheckMove");
				aiMove = temp; 
				keepChecking = false; 
				break;
			}
			if (takeOpponentPieceMoves.size() > 0){
				System.out.println("I selected a takeOpponentPieceMove");
				aiMove = takeOpponentPieceMoves.get(0);
				for (int m = 0; m < acceptableMoves.size(); m++){
					if (aiMove.equals(acceptableMoves.get(m))){
						System.out.println("takeOpponentPieceMoves was used");
						keepChecking = false; 
						break;
					}
				}
				if (!keepChecking)
					aiMove = null;
			}
			temp = checkTwoLists(canTakePieceAfterwardsMoves, opponentKingInCheckMoves);
			if (temp != null){
				System.out.println("I selected a canTakePieceAfterwards, opponentKingInCheckMove");
				aiMove = temp; 
				keepChecking = false; 
				break;
				}
			if (canTakePieceAfterwardsMoves.size() > 0){        	
				System.out.println("I selected a canTakePieceAfterwardsMove");
				aiMove = canTakePieceAfterwardsMoves.get(0);
				for (int m = 0; m < acceptableMoves.size(); m++){
					if (aiMove.equals(acceptableMoves.get(m))){
						System.out.println("canTakePieceAfterwardsMove was used");
						keepChecking = false; 
						break;
					}
				}
				if (!keepChecking)
					aiMove = null;
			}
			if (opponentKingInCheckMoves.size() > 0){ // OPPONENT KING IN CHECK MOVES <ON>
				System.out.println("I selected an opponentKingInCheckMove");
				aiMove = opponentKingInCheckMoves.get(rand.nextInt(opponentKingInCheckMoves.size()));
				for (int m = 0; m < acceptableMoves.size(); m++){
					if (aiMove.equals(acceptableMoves.get(m))){
						System.out.println("opponentKingInCheckMoves was used");
						keepChecking = false; 
						break;
					}
				}
				if (!keepChecking)
					aiMove = null;
			}
			System.out.println("I selected an acceptableMove");
			aiMove = acceptableMoves.get(rand.nextInt(acceptableMoves.size()));
			keepChecking = false; break;
		}
		// end of new code
	}
	
	// new check four lists method
		public MoveCommand checkFourLists(ArrayList<MoveCommand> list1, ArrayList<MoveCommand> list2, ArrayList<MoveCommand> list3, ArrayList<MoveCommand> list4){
			MoveCommand retval = null;
			boolean leave = false;
			for (int i = 0; i < list1.size(); i++){
				if (leave)
					break;
				for (int j = 0; j < list2.size(); j++){
					if (leave)
						break;
					for (int k = 0; k < list3.size(); k++){
						if (leave)
							break;
						for (int L = 0; L < list4.size(); L++){
							if (list1.get(i).equals(list3.get(j)) && list1.get(i).equals(list3.get(k)) && list1.get(i).equals(list4.get(L))){
								retval = list1.get(i);
								for (int m = 0; m < acceptableMoves.size(); m++){
									if (retval.equals(acceptableMoves.get(m))){
										System.out.println("checkFourLists was used");
										leave = true;
										break;
									}
								}
								if (!leave)
									retval = null;
							}
						}
					}
				}
			}
			return retval;
		}
	// end of new method
	
	// new check three lists method
	public MoveCommand checkThreeLists(ArrayList<MoveCommand> list1, ArrayList<MoveCommand> list2, ArrayList<MoveCommand> list3){
		MoveCommand retval = null;
		boolean leave = false;
		for (int i = 0; i < list1.size(); i++){
			if (leave)
				break;
			for (int j = 0; j < list2.size(); j++){
				if (leave)
					break;
				for (int k = 0; k < list3.size(); k++){
					if (list1.get(i).equals(list2.get(j)) && list1.get(i).equals(list3.get(k))){
						retval = list1.get(i);
						for (int m = 0; m < acceptableMoves.size(); m++){
							if (retval.equals(acceptableMoves.get(m))){
								System.out.println("checkThreeLists was used");
								leave = true;
								break;
							}
						}
						if (!leave)
							retval = null;
					}
				}
			}
		}
		return retval;
	}
	// end of new method
	
	// new check two lists method
	public MoveCommand checkTwoLists(ArrayList<MoveCommand> list1, ArrayList<MoveCommand> list2){
		MoveCommand retval = null;
		boolean leave = false;
		for (int i = 0; i < list1.size(); i++){
			if (leave)
				break;
			for (int j = 0; j < list2.size(); j++){
				if (list1.get(i).equals(list2.get(j))){
					retval = list1.get(i);
					for (int m = 0; m < acceptableMoves.size(); m++){
						if (retval.equals(acceptableMoves.get(m))){
							System.out.println("checkTwoLists was used");
							leave = true;
							break;
						}
					}
					if (!leave)
						retval = null;
				}
			}
		}
		return retval;
	}
	// end of new method
	
	/**
	 * This method returns the move decided on by the AI.
	 * 
	 * @return MoveCommand
	 */
	public MoveCommand getaiMove() {
		return aiMove;
	}
	
	/**
	 * This method returns the info about all of the move sets the AI analyzed.
	 * 
	 * @return String
	 */
	public String getMoveInfo() {
		return moveInfo;
	}
}