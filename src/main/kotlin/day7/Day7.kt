fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputExample = getResourceAsText("inputExample.txt")
    val inputLines = input.lines().drop(1)
    var folder = Folder("/", null, 0);

    var workFolder = folder

    var dirs = mutableListOf<Folder>()

    for (line in inputLines) {
        when {
            line.equals("$ cd ..") ->   workFolder = workFolder.parentFolder!!
            Regex("cd.*").matches(line.drop(2)) -> workFolder = handleFolderMove(workFolder, line, dirs)
            line.startsWith("dir") -> continue
            line.startsWith("$ ls") -> continue
            line[0].isDigit() -> handleFile(line, workFolder)
            else -> println("should not happen") // sanity check
        }
    }

    var sizes :MutableMap<String, Long> = mutableMapOf()
    calculateFolderSize(folder, sizes)

    val freeSpace = 70000000 - folder.size
    println("Answer A: " + dirs.filter { it.size <= 100000 }.sumOf { it.size })
    println("Answer B: " + dirs.filter { it.size >= 30000000 - freeSpace }.minOf { it.size })

    //This a bit overkill should have calculated it in parsing the input. Was initial set up.
    //Took me a while to realise name of folder was not unique.
    println("Recursive Answer A: " + sizes.map { it.value }.filter { it <= 100000 }.sum())
    println("Recursive Answer B: " + sizes.map { it.value }.filter { it >= 30000000 - freeSpace }.min())
}

private fun handleFolderMove(workFolder: Folder, line: String, dirs: MutableList<Folder>): Folder {
    val folderName =
        workFolder.name + "/" + line.drop(5) // also use the name of the parentFolder as folder names are not unique.
    val newFolder = Folder(folderName, workFolder, 0)
    workFolder.folders.add(newFolder)
    dirs.add(newFolder)
    return newFolder
}

private fun handleFile(line: String, workFolder: Folder) {
    var fileNameAndSize = line.split(" ")
    val size = fileNameAndSize[0].toLong()
    val name = fileNameAndSize[1]
    val file = File(name, size)

    workFolder.files.add(file)
    workFolder.size += size;

    //size should be updated to folders above as well.
    var parent = workFolder.parentFolder
    while (parent != null) {
        parent.size += size
        parent = parent.parentFolder
    }
}

fun calculateFolderSize(folder: Folder, sizes: MutableMap<String, Long>) {
    var size = folder.files.sumOf { it.size }
    sizes.merge(folder.name, size, sum)

    //size should be updated to folders above as well.
    var workFolder = folder.parentFolder
    while (workFolder != null) {
        sizes.merge(workFolder.name, size, sum)
        workFolder = workFolder.parentFolder
    }
    folder.folders.forEach { calculateFolderSize(it, sizes) }
}

val sum: (x: Long, y: Long) -> Long = { x, y -> x + y }

class Folder(var name: String, var parentFolder: Folder? = null, var size: Long) {
    var folders: MutableList<Folder> = mutableListOf()
    var files: MutableList<File> = mutableListOf()
    override fun toString(): String {
        return "name: $name size: $size"
    }
}

class File(var name: String, var size: Long) {
    override fun toString(): String {
        return "name: $name size: $size"
    }
}








