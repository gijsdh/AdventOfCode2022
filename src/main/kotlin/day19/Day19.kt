import java.lang.Integer.max

fun main(args: Array<String>) {
    val input = getResourceAsText("input19.txt")
    val inputExample = getResourceAsText("inputExample19.txt")
    val inputLines = input.lines().map { it.split(Regex(" |:")).filter { it.isNotEmpty() } }

    var list = mutableListOf<BleuPrint>()
    for (line in inputLines) {
        var id = line[1]
        var robotOre = line[6].toInt()
        var robotClay = line[12].toInt()
        var robotObsidianOre = line[18].toInt()
        var robotObsidianCLay = line[21].toInt()
        var robotGeodeOre = line[27].toInt()
        var robotGeodeObsidian = line[30].toInt()

        var maxOres = intArrayOf(robotOre, robotClay,robotGeodeOre).max()

        list.add(
            BleuPrint(
                id,
                intArrayOf(robotOre, 0, 0, 0),
                intArrayOf(robotClay, 0, 0, 0),
                intArrayOf(robotObsidianOre, robotObsidianCLay, 0, 0),
                intArrayOf(robotGeodeOre,0, robotGeodeObsidian, 0),
                intArrayOf(maxOres, robotObsidianCLay, robotGeodeObsidian)
            )
        )
    }

    println("AnswerA : ${calculate(list, 24).withIndex().map { (it.index + 1) * it.value }.sum()}")
    println("AnswerB : ${calculate(list.take(3).toMutableList(), 32).fold(1L) { i, j -> i * j }}")
}

private fun calculate(list: MutableList<BleuPrint>, time: Int): MutableList<Int> {
    var result = mutableListOf<Int>()
    for (blueprint in list) {
        var maxGeo = 0
        var states = listOf(blueprint.copy())
        var earlyResult = mutableListOf<Int>()
        for (t in 0 until time) {
            var newStates = mutableListOf<BleuPrint>()

            for (state in states) {
                if (optimisticBest(state, time - t, 2) <= state.robotGeode[2]) {
                    earlyResult.add(state.available[3] + state.collect[3] * (time - t))
                    continue
                }

                var buildRobotOptions = state.canBuild(time - t)
                state.produce()
                if (t == time - 1) {
                    maxGeo = maxGeode(states, 3)
                    continue
                }

                for (buildOption in buildRobotOptions) {
                    state.block = BooleanArray(4) { false }
                    var copy = state.copy()
                    copy.buildRobot(buildOption)
                    newStates.add(copy)
                }

                buildRobotOptions.forEach { state.block[it] = true }
                newStates.add(state) //When nothing is being build .
                maxGeo = max(maxGeo, state.available[3])
            }

            states = newStates.filter { optimisticBest(it, time - t - 1, 3) > maxGeo }


//            println("size: ${states.size}")
//            println("time remaining ${time - t}")

        }
        earlyResult.add(maxGeo);
        result.add(earlyResult.max());
    }
    return result
}

private fun maxGeode(newStates: Collection<BleuPrint>, res: Int) = newStates.map { it.available[res] }.max()

class BleuPrint(
    val id: String,
    val robotOre: IntArray,
    val robotClay: IntArray,
    val robotObsidian: IntArray,
    val robotGeode: IntArray,
    var maxNeeded: IntArray,
    var collect: IntArray = intArrayOf(1, 0, 0, 0),
    var available: IntArray = intArrayOf(0, 0, 0, 0),
    var block: BooleanArray = booleanArrayOf(false, false,false,false)
) {

    fun canBuild(timeLeft: Int): List<Int> {
        var list = mutableListOf<Int>()
        if (timeLeft > 15 && available[0] >= robotOre[0] && !block[0] && maxNeeded[0] > collect[0]) list.add(0)
        if (timeLeft > 6 && available[0] >= robotClay[0] && !block[1] && maxNeeded[1] > collect[1] && timeLeft > 6) list.add(1)
        if (timeLeft > 3 && available[0] >= robotObsidian[0] && !block[2] && maxNeeded[2] > collect[2] && available[1] >= robotObsidian[1]) list.add(2)
        if (available[0] >= robotGeode[0] && available[2] >= robotGeode[2]) list.add(3)

        if (list.contains(3)) return listOf(3)
        if (list.contains(2)) return listOf(2)
        return list
    }

    fun copy(): BleuPrint {
        return BleuPrint(id, robotOre, robotClay, robotObsidian, robotGeode, maxNeeded.clone(), collect.clone(), available.clone(), block.clone())
    }

    fun produce() {
        available = plus(available, collect)
    }

    fun buildRobot(buildOption: Int) {
        collect[buildOption]++
        when (buildOption) {
            0 -> available = minus(available, robotOre)
            1 -> available = minus(available, robotClay)
            2 -> available = minus(available, robotObsidian)
            3 -> available = minus(available, robotGeode)
        }
    }

    private fun minus(one: IntArray, two: IntArray): IntArray {
        var array = IntArray(4)
        for (i in one.indices) array[i] = one[i] - two[i]
        return array
    }

    private fun plus(one: IntArray, two: IntArray): IntArray {
        var array = IntArray(4)
        for (i in one.indices) array[i] = one[i] + two[i]
        return array
    }

    override fun toString(): String {
        return "BleuPrint(id='$id', collect=[ ${collect.joinToString { it.toString() }}], available=[${available.joinToString { it.toString() }}]"
    }
}


fun optimisticBest(state: BleuPrint, timeRemaining: Int, resource: Int): Int {
    return state.available[resource] + state.collect[resource] * timeRemaining + timeRemaining * (timeRemaining - 1) / 2
}

//fn optimistic_best(state: &SearchState, material: Material) -> u32 {
//    let mat = material as usize;
//    let i = state.time_remaining;
//
//    state.materials[mat]    // The material that we already have...
//    + state.robots[mat] * i // plus the material that will be generated by the existing robots...
//    + i * (i-1) / 2         // plus the optimistic assumption that one new robot will be added every turn (1 + 2 + ... + i)
//}


//val rx = ("Blueprint (\\d+): Each ore robot costs (\\d+) ore. " +
//        "Each clay robot costs (\\d+) ore. " +
//        "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
//        "Each geode robot costs (\\d+) ore and (\\d+) obsidian.").toRegex()
//
//input.lines().forEach{println(rx.find(it)!!.groupValues)}
