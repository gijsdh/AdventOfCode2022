import java.lang.Math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()

    var parsed =
        inputLines.map { it.split(Regex("Sensor at |: closest beacon is at |,")).filter { it.isNotEmpty() } }
            .map {
                var sensor = parseInput(it[0], it[1])
                var beacon = parseInput(it[2], it[3])
                listOf(sensor, beacon)
            }


    var y = 2000000
    for (i in 0 until 4000000) {
        var list = mutableListOf<Pair<Long, Long>>()
        for (obs in parsed) {
            val sensor = obs[0]
            val beacon = obs[1]

            var manhattan = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
            if (manhattan >= abs(i - sensor.second)) {
                var distanceToLine = manhattan - abs(sensor.second - i)
                var lineCovered = Pair(sensor.first - distanceToLine, sensor.first + distanceToLine)
                list.add(lineCovered)
            }
        }

        var reducedList = reduceLines(list)
        if(i == y) println("Answer A: " + reducedList.map { it.second - it.first }.sum())

        //We get back two list if there is gap.
        if (reducedList.size > 1) {
            println("Answer B: ${((reducedList[0].second + 1) * 4000000L) + i}")
            break
        }
    }
}


// Here we have a list of line segments, we reduce it by first sorting them on left coordinate. And then joining them together if they overlap, line2X1 <= line1X1
//      line 1  X1-------------------------X2
//      line 2             X1--------------------------X2
//      =
//      line 3  X1-------------------------------------X2
// We try to reduce all overlapping lines, so we can easily see what is covered of that line.
fun reduceLines(list: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
    var sortedList = list.sortedWith(compareBy({ it.first }, { it.second }))
    var previous = sortedList[0]
    var mutableList = mutableListOf<Pair<Long, Long>>()
    for (line in sortedList) {
        // we also consider lines overlapping when they are adjacent for example: (1,10) -> (11,16)
        if ((line.first <= previous.second || line.first - 1 <= previous.second) && line.second >= previous.second) {
            previous = Pair(previous.first, line.second)
        } else if (line.second >= previous.second) {
            mutableList.add(Pair(previous.first, previous.second))
            previous = line
        }
    }
    mutableList.add(previous)

    return mutableList
}

private fun parseInput(x: String, y: String) =
    Pair(x.substring(2).trim().toLong(), y.substring(3).trim().toLong())







