package com.learning

import java.util

object CollectionsScalaTest {

  def main(args: Array[String]): Unit = {

    val strList = new util.ArrayList[String]()
    strList.add("a");

    val array = Array[Int](1, 2, 3)
    println(array(0))

    val list = List(1, 2, 3, 4, 5)
    val set = Set(1, 2, 3, 4, 5, 6, 7, 1, 2)
    list.foreach(println)
    set.foreach(println)

    strList.forEach(println)
    array.foreach(println)

    val tuple = Tuple1(1)
    println(s"tuple_1:" + tuple._1)


  }

}
