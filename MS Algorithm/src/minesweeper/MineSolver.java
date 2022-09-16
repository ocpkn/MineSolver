package minesweeper;

// 9/15/21 Functional iteration of findSafeTile()
// Stats for expert field (30 x 16, 99) 100,000 trials
// Wins: 32179
// Losses: 67821
// ~32% win
//
// Guesses: 183966
// Correct guesses: 116145
// Wrong guesses: 67821
// ~63% accurate
//
// ~1.8 guesses per game on average
//
// ~145.56 games/second

// 9/17/21 First functional iteration of deepFindSafeTile()
// Stats for expert field (30 x 16, 99) 50,000 trials
// Wins: 16328
// Losses: 33672
// ~32% win
//
// Guesses: 91190
// Correct guesses: 57518
// Wrong guesses: 33672
// ~63% accurate
//
// ~1.8 guesses per game on average
//
// ~84.5 games/second
//
// This iteration of deepFindSafeTile has negligable increase in win rate, but
// ***SIGNIFICANT*** decrease in speed. Optimization needed to be viable.

public class MineSolver {
	public static boolean solve(MSBoard board) {
		int[] move = calcMove(board);
		if (move != null) {
			if (move[0] == 0)
				board.action("fa", move[1], move[2]);
			else
				board.action("ca", move[1], move[2]);
			//System.out.println(board);
			return true;
		}
		move = findSafeTile(board);
		if (move != null) {
			board.action("ct", move[0], move[1]);
			//System.out.println(board);
			return true;
		}
		move = guessSafeTile(board);
		if (move != null) {
			board.action("ct", move[0], move[1]);
			//System.out.println(board);
			return board.gameWL() == 0;
		}
		
		return false;
	}

	private static boolean simpleSolve(MSBoard board) {
		int[] move = calcMove(board);
		if (move != null) {
			if (move[0] == 0)
				board.action("fa", move[1], move[2]);
			else
				board.action("ca", move[1], move[2]);
			return true;
		}
		return false;
	}

	private static int[] calcMove(MSBoard board) {
		for (int r = 0; r < board.getLength(); r++)
			for (int c = 0; c < board.getWidth(); c++) {
				int tile = board.getTile(r, c);
				if (tile > 0 && tile == board.adjUncleared(r, c) && board.adjEmpty(r, c) > 0) {
					return new int[] { 0, r, c };
				}
				if ((tile > 0 && tile == board.adjFlags(r, c) && board.adjEmpty(r, c) > 0)) {
					return new int[] { 1, r, c };
				}
			}
		return null;
	}

	private static boolean isValid(MSBoard clone) {
		if (clone.mines() - clone.flags() < 0)
			return false;
		for (int r = 0; r < clone.getLength(); r++)
			for (int c = 0; c < clone.getWidth(); c++)
				if (clone.getTile(r, c) > 0 && (clone.getTile(r, c) < clone.adjFlags(r, c)
						|| clone.getTile(r, c) > clone.adjUncleared(r, c)))
					return false;

		return true;
	}

	private static int[] findSafeTile(MSBoard board) {
		if (board.gameWL() > 0)
			return null;

		for (int r = 0; r < board.getLength(); r++)
			for (int c = 0; c < board.getWidth(); c++)
				if (board.getTile(r, c) == -1 && board.adjCleared(r, c) > 0) {
					MSBoard clone = new MSBoard(board);
					clone.action("ft", r, c);

					while (simpleSolve(clone))
						;

					if (!isValid(clone))
						return new int[] { r, c };
				}
		return null;
	}

	// First iteration of guessSafeTile. Adds fractions for each blank tile, chooses
	// lowest
	// correct about 63% of the time
	public static int[] guessSafeTile(MSBoard board) {
		int minR = -1, minC = -1;
		double minChance = 100.0;

		for (int r = 0; r < board.getLength(); r++)
			for (int c = 0; c < board.getWidth(); c++)
				if (board.getTile(r, c) == -1 && board.adjUncleared(r, c) > 0) {
					double chance = 0;
					for (int[] d : MSBoard.adj) {
						int i = r + d[0], j = c + d[1];
						if (board.isValidCoord(i, j))
							chance += (double) (board.getTile(i, j) - board.adjFlags(i, j)) / board.adjEmpty(i, j);
					}
					if (chance < minChance) {
						minChance = chance;
						minR = r;
						minC = c;
					}
				}
		if (minChance < 100.0)
			return new int[] { minR, minC };

		return null;
	}
}