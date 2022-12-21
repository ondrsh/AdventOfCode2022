package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.other
import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/21").readLines()
    val nums = mutableMapOf<String, Long>()
    val children = mutableMapOf<String, Pair<String, String>>()
    val ops = mutableMapOf<String, Char>()
    
    lines.forEach {
        val (cur, rest) = it.split(": ")
        if (rest.any { it.isDigit() }) nums[cur] = rest.toLong()
        else {
            children[cur] = rest.take(4) to rest.takeLast(4)
            ops[cur] = rest[5]
        }
    }
    
    var trailToHuman = listOf<String>()
    fun goDown(cur: String, trail: List<String> = listOf()): Long {
        if (cur == "humn") trailToHuman = trail.drop(1) + "humn"
        return if (cur in nums) nums[cur]!!
        else {
            val (left, right) = children[cur]!!
            val k1 = goDown(left, trail + cur)
            val k2 = goDown(right, trail + cur)
            nums[left] = k1
            nums[right] = k2
            when (ops[cur]!!) {
                '+'  -> k1 + k2
                '-'  -> k1 - k2
                '*'  -> k1*k2
                else -> k1/k2
            }
        }
    }
    
    val ans1 = goDown("root")
    val toEqual = nums[children["root"]!!.other(trailToHuman.first())]!!
    val ans2 = trailToHuman.windowed(2).fold(initial = toEqual) { acc, (cur, next) ->
        val other = nums[children[cur]!!.other(next)]!!
        val isLeft = children[cur]?.first == next
        when (ops[cur]!!) {
            '+'  -> acc - other
            '-'  -> if (isLeft) acc + other else other - acc
            '*'  -> acc/other
            else -> if (isLeft) acc*other else other/acc
        }
    }
    
    println("ans1: $ans1")
    println("ans2: $ans2")
}