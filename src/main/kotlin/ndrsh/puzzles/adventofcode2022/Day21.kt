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
    
    fun goDown(cur: String, trail: List<String>): Pair<Long, List<String>> {
        return if (cur in nums) {
            Pair(nums[cur]!!, if (cur == "humn") trail + "humn" else listOf())
        } else {
            val (left, right) = children[cur]!!
            val k1 = goDown(left, trail + cur)
            val k2 = goDown(right, trail + cur)
            val res = when (ops[cur]!!) {
                '+'  -> k1.first + k2.first
                '-'  -> k1.first - k2.first
                '*'  -> k1.first*k2.first
                else -> k1.first/k2.first
            }
            nums[left] = k1.first
            nums[right] = k2.first
            return Pair(res, k1.second + k2.second)
        }
    }
    
    val (ans1, trail) = goDown("root", listOf())
    val toEqual = nums[children["root"]!!.other(trail[1])]!!
    val ans2 = trail.drop(1).windowed(2).fold(initial = toEqual) { acc, (cur, next) ->
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