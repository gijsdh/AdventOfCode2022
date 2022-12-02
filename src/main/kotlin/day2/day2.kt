fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val input2 = getResourceAsText("inputExample.txt")
    val inputLines = input.lines().map { it.split(" ").map { it.trim() } }
    println(inputLines)

    var sum = 0
    var sumB = 0
    for (line in inputLines) {
        sum += count(line[0], line[1])
        sumB += countB(line[0], line[1])
    }
    println("answer A " + sum)
    println("answer B " + sumB)

    // Answer with regex
    sum = 0
    sumB = 0
    for (line in input.lines()) {
        sum += countRegex(line)
        sumB += countRegexB(line)
    }

    println("answer A (regex)" + sum)
    println("answer B (regex)" + sumB)
}

fun countRegex(s: String): Int {
    var counter = 0
    if (s.matches(Regex(".*[X]"))) counter += 1
    if (s.matches(Regex(".*[Y]"))) counter += 2
    if (s.matches(Regex(".*[Z]"))) counter += 3
    if (s.matches(Regex("[A] [X]|[B] [Y]|[C] [Z]"))) counter += 3
    if (s.matches(Regex("[A] [Y]|[B] [Z]|[C] [X]"))) counter += 6
    return counter
}

fun countRegexB(s: String): Int {
    if (s.matches(Regex("[A] [Z]|[B] [Y]|[C] [X]"))) return countRegex(s[0] + " Y")
    if (s.matches(Regex("[B] [Z]|[C] [Y]|[A] [X]"))) return countRegex(s[0] + " Z")
    if (s.matches(Regex("[C] [Z]|[A] [Y]|[B] [X]"))) return countRegex(s[0] + " X")
    throw RuntimeException();
}

fun count(s: String, s1: String): Int {
    var counter = 0
    if (s1.equals("X")) counter += 1
    if (s1.equals("Y")) counter += 2
    if (s1.equals("Z")) counter += 3
    if ((s.equals("A") && s1.equals("X")) || (s.equals("B") && s1.equals("Y")) || (s.equals("C") && s1.equals("Z"))) counter += 3
    if ((s.equals("A") && s1.equals("Y")) || (s.equals("B") && s1.equals("Z")) || (s.equals("C") && s1.equals("X"))) counter += 6
    return counter
}


fun countB(s: String, s1: String): Int {
    var second = ""
    if (s1.equals("Z")) {
        if (s.equals("A")) second = "Y"
        if (s.equals("B")) second = "Z"
        if (s.equals("C")) second = "X"
    }
    if (s1.equals("Y")) {
        if (s.equals("A")) second = "X"
        if (s.equals("B")) second = "Y"
        if (s.equals("C")) second = "Z"
    }
    if (s1.equals("X")) {
        if (s.equals("A")) second = "Z"
        if (s.equals("B")) second = "X"
        if (s.equals("C")) second = "Y"
    }
    return count(s, second)
}

// X means you need to lose, Y means you need to end the round in a draw, and Z means you

//    A for Rock, B for Paper, and C for Scissors
//    X for Rock, Y for Paper, and Z for Scissors
//    1 for Rock, 2 for Paper, and 3 for Scissors
//   (0 if you lost, 3 if the round was a draw, and 6 if you won)




