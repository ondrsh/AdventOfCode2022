package ndrsh.puzzles.adventofcode2022


import ndrsh.puzzles.getAllLongs
import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/11").readLines()
	lines.solve(part2 = false)
	lines.solve(part2 = true)
}

private fun List<String>.solve(part2: Boolean) {
	val monkeys = mutableListOf<Monkey>()
	val allDivs = mutableListOf<Long>()
	
	parseAndFill(allDivs, monkeys)
	val mod = allDivs.reduce(Long::times)
	
	fun Monkey.handle() {
		while (items.isNotEmpty()) {
			val item = items.removeFirst()
			var v = op(item)
			v /= if (part2) 1 else 3
			val target = if (test(v)) monkeys[ifTrue] else monkeys[ifFalse]
			target.items.add(v%mod)
			handled++
		}
	}
	
	repeat(if (part2) 10000 else 20) {
		monkeys.forEach { it.handle() }
	}
	
	val ans = monkeys.map { it.handled }.sortedDescending().take(2).reduce(Long::times)
	println(ans)
}

private fun List<String>.parseAndFill(allDivs: MutableList<Long>, monkeys: MutableList<Monkey>) {
	windowed(6, 7) {
		val items = it[1].getAllLongs()
		val opstr = it[2].substringAfter("new = old ")
		val multiplies = opstr.contains('*')
		
		val op: (Long) -> Long = {
			if (opstr.contains("old")) {
				if (multiplies) it*it
				else it + it
			} else {
				opstr.split(" ").last().toLong().let { num ->
					if (multiplies) it*num
					else it + num
				}
			}
		}
		
		val ifTrue = it[4].split(" ").last().toInt()
		val ifFalse = it[5].split(" ").last().toInt()
		val divBy = it[3].split(" ").last().toLong()
		allDivs.add(divBy)
		
		monkeys.add(Monkey(items = items.toMutableList(),
		                   op = op,
		                   test = { it%divBy == 0L },
		                   ifTrue = ifTrue,
		                   ifFalse = ifFalse))
	}
}

data class Monkey(val items: MutableList<Long>,
                  var handled: Long = 0,
                  val op: (Long) -> Long,
                  val test: (Long) -> Boolean,
                  val ifTrue: Int,
                  val ifFalse: Int)