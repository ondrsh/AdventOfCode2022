package ndrsh.puzzles.adventofcode2022

import java.io.File


fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/3").readLines()
	
	fun Char.score() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27
	
	lines.map {
		it.take(it.length/2).toSet().intersect(it.takeLast(it.length/2).toSet()).first().score()
	}.let { println(it.sum()) }
	
	lines.chunked(3).map {
		it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).first().score()
	}.let { println(it.sum()) }
}