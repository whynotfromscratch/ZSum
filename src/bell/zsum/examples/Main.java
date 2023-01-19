package bell.zsum.examples;

import bell.zsum.Evaluator;

public class Main {

	public static void main(String[] args) {
		int n = 100;
		NimGame game = new NimGame(n);
		Evaluator<Integer> e = new Evaluator<>();

		if(e.evaluate(game,n) == Float.POSITIVE_INFINITY) {
		    System.out.println("Player 1 Wins");
		} else {
		    System.out.println("Player 2 Wins");
		}

		System.out.println(e.getBestLine(game,n,n));
	}
	
}
