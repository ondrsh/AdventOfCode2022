package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/14").readLines()
	val (sandStart, cols) = 500 to 1000
	var (ans1, ans2) = 0 to 0
	
	val paths = lines.toPaths(cols)
	val points = paths.toPoints(cols)
	val max = points.max()
	val rows = max/cols + 2
	val field = BooleanArray((rows+1)*(cols + 1)) { it in points }
	field.addBottom(rows, cols)
	
	while (field[sandStart] == false) {
		val target = landingPos(sandStart, field, cols)
		if (target > max && ans1 == 0) ans1 = ans2
		ans2++
	}
	println(ans1)
	println(ans2)
}

tailrec fun landingPos(k: Int, field: BooleanArray, cols: Int): Int = if (k > field.size) -1 else when {
	!field[k + cols]     -> landingPos(k + cols, field, cols)
	!field[k + cols - 1] -> landingPos(k + cols - 1, field, cols)
	!field[k + cols + 1] -> landingPos(k + cols + 1, field, cols)
	else                 -> { field[k] = true; k }
}

// mapping each point p to an integer k = p_x + p_y * cols
fun List<String>.toPaths(cols: Int) = map { it.split(" -> ").map { it.split(",").map { it.toInt() }.let { it.first() + it.last()*cols } } }

// for each path, take 2 points, sort them and add every point in between them. continue until path ends
fun List<List<Int>>.toPoints(cols: Int) = flatMapTo(mutableSetOf()) { it.windowed(2).flatMap { it.sorted().let { (l, h) -> (l..h step if (l%cols == h%cols) cols else 1) } } }

fun BooleanArray.addBottom(rows: Int, cols: Int) = (rows*cols until (rows + 1)*cols).forEach { set(it, true) }