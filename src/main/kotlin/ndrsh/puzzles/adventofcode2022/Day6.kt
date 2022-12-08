package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/6").readLines()
	val part1 = false
	val n = if (part1) 4 else 14
	println((n until lines[0].length).first { lines[0].substring(it-n until it).toSet().size == n })
}