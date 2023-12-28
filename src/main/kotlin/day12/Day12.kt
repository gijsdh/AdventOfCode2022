import java.util.*
import kotlin.math.min

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputexample = getResourceAsText("inputExample.txt")
    val heightMap = input.lines().map { it.split("").filter { it.isNotEmpty() }.toTypedArray() }.toTypedArray()

    var start: Pair<Int, Int> = Pair(0,0)
    var finish: Pair<Int, Int> = Pair(0,0)

    for (i in 0 until  heightMap.size) {
        for (j in 0 until  heightMap[1].size) {
            if(heightMap[i][j] == "S") start = Pair(i,j)
            if(heightMap[i][j] == "E") finish = Pair(i,j)
        }
    }

    println(findMinimumCostPath(heightMap, start, finish))
    var list = mutableListOf<Long>()

    for (i in 0 until  heightMap.size) {
        for (j in 0 until  heightMap[1].size) {
              if(heightMap[i][j] == "a") list.add(findMinimumCostPath(heightMap, Pair(i,j), finish))
        }
    }
    println(list.filter { it < 1000 })
    println(list.min())

}

private fun printNumber(numbers: Array<Array<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun printNumber(numbers: Array<LongArray>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun findMinimumCostPath(maze: Array<Array<String>>, start: Pair<Int, Int>, finish: Pair<Int, Int>): Long {
    var visted = Array(maze.size) { BooleanArray(maze[0].size) { false } }
    var costMap = Array(maze.size) { LongArray(maze[0].size) { 9999999999 } }

    val comparByCost: Comparator<Cost> = compareBy { it.costValue }
    val costQueue = PriorityQueue<Cost>(comparByCost)


    //Initialize start
    costQueue.add(Cost(0, 0, 0))
    costMap[start.first][start.second] = 0L
    maze[start.first][start.second]="a"
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)

    while (costQueue.isNotEmpty()) {
        val cell: Cost = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j

        if (visted[i][j]) continue
        visted[i][j] = true

        for (k in ith.indices) {
            val index_I = i + ith[k]
            val index_J = j + jth[k]

            if (isValidIndex(index_I, index_J, maze.size, maze[0].size) && !visted[index_I][index_J]
                && isValidMove(maze, index_I, index_J, i, j)) {
                if (maze[index_I][index_J] == "E") return costMap[i][j] + 1
                costMap[index_I][index_J] =
                    min(costMap[index_I][index_J], costMap[i][j] + 1)
                costQueue.add(Cost(index_I, index_J, costMap[index_I][index_J]))
            }
        }
    }
    return -1
}

private fun isValidMove(
    maze: Array<Array<String>>,
    index_I: Int,
    index_J: Int,
    i: Int,
    j: Int
) = (maze[index_I][index_J][0].code <= maze[i][j][0].code + 1 && maze[index_I][index_J] != "E")
            || (maze[i][j] == "z" && maze[index_I][index_J] == "E")
// Can only one level up, or any lvl down. As E has a lower int value, make an exception for E.
// Again do make sure it possible to walk from z to E.

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}
class Cost(val index_i: Int, val index_j: Int, val costValue: Long) {

}


