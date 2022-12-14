fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputexample = getResourceAsText("inputExample.txt")

    val inputLines = input.split("\n\r\n").map { it.trim() }.filter { it.isNotEmpty() }
    var mutableList:MutableList<Pair<Node, Node>> = mutableListOf()

    for (lines in inputLines) {
        var twoLine = lines.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        var lineOne = parsIt(twoLine[0]);
        var lineTwo = parsIt(twoLine[1]);
        mutableList.add(Pair(lineOne!!, lineTwo!!))
    }

    var sum = 0
    for ((i, pair) in mutableList.withIndex()) {
//        println(pair.first)
//        println(pair.second)
        val compareNodes = compareNodes(pair.first, pair.second)
//        println(compareNodes)
        if(compareNodes == -1) sum+=i+1
    }

    var additionOne = Node.NodeList(listOf(Node.NodeList(listOf(Node.NodeValue(2)))))
    var additionTwo = Node.NodeList(listOf(Node.NodeList(listOf(Node.NodeValue(6)))))

    var list = mutableList.map { listOf(it.first,it.second) }.flatten() + additionOne + additionTwo

    println(list)
    var sortedList = list.sortedWith { p1, p2 -> compareNodes(p1, p2) }

    println((sortedList.indexOf(additionOne) + 1) * (1 + sortedList.indexOf(additionTwo)))

}

fun compareNodes(p1: Node, p2: Node): Int {
    return when {
        p1 is Node.NodeValue && p2 is Node.NodeValue -> p1.nodes.compareTo(p2.nodes)
        p1 is Node.NodeList && p2 is Node.NodeList -> compareNodes(p1, p2)
        p1 is Node.NodeValue && p2 is Node.NodeList -> compareNodes(Node.NodeList(listOf(p1)), p2)
        p1 is Node.NodeList && p2 is Node.NodeValue -> compareNodes(p1, Node.NodeList(listOf(p2)))
        else -> {
            println(p1)
            println(p2)
            throw Exception("shit")
        }
    }
}

fun compareNodes(p1: Node.NodeList, p2: Node.NodeList): Int {
    var pairs = p1.nodes.zip(p2.nodes)
    for ((one, two) in pairs) {
        val compare = compareNodes(one, two)

        if (compare != 0) {
            return compare
        }
    }
    if (p1.nodes.size == p2.nodes.size) return 0
    if (p1.nodes.size < p2.nodes.size) return -1
    return 1
}


private fun parsIt(test: String): Node? {
    if (test.isEmpty()) return null
    if (test[0].isDigit()) return Node.NodeValue(test.toInt())

    var counter = -1 // start with -1 so it does not count first '['
    var loc = 0

    val nodes = mutableListOf<Node?>()

    for ((i, char) in test.withIndex()) {
        if (char == ']') {
            counter--
            if (counter == -1) nodes.add(parsIt(test.substring(loc + 1, i)))
        }
        if (char == '[') counter++
        if (char == ',' && counter == 0) {
            nodes.add(parsIt(test.substring(loc + 1, i)))
            loc = i
        }
    }
    return Node.NodeList(nodes.filterNotNull());
}


sealed class Node {
    data class NodeList(val nodes: List<Node>) : Node()
    data class NodeValue(val nodes: Int) : Node()

    override fun toString(): String {
        var s =  "" // if (value != null) "$value" else ""
        if(this is NodeList) {
            s += " {" + nodes.map { it.toString() } + " }"
        } else if (this is NodeValue) (
            return ""+nodes
        )

        return s
    }
}






