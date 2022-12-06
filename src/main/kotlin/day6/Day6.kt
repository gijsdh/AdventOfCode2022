fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val letters = input.split("").filter { it.isNotEmpty() }

    println("Answer A: " + getFirstDistinctSequence(letters, 4))
    println("Answer B: " + getFirstDistinctSequence(letters, 14))
}

private fun getFirstDistinctSequence(letters: List<String>, size: Int): Int {
    var set = mutableSetOf<String>()
    for (i in 0 until letters.size) {
        for (j in 0 until size) {
            set.add(letters[i + j])
        }
        if (set.size == size) {
            return i + size
        }
        set = mutableSetOf()
    }
    return -1
}




