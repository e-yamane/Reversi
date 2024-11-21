package reversi.impl

import reversi.{Board, BoardState, Player}

import java.awt.Point
import scala.jdk.CollectionConverters._

class GreatChampion(name: String) extends Player(name){

  private val boardPoints: Array[Array[Int]] = Array(Array(10, -(4), 5, 0, 0, 5, -(4), 10), Array(-(4), -(5), 5, 5, 5, 5, -(5), -(4)), Array(5, 5, 9, 0, 0, 9, 5, 5), Array(0, 5, 0, 0, 0, 0, 5, 0), Array(0, 5, 0, 0, 0, 0, 5, 0), Array(5, 5, 9, 5, 5, 9, 5, 5), Array(-(4), -(5), 0, 0, 0, 0, -(5), -(4)), Array(10, -(4), 5, 0, 0, 5, -(4), 10))

  private var step: Int = 1

  override def nextPoint(board: Board, state: BoardState): Point = {
    System.out.println(step + "目")
    val nextPoints = board.getAvailablePoints(state)
    var beforeCount: Int = 100
    var bestPoint: Point = nextPoints.get(0)
    for (p <- nextPoints.asScala) {
      val stubBoard: Board = board.clone
      stubBoard.put(p, state)
      if (step > 6) {
        val count: Int = stubBoard.getAvailablePoints(state.reverse).size
        var skip = false
        if (beforeCount > count) {
          beforeCount = count
          if (getAxisPoint(p.x, p.y) < 0) {
            skip = true
          } else {
            bestPoint = p
          }
        }
        if (!skip && beforeCount == count) {
          bestPoint = selectPoint(bestPoint, p)
        }
      } else {
        bestPoint = selectPoint(bestPoint, p)
      }
    }
    step += 1
    bestPoint
  }

  private def selectPoint(bestPoint: Point, p: Point): Point = {
    val p1: Int = getAxisPoint(bestPoint.x, bestPoint.y)
    val p2: Int = getAxisPoint(p.x, p.y)
    if (p1 > p2) {
      bestPoint
    }
    else {
      p
    }
  }

  def getAxisPoint(x: Int, y: Int): Int = {
    boardPoints(y)(x)
  }
}
