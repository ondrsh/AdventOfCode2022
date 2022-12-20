package ndrsh.puzzles.adventofcode2022

import java.io.File


fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/2").readLines()
    
    // 0=Rock, 1=Paper, 2=Scissors
    fun String.strategyGuide(): Pair<Int, Int> = first().code - 'A'.code to last().code - 'X'.code
    
    val ans1 = lines.sumOf {
        val (him, me) = it.strategyGuide()
        val score = when {
            him == me         -> 3
            (him + 1)%3 == me -> 6
            else              -> 0
        }
        score + me + 1
    }
    
    val ans2 = lines.sumOf {
        val (first, second) = it.strategyGuide()
        1 + when (second) {
            0    -> (first + 2)%3
            1    -> 3 + first
            else -> 6 + (first + 1)%3
        }
    }
    
    println(ans1)
    println(ans2)
}
