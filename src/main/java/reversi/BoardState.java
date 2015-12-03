package reversi;

public enum BoardState {
	EMPTY,
	WHITE,
	BLACK;

	public BoardState reverse() {
		switch(this) {
		case EMPTY:
			throw new IllegalStateException();
		case WHITE:
			return BLACK;
		case BLACK:
			return WHITE;
		}
		throw new IllegalStateException();
	}
}
