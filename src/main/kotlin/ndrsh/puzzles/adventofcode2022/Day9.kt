package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.Point
import ndrsh.puzzles.toVec
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/9").readLines()
	solve(lines, 2)
	solve(lines, 10)
}

fun solve(lines: List<String>, nKnots: Int) {
	val start = Point()
	val knots = MutableList(nKnots) { start.copy() }
	val visited = mutableSetOf(start)
	
	lines.forEach {
		val (dirVec, n) = it.split(" ").let { it.first().toVec() to it.last().toInt() }
		repeat(n) {
			knots[0] += dirVec
			for (i in 1 until knots.size) {
				val (tail, head) = knots[i] to knots[i - 1]
				if (tail isNotAdjacentTo head) {
					val vec = head - tail
					knots[i] = head - vec.normalized()
				}
			}
			visited.add(knots.last())
		}
	}
	
	println(visited.size)
}
