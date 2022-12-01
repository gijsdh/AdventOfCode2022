fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines()

    var sum = 0;
    var max = 0
    var list: MutableList<Int> = mutableListOf()

    for (line in inputLines) {
        if (line.isNotEmpty()) {
            sum += line.toInt();
            if (max < sum) {
                max = sum;
            }
        } else {
            list.add(sum);
            sum = 0;
        }
    }

    println("Answer A: " + max)

    list.sortDescending()
    var answer = list[0] + list[1] + list[2]
    println("Answer B: " + answer)



    //More functional approach
    var func = input.split("\n\r")
        .map { it.split("\r")
            .map { it.trim()}
            .filter { it.isNotEmpty() }
            .map { it.toInt() } }

    println("func A:" + func.map { it.reduce{ i, j -> i+j}}.max())
    println("func B:" + func.map { it.reduce{ i, j -> i+j}}.sorted().takeLast(3).sum())
}

fun getResourceAsText(path: String): String {
    return object {}.javaClass.getResource(path).readText()
}





