package minesweeper;

public class MSBoard {
	private int[][] board;
	private boolean[][] mineKey;

	private int mines;
	private int flags = 0;
	private int cleared = 0;
	boolean gameLost = false;

	public static int[][] adj = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 },
			{ -1, -1 } };

	// Constructors

	public MSBoard(int r, int c, int m) {
		mines = m;
		board = new int[r][c];
		mineKey = new boolean[r][c];
		// filling board with blanks
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++)
				board[i][j] = -1;
	}

	public MSBoard(MSBoard b) {
		board = b.clone();
		mines = b.mines();
		cleared = b.cleared();
		flags = b.flags();
	}

	public int[][] clone() {
		int[][] copy = new int[board.length][board[0].length];

		for (int r = 0; r < board.length; r++)
			copy[r] = board[r].clone();

		return copy;
	}

	// Get methods

	public int getLength() {
		return board.length;
	}
	
	public int getWidth() {
		return board[0].length;
	}

	public boolean isValidCoord(int i, int j) {
		return i >= 0 && j >= 0 && i < board.length && j < board[0].length;
	}

	public int getTile(int r, int c) {
		if (isValidCoord(r, c))
			return board[r][c];
		return -10;
	}
	
	public int getTile(int[] i) {
		if (isValidCoord(i[0], i[1]))
			return board[i[0]][i[1]];
		return -10;
	}
	
	public int mines() {
		return mines;
	}

	public int flags() {
		return flags;
	}

	public int cleared() {
		return cleared;
	}

	// Win/Lose check (0 = none, 1 = loss, 2 = win)

	public int gameWL() {
		if (mineKey == null)
			return -1;
		if (gameLost) 
			return 1;
		
		int ret = 2;
		
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[0].length; c++)
				if (board[r][c] == -1 && !mineKey[r][c])
					return 0;

		return ret;
	}

	// Adj checks

	private int adjMines(int r, int c) {
		int mines = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && mineKey[i][j])
				mines++;
		}

		return mines;
	}

	public int adjEmpty(int r, int c) {
		int empty = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && board[i][j] == -1)
				empty++;
		}

		return empty;
	}

	public int adjCleared(int r, int c) {
		int cl = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && (board[i][j] > 0 || board[i][j] == -4))
				cl++;
		}

		return cl;
	}
	
	public int trueAdjCleared(int r, int c) {
		int cl = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && (board[i][j] > 0))
				cl++;
		}

		return cl;
	}
	
	public int adjUncleared(int r, int c) {
		int uc = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && (board[i][j] == -1 || board[i][j] == -2))
				uc++;
		}

		return uc;
	}

	public int adjFlags(int r, int c) {
		int flags = 0;

		for (int[] d : MSBoard.adj) {
			int i = r + d[0], j = c + d[1];
			if (isValidCoord(i, j) && board[i][j] == -2)
				flags++;
		}

		return flags;
	}

	// toString methods

	public String toString() {
		int ufM = mines - flags();
		String ret = ufM > 9 ? " " + (ufM > 99 ? ufM > 999 ? ufM + "  " : " " + ufM + "  " : " " + ufM + "   ")
				: "   " + ufM + "   ";

		for (int c = 0; c < board[0].length; c++)
			ret += (c > 9 ? c + "  " : "0" + c + "  ");

		ret += "\n";

		for (int r = 0; r < board.length; r++) {
			// Horizontal border
			ret += "     ";

			for (int c = 0; c < board[0].length; c++)
				ret += " ---";

			ret += "\n" + (r > 9 ? "  " + r : "  0" + r) + " | ";
			// Printing board contents
			for (int c = 0; c < board[0].length; c++) {

				switch (board[r][c]) {
				case -1:
					ret += " ";
					break;
				case -2:
					ret += "X";
					break;
				case -3:
					ret += "@";
					break;
				case -4:
					ret += "?";
					break;
				default:
					ret += board[r][c];
				}

				ret += " | ";
			}

			ret += "\n";
		}
		// Bottom border
		ret += "     ";

		for (int c = 0; c < board[0].length; c++)
			ret += " ---";

		ret += "\n";

		return ret;
	}

	public String mineKeyToString() {
		if (gameWL() == 0)
			return "";
		
		int ufM = mines - flags();
		String ret = ufM > 9 ? " " + (ufM > 99 ? ufM > 999 ? ufM + "  " : " " + ufM + "  " : " " + ufM + "   ")
				: "   " + ufM + "   ";

		for (int c = 0; c < board[0].length; c++)
			ret += (c > 9 ? c + "  " : "0" + c + "  ");

		ret += "\n";

		for (int r = 0; r < board.length; r++) {
			// Horizontal border
			ret += "     ";

			for (int c = 0; c < board[0].length; c++)
				ret += " ---";

			ret += "\n" + (r > 9 ? "  " + r : "  0" + r) + " | ";
			// Printing board contents
			for (int c = 0; c < board[0].length; c++) {

				switch (board[r][c]) {
				case -1:
					ret += mineKey[r][c] ? "#" : " ";
					break;
				case -2:
					ret += mineKey[r][c] ? "X" : "!";
					break;
				case -3:
					ret += "@";
					break;
				case -4:
					ret += "?";
					break;
				default:
					ret += board[r][c];
				}

				ret += " | ";
			}

			ret += "\n";
		}
		// Bottom border
		ret += "     ";

		for (int c = 0; c < board[0].length; c++)
			ret += " ---";

		ret += "\n";

		return ret;
	}

	// Action methods

	public boolean action(String type, int r, int c) {
		if (isValidCoord(r, c)) {

			switch (type) {
			case "ct":
				return clearTile(r, c);
			case "ca":
				return clearAdj(r, c);
			case "ft":
				return flagTile(r, c);
			case "fa":
				return flagAdj(r, c);
			case "ut":
				return unflagTile(r, c);
			default:
				break;
			}
		}

		return false;
	}

	public boolean firstMove(int r, int c) {
		if (isValidCoord(r, c)) {
			for (int i = 0; i < mines; i++) {

				int randR = (int) (Math.random() * board.length);
				int randC = (int) (Math.random() * board[0].length);

				if ((Math.abs(r - randR) > 1 || Math.abs(c - randC) > 1) && !mineKey[randR][randC])
					mineKey[randR][randC] = true;
				else
					i--;
			}
			clearTile(r, c);
			return true;
		}
		return false;
	}

	private boolean clearTile(int r, int c) {
		if (board[r][c] == -1) {
			if (mineKey == null) {
				board[r][c] = -4;
				cleared++;
			}
			else if (mineKey[r][c]) {
				board[r][c] = -3;
				gameLost = true;
			}
			else {
				board[r][c] = adjMines(r, c);
				cleared++;
				if (board[r][c] == 0)
					clearAdj(r, c);
			}
			return true;
		}

		return false;
	}

	private boolean clearAdj(int r, int c) {
		if (board[r][c] >= 0) {
			for (int[] d : MSBoard.adj) {
				int i = r + d[0], j = c + d[1];
				if (isValidCoord(i, j))
					clearTile(i, j);
			}
			return true;
		}

		return false;
	}

	private boolean flagTile(int r, int c) {
		if (board[r][c] == -1) {
			board[r][c] = -2;
			flags++;
			return true;
		}

		return false;
	}

	private boolean flagAdj(int r, int c) {
		if (board[r][c] > 0) {
			for (int[] d : MSBoard.adj) {
				int i = r + d[0], j = c + d[1];
				if (isValidCoord(i, j))
					flagTile(i, j);
			}
			return true;
		}

		return false;
	}

	private boolean unflagTile(int r, int c) {
		if (board[r][c] == -2) {
			board[r][c] = -1;
			flags--;
			return true;
		}

		return false;
	}
}
