package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.getAllLongs
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/11").readLines()
	lines.solve(part1 = true)
	lines.solve(part1 = false)
}

data class Monkey(val items: MutableList<Long>,
                  var nHandled: Long = 0,
                  val op: (Long) -> Long,
                  val test: (Long) -> Boolean,
                  val ifTrue: Int,
                  val ifFalse: Int)

private fun List<String>.solve(part1: Boolean) {
	val n = if (part1) 20 else 10000
	val k = if (part1) 3 else 1
	val monkeys = mutableListOf<Monkey>()
	val product = parseAndFill(monkeys)
	
	fun Monkey.handle() {
		while (items.isNotEmpty()) {
			val item = items.removeFirst()
			var v = op(item)
			v /= k
			val target = if (test(v)) monkeys[ifTrue] else monkeys[ifFalse]
			target.items.add(v%product)
			nHandled++
		}
	}
	
	repeat(n) {
		monkeys.forEach { it.handle() }
	}
	
	val ans = monkeys.map { it.nHandled }
			.sortedDescending()
			.take(2)
			.reduce(Long::times)
	
	println(ans)
}

private fun List<String>.parseAndFill(monkeys: MutableList<Monkey>): Long {
	var product = 1L
	
	windowed(size = 6, step = 7) { lines ->
		val items = lines[1].getAllLongs().toMutableList()
		val opstr = lines[2].substringAfter("new = old ")
		val divBy = lines[3].split(" ").last().toLong()
		val ifTrue = lines[4].split(" ").last().toInt()
		val ifFalse = lines[5].split(" ").last().toInt()
		product *= divBy
		
		val op: (Long) -> Long = { a ->
			val b = if (opstr.contains("old")) a else opstr.split(" ").last().toLong()
			if (opstr.contains('*')) a*b
			else a + b
		}
		
		monkeys.add(Monkey(items = items,
		                   op = op,
		                   test = { it%divBy == 0L },
		                   ifTrue = ifTrue,
		                   ifFalse = ifFalse))
	}

	return product
}