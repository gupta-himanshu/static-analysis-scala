package tsa

import java.util._

class Scala_tc2 {

  def h(f: Int) {
    System.out.println("\ndifference is:" + f)
  }
  def f(g: Int): Int =
    {
      val i = g * g
      return g * g
    }

  def main(args: List[String]) {
    var x = 3
    val z = 5
    x = 4
    var y = 3
    var k = 0
    var j = 0
    var a = 0
    var b = 0
    var c = 0
    var d = 0
    b = 4
    c = 3
    a = b + c
    d = a * b

    if (x < y) {
      b = a - c
      System.out.print(b)
    } else {
      c = b + c
      if (x <= y) {
        do {
          d = a + b
          j = f(b + c)

          y = y + 1
        } while (x > y)
      } else {
        c = a * b
        h(b - c)
      }
    }
    h(a - b)
    // Create an array list
    val al: ArrayList[String] = new ArrayList[String]()
    // add elements to the array list
    al.add("C")
    al.add("A")
    al.add("E")
    al.add("B")
    al.add("D")
    al.add("F")

    // Use iterator to display contents of al
    System.out.print("Original contents of al: ")
    val itr: Iterator[String] = al.iterator()
    while (itr.hasNext()) {
      val element: Object = itr.next()
      if (itr.next() != null) {
        System.out.println(itr.next())
      } else
        System.out.println(itr.next())
      System.out.print(element + " ")
    }
    System.out.println()

  }
}
