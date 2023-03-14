package day25

import getResourceAsText
import kotlin.math.pow

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val parsedInput = input.lines()

    var sum : Long = 0
    for(line in parsedInput) {
        sum += parse(line)
    }
    println("Answer A : ${toSnaffu(sum)}")
}

fun toSnaffu(number: Long): String {
    val toQuerinary = (number.toString(5)
        .map { it.digitToInt() }
        .reversed() + 0)
        .toMutableList()
    var index = 0
    var snaffu = ""
    while (index < toQuerinary.size) {
        var number = toQuerinary[index]
        when (number) {
            0 -> if(index != toQuerinary.size -1) snaffu += number
            1, 2 -> snaffu += number
            3 -> {
                snaffu += "="
                toQuerinary[index + 1]++
            }
            4 -> {
                snaffu += "-"
                toQuerinary[index + 1]++
            }
            5 -> {
                snaffu += "0"
                toQuerinary[index + 1]++
            }
        }
        index++
    }
    return snaffu.reversed()
}

fun parse(line: String): Long {
    val size = line.length - 1
    var sum = 0L
    for(char in line.withIndex()) {
        sum += parse(char.value, size - char.index)
    }
    return sum
}

fun parse(char: Char, i: Int): Long {
    val value = when (char) {
        '2' -> 2
        '1' -> 1
        '0' -> 0
        '-' -> -1
        '=' -> -2
        else -> throw Exception("shit")
    }
    return 5.toDouble().pow(i).toLong() * value
}
