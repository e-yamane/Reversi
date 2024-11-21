package reversi.impl

import java.awt.Point

import reversi.Board
import reversi.BoardState
import reversi.Reversi

class SimpleScalaPlayer(name: String) extends GreatChampion(name) {
  override def nextPoint(board: Board, state: BoardState): Point = {
    board.getAvailablePoints(state).get(0)
  }
}

object SimpleScalaPlayer {
  def main(args: Array[String]): Unit = {
    val r = new Reversi(new SimpleScalaPlayer("黒"), new SimpleScalaPlayer("黒"))
    r.fight()
  }
}

