package minesweeper;

import java.text.DecimalFormat;
import java.util.Scanner;

public class MineSolverRunner  {

	public static void main(String[] args) {
		int wins = 0, losses = 0, stuck = 0;

		System.out.print("Number of trials: ");
		Scanner sc = new Scanner(System.in);
		int trials = Integer.parseInt(sc.next());
		sc.close();
		
		System.out.println();
		
		long start = System.currentTimeMillis();
		System.out.print("\r0%");
		for (int i = 0; i < trials; i++) {
			MSBoard test = new MSBoard(16, 30, 99);

			test.firstMove(test.getLength() / 2, test.getWidth() / 2);

			while (test.gameWL() == 0) {

				while (MineSolver.solve(test))
					;
			}
			switch (test.gameWL()) {
			case 0:
				stuck++;
				break;
			case 1:
				losses++;
				break;
			case 2:
				wins++;
				break;
			}
			if (trials > 99 && (i + 1) % (trials / 100) == 0)
				System.out.print("\r" + (i+1) / (trials/100) + "%");
		}
		long finish = System.currentTimeMillis();
		double timeElapsed = finish - start;
		
		System.out.println("\n\nWins: " + wins + "\nLosses: " + losses + "\nStuck: " + stuck);
		
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("\n" + timeElapsed/1000.0 + " seconds\n" + 
		timeElapsed / trials + " ms/game\n" +
		df.format(trials / (timeElapsed / 1000.0)) + " games/s");
	}
}
