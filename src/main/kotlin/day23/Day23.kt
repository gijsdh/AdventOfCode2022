fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val parsedInput = input.lines().map { it.split("").filter { it.isNotEmpty() } }

    var elfSet: MutableSet<Pair<Int, Int>> = mutableSetOf()
    var proposedMoves: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>> = mutableMapOf()

    // Direction a elf could move, North, South, West, East.
    // Axis are positive in south and east direction.
    var moves = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))


    // we add the Elfs to a set, so we do not keep track of a growing grid.
    for (i in 0 until parsedInput.size) {
        for (j in 0 until parsedInput[0].size) {
            if (parsedInput[i][j] == "#") elfSet.add(Pair(i, j));
        }
    }

    println(elfSet.size)
    var index = 0
    for (i in 0 until 1000) {
        for (elf in elfSet) {
            if (hasNoNeighbours(elfSet, elf)) continue
            for (j in 0 until 4) {
                var direction = moves[(index + j) % 4]
                var neighbours = getNeighboursToCheck(direction)

                // So check if there are no neighbours in a certain direction.
                if (!neighbours.map { Pair(elf.first + it.first, elf.second + it.second) }.any { elfSet.contains(it) }) {
                    // Add move as a possible move, and merge if multiple Elfs propose the same move.
                    // key is new location, addition to list is where elf is moving from
                    proposedMoves.merge(Pair(elf.first + direction.first, elf.second + direction.second), mutableListOf(elf), mergeLists())
                    break
                }
            }
        }

        //Only accept if only one elf proposed the new location
        val moves = proposedMoves.filter { it.value.size == 1 }
        if (moves.isEmpty()) {
            println("Answer B : ${i + 1}");
            break
        }

        for (new in moves) {
            if(!elfSet.remove(new.value[0])) throw Exception("removed something not there")
            if(!elfSet.add(new.key)) throw Exception("added something already there")
        }

        if(i == 9 ) calculateAnswerA(elfSet)

        //Reset moves and update index
        proposedMoves = mutableMapOf()
        index++
    }

}

private fun calculateAnswerA(elfSet: MutableSet<Pair<Int, Int>>) {
    var maxX = elfSet.maxOf { it.second }
    var minX = elfSet.minOf { it.second }
    var maxY = elfSet.maxOf { it.first }
    var minY = elfSet.minOf { it.first }

    println("Answer A : ${((maxX - minX + 1) * (maxY - minY + 1)) - elfSet.size}")
}

fun printNicely(elfSet: MutableSet<Pair<Int, Int>>) {
    println()
    for (i in elfSet.minOf { it.first } until elfSet.maxOf { it.first } + 1) {
        for (j in elfSet.minOf { it.second } until elfSet.maxOf { it.second } + 1) {
            if (elfSet.contains(Pair(i, j))) print("#") else print(".")
        }
        println()
    }
}

private fun mergeLists(): (MutableList<Pair<Int, Int>>, MutableList<Pair<Int, Int>>) -> MutableList<Pair<Int, Int>> =
    { i, j -> i.addAll(j); i }

fun getNeighboursToCheck(pair: Pair<Int, Int>): List<Pair<Int, Int>> {
    return when (pair) {
        Pair(-1, 0) -> listOf(Pair(-1, 0), Pair(-1, 1), Pair(-1, -1))
        Pair(1, 0) -> listOf(Pair(1, 0), Pair(1, 1), Pair(1, -1))
        Pair(0, -1) -> listOf(Pair(0, -1), Pair(1, -1), Pair(-1, -1))
        Pair(0, 1) -> listOf(Pair(0, 1), Pair(1, 1), Pair(-1, 1))
        else -> throw Exception("shit")
    }
}


private fun hasNoNeighbours(elfs: MutableSet<Pair<Int, Int>>, elf: Pair<Int, Int>): Boolean {
    val ith = intArrayOf(0, 1, -1, 0, -1, 1, 1, -1)
    val jth = intArrayOf(1, 0, 0, -1, -1, 1, -1, 1)

    for (k in ith.indices) {
        val index_I = elf.first + ith[k]
        val index_J = elf.second + jth[k]
        if (elfs.contains(Pair(index_I, index_J))) {
            return false
        }
    }
    return true
}



