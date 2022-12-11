package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.getAllInts
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/5").readLines()
	val cols = lines.first().length
	val stacks = MutableList(1 + cols/4) { "" }
	
	lines.takeWhile { it.isNotEmpty() }
			.reversed()
			.drop(1)
			.forEach { line ->
				line.forEachIndexed { i, char ->
					if (char.isUpperCase()) stacks[i/4] += char.toString()
				}
			}
	
	lines.filter { it.contains("move") }.forEach {
		val (n, from, to) = it.getAllInts()
		stacks[to - 1] += stacks[from - 1].takeLast(n).reversed() // for part 2, drop .reversed()
		stacks[from - 1] = stacks[from - 1].dropLast(n)
	}
	
	val ans = stacks.map { it.last() }.joinToString("")
	println(ans)
}
