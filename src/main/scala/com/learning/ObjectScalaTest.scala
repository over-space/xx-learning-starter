package com.learning

import java.util.Date

object ObjectScalaTest {

  println("scala object....1")

  val classScalaTest: ClassScalaTest = new ClassScalaTest("abc")

  def main(args: Array[String]): Unit = {
    println("hello world!")

    classScalaTest.map()

    for (i <- 1 to 10) {
      println(i);
    }

    for (i <- 1 until 10) {
      println(i)
    }

    for (i <- 1 to 9) {
      for (j <- 1 to 9) {
        if (j <= i) {
          print(s"$j * $i = ${i * j}\t")
        }
      }
      println();
    }
    println("-------------------------------")
    for (i <- 1 to 9; j <- 1 to 9 if (j <= i)) {
      print(s"$j * $i = ${i * j}\t")
      if (j == i) {
        println()
      }
    }

    def fun01(a: Int, b: Int): Int = {
      a * b
    }

    val v1 = fun01(2, 3)
    println(s"v1: $v1")

    var fun02 = (a: Int, b: Int) => {
      a + b
    }
    var fun03: (Int, Int) => Int = (a: Int, b: Int) => {
      a + b
    }
    var v2 = fun02(1, 2);
    var v3 = fun03(2, 3);
    println(s"v2: $v2")
    println(s"v3: $v3")

    def fun04(a: Int): Int = {
      if (a == 1) {
        a
      } else {
        a * fun04(a - 1)
      }
    }

    var v4 = fun04(5);
    println(s"v4: $v4")

    def fun05(a: Date, b: String, c: String): Unit = {
      println(s"a:$a, b:$b, c:$c")
    }

    fun05(new Date(), "info", "hello");
    fun05(new Date(), "error", "hello");

    var info = fun05(_: Date, "info", _: String);
    var error = fun05(_: Date, "error", _: String)
    info(new Date(), "info msg")
    error(new Date(), "error msg")

    var seq = 1 to 10;
    seq.foreach(a => print(a))
    println()

    def computer(a: Int, b: Int, fun: (Int, Int) => Int): Unit = {
      val v = fun(a, b)
      println(s"computer : $v")
    }

    computer(3, 8, (a: Int, b: Int) => {
      a + b
    })
    computer(3, 8, _ + _)
    computer(3, 8, (a: Int, b: Int) => {
      a * b
    })
    computer(3, 8, _ * _)

    def factory(op: String): (Int, Int) => Int = {
      if ("+".equals(op)) {
        (a: Int, b: Int) => {
          a + b
        }
      } else if ("-".equals(op)) {
        (a: Int, b: Int) => {
          a - b
        }
      } else if ("*".equals(op)) {
        _ * _
      } else {
        _ / _
      }
    }

    computer(2, 4, factory("+"))
    computer(2, 4, factory("-"))
    computer(2, 4, factory("*"))
    computer(5, 3, factory("/"))

    // 柯里化
    def fun06(a: Int)(b: String)(c: String): Unit = {
      println(s"$a\t$b\t$c")
    }

    fun06(1)("hello")("world");

    def fun07(a: Int*)(b: String*): Unit = {
      a.foreach(v => print(s"$v\t"))
      b.foreach(v => print(s"$v\t"))
      println()
    }

    fun07(1, 2, 3)("a", "b", "c");

    def fun08(): Unit = {
      println("===========================")
    }

    var fun09 = fun08
    var fun10 = fun08 _


  }

  println("scala object....2")

}
