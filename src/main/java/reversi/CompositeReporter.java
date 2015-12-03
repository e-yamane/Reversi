package reversi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import reversi.Reversi.Result;

public class CompositeReporter implements Reporter {
	private final List<Reporter> reporters;
	public CompositeReporter() {
		this(new ArrayList<Reporter>());
	}
	public CompositeReporter(Reporter... reporters) {
		this(Arrays.asList(reporters));
	}
	
	public CompositeReporter(List<Reporter> reporters) {
		this.reporters = new ArrayList<Reporter>(reporters);
	}
	
	@Override
	public void finish(Result result) {
		for(Reporter r: reporters) {
			r.finish(result);
		}
	}

	@Override
	public void put(Board board, int number, Player player, Point p) {
		for(Reporter r: reporters) {
			r.put(board, number, player, p);
		}
	}

	@Override
	public void skip(Board board, Player skipper) {
		for(Reporter r: reporters) {
			r.skip(board, skipper);
		}
	}

	@Override
	public void start(Board board, Player black, Player white) {
		for(Reporter r: reporters) {
			r.start(board, black, white);
		}
	}

}
