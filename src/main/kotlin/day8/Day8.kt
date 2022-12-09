fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()
    val trees = inputLines.map { it.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toIntArray() }.toTypedArray()

    var sum = 0
    var max = 0
    for (i in 0 until trees.size) {
        for (j in 0 until trees[0].size) {
            var score = checkNeighboursScenicScore(trees,  i, j)
            if (score > max) {
                max = score
            }
            if (checkNeighbours(trees,  i, j)) {
                sum++
            }
        }
    }
    println("Answer A: $sum")
    println("Answer B: $max")
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}

private fun checkNeighbours(numbers: Array<IntArray>, i: Int, j: Int): Boolean {
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    for (k in ith.indices) {
        val index_I = i + ith[k]
        val index_J = j + jth[k]
        if (!isValidIndex(index_I, index_J, numbers.size, numbers[0].size)) {
            return true //Edges are always visible
        }
    }
    var rangeRight = IntRange(j + 1, numbers[0].size - 1)
    var rangeLeft = j - 1 downTo 0
    var rangeDown = IntRange(i + 1, numbers.size - 1)
    var rangeUp = i - 1 downTo 0

    if (checkRange(rangeRight, numbers, i, j, true)) return true
    if (checkRange(rangeLeft, numbers, i, j, true)) return true
    if (checkRange(rangeDown, numbers, i, j, false)) return true
    if (checkRange(rangeUp, numbers, i, j, false)) return true
    return false
}

private fun checkRange(
    range: Iterable<Int>,
    numbers: Array<IntArray>,
    i: Int,
    j: Int,
    isHorizontal: Boolean
): Boolean {
    for (index in range) {
        if (isHorizontal && numbers[i][index] >= numbers[i][j]) return false
        if (!isHorizontal && numbers[index][j] >= numbers[i][j]) return false
    }
    return true
}


private fun checkNeighboursScenicScore(numbers: Array<IntArray>, i: Int, j: Int): Int {
    var scoreArray = intArrayOf(0, 0, 0, 0)

    var rangeRight = if (j + 1 != numbers[0].size) IntRange(j + 1, numbers[0].size - 1) else IntRange.EMPTY
    var rangeLeft = if (j - 1 > -1) j - 1 downTo 0 else IntRange.EMPTY
    var rangeDown = if (i + 1 != numbers.size) IntRange(i + 1, numbers.size - 1) else IntRange.EMPTY
    var rangeUp = if (i + 1 > -1) i - 1 downTo 0 else IntRange.EMPTY

    scoreArray[0] = countTree(rangeRight, numbers, i, j, true)
    scoreArray[1] = countTree(rangeLeft, numbers, i, j, true)
    scoreArray[2] = countTree(rangeDown, numbers, i, j, false)
    scoreArray[3] = countTree(rangeUp, numbers, i, j, false)

    return scoreArray[0] * scoreArray[1] * scoreArray[2] * scoreArray[3]
}

private fun countTree(
    range: Iterable<Int>,
    numbers: Array<IntArray>,
    i: Int,
    j: Int,
    isHorizontal: Boolean
): Int {
    var counter = 0;
    for (index in range) {
        counter++
        if (isHorizontal && numbers[i][index] >= numbers[i][j]) break
        if (!isHorizontal && numbers[index][j] >= numbers[i][j]) break
    }
    return counter
}


