package bell.zsum.examples;

import java.util.LinkedList;
import bell.zsum.Evaluatable;

public class NimGame implements Evaluatable<Integer> {

	private int itemsLeft;
	private boolean firstPlayersTurn;
	
	public NimGame(int n) {
		itemsLeft = n;
		firstPlayersTurn = true;
	}
	
	@Override
	public LinkedList<Integer> getPossibleMoves() {
		LinkedList<Integer> moves = new LinkedList<>();
		if(itemsLeft > 0) {moves.add(1);} //If at least 1 is left, taking 1 is possible
		if(itemsLeft > 1) {moves.add(2);} //If at least 2 are left, taking 2 is possible
		return moves;
	}

	@Override
	public void pushMove(Integer move) {
		firstPlayersTurn = !firstPlayersTurn; //Advance players
		itemsLeft -= move; //Take away intended number of items
	}

	@Override
	public void popMove(Integer move) {
		firstPlayersTurn = !firstPlayersTurn; //Backtrack players
		itemsLeft += move; //Put back removed number of items
	}

	@Override
	public float getEvaluation() {
		//If we are left with no items, the other player lost and thus we won
		if(firstPlayersTurn && itemsLeft == 0) {return Float.POSITIVE_INFINITY;} 
		if(!firstPlayersTurn && itemsLeft == 0) {return Float.NEGATIVE_INFINITY;}
		
		//Otherwise our (somewhat stupid) heuristic assumes it is an "even" game,
		//despite the fact that each game has a guaranteed winning player assuming
		//optimal play
		return 0;
	}

	@Override
	public Object getGameState() {
		//Returns a unique string representing the game state
		return "P1: "+firstPlayersTurn+" I: "+itemsLeft;
	}

	@Override
	public boolean isFirstPlayersTurn() {
		return firstPlayersTurn;
	}

}
