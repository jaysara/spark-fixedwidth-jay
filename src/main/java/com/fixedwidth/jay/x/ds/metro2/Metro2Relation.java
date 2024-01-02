package com.fixedwidth.jay.x.ds.metro2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fixedwidth.jay.x.extlib.Metro2Segment;
import com.fixedwidth.jay.x.utils.Schema;
import com.fixedwidth.jay.x.utils.SparkBeanUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.TableScan;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Metro2Relation extends BaseRelation
    implements Serializable, TableScan{

      private static final long serialVersionUID = 4598175080393877334L;
private static transient Logger log =
      LoggerFactory.getLogger(Metro2Relation.class);
private SQLContext sqlContext;
  private Schema schema = null;
    @Override
    public RDD<Row> buildScan() {
        log.debug("-> buildScan()");
        schema();
        List<Metro2Segment> table = collectData();
        @SuppressWarnings("resource")
    JavaSparkContext sparkContext =
        new JavaSparkContext(sqlContext.sparkContext());
    JavaRDD<Row> rowRDD = sparkContext.parallelize(table)
        .map(metro2 -> SparkBeanUtils.getRowFromBean(schema, metro2));

    return rowRDD.rdd();
    }

    private List<Metro2Segment> collectData() {
      String path="/Users/jay/Workspace/SparkExample/net.jgp.books.spark.ch01-master/data/fixed_width.txt";
      //FileUtils.read  
      List<Metro2Segment> list = new ArrayList<Metro2Segment>();
            

      try (FileInputStream fis = new FileInputStream(new File(path))) {

            // remaining bytes that can be read
            // System.out.println( "Remaining bytes that can be read : " + fis.available());

           // 64 a time
            int i =0;
            int blocksize = 64;
            byte[] bytes = new byte[blocksize];
            System.out.println("calling READ");
            // reads 8192 bytes at a time, if end of the file, returns -1
            while (fis.read(bytes) != -1) {

                // convert bytes to string for demo
                // System.out.println(new String(bytes, StandardCharsets.UTF_8));
                Metro2Segment metro2Segment = new Metro2Segment();
                metro2Segment.setLength(blocksize);
                int startOffset = (i*64);
                metro2Segment.setStartOffset(startOffset);
                metro2Segment.setRecordBytes(bytes);
                // System.out.println(new String(bytes, StandardCharsets.UTF_8));
                list.add(metro2Segment);
                bytes = new byte[blocksize];
                // System.out.println("Remaining bytes that can be read : " + fis.available());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      
      
    
        return list;
    }

    @Override
    public StructType schema() {
        if (schema == null) {
      schema = SparkBeanUtils.getSchemaFromBean(Metro2Segment.class);
    }
    return schema.getSparkSchema();
    }

    
    @Override
  public SQLContext sqlContext() {
    return this.sqlContext;
  }

  public void setSqlContext(SQLContext sqlContext) {
    this.sqlContext = sqlContext;
  }

    
}
