package ndrsh.puzzles.adventofcode2022

import java.io.File

// positions are represented as ints, a position with coords x,y (where y increases as rocks fall) is represented as k = y*cols + x
typealias Rocks = List<Int>

val m = 20_000
val n = 7
val bottom = m*n
val line = File("/home/ndrsh/software/adventofcode/2022/17").readLines().first()

val rockVecs = listOf(listOf(0, 1, 2, 3),
                      listOf(1, 1 - n, 1 - 2*n, 2 - n, -n),
                      listOf(0, 1, 2, 2 - n, 2 - 2*n),
                      listOf(0, -n, -2*n, -3*n),
                      listOf(0, 1, -n, -n + 1))

fun main(args: Array<String>) {
	val ans1 = State(2022L).simulate()
	val ans2 = State(1000000000000L).simulate()
	println(ans1)
	println(ans2)
}

fun State.firstCycleEnd() = step == line.length
fun State.thirdCycleEnd() = step == line.length*3

// state repeats after each cycle through input, but we wait 2 cycles until we forward because line.length could be odd
tailrec fun State.simulate(): Long {
	if (rockCount == target) return m - row + heightToAdd
	if (firstCycleEnd()) save = Save(rockCount, row)
	return if (thirdCycleEnd() && target != 2022L) forward().simulate()
	else updateRocks().simulate()
}

fun State.forward(): State {
	val rockDiff = rockCount - save.rockCount
	val factor = (target - save.rockCount)/rockDiff
	return copy(heightToAdd = (save.row - row)*(factor - 1),
	            rockCount = save.rockCount + factor*rockDiff,
	            step = step + 1)
}

fun State.rightDir() = line[(step/2)%line.length] == '>'
fun State.jetPushTime() = step and 1 == 1
fun State.updateRocks() = apply {
	rocks = if (jetPushTime()) if (rightDir()) rocks.right() else rocks.left()
	else if (rocks.canFall()) rocks.fall()
	else {
		rocks.forEach { taken[it] = true }
		row = minOf(row, rocks.min()/n)
		rockVecs[(++rockCount%5).toInt()].map { it + getSpawningPoint(row*n) }
	}
	step++
}

data class Save(val rockCount: Long = 0L, val row: Int = 0)

data class State(val target: Long,
                 var rocks: Rocks = rockVecs.first().map { it + getSpawningPoint(bottom) },
                 val taken: BooleanArray = BooleanArray((m + 1)*n),
                 var rockCount: Long = 0,
                 var row: Int = m,
                 var step: Int = 1,
                 var heightToAdd: Long = 0L,
                 var save: Save = Save()) {
	fun Rocks.left() = if (any { it%n == 0 || taken[it - 1] }) this else map { it - 1 }
	fun Rocks.right() = if (any { it%n == n - 1 || taken[it + 1] }) this else map { it + 1 }
	fun Rocks.canFall() = none { it + n > m*n || taken[it + n] }
	fun Rocks.fall() = map { it + n }
}

fun getSpawningPoint(initial: Int) = (initial/n - 4)*n + 2