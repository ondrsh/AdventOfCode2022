package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.*
import java.io.File

var mat = mutableListOf<MutableList<Char>>()

fun main(args: Array<String>) {
	mat = File("/home/ndrsh/software/adventofcode/2022/12").readLines().toMutableMatrix()
	println(solve(part2 = false))
	println(solve(part2 = true))
}

fun setAllStarts(p: Point) {
	mat[p] = 'S'
	p.cardinalAdjacents().filter { it in mat && mat[it] == 'a' }.forEach { setAllStarts(it) }
}

private fun solve(part2: Boolean): Int {
	val start = mat.points().single { mat[it] == 'S' }
	if (part2) setAllStarts(start)
	return mat.points().filter { mat[it] == 'S' }.minOf { bfs(it) }
}

private fun bfs(start: Point): Int {
	val steps = mat.points().associateWith { Int.MAX_VALUE }.toMutableMap()
	val queue = mutableListOf(start)
	var ans = mat.nCols*mat.nRows
	steps[start] = 0
	while (queue.isNotEmpty()) {
		val p: Point = queue.removeFirst()
		val step = 1 + steps[p]!!
		val adjs = p.cardinalAdjacents().filter { it in mat }
		fun List<Point>.hasEnd() = any { mat[it] == 'E' } && mat[p] >= 'y'
		if (adjs.hasEnd()) ans = minOf(ans, step)
		adjs.filter { mat[it] <= mat[p] + 1 || p == start }
				.filter { it !in steps || step < minOf(ans, steps[it]!!) }
				.forEach {
					steps[it] = step
					queue.add(it)
				}
	}
	return ans
}