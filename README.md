# ZSum

ZSum is an extremely small "library" written in pure Java which can evaluate arbitrary zero-sum games. Users of ZSum need only to implement a handful of simple methods from the Evaluatable interface to begin finding best moves, best lines, and overall evaluation for their particular game.

---

# Usage

### Evaluatable<T>

The Evaluatable interface is used to represent a zero sum game. 


##### **Type Parameter**

The type paramater **T** is used to denote what type represents a move in this game. For example,

- **T** might be **Integer** if the game implementing Evaluatable were a nim-like game where each move represents taking away some number of items from a set of items.
- **T** might be a more complex custom type in a game such as Chess.

##### **Methods**

| Name | Description |
| ---- | ----------- |
| getPossibleMoves | Returns a linked list of possible moves (type T) given the current state of the game|
| pushMove | Takes in a move of type T and performs that move by updating the game state accordingly. Note that moves MUST be reversible.|
| popMove| Takes in a move of type T and undoes that move by updating the game state to its state prior to making the move.
| getEvaluation | Returns a heuristic evaluation of the game's current state, where 0 is a draw, positive values indicate a first player advantage, and negative values indicate a second player advantage. Infinite values indicate a win condition for their respective player depending on sign.
| getGameState | Returns an arbitrary object which uniquely represents the state of the game. The type used must correctly implement the equals method.
| isFirstPlayersTurn | Must return whether it is the first player's turn or not. The inclusion of this method is critical for games in which multiple moves can possibly be made before alternating players. |

---

### Evaluator<T>

This class can evaluate any game which implements Evaluatable<T>.

##### **Methods**

| Name | Description |
| ---- | ----------- |
| getBestMove | Finds and returns the best next move for the current player to a given search depth. |
| getBestLine | Finds and returns a line of best moves (for both players) of a certain length to a given search depth. 
| evaluate | Finds and returns the evaluation  of the current position to a given depth by running minimax and using the evaluation heuristic provided by the Evaluatable position. |

---

# Example

Consider a nim-like game where players take turns taking either 1 or 2 items from a pile initally containing *n* items, where the player to take the last of the items loses. 

### Possible Implementation

``` Java
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
```

The winning strategy / winning player for this game with *n* items could then be found like so:

``` Java
NimGame game = new NimGame(n);
Evaluator<Integer> e = new Evaluator<>();

//Say who has the ability to play optimally and win
if(e.evaluate(game,n) == Float.POSITIVE_INFINITY) {
    System.out.println("Player 1 Wins");
} else {
    System.out.println("Player 2 Wins");
}

//Show the line which allows for that win
System.out.println(e.getBestLine(game,n,n));
```

Note that the use of *n* is an upper bound for the number of moves in a game, and if the game ends before that number of moves is reached, this number is essentially ignored.
