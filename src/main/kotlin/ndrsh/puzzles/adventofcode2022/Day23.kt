package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/23").readLines()
    val (rows, cols) = 500 to 500
    val elves = mutableSetOf<Int>()
    lines.forEachIndexed { m, l -> l.forEachIndexed { n, c -> if (c == '#') elves.add((m + rows/2)*cols + n + cols/2) } }
    val adjacents = intArrayOf(-cols, -cols + 1, 1, 1 + cols, cols, cols - 1, -1, -1 - cols)
    val dirs = listOf(intArrayOf(-cols, -cols + 1, -cols - 1),
                      intArrayOf(cols, cols + 1, cols - 1),
                      intArrayOf(-1, -1 - cols, -1 + cols),
                      intArrayOf(1, 1 - cols, 1 + cols))
    
    var ans1 = 0
    var ans2 = 0
    var dirStart = 0
    val fromTo = mutableMapOf<Int, Int>()
    val toFrom = mutableMapOf<Int, Int>()
    val blocked = mutableSetOf<Int>()
    
    // tweaked this for maximum performance. if an elf moves from a to b, set fromTo[a] = b
    // and increase elvesMoved by 1. if there is a conflict later, delete that entry and decrease
    // elvesMoved by 1. in the end, remove all keys from fromTo from elves and add all values
    fun simulate(): Int {
        fromTo.clear()
        toFrom.clear()
        blocked.clear()
        var elvesMoved = 0
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
                            elvesMoved--
                        } else {
                            fromTo[e] = target
                            toFrom[target] = e
                            elvesMoved++
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
        return elvesMoved
    }
    
    // benchmarked at 182ms on a 12900k
    while (simulate() > 0) {
        if (ans2 == 10) ans1 = elves.fieldSize(cols) - elves.count()
    }
    println(ans1)
    println(ans2)
}

fun Set<Int>.fieldSize(cols: Int) = ((max() - min())/cols + 1)*(maxOf { it.mod(cols) } - minOf { it.mod(cols) } + 1)