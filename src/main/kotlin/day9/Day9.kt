fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()

    var head = Pair(0, 0)
    var tail = Pair(0, 0)
    var positionsA = mutableSetOf<Pair<Int, Int>>()
    var positionsB = mutableSetOf<Pair<Int, Int>>()
    positionsA.add(tail)
    positionsB.add(tail)

    var tailList = (1 until 10).map {Pair(0,0)}.toMutableList()
    val map: HashMap<String, Pair<Int,Int>> = hashMapOf(
        Pair("R", Pair(1, 0)),
        Pair("L", Pair(-1, 0)),
        Pair("U", Pair(0, 1)),
        Pair("D", Pair(0, -1))
    )

    for (line in inputLines) {
        var move = line.split(" ")
        val moveCount = move[1].toInt()
        val moveDirection = move[0].trim()
        for (i in 1 until moveCount + 1) {
            val incrementPair = map[moveDirection]!!
            var x = head.first + incrementPair.first
            var y = head.second + incrementPair.second
            head = Pair(x, y)
            updateTail(tailList, head, positionsA, positionsB)
        }
    }

    println(positionsA.count() + 1)
    println(positionsB.count() + 1)
}

private fun updateTail(
    list: MutableList<Pair<Int, Int>>,
    headParam: Pair<Int, Int>,
    positionsA: MutableSet<Pair<Int, Int>>,
    positionsB: MutableSet<Pair<Int, Int>>
) {
    var head = headParam
    for (i in 0 until list.size) {
        var tail = list[i]
        if (shouldMove(head, tail)) {
            list[i] = move(head, tail)
            if (i == 8) positionsB.add(tail)
            if (i == 0) positionsA.add(tail)
        }
        head = list[i]
    }
}

//This made it very easy
private fun move(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
    var x = tail.first + head.first.compareTo(tail.first)
    var y = tail.second + head.second.compareTo(tail.second)
    return Pair(x, y)
}

private fun shouldMove(head: Pair<Int, Int>, tail: Pair<Int, Int>): Boolean {
    val ith = intArrayOf(-1, -1, -1, 0, 0, 0, 1, 1, 1, 0)
    val jth = intArrayOf(-1, 0, 1, -1, 0, 1, -1, 0, 1, 0)
    for (k in ith.indices) {
        val index_I = tail.first + ith[k]
        val index_J = tail.second + jth[k]
        if (head.first == index_I && head.second == index_J) {
            return false
        }
    }
    return true
}
