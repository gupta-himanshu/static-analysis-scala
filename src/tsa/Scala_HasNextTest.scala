package tsa

import java.util.Collection
import java.util.HashSet
import java.util.Iterator

class Scala_HasNextTest {

	def main(args: Array[String]) {
		val container:Collection[Integer] = new HashSet[Integer]()
		val copyContainer:Collection[Integer] = new HashSet[Integer]()

		for(i <- 0 to 10)
		{
			container.add(i)
			copyContainer.add(i)
		}	
		
		val hnt: Scala_HasNextTest = new Scala_HasNextTest()
		hnt.f1(container)
		hnt.f5(container, copyContainer)
	}
	
	def f1(c: Collection[Integer]){
		val it1: Iterator[Integer] = c.iterator()
		var x = it1.next()
		while(it1.hasNext()){
			x = it1.next()
			if(it1.hasNext()){
				val y = it1.next()
				if(x > y){
					f2(it1, x)
				}else{
					f3(it1, y)
				}
			}
		}
	}
	
	private def f2(it2: Iterator[Integer], i: Int){
		val j = it2.next()
		if(i > j){
			if(it2.hasNext())
				System.out.println(it2.next())
		} else {
			System.out.println(it2.next())
		}				
	}
	
	private def f3(it3: Iterator[Integer], i: Int){
		if(i > 100){
			it3.hasNext()
		}
		System.out.println(it3.next())
		if(it3.hasNext()){
			f4(it3)
			it3.next()
		}
	}
	
	private def f4(it4: Iterator[Integer]){
		while(it4.hasNext()){
			if(it4.next()>200)
				System.out.println("Greater than 200")
		}
	}
	
	
	private def f5(c1: Collection[Integer], c2: Collection[Integer]){
		val it5: Iterator[Integer] = c1.iterator()
		val it6: Iterator[Integer] = c2.iterator()

		while(it5.hasNext() && it6.hasNext()){
			if(it5.next() > it6.next())
				System.out.println("First collection greater than the second.")
			else 
				System.out.println("Second collection greater than the first.")
		}
	}
	
}
