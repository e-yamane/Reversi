package reversi;

import java.awt.Point;

public interface Reporter {
	public void start(Board board, Player black, Player white);
	public void skip(Board board, Player skipper);
	public void put(Board board, int number, Player player, Point p);
	public void finish(Reversi.Result result);
}
