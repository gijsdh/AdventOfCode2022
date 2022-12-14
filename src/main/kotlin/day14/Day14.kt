import java.lang.Integer.max
import java.lang.Math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputexample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()

    var set = mutableSetOf<Pair<Int, Int>>()
    for (line in inputLines) {
        var points = line.split("->").filter { it.isNotBlank() }
        for (point in points.windowed(2)) {

            var pointOne = extracted(point[0])
            var pointTwo = extracted(point[1])

            var dx = pointOne.first - pointTwo.first
            var dy = pointOne.second - pointTwo.second

            for (i in 0 until max(abs(dx), abs(dy)) + 1) {
                var x = pointOne.first + getIncrement(dx, i)
                var y = pointOne.second + getIncrement(dy, i)
                set.add(Pair(x, y))
            }
        }
    }

    val max = set.map { it.second }.max()
    for (i in -200 until 700) {
        set.add(Pair(i, max + 2))
    }

    var sum = 0
    var keepGoing = true
    var partOne = true
    while (keepGoing) {
        var sand = Pair(500, 0)
        while (keepGoing) {

            if (sand.second == max && partOne) {
                partOne = false
                println("Asnwer A: $sum")
            }

            if (!set.contains(Pair(sand.first, sand.second + 1))) {
                sand = Pair(sand.first, sand.second + 1)
                continue
            } else if (!set.contains(Pair(sand.first - 1, sand.second + 1))) {
                sand = Pair(sand.first - 1, sand.second + 1)
                continue
            } else if (!set.contains(Pair(sand.first + 1, sand.second + 1))) {
                sand = Pair(sand.first + 1, sand.second + 1)
                continue
            } else {
                sum++
                if (sand == Pair(500, 0)) {
                    println("Asnwer B: ${sum}")
                    keepGoing = false
                    break
                }
                set.add(sand)
                break
            }
        }
    }
}

private fun getIncrement(delta: Int, i: Int) = if(delta != 0)  if (delta > 0) i * -1 else i * 1 else 0

private fun extracted(point: String): Pair<Int, Int> {
    val cor = point.split(",")
    var x = cor[0].trim().toInt()
    var y = cor[1].trim().toInt()
    return Pair(x, y)
}






