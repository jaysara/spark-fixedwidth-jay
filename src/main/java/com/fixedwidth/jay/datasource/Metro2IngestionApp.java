package com.fixedwidth.jay.datasource;

import java.util.Arrays;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

/**
 * process data from a fixed width file
 * 
 * @author jgp
 */
public class Metro2IngestionApp {
  public static void main(String[] args) {
    Metro2IngestionApp app = new Metro2IngestionApp();
    app.start();
//    try {
//      Thread.sleep(100000);
//    } catch (InterruptedException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
  }

  /**
   * Starts the application
   * 
   * @return <code>true</code> if all is ok.
   */
  private boolean start() {
    // Get a session
    SparkSession spark = SparkSession.builder()
        .appName("Metro2 to Dataset")
        .master("local").getOrCreate();
    // runjsonfile(spark);
    // Import directory
    String importDirectory = "data";

    // read the data
    Dataset<Row> df = spark.read()
        .format("metro2")
        .option("recursive", "true")
        .option("limit", "100000")
        .option("extensions", "txt,dat")
        .load(importDirectory);

    System.out.println("I have imported " + df.count() + " records.");
    df.printSchema();
    df.show(5);

    // df.foreach((ForeachFunction<Row>) row -> {
    // System.out.println(new String( (byte[])row.get(row.fieldIndex("segment"))));

    Dataset<Person> persontSet = df.map(mapFunction, Encoders.bean(Person.class));
    Dataset<Person> filtered = persontSet.filter(filterFunction);
    filtered.write().mode("overwrite").json("/Users/jay/TEMP/sparkoutput");

    filtered.orderBy(desc("idString")).show();
    // Shows at most 5 rows from the dataframe
    // System.out.println("Total rows flatMapped "+flatMapped.count());
    // filtered.show(5);

    // row.get(row.fieldIndex("StartOffset"))+
    return true;
  }

  static FilterFunction<Person> filterFunction = new FilterFunction<Person>() {

    @Override
    public boolean call(Person value) throws Exception {
      return value.getGender().trim().equals("Female");
    }
    
  };
  
  static MapFunction<Row, Person> mapFunction = new MapFunction<Row,Person>() {

    @Override
    public Person call(Row row) throws Exception {
      return Person.createPersonFromString(new String((byte[]) row.get(row.fieldIndex("segment"))));
    }
    
  };

  /**
   * Following function is for testing purpose to compare the spark distribution behavior. Not related to the fixed with data source.
   * You can ignore this.
   * @param spark
   */
  private void runjsonfile(SparkSession spark) {
    String redditFile = "/Users/jay/Workspace/SparkExample/sparkwithjava-master/project6/data/realsmall.json"; // <-
                                                                                                               // change
                                                                                                               // your
                                                                                                               // file
                                                                                                               // location

    Dataset<Row> redditDf = spark.read().format("json")
        .option("inferSchema", "true") // Make sure to use string version of true
        .option("header", true)
        .load(redditFile);

    redditDf = redditDf.select("body");
    Dataset<String> wordsDs = redditDf.flatMap(
        (FlatMapFunction<Row, String>) r -> Arrays
            .asList(r.toString().replace("\n", "").replace("\r", "").trim().toLowerCase()
                .split(" "))
            .iterator(),
        Encoders.STRING());

    Dataset<Row> wordsDf = wordsDs.toDF();

    Dataset<Row> boringWordsDf = spark.createDataset(Arrays.asList(WordUtils.stopWords), Encoders.STRING()).toDF();

    // wordsDf = wordsDf.except(boringWordsDf); // <-- This won't work because it
    // removes duplicate words!!

    wordsDf = wordsDf.join(boringWordsDf, wordsDf.col("value").equalTo(boringWordsDf.col("value")), "leftanti");

    wordsDf = wordsDf.groupBy("value").count();
    wordsDf.orderBy(desc("count")).show();

  }
}
