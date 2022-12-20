package ndrsh.puzzles.adventofcode2022

import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/10").readLines()
    
    var (x, c, res) = listOf(1, 1, 0)
    val crt = Array(6) { Array(39) { '.' } }
    
    fun handleCycle() {
        val (col, row) = (c - 1)%40 to (c - 1)/40
        if (abs(x - col) <= 1) crt[row][col] = '#'
        if (c%40 == 20) res += x*c
        c++
    }
    
    lines.forEach {
        handleCycle()
        if (it != "noop") {
            handleCycle()
            x += it.split(" ").last().toInt()
        }
    }
    
    println(res)
    crt.forEach { println(it.joinToString("")) }
}
