package reversi.impl;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import reversi.Board;
import reversi.BoardState;
import reversi.CompositeReporter;
import reversi.Player;
import reversi.Reporter;
import reversi.Reversi;
import reversi.Reversi.Result;

public class MinaPlayerServer extends Player {
	private final Object listeningMonitor = new Object();
	private final Object responseMonitor = new Object();
	private final Object answerMonitor = new Object();
	private final NioSocketAcceptor acceptor;
	
	protected MinaPlayerServer(int port) {
		super("");
		try {
			acceptor = new NioSocketAcceptor();
			acceptor.setHandler(new Receiver());
			System.out.println("Listening port:" + port);
			acceptor.bind(new InetSocketAddress(port));
			synchronized(listeningMonitor) {
				try {
					listeningMonitor.wait();
				} catch (InterruptedException e) {
				}
			}
		} catch (IOException e1) {
			throw new RuntimeException();
		}
	}
	
	Board board;
	BoardState state;
	Point point;
	Result result;
	Board last;
	Player black;
	Player white;
	String name = "";
	
	@Override
	public String getName() {
		return name;
	}

	public Reporter getReporter() {
		return new Reporter() {
			@Override
			public void finish(Result result) {
				synchronized(responseMonitor) {
					MinaPlayerServer.this.result = result;
					board = last;
					responseMonitor.notifyAll();
				}
			}

			@Override
			public void put(Board board, int number, Player player, Point p) {
				last = board;
			}

			@Override
			public void skip(Board board, Player skipper) {
			}

			@Override
			public void start(Board board, Player black, Player white) {
				MinaPlayerServer.this.black = black;
				MinaPlayerServer.this.white = white;
			}
		};
	}

	@Override
	public Point nextPoint(Board clone, BoardState state) {
		try {
			this.state = state;
			this.board = clone;
			synchronized(responseMonitor) {
				responseMonitor.notifyAll();
			}
            synchronized(answerMonitor) {
            	while(true) {
            		try {
						answerMonitor.wait(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
            		if(point != null) {
            			break;
            		}
            	}
			}
            Point ret = this.point;
			return ret;
		} finally {
			this.board = null;
			this.point = null;
		}
	}

	static String toString(Board board, BoardState state, Result result, Player black, Player white) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		if(result != null) {
			sb.append("result:");
			switch(result.judge()) {
			case WHITE:
				sb.append(toString(BoardState.WHITE));
				break;
			case BLACK:
				sb.append(toString(BoardState.BLACK));
				break;
			default:
				sb.append(toString(BoardState.EMPTY));
				break;
			}
			sb.append(",\n");
		}
		sb.append("your:");
		sb.append(toString(state));
		sb.append(",\n");
		sb.append("black:\"");
		sb.append(black.getName());
		sb.append("\",\n");
		sb.append("white:\"");
		sb.append(white.getName());
		sb.append("\",\n");
		sb.append("board:[\n");
		String lineDelim = "";
		for(int y = 0 ; y < Board.MAX ; y++) {
			sb.append(lineDelim);
			lineDelim=",";
			sb.append("[");
			String colDelim = "";
			for(int x = 0 ; x < Board.MAX ; x++) {
				sb.append(colDelim);
				colDelim=",";
				sb.append(toString(board.getState(new Point(x, y))));
			}
			sb.append("]\n");
		}
		sb.append("]}");
		return sb.toString();
	}
	
    static String toString(BoardState bs) {
    	switch(bs) {
    	case BLACK:
    		return " 1";
    	case WHITE:
    		return "-1";
    	case EMPTY:
    		return " 0";
    	}
    	throw new RuntimeException();
    }

    static BoardState toBoardState(int state) {
    	switch(state) {
    	case 1:
    		return BoardState.BLACK;
    	case -1:
    		return BoardState.WHITE;
    	case 0:
    		return BoardState.EMPTY;
    	}
    	throw new RuntimeException();
    }
    
    static Point toPoint(Map<String, String> params) {
		if(params.containsKey("x") && params.containsKey("y")) {
			int x = Integer.parseInt(params.get("x"));
			int y = Integer.parseInt(params.get("y"));
			return new Point(x, y);
		} else {
			return null;
		}
    }
    
	static Map<String, String> getParams(String url) {
		try {
	    	final Pattern p = Pattern.compile("/\\?(.+)");
	    	Matcher m = p.matcher(url);
			Map<String, String> map = new HashMap<String, String>();
	    	if(m.matches()) {
	    		String values = m.group(1);
	    		for(String entry : values.split("&")) {
	        		String[] nameValue = entry.split("=");
	        		if(nameValue.length == 1) {
	        			map.put(nameValue[0], "");
	        		} else {
						map.put(nameValue[0], URLDecoder.decode(nameValue[1], "UTF-8"));
	        		}
	    		}
	    	}
	    	return map;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected String getContentType() {
		return "text/json";
	}
	
	protected String getBodyData() {
		return MinaPlayerServer.toString(board, state, result, black, white);
	}
	
	public class Receiver extends StreamIoHandler {
		private IoSession session;
	    @Override
	    protected void processStreamIo(IoSession session, InputStream in, OutputStream out) {
	    	this.session = session;
			synchronized (listeningMonitor) {
				listeningMonitor.notifyAll();
			}
	        new Worker(in, out).start();
	    }

	    private class Worker extends Thread {
	        private final InputStream in;

	        private final OutputStream out;

	        public Worker(InputStream in, OutputStream out) {
	            setDaemon(true);
	            this.in = in;
	            this.out = out;
	        }

	        @Override
	        public void run() {
	            String url;
	            BufferedReader in = new BufferedReader(new InputStreamReader(this.in));
	            try {
		            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.out, "UTF-8")));
	                url = in.readLine().split(" ")[1];
	                System.out.println(url);
	                Map<String, String> params = getParams(url);
	                if(params.containsKey("name")) {
	                	MinaPlayerServer.this.name = (String)params.get("name");
	                }
	                point = toPoint(params);
	                synchronized(answerMonitor) {
	                	answerMonitor.notifyAll();
					}
	                synchronized(responseMonitor) {
	                	while(true) {
	                		responseMonitor.wait(100);
	                		if(board != null) {
	                			break;
	                		}
	                	}
					}
	                // Write header
	                out.println("HTTP/1.0 200 OK");
	                out.println("Content-Type: " + getContentType());
	                out.println("Server: MINA Example");
	                out.println();

	                // Write content
	                out.println(getBodyData());
	                out.flush();
	                out.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                }
	                if(result != null) {
	                	session.close(true);
	                	acceptor.dispose(true);
	                }
	            }
	        }
	    }
	}

	public static void main(String[] args) {
		while(true) {
			MinaPlayerServer black = new MinaPlayerServer(8088);
			MinaPlayerServer white = new MinaPlayerServer(8089);
			Reversi r = new Reversi(black, white, 
					new CompositeReporter(black.getReporter(), white.getReporter(), new SwingReporter()));
			System.out.println(r.fight());
		}
	}
}
