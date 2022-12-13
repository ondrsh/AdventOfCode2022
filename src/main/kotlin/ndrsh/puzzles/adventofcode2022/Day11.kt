package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.getAllLongs
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/11").readLines()
	solve(lines, part1 = true)
	solve(lines, part1 = false)
}

data class Monkey(val items: MutableList<Long>,
                  var nHandled: Long = 0,
                  val op: (Long) -> Long,
                  val divBy: Long,
                  val ifTrue: Int,
                  val ifFalse: Int)

private fun solve(lines: List<String>, part1: Boolean) {
	val n = if (part1) 20 else 10000
	val k = if (part1) 3 else 1
	val monkeys: List<Monkey> = lines.windowed(size = 6, step = 7).map {
		val opString = it[2].substringAfter("new = old ")
		Monkey(items = it[1].getAllLongs().toMutableList(),
		       divBy = it[3].split(" ").last().toLong(),
		       ifTrue = it[4].split(" ").last().toInt(),
		       ifFalse = it[5].split(" ").last().toInt(),
		       op = { a: Long ->
			       val b = if (opString.contains("old")) a else opString.split(" ").last().toLong()
			       if (opString.contains('*')) a*b
			       else a + b
		       })
	}
	val product = monkeys.map { it.divBy }.reduce(Long::times)
	
	repeat(n) {
		monkeys.forEach {
			while (it.items.isNotEmpty()) {
				val item = it.items.removeFirst()
				val v = it.op(item)/k
				val target = if (v%it.divBy == 0L) monkeys[it.ifTrue] else monkeys[it.ifFalse]
				target.items.add(v%product)
				it.nHandled++
			}
		}
	}
	
	val ans = monkeys.map { it.nHandled }
			.sortedDescending()
			.take(2)
			.reduce(Long::times)
	
	println(ans)
}