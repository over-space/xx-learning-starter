package com.learning

class ClassScalaTest(value: String) {

  var i = 5;
  i = 10;

  val n = 10;

  println("scala class, a")

  def map(): Unit = {
    println("scala class, hello scala.....")
  }

  println(s"scala class, b ${value}, i=${i}, n=${n + 1}")
}
