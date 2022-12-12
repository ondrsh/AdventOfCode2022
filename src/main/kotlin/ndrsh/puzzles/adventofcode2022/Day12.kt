package ndrsh.puzzles.adventofcode2022

import java.io.File

val arr = File("/home/ndrsh/software/adventofcode/2022/12").readLines().joinToString("").toCharArray()
val cols = 114

fun adjs(k: Int) = buildList {
	if (k - cols > 0) add(k - cols)
	if (k + cols < arr.size) add(k + cols)
	if (k%cols != 0) add(k - 1)
	if (k%cols != cols - 1) add(k + 1)
}

fun main(args: Array<String>) {
	val start = arr.indexOf('S')
	val ans1 = bfs(start)
	setAllStarts(start)
	val ans2 = arr.indices.filter { arr[it] == 'S' }.map { bfs(it) }.min()
	println(ans1)
	println(ans2)
}

fun setAllStarts(start: Int) {
	arr[start] = 'S'
	adjs(start).filter { arr[it] == 'a' }.forEach { setAllStarts(it) }
}

private fun bfs(start: Int): Int {
	val steps = IntArray(arr.size) { Int.MAX_VALUE }.apply { set(start, 0) }
	var ans = arr.size
	val queue = mutableListOf(start)
	while (queue.isNotEmpty()) {
		val k = queue.removeFirst()
		val step = 1 + steps[k]
		if (adjs(k).any { arr[it] == 'E' } && arr[k] >= 'y') ans = minOf(ans, step)
		adjs(k).filter { step < minOf(ans, steps[it]) && (arr[it] <= arr[k] + 1 || arr[k] == 'S') }
				.forEach {
					steps[it] = step
					queue.add(it)
				}
	}
	return ans
}