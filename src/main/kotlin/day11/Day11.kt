import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputexample = getResourceAsText("inputExample.txt")
    val inputLines =
        input.split(Regex("Monkey *.:"))
            .filter { it.isNotEmpty() }
            .map { it.lines().filter { it.isNotEmpty() } }

    var monkeyListPartOne = mutableListOf<Monkey>()
    var monkeyListPartTwo = mutableListOf<Monkey>()
    parseInput(inputLines, monkeyListPartOne, monkeyListPartTwo)

    val modulo = monkeyListPartOne
        .map { it.test }
        .reduce { first, second -> first * second }

    playRounds(monkeyListPartTwo, modulo, 10000, false)
    playRounds(monkeyListPartOne, modulo, 20, true)

    println("Answer A  = " + calcAnswer(monkeyListPartOne))
    println("Answer B  = " +calcAnswer(monkeyListPartTwo))
}

private fun parseInput(
    inputLines: List<List<String>>,
    monkeyListPartOne: MutableList<Monkey>,
    monkeyListPartTwo: MutableList<Monkey>
) {
    for (monkeyInput in inputLines) {
        //This can be probably be done a lot more efficient. One day...
        var items =
            monkeyInput[0].split("Starting items:")[1].split(",").filter { it.isNotEmpty() }.map { it.trim().toLong() }
        var operations = monkeyInput[1].split("Operation: new =")[1].split(" ").filter { it.isNotEmpty() }
        var operationNumber = if (operations[2].equals("old")) 0 else operations[2].trim().toInt()
        var operationAction = if (operations[2].equals("old")) "x^2" else operations[1]
        var test = monkeyInput[2].split("Test:")[1].split(" ").filter { it.isNotEmpty() }[2].trim().toInt()
        var throwToMonkeyTrue =
            monkeyInput[3].split("If true: throw to monkey").filter { it.isNotEmpty() }[1].trim().toInt()
        var throwToMonkeyFalse =
            monkeyInput[4].split("If false: throw to monkey").filter { it.isNotEmpty() }[1].trim().toInt()

        val monkeyOne = Monkey(test, operationAction, operationNumber, Pair(throwToMonkeyTrue, throwToMonkeyFalse))
        val monkeyTwo = Monkey(test, operationAction, operationNumber, Pair(throwToMonkeyTrue, throwToMonkeyFalse))
        monkeyOne.items.addAll(items)
        monkeyTwo.items.addAll(items)
        monkeyListPartOne.add(monkeyOne)
        monkeyListPartTwo.add(monkeyTwo)
    }
}

private fun calcAnswer(monkeyList: MutableList<Monkey>) = monkeyList
    .sortedWith(compareBy { it.inspections })
    .takeLast(2)
    .map { it.inspections }
    .reduce { acc, l -> acc * l }

private fun playRounds(monkeyList: MutableList<Monkey>, modulo: Int, rounds: Int, partOne: Boolean) {
    for (i in 0 until rounds) {
        for (monkey in monkeyList) {
            while (monkey.items.isNotEmpty()) {
                monkey.inspections++
                var item = monkey.items.poll()
                var worry: Long = calc(item, monkey.operation, monkey.operationNumber)
                if (partOne) worry /= 3

                // To handle big numbers we can use some modulo arithmetics:  (x + b)  %  c  ==  ((x % c) + b)  %  c
                //                                                            (x * b)  %  c  ==  ((x % c) * b)  %  c
                // We can apply modulo by all test divisors multiplied together.
                // As we are just interested if the number is divisible by the test number.
                // This makes that the number never exceeds all divisors multiplied together.
                worry %= modulo

                // Pass the item to the next monkey.
                if (worry % monkey.test == 0L) {
                    monkeyList[monkey.throwToo.first].items.add(worry)
                } else {
                    monkeyList[monkey.throwToo.second].items.add(worry)
                }
            }
        }
    }
}

fun calc(item: Long, operation: String, operationNumber: Int): Long {
    return when (operation) {
        "+" -> item + operationNumber
        "*" -> item * operationNumber
        "x^2" -> item * item
        else -> throw Exception("error")
    }
}

class Monkey(var test: Int, var operation: String, var operationNumber: Int, var throwToo: Pair<Int, Int>) {
    var items: Queue<Long> = LinkedList()
    var inspections: Long = 0

    override fun toString(): String {
        return "test: $test operation: $operation operationNumber: $operationNumber items: $items pair:$throwToo"
    }
}
