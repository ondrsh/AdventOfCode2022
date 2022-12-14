package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/14").readLines()
	val (sandStart, cols) = 500 to 1000
	var (ans1, ans2) = 0 to 0
	
	// mapping each point p to an integer k where p_x = k%cols and p_y = k/cols
	val paths: List<List<Int>> = lines.map { it.split(" -> ").map { it.split(",").map { it.toInt() }.let { it.first() + cols*it.last() } } }
	val rows = paths.flatten().max()/cols + 2
	val points = BooleanArray((rows + 1)*(cols + 1))
	
	paths.forEach {
		it.windowed(2).forEach {
			val (l, h) = it.sorted()
			val range = if (l%cols != h%cols) l..h else l..h step cols
			range.forEach { points[it] = true }
		}
	}
	
	val last = points.indices.filter { points[it] }.max()
	val bottom = (rows*cols until (rows + 1)*cols)
	bottom.forEach { points[it] = true }
	
	while (true) {
		var k = sandStart
		while (k < cols*rows) {
			k += when {
				!points[k + cols]     -> cols
				!points[k + cols - 1] -> cols - 1
				!points[k + cols + 1] -> cols + 1
				else                  -> { points[k] = true; break }
			}
		}
		if (k > last && ans1 == 0) ans1 = ans2
		if (points[sandStart]) break
		ans2++
	}
	println(ans1)
	println(++ans2)
}