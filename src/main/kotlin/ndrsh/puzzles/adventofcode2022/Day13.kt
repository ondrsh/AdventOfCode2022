package ndrsh.puzzles.adventofcode2022

import java.io.File

fun main(args: Array<String>) {
	val lines = File("/home/ndrsh/software/adventofcode/2022/13").readLines()
	val pairs = lines.windowed(2, 3)
	
	val compare: (String, String) -> Int = { s1: String, s2: String -> if (inOrder(s1.toMutableList(), s2.toMutableList())) -1 else 1 }
	val ans1 = pairs.mapIndexed { i, packets -> if (compare(packets.first(), packets.last()) != 1) i + 1 else 0 }.sum()
	println(ans1)
	
	val packets = pairs.flatten() + "[[2]]" + "[[6]]"
	val sorted = packets.sortedWith(compare)
	val ans2 = (1 + sorted.indexOf("[[2]]"))*(1 + sorted.indexOf("[[6]]"))
	println(ans2)
}

fun inOrder(l: MutableList<Char>, r: MutableList<Char>): Boolean {
	val (lk, rk) = l.getNumber() to r.getNumber()
	if (l[0] == '[' && rk != null) r.addBrackets(1+rk/10)
	if (r[0] == '[' && lk != null) l.addBrackets(1+lk/10)
	return when {
		l[0] == ']' && r[0] != ']' -> true
		l[0] != ']' && r[0] == ']' -> false
		lk == (rk ?: -1)           -> inOrder(l.subList(1+lk/10, l.size), r.subList(1+rk!!/10, r.size))
		lk != null && rk != null   -> lk < rk
		else                       -> inOrder(l.subList(1, l.size), r.subList(1, r.size))
	}
}

fun MutableList<Char>.addBrackets(len: Int) {
	add(len, ']')
	add(0, '[')
}

fun List<Char>.getNumber(): Int? = if (!first().isDigit()) null else takeWhile { it.isDigit() }.joinToString("").toInt()