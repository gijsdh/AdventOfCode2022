fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val input2 = getResourceAsText("inputExample.txt")
    val inputLines = input.lines().map { it.split(",") }

    var sum = 0
    var sum2 = 0
    for (line in inputLines) {
        var partOne = line[0].split("-")
        var partTwo = line[1].split("-")
        val X1 = partOne[0].toInt()
        val X2 = partOne[1].toInt()

        val Y1 = partTwo[0].toInt()
        val Y2 = partTwo[1].toInt()

        if (X1 <= Y1 && X2 >= Y2) {
            sum++
        } else if (X2 <= Y2 && X1 >= Y1) {
            sum++
        }
        if (X2 < Y1 || X1 > Y2) {
            sum2++
        }
    }

    println(sum)
    println(inputLines.size - sum2)
}





