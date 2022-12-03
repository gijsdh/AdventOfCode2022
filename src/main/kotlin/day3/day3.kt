import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines()

    var letterArray = IntArray(123)

    var sum = 0
    for (line in inputLines) {
        val endIndex = line.length / 2
        var lineOne = line.subSequence(0, endIndex)
        var lineTwo = line.subSequence(endIndex, line.length)

        if (lineOne.length != lineTwo.length) throw Exception("error")

        lineOne.forEach { letterArray[it.toInt()] = 1 }

        for (letter in lineTwo) {
            if (letterArray[letter.toInt()] != 0) {
                sum += letterValue(letter)
                break
            }
        }
        letterArray = IntArray(123)
    }

    println(sum)
    letterArray = IntArray(123)
    var sum2 = 0
    for (lines in inputLines.windowed(3, 3)) {
        lines[0].forEach { letterArray[it.toInt()] = 1 }
        lines[1].forEach { if (letterArray[it.toInt()] == 1) letterArray[it.toInt()] = 2 }
        for (letter in lines[2]) {
            if (letterArray[letter.toInt()] == 2) {
                sum2 += letterValue(letter)
                break
            }
        }
        letterArray = IntArray(123)
    }
    println(sum2)
}

// In Kotlin the ASCII value of lowercase alphabets are from 97 to 122. And, the ASCII value of uppercase alphabets are from 65 to 90.
private fun letterValue(letter: Char): Int {
    if (letter.toInt() > 90) return letter.toInt() - 96
    return letter.toInt() - 38 // -64 + 26
}





