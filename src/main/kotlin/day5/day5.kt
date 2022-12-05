import java.util.LinkedList

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val inputLines = input.split("\n\r\n") //split on empty line

    val inputOne = inputLines[0].lines()
    val inputTwo = inputLines[1].lines()
    var queusPartOne: MutableList<LinkedList<Char>> = linkedLists(inputOne)
    var queusPartTwo: MutableList<LinkedList<Char>> = linkedLists(inputOne)

    for (line in inputTwo) {
        var input =
            line.split(Regex("move|from|to")).map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() - 1 }
        var temp = mutableListOf<Char>()
        for (i in 0 until input[0] + 1) {
            temp.add(queusPartTwo[input[1]].pop())
            queusPartOne[input[2]].push(queusPartOne[input[1]].pop())
        }
        for (char in temp.reversed()) {
            queusPartTwo[input[2]].push(char)
        }
    }

    println("---------Answer A------------- ")
    println()
    for (que in queusPartOne) {
        print(que.first)
    }
    println()
    println("---------Answer B-------------")
    println()
    for (que in queusPartTwo) {
        print(que.first)
    }
}

//Input
//[T]     [Q]             [S]
//[R]     [M]             [L] [V] [G]
//[D] [V] [V]             [Q] [N] [C]
//[H] [T] [S] [C]         [V] [D] [Z]
//[Q] [J] [D] [M]     [Z] [C] [M] [F]
//[N] [B] [H] [N] [B] [W] [N] [J] [M]
//[P] [G] [R] [Z] [Z] [C] [Z] [G] [P]
//[B] [W] [N] [P] [D] [V] [G] [L] [T]
// 1   2   3   4   5   6   7   8   9

private fun linkedLists(inputOne: List<String>): MutableList<LinkedList<Char>> {
    var queus: MutableList<LinkedList<Char>> = mutableListOf();

    for (j in 0 until 9) {
        var list = LinkedList<Char>()
        queus.add(list)
    }
    var counter = 1
    for (i in 0 until inputOne.size - 1) {
        for (j in 0 until 9) {
            if (inputOne[i][counter].isLetter()) {
                queus[j].add(inputOne[i][counter])
            }
            counter += 4
        }
        counter = 1
    }
    return queus
}




