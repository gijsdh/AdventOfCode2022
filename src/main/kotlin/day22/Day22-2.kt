
var R = 50
fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.split("\n\r\n").filter {  it.isNotEmpty() }

    val maze = inputLines[0].lines()
    val moves = inputLines[1]

    var map: MutableMap<Pair<Int, Int>, String> = mutableMapOf()
    var regionMap: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    var rowMaxMin: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()
    var columnMaxMin: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()

    var regions = listOf(Pair(1,0), Pair(2, 0), Pair(1,1), Pair(1,2), Pair(0,2), Pair(0,3))


    for (line in maze.dropLast(1).withIndex()) {
        var positions = line.value.split("").drop(1)
        for (i in 0 until positions.size) {
            val item = positions[i]
            if (item == "." || item == "#") {

                map[Pair(i, line.index)] = item
                regionMap[Pair(i, line.index)] = regions.indexOf(Pair(i/R, line.index/R))+1
            }
        }
        rowMaxMin.put(line.index, Pair(valuesRow(map, line.index).min(), valuesRow(map, line.index).max()))
    }

    var max = map.keys.map { it.first }.max()
    for (i in 0 until max +1) {
        columnMaxMin.put(i, Pair(valuesColumn(map, i).min(), valuesColumn(map, i).max()))
    }


    var position = Pair(valuesRow(map, 0).min(), 0)
    var direction = 0

    var index = 0
    var previous = 0
    while (index < moves.length) {
        if (moves[index].isLetter() || index + 1 == moves.length) {
            if(index + 1 == moves.length) index ++
            var move = moves.subSequence(previous, index).toString().toInt()

            for (i in 0 until move) {

                val incrementY = getIncrementY(direction)
                val incrementX = getIncrementX(direction)

                var x = position.first + incrementX
                var y = position.second + incrementY

                var pair = Pair(x, y)
                if (map[pair] == null) {
                    var posDirection = moveOfMap(direction, pair, rowMaxMin, position, columnMaxMin, regionMap)
                    pair =posDirection.first
                    if (map[pair] == ".") {
                        direction = posDirection.second
                        position = pair
                    } else {
                        break
                    }
                } else{
                    if (map[pair] == ".") {
                        position = pair
                    } else {
                        break
                    }
                }
            }

            if ( index < moves.length) {
                var newDirection = moves[index].toString()
                direction = modWithNegative(direction + operation(newDirection), 4)
                previous = index + 1
            }
        }
        index++
    }


    println(" $position $direction")
    println(" ${(position.first +1)* 4 + (position.second +1)*1000 + direction} ")

}

fun moveOfMap(
    direction: Int,
    pair: Pair<Int, Int>,
    rowMaxMin: MutableMap<Int, Pair<Int, Int>>,
    position: Pair<Int, Int>,
    columnMaxMin: MutableMap<Int, Pair<Int, Int>>,
    regionMap: Map<Pair<Int, Int>, Int>
): Pair<Pair<Int, Int>, Int> {

    val result = when (Pair(regionMap[position], direction)) {
        Pair(1, 2) -> validateTranslation(Pair(Pair(0, 2 * R + ((R-1) - (position.second % R))), 0),regionMap,5,1,position)
        Pair(1, 3) -> validateTranslation(Pair(Pair(0, 3 * R + (position.first % R)), 0),regionMap,6,1,position)

        Pair(2, 1) -> validateTranslation(Pair(Pair((2 * R)-1, R + (position.first % R)), 2),regionMap,3,2,position)
        Pair(2, 0) -> validateTranslation(Pair(Pair((2 * R)-1, 2 * R + (R-1 - (position.second % R))), 2),regionMap,4,2,position)
        Pair(2, 3) -> validateTranslation(Pair(Pair((position.first % R), (4 * R)-1), 3),regionMap,6,2,position)

        Pair(3, 0) -> validateTranslation(Pair(Pair(2 * R + (position.second % R), R-1), 3),regionMap,2,3,position)
        Pair(3, 2) -> validateTranslation(Pair(Pair((position.second % R), (2 * R)), 1),regionMap,5,3,position)

        Pair(4, 0) -> validateTranslation(Pair(Pair((3 * R)-1, (R-1 - (position.second % R))), 2),regionMap,2,4,position)
        Pair(4, 1) -> validateTranslation(Pair(Pair(R-1, (3 * R + (position.first % R))), 2),regionMap,6,4,position)

        Pair(5, 3) -> validateTranslation(Pair(Pair(R, (R + (position.first % R))), 0),regionMap,3,5,position)
        Pair(5, 2) -> validateTranslation(Pair(Pair(R, (R-1 - (position.second % R))), 0),regionMap,1,5,position)

        Pair(6, 0) -> validateTranslation(Pair(Pair(R + (position.second % R), (3 * R)-1), 3), regionMap, 4,6,position)
        Pair(6, 1) -> validateTranslation(Pair(Pair(2 * R + (position.first % R), 0), 1), regionMap, 2,6,position)
        Pair(6, 2) -> validateTranslation(Pair(Pair(R + (position.second % R), 0), 1), regionMap, 1,6, position)
        else -> Pair(moveOfMap(direction, pair, rowMaxMin, position, columnMaxMin), direction)
    }
    return result
}

