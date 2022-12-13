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
	fun rec(ldrop: Int, rdrop: Int) = inOrder(l.subList(ldrop, l.size), r.subList(rdrop, r.size))
	fun MutableList<Char>.addBrackets(len: Int) = apply { add(len, ']'); add(0, '[') }
	fun List<Char>.num(): Int? = if (!this.first().isDigit()) null else this.takeWhile { it.isDigit() }.joinToString("").toInt()
	
	if (l.first() == '[' && r.num() != null) r.addBrackets(r.num().toString().length)
	if (r.first() == '[' && l.num() != null) l.addBrackets(l.num().toString().length)
	
	return when {
		l[0] == ']' && r[0] != ']'         -> true
		l[0] != ']' && r[0] == ']'         -> false
		l.num() == (r.num() ?: -1)         -> rec(l.num().toString().length, r.num().toString().length)
		l.num() != null && r.num() != null -> l.num()!! < r.num()!!
		else                               -> rec(1, 1)
	}
}