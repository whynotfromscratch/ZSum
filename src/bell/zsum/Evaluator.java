package bell.zsum;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class which can evaluate zero-sum games (implements evaluatable) whose moves are of type T.
 * The overall evaluation of the current position of the game, best move, and series of best moves
 * can be computed to a desired depth using minimax with alpha-beta pruning and a rudimentary
 * transposition table.
 * @author Doug Bell
 * @param <T> Data type representing a move. For example, a nim-like game may only
 * 			  use an integer to represent how many items were taken in total, while
 * 			  a game such as chess might need a custom class to represent more complex
 * 			  movements on a board.
 */
public class Evaluator<T> {
	
	private class EV {
		public final T bestMove;
		public final float eval;
		public EV(T bestMove, float eval) {
			this.bestMove = bestMove;
			this.eval = eval;
		}
	}
	
	private T bestMove;
	private HashMap<Transposition, EV> cachedResults;
	
	public Evaluator() {
		cachedResults = new HashMap<>();
	}

	public T getBestMove(Evaluatable<T> pos, int depth) {
		LinkedList<T> singleMove = getBestLine(pos,1,depth);
		return singleMove.getFirst();
	}
	
	public LinkedList<T> getBestLine(Evaluatable<T> pos, int length, int depth) {
		LinkedList<T> bestLine = new LinkedList<>();
		int i=0;
		for(i=0; i<length; i++) {
			evaluate(pos,depth);
			if(bestMove == null) {break;}
			bestLine.addLast(bestMove);
			pos.pushMove(bestMove);
		}
		while(i!=0) {
			T move = bestLine.removeLast();
			pos.popMove(move);
			bestLine.addFirst(move);
			i--;
		}
		return bestLine;
	}
	
	public float evaluate(Evaluatable<T> pos, int depth) {
		bestMove = null;
		cachedResults.clear();
		return minimax(pos,depth,Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY);
	}
	
	private float minimax(Evaluatable<T> curPos, int depth, float alpha, float beta) {
		Transposition tpos = new Transposition(curPos.getGameState(), depth);
		if(cachedResults.containsKey(tpos)) {
			EV ev = cachedResults.get(tpos);
			this.bestMove = ev.bestMove;
			return ev.eval;
		}
		
		if(depth == 0) {
			return curPos.getEvaluation();
		}
		
		LinkedList<T> moves = curPos.getPossibleMoves();
		if(moves.isEmpty()) {
			return curPos.getEvaluation();
		}
		
		if(curPos.isFirstPlayersTurn()) {
			float max = Float.NEGATIVE_INFINITY;
			T bestMove = null;
			
			for(T move : moves) {
				if(bestMove == null) {bestMove = move;}
				curPos.pushMove(move);
					float moveEval = minimax(curPos, depth-1, alpha, beta);
					if(moveEval > max) {
						max = moveEval;
						bestMove = move;
					}
				curPos.popMove(move);
				
				alpha = Math.max(alpha, max);
				if(max >= beta) {
					break;
				}
			}
			
			this.bestMove = bestMove;
			return saveEval(tpos, max, bestMove);
		} else {
			float min = Float.POSITIVE_INFINITY;
			T bestMove = null;
			
			for(T move : moves) {
				if(bestMove == null) {bestMove = move;}
				curPos.pushMove(move);
					float moveEval = minimax(curPos, depth-1, alpha, beta);
					if(moveEval < min) {
						min = moveEval;
						bestMove = move;
					}
				curPos.popMove(move);
				
				beta = Math.min(beta, min);
				if(min <= alpha) {
					break;
				}
			}

			this.bestMove = bestMove;
			return saveEval(tpos, min, bestMove);
		}
	}
	
	private float saveEval(Transposition tpos, float eval, T bestMove) {
		cachedResults.put(tpos, new EV(bestMove, eval));
		return eval;
	}
	
}
