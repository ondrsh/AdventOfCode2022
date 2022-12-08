package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/4").readLines()
	val ranges = lines.map { it.split(",").map { it.split("-").let { it.first().toInt()..it.last().toInt() } } }
	println(ranges.count { (r1, r2) -> r1.all { it in r2 } || r2.all { it in r1 } })
	println(ranges.count { (r1, r2) -> r1.any { it in r2 } })
}

