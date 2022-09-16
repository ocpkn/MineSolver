package minesweeper;

import java.util.Scanner;

public class MSRunner {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		do {
			MSBoard test = new MSBoard(16, 30, 99);
			System.out.println(test);
			
			System.out.print("Row:");
			int fr = Integer.parseInt(sc.next());

			System.out.print("Column:");
			int fc = Integer.parseInt(sc.next());

			test.firstMove(fr, fc);

			while (test.gameWL() == 0) {
				// TODO for testing
//				while (MineSolver.solve(test))
//					;
				
				System.out.println(test);

				System.out.print("Move:");
				String move = sc.next();

				System.out.print("Row:");
				int r = Integer.parseInt(sc.next());

				System.out.print("Column:");
				int c = Integer.parseInt(sc.next());

				test.action(move, r, c);
			}
			System.out.println(test.mineKeyToString());
			
			System.out.println((test.gameWL() == 2 ? "You Win!" : "You Lose!") + "\n\nPlay Again? y/n");

		} while (sc.next().equals("y"));
		sc.close();
	}

}