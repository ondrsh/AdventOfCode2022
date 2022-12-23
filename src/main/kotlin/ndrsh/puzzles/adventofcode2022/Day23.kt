package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/23").readLines()
    val n = 500
    val elves = mutableSetOf<Int>()
    val taken = BooleanArray(n*n)
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') {
                val k = (y+n/2)*n + x + n/2
                elves.add(k)
                taken[k] = true
            }
        }
    }
    val adjVecs = intArrayOf(-n, -n + 1, 1, 1 + n, n, n - 1, -1, -1 - n)
    val dirs = listOf(intArrayOf(-n, -n + 1, -n - 1),
                      intArrayOf(n, n + 1, n - 1),
                      intArrayOf(-1, -1 - n, -1 + n),
                      intArrayOf(1, 1 - n, 1 + n))
    
    var ans1 = 0
    var ans2 = 0
    var dirStart = 0
    val toFrom = mutableMapOf<Int, Int>()
    
    // tweaked this for maximum performance. if an elf moves from a to b, set toFrom[b] = a.
    // however, if b is already present in toFrom, remove it. this works because only 2 elves
    // can ever choose the same spot. finally, update the elves by iterating through toFrom.
    fun simulate(): Int {
        toFrom.clear()
        for (e in elves) {
            if (adjVecs.any { taken[it + e] }) {
                for (k in 0..3) {
                    val dir = dirs[(k + dirStart)%4]
                    val target = e + dir[0]
                    if (dir.none { taken[it + e] }) {
                        if (target in toFrom) toFrom.remove(target)
                        else toFrom[target] = e
                        break
                    }
                }
            }
        }
        for (entry in toFrom) {
            elves.add(entry.key)
            taken[entry.key] = true
            elves.remove(entry.value)
            taken[entry.value] = false
        }
        dirStart++
        ans2++
        return toFrom.size
    }
    
    // benchmarked at 71ms on a 12900k
    while (simulate() > 0) {
        if (ans2 == 10) ans1 = elves.fieldSize(n) - elves.count()
    }
    println(ans1)
    println(ans2)
}

fun Set<Int>.fieldSize(cols: Int) = ((max() - min())/cols + 1)*(maxOf { it.mod(cols) } - minOf { it.mod(cols) } + 1)