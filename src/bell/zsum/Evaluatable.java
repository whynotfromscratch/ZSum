package bell.zsum;

import java.util.LinkedList;

/**
 * An interface to allow for the evaluation of arbitrary 2-player zero-sum games.
 * The type parameter T is for the class which represents a move in the implementing
 * evaluatable game.
 * @author Douglas Bell
 */
public interface Evaluatable<T> {
	
	/**
	 * Returns a list of the possible moves given the current game state.
	 * @return A list of the possible moves given the current game state.
	 */
	public LinkedList<T> getPossibleMoves();
	
	/**
	 *
	 * Updates the current game state with a given move.
	 * @param move Move to perform.
	 */
	public void pushMove(T move);
	
	/**
	 * Updates the current game state by undoing a move.
	 * @param move Move to undo.
	 */
	public void popMove(T move);
	
	/**
	 * Returns a heuristic (or absolute, if possible) evaluation of the current position
	 * of the game. A positive value indicates that player one is winning, a negative value that
	 * player two is winning, and a return value of zero indicates an essentially even game.
	 * Returning Float.POSITIVE_INFINITY or Float.NEGATIVE_INFINITY indicates a winning game
	 * state for the respective player (+ being p1 and - being p2, as before).
	 * @return A heuristic evaluation of the current position of the game (positive
	 *  is infavor of player one and negative is in favor of player two)
	 */
	public float getEvaluation();
	
	/**
	 * Returns an object which uniquely represents the state of this game.
	 * The object returned must always be a new instance and must properly
	 * implement the equals method.
	 * @return An object which uniquely represents the state of this game.
	 */
	public Object getGameState();
	
	/**
	 * Returns whether it is the first player's turn or not.
	 * If not, then it is the second player's turn.
	 * @return Whether it is the first player's turn or not.
	 */
	public boolean isFirstPlayersTurn();
	
}
