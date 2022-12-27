package ndrsh.puzzles.adventofcode2022

import ndrsh.puzzles.Point
import java.io.File

fun main(args: Array<String>) {
    val lines = File("/home/ndrsh/software/adventofcode/2022/15").readLines()
    val (sensors, beacons) = lines.map { it.drop(10).split(": closest beacon is at ").map { it.toPoint() }.zipWithNext().single() }.unzip()
    val ranges = sensors.associateWith { s -> beacons.minOf { it manhattanTo s } }
    
    fun Point.anySensorsInRange() = sensors.any { it manhattanTo this <= ranges[it]!! }
    
    val buffer = ranges.values.max()
    val nMin = (sensors + beacons).minOf { it.n }
    val nMax = (sensors + beacons).maxOf { it.n }
    val onLine = (nMin - buffer..nMax + buffer).map { Point(2_000_000, it) }
    val ans1 = onLine.count { it.anySensorsInRange() && it !in beacons }
    println(ans1)
    
    val downRights = sensors.flatMap { listOf(it.m - it.n + ranges[it]!! + 1, it.m - it.n - ranges[it]!! - 1) }
    val upRights = sensors.flatMap { listOf(it.m + it.n + ranges[it]!! + 1, it.m + it.n - ranges[it]!! - 1) }
    val intersections = downRights.flatMap { dr -> upRights.map { ur -> Point(ur + dr, ur - dr)/2 } }
    val target = intersections.first { it.inSearchSpace() && !it.anySensorsInRange() }
    val ans2 = target.n*4_000_000L + target.m
    println(ans2)
}

fun Point.inSearchSpace() = coords().all { it in 0..4_000_000 }