package ndrsh.puzzles.adventofcode2022

import java.io.File

// solving runs in 3µs but could be reduced to 1.6µs when using when-statement in sumOf()

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/25").readLines().map { it.reversed() }
    val len = lines.maxOf { it.length }
    val digits = "=-012"
    var k = 0
    var carry = 0
    var ans1 = ""
    while (carry != 0 || k < len) {
        var v = carry + lines.sumOf { if (it.length <= k) 0 else digits.indexOf(it[k]) - 2 }
        carry = (v + 2)/5
        v %= 5
        ans1 = digits[(v + 2)%5] + ans1
        k++
    }
    println(ans1)
}