package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.splitBy
import java.io.File


fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/1").readLines()
	
	// separate calories by elf
	val elfCals: List<List<Int>> = lines.splitBy { it.isEmpty() }.map { it.map { it.toInt() } }
	
	val ans1 = elfCals.maxBy { it.sum() }.sum()
	val ans2 = elfCals.sortedByDescending { it.sum() }.take(3).flatten().sum()
	
	println(ans1)
	println(ans2)
}