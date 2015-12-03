package reversi.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import reversi.CompositeReporter;
import reversi.Reversi;

public class WebUIPlayer extends MinaPlayerServer {
	protected WebUIPlayer(int port) {
		super(port);
	}

	protected String getContentType() {
		return "text/html;charset=UTF-8";
	}

	@Override
	protected String getBodyData() {
		try {
			String json = super.getBodyData();
			InputStream is = this.getClass().getResourceAsStream("reversi.html");
			String base = IOUtils.toString(is, "UTF-8");
			String ret = String.format(base, json);
			return ret;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		MinaPlayerServer black = new WebUIPlayer(8088);
		MinaPlayerServer white = new WebUIPlayer(8089);
		Reversi r = new Reversi(black, Long.MAX_VALUE, white, Long.MAX_VALUE, 
				new CompositeReporter(black.getReporter(), white.getReporter()));
		r.fight();
	}
}
