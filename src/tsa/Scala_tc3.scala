package tsa

import java.util.Collection
import java.util.HashSet
import java.util.Iterator

class Scala_tc3 {
  def fun1(g: Int): Int =
    {
      var i = 0
      if (g < 2)
        i = 1
      else if (g < 4)
        i = g / 2
      else {}
      return i
    }
  def fun2(b: Int): Int =
    {
      var c = 0
      for (j <- 0 to b) {
        for (k <- 0 to j)
          c += b * b
      }
      return c
    }
  def fun3(d: Int): Int =
    {
      var e = 10
      var D = d
      if (d % 2 == 0) {
        while (d != 0) {
          e = e + 1
          D = D - 1
        }
      } else {
        do {
          e = e * 2
          D = D - 1
        } while (d != 0)
      }
      return e

    }
  private def f11(c: Collection[Integer]) {
    val it: Iterator[Integer] = c.iterator()
    var x = it.next()
    while (it.hasNext()) {
      x = it.next()
      if (it.next() != null) {
        val y = it.next()
        if (x > y) {
        } else {
        }
      }
    }
  }

  private def f51(c1: Collection[Integer], c2: Collection[Integer]) {
    val it1: Iterator[Integer] = c1.iterator()
    val it2: Iterator[Integer] = c2.iterator()

    while (it1.hasNext() && it2.hasNext()) {
      if (it1.next() > it2.next())
        System.out.println("First collection greater than the second.")
      else
        System.out.println("Second collection greater than the first.")
    }
  }

  def main(args: Array[String]) {
    val f = 3
    val l = 5
    val z = 3
    val m = fun1(z * l)
    val n = fun2(f)
    val o = fun3(f)
    val p = n * o

    val container: Collection[Integer] = new HashSet[Integer]()
    val copyContainer: Collection[Integer] = new HashSet[Integer]()

    for (i <- 0 to 10) {
      container.add(i)
      copyContainer.add(i)
    }

    f11(container)
    f51(container, copyContainer)
  }
}
