package reversi.impl;

import java.awt.Point;
import java.util.Map;

import junit.framework.TestCase;
import reversi.Board;
import reversi.BoardState;
import reversi.DebugBoard;
import reversi.Player;
import reversi.Reversi;

public class MinaPlayerServerTest extends TestCase {
	public void testToString() {
		Player black = new Player("ほげ"){
			@Override
			public Point nextPoint(Board clone, BoardState state) {
				return null;
			}
			
		};
		Player white = new SimplePlayer("ぽげ");
		DebugBoard db = new DebugBoard();
		String ret1 = MinaPlayerServer.toString(db, BoardState.BLACK, null, black, white);
		final String result1 = 
			"{\n" +
			"your: 1,\n" +
			"black:\"ほげ\",\n" +
			"white:\"ぽげ\",\n" +
			"board:[\n" +
			"[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0,-1, 1, 0, 0, 0]\n" +
			",[ 0, 0, 0, 1,-1, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			"]}";
		assertEquals(result1, ret1);
		Reversi r = new Reversi(black, white);
		String ret2 = MinaPlayerServer.toString(db, BoardState.BLACK, r.fight(), black, white);
		final String result2 = 
			"{\n" +
			"result:-1,\n" + 
			"your: 1,\n" +
			"black:\"ほげ\",\n" +
			"white:\"ぽげ\",\n" +
			"board:[\n" +
			"[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0,-1, 1, 0, 0, 0]\n" +
			",[ 0, 0, 0, 1,-1, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
			"]}";
		assertEquals(result2, ret2);
	}

	public void testGetParams() {
		Map<String, String> map = MinaPlayerServer.getParams("/?widthUI=&x=2&y=0");
		assertEquals(3, map.size());
	}
	
	public void testToPoint() {
		assertNull(MinaPlayerServer.toPoint(MinaPlayerServer.getParams("/")));
		assertEquals(new Point(2, 0), MinaPlayerServer.toPoint(MinaPlayerServer.getParams("/?x=2&y=0")));
	}
}
