package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.Location
import ndrsh.puzzles.Point
import ndrsh.puzzles.lcm
import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/24").readLines()
    val rs = lines.size - 2
    val cs = lines[0].length - 2
    val lcm = lcm(rs.toLong(), cs.toLong()).toInt()
    val freeStates = Array(lcm) { Array(rs) { BooleanArray(cs) { true } } }
    
    data class Situation(val p: Point, val minute: Int)
    data class Blizzard(val p: Point, val dir: Char) : Location by p {
        fun move() = copy(p = when (dir) {
            'v'  -> p.down().mod(rs, cs)
            '^'  -> p.up().mod(rs, cs)
            '>'  -> p.right().mod(rs, cs)
            else -> p.left().mod(rs, cs)
        })
    }
    
    val blizzards = lines.drop(1).dropLast(1).flatMapIndexed { m, line ->
        line.drop(1).dropLast(1).mapIndexedNotNull { n, c ->
            if (c == '.') null else Blizzard(Point(m, n), c)
        }
    }.toMutableList()
    
    for (i in 0 until lcm) {
        for (j in blizzards.indices) {
            val b = blizzards.removeAt(j)
            freeStates[i][b.m][b.n] = false
            blizzards.add(j, b.move())
        }
    }
    
    fun solve(start: Point, end: Point, minutes: Int = 0): Int {
        val queue = mutableListOf(Situation(start, minutes))
        val best = mutableMapOf<Situation, Int>()
        while (true) {
            val e = queue.removeFirst()
            if (e.p == end) return e.minute
            val s = Situation(e.p, e.minute.mod(lcm))
            if (e.minute >= (best[s] ?: Int.MAX_VALUE)) continue
            else best[s] = e.minute
            val state = freeStates[(e.minute + 1).mod(lcm)]
            if (e.p == start || state[e.p]) queue.add(e.copy(minute = e.minute + 1))
            e.p.cardinalAdjacents().filter { it == end || state[it] }.forEach {
                queue.add(Situation(p = it,
                                    minute = e.minute + 1))
            }
        }
    }
    
    val start = Point(-1, 0)
    val end = Point(rs, cs - 1)
    val ans1 = solve(start, end)
    val ans2 = solve(start, end, solve(end, start, ans1))
    println(ans1)
    println(ans2)
}

operator fun Array<BooleanArray>.contains(loc: Location) = loc.m in indices && loc.n in 0 until first().size
operator fun Array<BooleanArray>.get(loc: Location) = if (loc !in this) false else get(loc.m)[loc.n]
operator fun Array<BooleanArray>.set(loc: Location, value: Boolean) { get(loc.m)[loc.n] = value }