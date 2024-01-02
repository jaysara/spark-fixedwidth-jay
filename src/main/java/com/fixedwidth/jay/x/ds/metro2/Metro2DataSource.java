package com.fixedwidth.jay.x.ds.metro2;

// import static scala.collection.JavaConverters.mapAsJavaMapConverter;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.RelationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.collection.immutable.Map;

public class Metro2DataSource implements RelationProvider {

    private static Logger log = LoggerFactory.getLogger(
      Metro2DataSource.class);

    @Override
    public BaseRelation createRelation(SQLContext sqlContext, Map<String, String> parameters) {
        log.debug("-> createRelation()");

    // java.util.Map<String, String> optionsAsJavaMap =
    //     mapAsJavaMapConverter(parameters).asJava();

    // Creates a specifif EXIF relation
    Metro2Relation br = new Metro2Relation();
    br.setSqlContext(sqlContext);
    return br;    
        
    }
    
}
