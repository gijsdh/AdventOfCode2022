import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines()

    var set: MutableSet<Triple<Int, Int, Int>> = mutableSetOf();


    for (line in inputLines) {
        var split = line.split(",")
        assert(split.size == 3)
        set.add(Triple(split[0].toInt(), split[1].toInt(), split[2].toInt()))
    }

    var sum = 0
    var sum2 = 0
    for (cube in set) {
       sum += checkNeighbours(set, cube, true)
        sum2 += checkNeighbours(set, cube, false)
    }


    println(sum)
    println(sum2)



}


// memoization so we are not checking places muteple times.
var outside: MutableSet<Triple<Int, Int, Int>> = mutableSetOf();
var inside: MutableSet<Triple<Int, Int, Int>> = mutableSetOf();

fun checkOutside(cubes: MutableSet<Triple<Int, Int, Int>>, cube: Triple<Int, Int, Int>): Boolean {
    if (outside.contains(cube)) return true
    if (inside.contains(cube)) return false

    var dequeue: LinkedList<Triple<Int, Int, Int>> = LinkedList<Triple<Int, Int, Int>>();
    dequeue.add(cube)

    var visited: MutableSet<Triple<Int, Int, Int>> = mutableSetOf();
    while (dequeue.isNotEmpty()) {
        var nextCube = dequeue.pollFirst()
        if (cubes.contains(nextCube)) continue
        if (visited.contains(nextCube)) continue

        //Here we can probably also check memoization, if the new node is in outSide or Inside.

        visited.add(nextCube)
        if (visited.size > 5000) {
            for (seen in visited) {
                outside.add(cube);
            }
            return true
        }

        //Flood fill algorithm, we continuously move outwards and check we that we are not in an enclosed area.
        // the visited.size > 5000 is a guess we don't know how big the enclosed area is.
        val ith = intArrayOf(0, 1, -1, 0, 0, 0)
        val jth = intArrayOf(1, 0, 0, -1, 0, 0)
        val kth = intArrayOf(0, 0, 0, 0, 1, -1)
        for (k in ith.indices) {
            val element = Triple(nextCube.first + ith[k], nextCube.second + jth[k], nextCube.third + kth[k])
            dequeue.add(element)
        }
    }
    for (cube in visited) {
        inside.add(cube);
    }
    return false
}


private fun checkNeighbours(
    cubes: MutableSet<Triple<Int, Int, Int>>,
    cube: Triple<Int, Int, Int>,
    part1: Boolean
): Int {
    val ith = intArrayOf(0, 1, -1, 0, 0, 0)
    val jth = intArrayOf(1, 0, 0, -1, 0, 0)
    val kth = intArrayOf(0, 0, 0, 0, 1, -1)
    var sum = 0;
    for (k in ith.indices) {
        val element = Triple(cube.first + ith[k], cube.second + jth[k], cube.third + kth[k])
        if (part1 && !cubes.contains(element)) sum++
        if (!part1 && checkOutside(cubes, element)) sum++
    }
    return sum
}

//private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
//    if (i < 0 || j < 0 || i >= l || j >= k) return false
//    return true
//}




