package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	File("/home/ndrsh/software/adventofcode/2022/7").readLines().let { lines ->
		val root = Dir("/")
		val dirs = mutableListOf(root)
		var cur = root
		
		lines.drop(1).forEach {
			when {
				it.startsWith("$ cd")   -> {
					val name = it.drop(5)
					cur = if (name == "..") cur.parent!! else cur.children.getValue(name)
				}
				it.startsWith("dir")    -> {
					val name = it.drop(4)
					val child = Dir(parent = cur,
					                name = name)
					dirs.add(child)
					cur.children[name] = child
				}
				it.first().isDigit() -> {
					val toAdd = it.split(" ").first().toInt()
					cur.size += toAdd
					var p = cur.parent
					while (p != null) {
						p.size += toAdd
						p = p.parent
					}
				}
			}
		}

		
		val missing = 70000000 - root.size
		println(dirs.filter { it.size <= 100000 }.sumOf { it.size })
		println(dirs.filter { it.size >= 30000000 - missing }.minOf { it.size })
	}
}

class Dir(val name: String,
          val parent: Dir? = null,
          val children: MutableMap<String, Dir> = mutableMapOf(),
          var size: Int = 0)

