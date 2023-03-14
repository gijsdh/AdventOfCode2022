import java.lang.Math.abs
import java.util.*


var blizzardMap: MutableMap<Int, Array<Array<String>>> = mutableMapOf()

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val parsedInput = input.lines()
        .map { it.split("")
            .filter { it.isNotEmpty() } }
        .map { it.toTypedArray() }
        .toTypedArray()

    blizzardMap[0] = parsedInput
    println(parsedInput.flatMap {it.asIterable()}.count{it == ">" || it == "<" || it == "v" || it == "^" })


    println("${parsedInput.size-1}, ${parsedInput[0].size - 2}")
    var time = findMinimumCostPath(Pair(0,1),Pair(parsedInput.size-1, parsedInput[0].size - 2), 0)
//    printNicely(blizzardMap[time-1]!!)
    println("answer A: $time")
    var time2 = findMinimumCostPath(Pair(parsedInput.size-1, parsedInput[0].size - 2),Pair(0,1), time)
//    printNicely(blizzardMap[time2-1]!!)
    println(time2)
    var time3 = findMinimumCostPath(Pair(0,1),Pair(parsedInput.size-1, parsedInput[0].size - 2), time2)

//    printNicely(blizzardMap[time3-1]!!)
    println("Answer B: $time3")
}


// Minimum path with distance heuristic, so the cost is the cost of the most ideal path.
// This works as the path is uncertain due to the moving storm over the map.
private fun findMinimumCostPath(start: Pair<Int, Int>, finish: Pair<Int, Int>, time: Int): Int {
    var visted = mutableSetOf<Pair<Int, Pair<Int, Int>>>()

    val comparByCost: Comparator<State> = compareBy { it.costValue }
    val costQueue = PriorityQueue<State>(comparByCost)

    //Initialize start
    costQueue.add(State(start.first, start.second, 0, time))

    val ith = intArrayOf(0, 0, 1, -1, 0)
    val jth = intArrayOf(0, 1, 0, 0, -1)

    while (costQueue.isNotEmpty()) {
        val cell: State = costQueue.remove()
        val time = cell.time + 1
        val i: Int = cell.index_i
        val j: Int = cell.index_j

        val newElement = Pair(time, Pair(i, j))
        if (visted.contains(newElement)) continue
        visted.add(newElement)

        var blizzard = getBlizzardMap(time)


        for (k in ith.indices) {
            val index_I = i + ith[k]
            val index_J = j + jth[k]

            if (isValidIndex(index_I, index_J, blizzard)) {
                if (index_I == finish.first && index_J == finish.second) return cell.costValue
                val newCostValue = heuristicDistance(Pair(index_I, index_J), finish) + time

                costQueue.add(State(index_I, index_J, newCostValue, time))
            }
        }
    }
    return -1
}

fun isValidIndex(indexI: Int, indexJ: Int, blizzard: Array<Array<String>>): Boolean {
    if(!isValidIndex(indexI, indexJ, blizzard.size, blizzard[0].size)) return false

    if (blizzard[indexI][indexJ] == ".") {
        return true
    }

    return false
}

fun getBlizzardMap(time: Int): Array<Array<String>> {
    return blizzardMap[time] ?: return moveBlizzardInTime(time)
}

fun moveBlizzardInTime(time: Int): Array<Array<String>> {
    var oldMap: Array<Array<String>> = blizzardMap[time - 1]!!
    var newMap: Array<Array<String>> = Array(oldMap.size) { Array(oldMap[0].size) { "" } }
    for (i in 0 until oldMap.size ) {
        for (j in 0 until oldMap[0].size ) {
            val element = oldMap[i][j]
            val position = Pair(i, j)
            for (direction in element) {
                if(direction == '.') continue
                var newIndex = moveStormElement(direction, position, oldMap.size, oldMap[0].size)
                newMap[newIndex.first][newIndex.second] = newMap[newIndex.first][newIndex.second] + direction
            }
        }
    }
    //If a map entry is empty we put a dot there.
    newMap = newMap.map { it.map { it.ifEmpty { "." } }.toTypedArray() }.toTypedArray()

    blizzardMap[time] = newMap
    return newMap
}


fun moveStormElement(direction: Char, position: Pair<Int, Int>, sizeY: Int, sizeX: Int): Pair<Int, Int> {
    return when (direction) {
        '^' -> applyBoundaryCondition(position.first - 1, position.second, sizeY, sizeX, false)
        'v' -> applyBoundaryCondition(position.first + 1, position.second, sizeY, sizeX, false)
        '>' -> applyBoundaryCondition(position.first, position.second + 1, sizeY, sizeX, true)
        '<' -> applyBoundaryCondition(position.first, position.second - 1, sizeY, sizeX, true)
        '#' -> Pair(position.first, position.second)
        else -> throw Exception("shit")
    }
}


fun printNicely(storm: Array<Array<String>>) {
    println()
    for (i in 0 until storm.size) {
        for (j in 0 until storm[0].size) {
            val element = storm[i][j]
            if (element.length > 1) print(element.length)
            else print(element)
        }
        println()
    }
}

private fun applyBoundaryCondition(y: Int, x: Int, sizeY: Int, sizeX: Int, horizontal: Boolean): Pair<Int, Int> {
    if (horizontal) {
        return when (x) {
            0 -> Pair(y, sizeX - 2)
            sizeX - 1 -> Pair(y, 1)
            else -> Pair(y, x)
        }
    }
    return when (y) {
        0 -> Pair(sizeY - 2, x)
        sizeY - 1 -> Pair(1, x)
        else -> Pair(y, x)
    }
}

fun heuristicDistance(start: Pair<Int, Int>, finish: Pair<Int, Int>): Int {
    val dx = abs(start.first - finish.first)
    val dy = abs(start.second - finish.second)
    return (dx + dy)
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}


class State(val index_i: Int, val index_j: Int, val costValue: Int, val time: Int)  {

}