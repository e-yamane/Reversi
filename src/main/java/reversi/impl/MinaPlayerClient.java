package reversi.impl;

import java.awt.Point;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.IOUtils;

import reversi.Board;
import reversi.BoardState;
import reversi.DebugBoard;
import reversi.Player;

public class MinaPlayerClient {
	private final Player player;
	private final String url;
	protected MinaPlayerClient(Player player, String url) {
		this.player = player;
		this.url = url;
	}
	
	public static MinaPlayerClient getInstance(Player player, String url) {
		return new MinaPlayerClient(player, url);
	}
	
	public final BoardState start() {
		return start(null);
	}

	@SuppressWarnings("unchecked")
	private BoardState start(Point p) {
		while(true) {
			try {
				URL url = getURL(p);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				String body = IOUtils.toString(con.getInputStream(), "UTF-8");
				System.out.println(body);
				Map map = jsonToMap(body);
				if(map.containsKey("result")){
					return MinaPlayerServer.toBoardState(((Number)map.get("result")).intValue());
				}
				BoardState your = MinaPlayerServer.toBoardState(((Number)map.get("your")).intValue());
				Board board = toBoard((List<List<Number>>)map.get("board"));
				p = player.nextPoint(board, your);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
	}

	private Board toBoard(List<List<Number>> board) {
		DebugBoard b = new DebugBoard();
		for(int y = 0 ; y < board.size() ; y++) {
			List<Number> row = board.get(y);
			for(int x = 0 ; x < row.size() ; x++) {
				b.setState(new Point(x, y), MinaPlayerServer.toBoardState(row.get(x).intValue()));
			}
		}
		return b.clone();
	}

	URL getURL(Point p) {
		try {
			StringBuilder sb = new StringBuilder(url);
			if(!url.endsWith("/")) {
				sb.append('/');
			}
			sb.append("?name=");
			sb.append(URLEncoder.encode(player.getName(), "UTF-8"));
			if(p != null) {
				sb.append("&x=");
				sb.append(p.x);
				sb.append("&y=");
				sb.append(p.y);
			}
			return new URL(sb.toString());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	static Map jsonToMap(String json) {
		return (Map)JSON.decode(json);
	}
	
	public static void main(String[] args) throws Exception {
		Thread blackT = new Thread(new Runnable() {
			@Override
			public void run() {
				MinaPlayerClient player = MinaPlayerClient.getInstance(new SimplePlayer("ほげブラック"), "http://localhost:8088");
				System.out.println(player.start());
			}
		});
		blackT.start();
		MinaPlayerClient player = MinaPlayerClient.getInstance(new BeginerPlayer("ぽげホワイト"), "http://localhost:8089");
		System.out.println(player.start());
	}
}
