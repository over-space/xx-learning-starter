package com.learning.spark

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("word_count_by_scala");
    conf.setMaster("local");

    val context = new SparkContext(conf)

    val fileRDD = context.textFile("resources/data/word.txt")

    val words = fileRDD.flatMap((x) => x.split(" "))

    val pairWord = words.map((x) => new Tuple2(x, 1))

    val result = pairWord.reduceByKey((total, value) => total + value)

    result.foreach(println);
  }

}
