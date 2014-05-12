package tsa

import java.util.Collection
import java.util.HashSet
import java.util.Iterator

class Scala_tc1 {
  var c = 2
  def main(args: Array[String]) {
    val container: Collection[Integer] = new HashSet[Integer]()
    for (i <- 0 to 9) {
      container.add(i)
    }
    var count = 0
    var a = 0 
    var b = 1
    val d = 3
    var sum = 0

    val it1: Iterator[Integer] = container.iterator()
    var x = it1.next()
    while (it1.hasNext()) {
      x = it1.next()
      if (it1.next() != null) {
        val y = it1.next()
        if (x > y) {
          System.out.println(x)
        } else {
          System.out.println(y)
        }
      }
    }
    if (count == 0) {
      a = a + b
      b = a + c
      c = c * c
      System.out.println(c)
    }

    sum = series_sum()
    count = a
    System.out.println("Sum of series is : " + sum)
  }

  def series_sum(): Int =
    {
      var sum = 0
      for (i <- 0 to c) {
        sum = sum + i
      }
      return sum
    }
}
