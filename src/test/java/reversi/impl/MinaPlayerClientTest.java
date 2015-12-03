package reversi.impl;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class MinaPlayerClientTest extends TestCase {
	public void testGetURL() throws Exception {
		MinaPlayerClient client1 = MinaPlayerClient.getInstance(new SimplePlayer("abc"), "http://localhost:8088");
		assertEquals("http://localhost:8088/?name=abc", client1.getURL(null).toString());
		assertEquals("http://localhost:8088/?name=abc&x=5&y=7", client1.getURL(new Point(5, 7)).toString());
		MinaPlayerClient client2 = MinaPlayerClient.getInstance(new SimplePlayer("‚Ù‚°"), "http://localhost:8088/");
		assertEquals("http://localhost:8088/?name=%E3%81%BB%E3%81%92", client2.getURL(null).toString());
		assertEquals("http://localhost:8088/?name=%E3%81%BB%E3%81%92&x=3&y=1", client2.getURL(new Point(3, 1)).toString());
	}
	
	@SuppressWarnings("unchecked")
	public void testGetPOJO() throws Exception {
		Map map = MinaPlayerClient.jsonToMap(JSON);
		assertEquals(1, ((Number)map.get("your")).intValue());
		assertNull(map.get("result"));
		List<Integer[]> board = (List<Integer[]>)map.get("board");
		assertEquals(8, board.size());
	}

	final static String JSON = 
		"{\n" +
		"your: 1,\n" +
		"board:[\n" +
		"[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		",[ 0, 0, 0,-1, 1, 0, 0, 0]\n" +
		",[ 0, 0, 0, 1,-1, 0, 0, 0]\n" +
		",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		",[ 0, 0, 0, 0, 0, 0, 0, 0]\n" +
		"]}\n";
}
