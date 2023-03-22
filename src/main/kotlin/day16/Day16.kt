fun main(args: Array<String>) {
    val input = getResourceAsText("input16.txt")
    val inputExample = getResourceAsText("inputExample16.txt")
    val inputLines = inputExample.lines().map { it.split(Regex(" |,|rate=|;")).filter { it.isNotEmpty() } }

    var map = mutableMapOf<String, Valve>()
    for (line in inputLines) {
        var name = line[1]
        var flow = line[4].toLong()
        var valves = mutableListOf<String>()
        for (i in 9 until line.size){ valves.add(line[i])
        }
        var valve = Valve(name, flow)
        valve.valves = valves
        map.put(name, valve)
    }

    // create
    for() {

    }


    var t = 0
    while (t < 26) {

        t++
    }

    println(map)
}


class Valve(var name: String, var flow: Long) {
    var valves: MutableList<String> = mutableListOf()
    override fun toString(): String {
        return "name: $name flow: $flow valves: $valves"
    }
}


//fun computeDistances(map: MutableMap<String, Valve>): Map<String, List<Pair<Valve, Int>>> {
//
//}


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




