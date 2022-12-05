import java.util.LinkedList

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")

    val inputLines = input.split("\n\r\n") //split on empty line

    val inputOne = inputLines[0].lines().filter { it.isNotEmpty() }
    val inputTwo = inputLines[1].lines()
    var queuesPartOne: MutableList<LinkedList<Char>> = parseInputToLinkedLists(inputOne)
    var queuesPartTwo: MutableList<LinkedList<Char>> = parseInputToLinkedLists(inputOne)

    for (line in inputTwo) {
        var input =
            line.split(Regex("move|from|to"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toInt() - 1 } //To zero based

        val destinationIndex = input[2]
        val originIndex = input[1]
        var temp = mutableListOf<Char>()

        for (i in 0 until input[0] + 1) {
            temp.add(queuesPartTwo[originIndex].pop())
            queuesPartOne[destinationIndex].push(queuesPartOne[originIndex].pop())
        }
        for (char in temp.reversed()) {
            queuesPartTwo[destinationIndex].push(char)
        }
    }

    println("---------Answer A------------- ")
    println()
    for (que in queuesPartOne) {
        print(que.first)
    }
    println()
    println("---------Answer B-------------")
    println()
    for (que in queuesPartTwo) {
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

private fun parseInputToLinkedLists(inputOne: List<String>): MutableList<LinkedList<Char>> {
    var queus: MutableList<LinkedList<Char>> = mutableListOf();
    val size = inputOne[inputOne.size - 1].trim().last().toString().toInt()
    for (j in 0 until size) {
        var list = LinkedList<Char>()
        queus.add(list)
    }
    var counter = 1
    for (i in 0 until inputOne.size - 1) {
        for (j in 0 until size) {
            if (inputOne[i][counter].isLetter()) {
                queus[j].add(inputOne[i][counter])
            }
            counter += 4
        }
        counter = 1
    }
    return queus
}




