import java.lang.Long.max
import java.lang.Math.pow
import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input16.txt")
    val inputExample = getResourceAsText("inputExample16.txt")
    val inputLines = input.lines().map { it.split(Regex(" |,|rate=|;")).filter { it.isNotEmpty() } }

    var open = mutableListOf<String>()

    for (line in inputLines) {
        val name = line[1]
        val flow = line[4].toLong()
        if (flow > 0) {
            open.add(name)
        }
        var valves = mutableListOf<String>()
        for (i in 9 until line.size) {
            valves.add(line[i])
        }
        var valve = Valve(name, flow)
        valve.valves = valves
        graph.put(name, valve)
    }

    println(open.size)

    for (valve in graph.values) {
        val dequeue: LinkedList<Pair<String, Int>> = LinkedList();
        dequeue.add(Pair(valve.name, 0))

        var visited = mutableSetOf(valve.name)
        while (dequeue.isNotEmpty()) {
            var current = dequeue.pollFirst()

            for (neighbour in graph[current.first]!!.valves) {
                if (visited.contains(neighbour)) continue
                visited.add(neighbour)

                if (graph[neighbour]!!.flow != 0L) {
                    distanceMap.merge(
                        valve.name,
                        mutableMapOf(neighbour to current.second + 1)
                    ) { i, j -> (i + j).toMutableMap() }
                }
                dequeue.add(Pair(neighbour, current.second + 1))
            }
        }
    }

    println(dfs(30, "AA", mutableSetOf("AA")))
    val combinations = (pow(2.0, open.size.toDouble()) - 1).toInt()

    println(combinations)

    var maxval = 0L
    for (i in 1 until combinations / 2 + 1) {
        val setOne = mutableSetOf("AA")
        val setTwo = mutableSetOf("AA")

        // https://stackoverflow.com/questions/6999460/algorithm-for-all-possible-ways-of-splitting-a-set-of-elements-into-two-sets
        // so using a binary number to split the set of open valves into 2,
        var binary = i.toString(2).padStart(open.size, '0')
        for (k in 0 until open.size) {
            if (binary[k] == '0') {
                setOne.add(open[k])
            } else {
                setTwo.add(open[k])
            }
        }
        maxval = max(maxval, dfs(26, "AA", setOne) + dfs(26, "AA", setTwo))
    }

    println(maxval)

}

private var distanceMap: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
private var graph = mutableMapOf<String, Valve>()
private var cash : MutableMap<Triple<MutableSet<String>, String, Int>, Long> = mutableMapOf()

private fun dfs(time: Int, valve: String, hist: MutableSet<String>): Long {

    if (cash.contains(Triple(hist, valve, time))) return cash[Triple(hist, valve, time)]!!

    var maxFLow = 0L
    for (neighbour in distanceMap[valve]!!) {

        val timeRemaining = time - neighbour.value - 1
        val valveName = neighbour.key

        if (timeRemaining <= 0) continue
        if (hist.contains(valveName)) continue

        val flowRate = graph[valveName]!!.flow * timeRemaining

        val local = hist.toMutableSet()
        local.add(valveName)

//        println(valveName)
        maxFLow = max(maxFLow, flowRate + dfs(timeRemaining, valveName, local))
    }
    var state = Triple(hist, valve, time)
    cash.put(state, maxFLow)

    return maxFLow
}
class Valve(var name: String, var flow: Long) {
    var valves: MutableList<String> = mutableListOf()
    override fun toString(): String {
        return "name: $name flow: $flow valves: $valves"
    }
}

private fun setAllPairsDistances(distance: Array<IntArray>) {
    for (k in distance.indices) {
        for (i in distance.indices) {
            for (j in distance.indices) {
                if (distance[i][j] > distance[i][k] + distance[k][j]) {
                    distance[i][j] = distance[i][k] + distance[k][j]
                }
            }
        }
    }
}




