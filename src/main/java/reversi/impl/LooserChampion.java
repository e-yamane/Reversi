package reversi.impl;

import reversi.Board;
import reversi.BoardState;
import reversi.Player;

import java.awt.*;
import java.util.List;

public class LooserChampion extends Player {

	public LooserChampion(String name) {
		super(name);
	}

	private static int[][] boardPoints = {
		{-1, -99, -3, -4, -4, -3, -99, -1,},
		{-99, -999, -9, -9, -9, -9, -999, -99,},
		{-3, -9, -2, -2, -2, -2, -9, -3,},
		{-4, -9, -2, 0, 0, -2, -9, -4,},
		{-4, -9, -2, 0, 0, -2, -9, -4,},
		{-3, -9, -2, -2, -2, -2, -9, -3,},
		{-99, -999, -9, -9, -9, -9, -999, -99,},
		{-1, -99, -3, -4, -4, -3, -99, -1,},
	};

	private int step = 1;
	@Override
	public Point nextPoint(Board board, BoardState state) {
		System.out.println(step + "目");
		List<Point> nextPoints = board.getAvailablePoints(state);
	
		int beforeCount = 100;
		Point bestPoint = nextPoints.get(0);
		for (Point p : nextPoints) {
			Board stubBoard = board.clone();
			stubBoard.put(p, state);
			if (step > 6) {
				int count = stubBoard.getAvailablePoints(state.reverse()).size();
				if (beforeCount > count) {
					beforeCount = count;
					if (getAxisPoint(p.x, p.y) < 0) {
						continue;
					}
					bestPoint = p;
				}
			
				if (beforeCount == count) {
					bestPoint = selectPoint(bestPoint, p);
				}
			} else {
				bestPoint = selectPoint(bestPoint, p);
			}
		}
	
		step++;
		return bestPoint;
	}

	private Point selectPoint(Point bestPoint, Point p) {
		int p1 = getAxisPoint(bestPoint.x, bestPoint.y);
		int p2 = getAxisPoint(p.x, p.y);
	
		return (p1 > p2) ? bestPoint : p;
	}

	public static int getAxisPoint(int x, int y) {
		return boardPoints[y][x];
	}
}
