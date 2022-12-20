package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/14").readLines()
    val (sandStart, cols) = 500 to 1000
    var (ans1, ans2) = 0 to 0
    
    // map each point p to an integer k = p_n + p_m * cols
    fun List<Int>.toIntPoint() = this.first() + this.last()*cols
    fun getPointsBetween(a: Int, b: Int) = (a..b step if (a%cols == b%cols) cols else 1)
    
    val paths = lines.map { it.split(" -> ").map { it.getCoords().toIntPoint() } }
    val points = paths.flatMap { it.windowed(2).flatMap { getPointsBetween(it.min(), it.max()) } }.toMutableSet()
    val max = points.max()
    val rows = max/cols + 2
    val field = BooleanArray((rows + 1)*(cols + 1)) { it in points }
    val bottomLine = rows*cols until (rows + 1)*cols
    for (p in bottomLine) field[p] = true
    
    tailrec fun landingPos(k: Int, field: BooleanArray): Int = if (k > field.size) -1 else when {
        !field[k + cols]     -> landingPos(k + cols, field)
        !field[k + cols - 1] -> landingPos(k + cols - 1, field)
        !field[k + cols + 1] -> landingPos(k + cols + 1, field)
        else                 -> { field[k] = true; k }
    }
    
    while (field[sandStart] == false) {
        val target = landingPos(sandStart, field)
        if (target > max && ans1 == 0) ans1 = ans2
        ans2++
    }
    println(ans1)
    println(ans2)
}

fun String.getCoords(): List<Int> = replace(Regex("[^0-9]+"), ";").split(";").filterNot { it.isEmpty() }.map { it.toInt() }