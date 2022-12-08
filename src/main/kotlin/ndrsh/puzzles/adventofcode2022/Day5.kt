package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.getAllInts
import ndrsh.puzzles.removeLast
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/5").readLines()
	val stacks = lines.maxOf { it.length }.let { (0..it/4).map { mutableListOf<Char>() } }
	
	lines.takeWhile { it.isNotEmpty() }.reversed().drop(1).forEach {
		it.forEachIndexed { i, c -> if (c.isUpperCase()) { stacks[i/4].add(c)} }
	}
	lines.filter { it.contains("move") }.forEach {
		val (n, from, to) = it.getAllInts()
		stacks[to-1].addAll(stacks[from-1].removeLast(n).reversed()) // for part 2, drop .reversed()
	}
	println(stacks.map { it.last() }.joinToString(""))
}

