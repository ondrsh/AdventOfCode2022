package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/8").readLines()
	val rows = lines.map { it.map { it.digitToInt() } }
	val columns = List(lines.size) { m -> rows.map { it[m] } }
	val visible = List(lines.size) { MutableList(lines.size) { false } }
	
	fun List<Int>.visibleAfter(i: Int) = drop(i + 1).none { it >= get(i) } || reversed().drop(size - i).none { it >= get(i) }
	
	var scenicScore = -1
	for (i in rows.indices) {
		for (j in columns.indices) {
			visible[i][j] = rows[i].visibleAfter(j) || columns[j].visibleAfter(i)
			scenicScore = maxOf(scenicScore, (i to j).scenicScore(rows[i], columns[j]))
		}
	}
	
	println(visible.sumOf { it.count { it } })
	println(scenicScore)
}

// pair: (x, y) position in matrix
fun Pair<Int, Int>.scenicScore(row: List<Int>, column: List<Int>): Int {
	val right = row.scenicScore(second)
	val left = row.reversed().scenicScore(row.size - 1 - second)
	val down = column.scenicScore(first)
	val up = column.reversed().scenicScore(column.size - 1 - first)
	return right*left*up*down
}

// list: row/column
fun List<Int>.scenicScore(i: Int): Int {
	val rest = drop(i + 1)
	return if (rest.none { it >= get(i) }) rest.count()
	else 1 + rest.takeWhile { it < get(i) }.count()
}