fun validateTranslation(
    pair: Pair<Pair<Int, Int>, Int>,
    regionMap: Map<Pair<Int, Int>, Int>,
    i: Int,
    j: Int,
    position: Pair<Int, Int>
): Pair<Pair<Int, Int>, Int> {
    if (regionMap[pair.first] != i) println("$j $i ${regionMap[pair.first]} $pair")
    return pair
}
/*
 12
 3
54
6

 6     3     1    4    3    5
512   124   532  542  154  164
 3     6     4    6    6    2

define regions = R is the width of a side
1 -> (R -> 2R, 0 -> R)
2 -> (2R -> 3R, 0-> R)
3 -> (R -> 2R, R -> R)
4 -> (R -> 2R, 2R -> 3R)
5 -> (0 -> R, 2R -> 3R)
5 -> (0 -> R, 3R -> 4R)
*/

//1 -> 2 normal
//1 -> 3 normal
//1 -> 5 (direction 2 -> 0) (R,yp)  (x R -> 0) ,(y yp -> 2R + (R-yp))
//1 -> 6 (direction 3 -> 0) (xp, 0) -> (x xp-> 0) , y (y, 0 -> 3R+xp)
//
//2 -> 1 normal
//2 -> 3 (direction 1 -> 2) (xp, R) (x xp -> 2R) (y -> R + (xp))
//2 -> 4 (direction 0 -> 2) (3R,yp)  (x 3R -> 2R) ,(y yp -> 2R + (R-yp))
//2 -> 6 (direction 3 -> 3) (xp, 0) -> (x xp-> xp + 0) , y (y, 0 -> 4R)
//
//3 -> 1 normal
//3 -> 2 (direction 0 -> 3) (2R, yp) (x xp -> 2R + yp) (y yp -> R)
//3 -> 4 normal
//3 -> 5 (direction 2 -> 1) (R, yp) -> (x xp-> yp + 0) , y (y, 0 -> 2R)
//
//4 -> 3 normal
//4 -> 2 (direction 0 -> 2) (2R, yp) (x 2R -> 3R) (y yp -> 0 + (R-yp) )
//4 -> 5 normal
//4 -> 6 (direction 1 -> 2) (xp, 3R) -> (x xp-> R) , y (y, 0 -> 3R + xp)
//
//5 -> 4 normal
//5 -> 3 (direction 3 -> 0) (xp, 2R) (x xp -> R) (y 2R -> R + xp)
//5 -> 6 normal
//5 -> 1 (direction 2 -> 0) (0, yp) -> (x 0 -> R) , y (y, 0 -> 0 + (R-yp))
//
//6 -> 5 normal
//6 -> 4 (direction 3 -> 0) (R, yp) (x xp -> R+yp) (y yp -> 3R)
//6 -> 2 (direction 3 -> 3) (xp, 4R) (x 2R +xp) (y 0)
//6 -> 1 (direction 2 -> 1) (0, yp) -> (x 0 -> R + yp) , y (y, yp -> 0)


private fun moveOfMap(
    direction: Int,
    pair: Pair<Int, Int>,
    rowMaxMin: MutableMap<Int, Pair<Int, Int>>,
    position: Pair<Int, Int>,
    columnMaxMin: MutableMap<Int, Pair<Int, Int>>
): Pair<Int, Int> {
    var pair1 = pair
    when (direction) {
        0 -> pair1 = Pair(rowMaxMin[position.second]!!.first, position.second)
        1 -> pair1 = Pair(position.first, columnMaxMin[position.first]!!.first)
        2 -> pair1 = Pair(rowMaxMin[position.second]!!.second, position.second)
        3 -> pair1 = Pair(position.first, columnMaxMin[position.first]!!.second)
    }
    return pair1
}


private fun getIncrementX(delta: Int): Int {
    return when (delta) {
        0 -> 1
        1 -> 0
        2 -> -1
        3 -> 0
        else -> throw Exception("shit")
    }
}

private fun getIncrementY(delta: Int): Int {
    return when (delta) {
        0 -> 0
        1 -> 1
        2 -> 0
        3 -> -1
        else -> throw Exception("shit")
    }
}


private fun modWithNegative(input: Int, size: Int): Int {
    val i = input % size
    if (i >= 0) return i
    return (size + i)
}

private fun operation(operation: String): Int {
    return when (operation) {
        "R" -> 1
        "L" -> -1
        else -> throw Exception("shit")
    }
}

private fun valuesRow(
    map: MutableMap<Pair<Int, Int>, String>,
    index: Int
) = map.keys.filter { it.second == index }.map { it.first }


private fun valuesColumn(
    map: MutableMap<Pair<Int, Int>, String>,
    index: Int
) = map.keys.filter { it.first == index }.map { it.second }




