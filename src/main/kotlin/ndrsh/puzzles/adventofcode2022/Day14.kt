package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/14").readLines()
	val (sandStart, cols) = 500 to 1000
	var (ans1, ans2) = 0 to 0
	
	// mapping each point p to an integer k = p_x + p_y * cols
	val paths: List<List<Int>> = lines.map { it.split(" -> ").map { it.split(",").map { it.toInt() }.let { it.first() + it.last() * cols } } }
	val rows = paths.flatten().max()/cols + 2
	
	// for each path, take 2 points, sort them and add every point in between them. continue until path ends
	val points = paths.flatMapTo(mutableSetOf()) { it.windowed(2).flatMap { it.sorted().let { (l, h) -> (l..h step if (l%cols == h%cols) cols else 1) } } }
	val field = BooleanArray((rows + 1)*(cols + 1)) { it in points }
	val lowest = points.max()
	val bottom = (rows*cols until (rows + 1)*cols)
	bottom.forEach { field[it] = true }
	
	while (true) {
		var k = sandStart
		while (k < cols*rows) {
			k += when {
				!field[k + cols]     -> cols
				!field[k + cols - 1] -> cols - 1
				!field[k + cols + 1] -> cols + 1
				else                 -> { field[k] = true; break }
			}
		}
		if (k > lowest && ans1 == 0) ans1 = ans2
		if (field[sandStart]) break
		ans2++
	}
	println(ans1)
	println(++ans2)
}