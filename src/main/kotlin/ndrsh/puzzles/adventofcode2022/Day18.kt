package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/18").readLines()
	val cubes = lines.map { it.split(",").map { it.toInt() }.let { (x, y, z) -> Point(x, y, z) } }.toSet()
	
	val xRange = cubes.minOf { it.x } until cubes.maxOf { it.x }
	val yRange = cubes.minOf { it.y } until cubes.maxOf { it.y }
	val zRange = cubes.minOf { it.z } until cubes.maxOf { it.y }
	fun Point.outOfRange() = x !in xRange || y !in yRange || z !in zRange
	
	val allPoints = xRange.flatMap { x -> yRange.flatMap { y -> zRange.map { z -> Point(x, y, z) } } }
	val trapped = allPoints.toMutableSet()
	
	tailrec fun Point.removeFromTrapped(start: Point = this, seen: Set<Point> = setOf(start)) {
		if (outOfRange()) trapped.removeAll(seen + this)
		else adjacents().filter { it !in cubes && it !in seen }.forEach {
			return it.removeFromTrapped(start, seen + this)
		}
	}
	
	allPoints.forEach { it.removeFromTrapped() }
	
	val ans1 = cubes.sumOf { it.adjacents().count { it !in cubes } }
	val ans2 = cubes.sumOf { it.adjacents().count { it !in cubes && it !in trapped } }
	println(ans1)
	println(ans2)
}

data class Point(val x: Int, val y: Int, val z: Int) {
	fun adjacents() = listOf(Point(x + 1, y, z), Point(x - 1, y, z), Point(x, y + 1, z), Point(x, y - 1, z), Point(x, y, z + 1), Point(x, y, z - 1))
}