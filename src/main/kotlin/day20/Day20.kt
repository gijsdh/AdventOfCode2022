fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")


    val listOne =
        parseInput(input,1)
    moveListAround(listOne)
    println("Answer A: ${getResult(listOne)}")

    val list =
        parseInput(input, 811589153L)
    for (i in 0 until 10) {
        moveListAround(list)
    }
    println("Answer B: ${getResult(list)}")

}

private fun getResult(list: MutableList<Pair<Long, Int>>
): Long {
    var indexZero = list.indexOfFirst { 0L == it.first }
    return listOf(1000, 2000, 3000).fold(0L) { i, j ->
        val index = ((j + indexZero) % list.size)
        i + list[index].first
    }
}
private fun moveListAround(list: MutableList<Pair<Long, Int>>) {
    for (i in 0 until list.size) {
        var index = list.indexOfFirst { i == it.second }
        var item = list.removeAt(index)
        list.add(i(index, item, list), item)
    }
}

private fun parseInput(input: String, multiply: Long) =
    input.lines().map { it.toLong() }.withIndex().map { Pair(it.value * multiply, it.index) }.toMutableList()

private fun i(
    index: Int,
    item: Pair<Long, Int>,
    inputLines: MutableList<Pair<Long, Int>>
): Int {
    val i = (index + item.first) % inputLines.size
    if (i > 0) return i.toInt()
    return (inputLines.size + i).toInt()
}

