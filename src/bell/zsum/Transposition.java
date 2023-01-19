package bell.zsum;

/**
 * A class used in the transposition table within the Evaluator.
 * Holds information on the game state and what depth this state
 * was first searched at. The evaluation results mapped to by
 * this transposition can be reused if the search depth exceeds
 * that which is required in the current search.
 * @author Douglas Bell
 */
class Transposition {
	
	private final Object gameState;
	private final int depth;
	private final int hash;
	
	public Transposition(Object gameState, int depth) {
		this.gameState = gameState;
		this.depth = depth;
		this.hash = gameState.hashCode();
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Transposition)) {return false;}
		Transposition t = (Transposition)o;
		return depth >= t.depth && gameState.equals(t.gameState);
	}
}
