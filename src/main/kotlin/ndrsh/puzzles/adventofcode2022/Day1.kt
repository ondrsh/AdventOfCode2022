package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.splitBy
import java.io.File


fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/1").readLines()
	
	val elves = mutableListOf<MutableList<Int>>()
	var c = 0
	elves.add(mutableListOf())
	lines.forEach {
		if (it.none { it.isDigit() }) {
			c++
			elves.add(mutableListOf())
		} else {
			elves[c].add(it.toInt())
		}
	}
	
	lines.splitBy { it == "" }.map { it.map { it.toInt() } }.maxBy { it.sum() }.sum().let { println(it) }
	lines.splitBy { it == "" }.map { it.map { it.toInt() } }.sortedByDescending { it.sum() }.take(3).flatten().sum().let { println(it) }
}