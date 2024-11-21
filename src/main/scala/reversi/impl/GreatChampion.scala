package reversi.impl

import reversi.{Board, BoardState, Player}

import java.awt.Point
import scala.jdk.CollectionConverters._

class GreatChampion(name: String) extends Player(name){

  private val boardPoints: Array[Array[Int]] = Array(Array(10, -(4), 5, 0, 0, 5, -(4), 10), Array(-(4), -(5), 5, 5, 5, 5, -(5), -(4)), Array(5, 5, 9, 0, 0, 9, 5, 5), Array(0, 5, 0, 0, 0, 0, 5, 0), Array(0, 5, 0, 0, 0, 0, 5, 0), Array(5, 5, 9, 5, 5, 9, 5, 5), Array(-(4), -(5), 0, 0, 0, 0, -(5), -(4)), Array(10, -(4), 5, 0, 0, 5, -(4), 10))

  override def nextPoint(board: Board, state: BoardState): Point = {
    hoge(board, state, 8)
  }

  private def hoge(board: Board, state: BoardState, depth: Int): Point = {
    def walk(board: Board, state: BoardState, depth: Int): (Point, Int) = {
      // その状態の中で一番いい状態を返したい
      val points = board.getAvailablePoints(state).asScala.toList
      points.map {p =>
        // そのポイントを選んだ時に一番いい状態を返したい
        if (depth == 0) {
          (p, getAxisPoint(p.x, p.y))
        } else {
          val stubBoard: Board = board.clone
          stubBoard.put(p, state)
          p -> walk(stubBoard, state.reverse, depth - 1)._2
      }
      }.reduceLeft((a, b) => if (a._2 > b._2) a else b)
    }
    walk(board, state, depth)._1
  }

  def getAxisPoint(x: Int, y: Int): Int = {
    boardPoints(y)(x)
  }
}
