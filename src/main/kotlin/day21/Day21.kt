fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputMap = input.lines()
        .map { it.split(": ") }
        .map { it[0] to it[1] }
        .toMap()
        .toMutableMap()

    println(inputMap)

    println("AnswerA: ${calculate("root",inputMap )}")

    val split = inputMap["root"]!!.split(" ")
    var one = split[0]
    var two = split[2]


    // MonkeyTwo is stable and does not depend on humn
    val monkeyTwo = calculate(two, inputMap)

    // Number is too big to brute force, trying binarySearch worked as we saw that when we increase humn the number was monkeyTwo was going down.
    var low = 0L
    var high =  47075653900000L
    var mid:Long
    while(low <= high) {
        mid = low + ((high - low) / 2)
        inputMap["humn"] = mid.toString()
        val monkeyOne = calculate(one, inputMap)
        when {
            monkeyTwo - monkeyOne < 0  -> low = mid+1L
            monkeyOne == monkeyTwo  -> { println("AnswerB: $mid"); break }
            else   -> high = mid-1L
        }
    }
}


fun calculate(name: String, map: MutableMap<String, String>): Long {
    var item = map[name]!!
    if (item[0].isDigit()) {
        return item.toLong()
    }
    var items = item.split(Regex(" "))
    return operation(calculate(items[0].trim(), map), calculate(items[2].trim()!!, map), items[1])
}

fun operation(one: Long, two: Long, operation: String): Long {
    return when (operation) {
        "+" -> one + two
        "-" -> one - two
        "/" -> one / two
        "*" -> one * two
       else -> throw Exception("shit")
   }
}






