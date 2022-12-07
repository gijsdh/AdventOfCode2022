fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines().drop(1)
    var folder: Folder = Folder("/", null, 0);

    var workFolder = folder

    var dirs = mutableListOf<Folder>()

    for (line in inputLines) {
        if (line.startsWith("$")) {
            if (line.drop(2).equals("cd ..")) {
                workFolder = workFolder.parentFolder!!
            } else if (Regex("cd.*").matches(line.drop(2))) {
                val name = workFolder.name + line.drop(5)
                val newFolder = Folder(name, workFolder, 0)
                workFolder.folders.add(newFolder)
                dirs.add(newFolder)
                workFolder = newFolder
            }
        } else if (line.startsWith("dir")) {

        } else if (Regex("[0-9]{1}").matches(line.subSequence(0, 1))) {
            var fileNameAndSize = line.split(" ")
            val size = fileNameAndSize[0].toLong()
            val name = fileNameAndSize[1]

            val file = File(name, size)

            workFolder.files.add(file)
            workFolder.size+= size;

            var parent = workFolder.parentFolder
            while (parent != null) {
                parent.size += size
                parent = parent.parentFolder
            }
        } else {
            println("should not happen")
        }
    }

    var sizes :MutableMap<String, Long> = mutableMapOf()
    calculateFolderSize(folder, sizes)
    println(sizes.filter {it.value <= 100000 }.map { it.value }.sum())

    println(sizes.filter { i -> !dirs.stream().anyMatch{it.size.equals(i.value) }})
    println(dirs.filter { i -> !sizes.values.stream().anyMatch{it.equals(i.size) }})

    val freeSpace = 70000000 - folder.size
    println("Answer A: " + dirs.filter { it.size <= 100000 }.sumOf { it.size })
    println("Answer B: " + dirs.filter { it.size >= 30000000 - freeSpace }.minOf { it.size })

    println("Recursive Answer A: " + sizes.map { it.value }.filter { it <= 100000 }.sum())
    println("Recursive Answer B: " + sizes.map { it.value }.filter { it >= 30000000 - freeSpace }.min())
}

val sum: (x: Long, y: Long) -> Long = { x, y -> x + y }

fun calculateFolderSize(folder: Folder, sizes: MutableMap<String, Long>) {
    var size = folder.files.sumOf { it.size }
    sizes.merge(folder.name, size, sum)
    var workFolder = folder.parentFolder
    while (workFolder != null) {
        sizes.merge(workFolder!!.name, size, sum)
        workFolder = workFolder.parentFolder
    }
    folder.folders.forEach { calculateFolderSize(it, sizes) }
}


class Folder(var name: String, var parentFolder: Folder? = null, var size: Long) {
    var folders: MutableList<Folder> = mutableListOf()
    var files: MutableList<File> = mutableListOf()
    override fun toString(): String {
        return "name: " + name + " size " + size
    }
}

class File(var name: String, var size: Long) {
    override fun toString(): String {
        return "name: " + name + " size: " + size
    }
}








