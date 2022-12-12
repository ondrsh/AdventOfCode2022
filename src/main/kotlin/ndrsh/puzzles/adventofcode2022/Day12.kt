package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.*
import java.io.File
import java.util.PriorityQueue

fun main(args: Array<String>) {
	val mat = File("/home/ndrsh/software/adventofcode/2022/12").readLines().toMutableMatrix()
	println(solve(mat, part2 = false))
	println(solve(mat, part2 = true))
}

fun setStarts(p: Point, mat: MutableList<MutableList<Char>>) {
	mat[p] = 'S'
	p.cardinalAdjacents().filter { it in mat && mat[it] == 'a' }.forEach { setStarts(it, mat) }
}

private fun solve(mat: MutableList<MutableList<Char>>, part2: Boolean): Int {
	val start = mat.points().single { mat[it] == 'S' }
	val end = mat.points().single { mat[it] == 'E' }
	if (part2) setStarts(start, mat)
	val starts = mat.points().filter { mat[it] == 'S' }
	return starts.minOf { dijkstra(it, end, mat) }
}

private fun dijkstra(start: Point, end: Point, mat: List<List<Char>>): Int {
	val minSteps = mat.points()
			.associateWith { Int.MAX_VALUE }
			.toMutableMap()
	minSteps[start] = 0
	val queue = PriorityQueue { a: Point, b: Point -> minSteps[a]!!.compareTo(minSteps[b]!!) }.apply { add(start) }
	var ans = mat.nCols*mat.nRows
	outer@ while (queue.isNotEmpty()) {
		val p: Point = queue.poll()
		val step = minSteps[p]!!
		for (adj in p.cardinalAdjacents().filter { it in mat }) {
			if (adj == end && mat[p] == 'y') {
				ans = minOf(ans, step + 1)
				continue@outer
			}
			if (mat[p] == 'S' || mat[adj] <= mat[p] + 1) {
				if (step - 1 < ans && (minSteps[adj] ?: Int.MAX_VALUE) > step + 1) {
					minSteps[adj] = step + 1
					queue.add(adj)
				}
			}
		}
	}
	return ans
}