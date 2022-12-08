package ndrsh.puzzles.adventofcode2022

import java.io.File


fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/2").readLines()
	
	lines.sumOf {
		val (him, me) = it.first().code - 'A'.code to it.last().code - 'X'.code
		val score = if (him == me) 3 else if ((him + 1)%3 == me) 6 else 0
		score + me + 1
	}.let { println(it) }
	
	lines.sumOf {
		val (first, second) = it.first().code - 'A'.code to it.last().code - 'X'.code
		1 + when (second) {
			0    -> (first + 2)%3
			1    -> 3 + first
			else -> 6 + (first + 1)%3
		}
	}.let { println(it) }
}
