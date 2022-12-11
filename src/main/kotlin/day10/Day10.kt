fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputexample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()

    var signalStrength = 0;
    var cycle = 0
    var x = 1

    var position = Pair(0,0)
    val array: Array<CharArray> = Array(6) { CharArray(40) }

    for (line in inputLines) {
        var input = line.split(" ")
        when {
            "noop".equals(input[0]) -> {
                cycle++
                drawPixel(x, array, position)
                position = newPosition(position)
            }
            "addx".equals(input[0]) -> {
                var number = input[1].trim().toInt()

                cycle ++
                signalStrength += calculateSignal(cycle, x)
                drawPixel(x, array, position)
                position = newPosition(position)

                cycle ++
                signalStrength += calculateSignal(cycle, x)
                drawPixel(x, array, position)
                position = newPosition(position)

                x += number
            }
        }
    }


    println("Answer A: " + signalStrength)


    println("-----------------------------------------------------------------------------------------------")
    println("Answer B: ")

    for (i in 0 until array.size) {
        for (j in 0 until array[0].size) {
            print(" " + array[i][j]);
        }
        println();
    }

}

private fun newPosition(position: Pair<Int, Int>): Pair<Int, Int> {
    var pos = position.first + 1
    if (pos % 40 == 0) {
        return Pair(0, position.second + 1)
    }
    return Pair(pos, position.second)
}

private fun drawPixel(x: Int, array: Array<CharArray>, pos: Pair<Int, Int>) {
    val xCoordinate = pos.first
    val yCoordinate = pos.second
    array[yCoordinate][xCoordinate] = '.'
    if (xCoordinate == x || xCoordinate + 1 == x || xCoordinate - 1 == x) array[pos.second][xCoordinate] = '#'
}

private fun calculateSignal(cycle: Int, x: Int): Int {
    if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || 180 ==cycle || 220 == cycle)  return cycle * x
    return 0
}





