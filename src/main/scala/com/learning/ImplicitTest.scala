package com.learning

import java.util

object ImplicitTest {

  def main(args: Array[String]): Unit = {

    var list = new util.ArrayList[Int]();
    list.add(1)
    list.add(2)
    list.add(3)
    list.add(4)
    list.add(5)

    val strList = new util.LinkedList[String]()
    strList.add("a")
    strList.add("b")
    strList.add("c")
    strList.add("d")

    println("-----------隐式转换类------------")
    // 1.
    implicit class AAA[T](list:util.LinkedList[T]){
      def foreach(f:(T) => Unit): Unit = {
        val iter = list.iterator()
        while(iter.hasNext)f(iter.next())
      }
    }
    strList.foreach(println)

    // 2.
    implicit def a[T](list: util.ArrayList[T]) = {
      new KKK(list);
    }
    list.foreach(println)

  }

class KKK[T](list: util.ArrayList[T]) {
    def foreach(f: (T) => Unit): Unit = {
      val iter: util.Iterator[T] = list.iterator()
      while (iter.hasNext) f(iter.next())
    }
  }
}

