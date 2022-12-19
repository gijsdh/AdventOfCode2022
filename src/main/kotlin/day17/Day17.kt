fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = inputExample.split("").filter { it.isNotEmpty() }

    //Start with ground
    var totalTower: MutableSet<Pair<Long, Long>> =
        mutableSetOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0), Pair(4, 0), Pair(5, 0), Pair(6, 0))


    //Stole solution strategy from the net kudos to Jonathan Paulson
    var repeatingShape: MutableMap<Triple<MutableSet<Pair<Long, Long>>, Long, Long>, Pair<Long, Long>> = mutableMapOf()

    var count = 0L
    var action = 0L
    var added = 0L

    while (count < 1000000000000) {
        var height = getMaxHeight(totalTower) + 4
        
        var piece: Set<Pair<Long, Long>> = getPiece((count % 5).toLong(), height);
        while (true) {
            val i = (action++ % inputLines.size.toLong()).toInt()
            var move = inputLines[i]
                if (move.equals("<")) {
                    piece = moveLeft(piece)
                    if (piece.filter { totalTower.contains(Pair(it.first, it.second)) }.isNotEmpty()) piece =
                        moveRight(piece)
                }
                if (move.equals(">")) {
                    piece = moveRight(piece)
                    if (piece.filter { totalTower.contains(Pair(it.first, it.second)) }.isNotEmpty()) piece =
                        moveLeft(piece)
                }

            if (piece.filter { totalTower.contains(Pair(it.first, it.second - 1)) }.size > 0) {
                totalTower.addAll(piece)


                // We take the last 30 rows as the shape of the tower.
                // And we look in the map if we have already seen this same shape with corresponding action and piece.
                // If we have we know that we have a repeagin pattern and we can calculate the height which we gained.
                var shape = transformToZeroHeigh(totalTower)
                var repeat = Triple(shape, (count % 5).toLong(), i.toLong())
                if (repeatingShape.contains(repeat) && count > 2022) {
                    var old = repeatingShape[repeat]
                    var height = getMaxHeight(totalTower)

                    var dy = height - old!!.first
                    var dt = count - old!!.second
                    var amt = (1000000000000L - count) / dt
                    added += amt * dy
                    count += amt*dt
                }
                repeatingShape.put(repeat, Pair(getMaxHeight(totalTower), count))
                break
            }
            piece = moveDown(piece)
        }
        if(count == 2021L) println(getMaxHeight(totalTower))
        count++
    }
    

    //3068 --> example
    //3119 --> 3119

    //1536994219669
    println(getMaxHeight(totalTower)+added)

}

fun transformToZeroHeigh(totalTower: MutableSet<Pair<Long, Long>>): MutableSet<Pair<Long, Long>> {
    var max = getMaxHeight(totalTower)

    return totalTower.filter { max - it.second <= 50 }.map { Pair(it.first, max - it.second) }.toMutableSet()
}

private fun getMaxHeight(totalTower: MutableSet<Pair<Long, Long>>) =
    totalTower.map { it.second }.max()

fun getPiece(i: Long, height: Long): Set<Pair<Long, Long>> {
    return when (i) {
        0L -> mutableSetOf(Pair(2, height), Pair(3, height), Pair(4, height), Pair(5, height))
        1L -> mutableSetOf(Pair(3, height), Pair(3, height+1), Pair(2, height+1), Pair(4, height+1),Pair(3, height+2))
        2L -> mutableSetOf(Pair(2, height), Pair(3, height), Pair(4, height), Pair(4, height+1),Pair(4, height+2))
        3L -> mutableSetOf(Pair(2, height), Pair(2, height+1), Pair(2, height+2), Pair(2, height+3))
        4L -> mutableSetOf(Pair(2, height), Pair(3, height), Pair(2, height+1), Pair(3, height+1))
        else -> throw Exception("shit")
    }
}

fun moveLeft(piece: Set<Pair<Long, Long>>): Set<Pair<Long, Long>> {
    if (piece.filter { it.first == 0L }.isNotEmpty()) return piece
    return piece.map { Pair(it.first - 1, it.second) }.toSet()
}

fun moveRight(piece: Set<Pair<Long, Long>>): Set<Pair<Long, Long>> {
    if (piece.filter { it.first == 6L }.isNotEmpty()) return piece
    return piece.map { Pair(it.first + 1, it.second) }.toSet()
}

fun moveDown(piece: Set<Pair<Long, Long>>): Set<Pair<Long, Long>> {
    return piece.map { Pair(it.first, it.second - 1) }.toSet()
}

//####
//
//.#.
//###
//.#.
//
//..#
//..#
//###
//
//#
//#
//#
//#
//
//##
//##


