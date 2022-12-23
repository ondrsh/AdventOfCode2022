package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/23").readLines()
    val n = 500
    val elves = mutableSetOf<Int>()
    lines.forEachIndexed { y, l -> l.forEachIndexed { x, c -> if (c == '#') elves.add(y*n + x + n/2) } }
    val adjacents = intArrayOf(-n, -n + 1, 1, 1 + n, n, n - 1, -1, -1 - n)
    val dirs = listOf(intArrayOf(-n, -n + 1, -n - 1),
                      intArrayOf(n, n + 1, n - 1),
                      intArrayOf(-1, -1 - n, -1 + n),
                      intArrayOf(1, 1 - n, 1 + n))
    
    var ans1 = 0
    var ans2 = 0
    var dirStart = 0
    val fromTo = mutableMapOf<Int, Int>()
    val toFrom = mutableMapOf<Int, Int>()
    val blocked = mutableSetOf<Int>()
    
    // tweaked this for maximum performance. if an elf moves from a to b, set fromTo[a] = b.
    // if there is a conflict later, delete that entry and add that point to a blocked set.
    // in the end, update the elves by looking at the fromTo map
    fun simulate(): Int {
        fromTo.clear()
        toFrom.clear()
        blocked.clear()
        for (e in elves) {
            if (adjacents.any { it + e in elves }) {
                for (k in 0..3) {
                    val dir = dirs[(k + dirStart)%4]
                    val target = e + dir[0]
                    if (target !in blocked && dir.none { it + e in elves }) {
                        if (target in toFrom) {
                            val source = toFrom[target]!!
                            fromTo.remove(source)
                            blocked.add(target)
                        } else {
                            fromTo[e] = target
                            toFrom[target] = e
                        }
                        break
                    }
                }
            }
        }
        for (entry in fromTo) {
            elves.remove(entry.key)
            elves.add(entry.value)
        }
        dirStart++
        ans2++
        return fromTo.size
    }
    
    // benchmarked at 182ms on a 12900k
    while (simulate() > 0) {
        if (ans2 == 10) ans1 = elves.fieldSize(n) - elves.count()
    }
    println(ans1)
    println(ans2)
}

fun Set<Int>.fieldSize(cols: Int) = ((max() - min())/cols + 1)*(maxOf { it.mod(cols) } - minOf { it.mod(cols) } + 1)