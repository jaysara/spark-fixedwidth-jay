import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import static org.apache.spark.sql.functions.*;
import java.util.*;

public class CompareDatasets {
    public static void main(String[] args) {
        // Initialize Spark Session
        SparkSession spark = SparkSession.builder()
                .appName("Compare Datasets")
                .master("local")
                .getOrCreate();

        // Define schema for the datasets
        StructType schema = new StructType()
                .add("id", DataTypes.IntegerType, false)
                .add("field1", DataTypes.StringType, true)
                .add("field2", DataTypes.DoubleType, true)
                .add("field3", DataTypes.IntegerType, true);

        // Create two example datasets
        Dataset<Row> dataset1 = spark.createDataFrame(
                Arrays.asList(
                        RowFactory.create(1, "A", 1.1, 10),
                        RowFactory.create(2, "B", 2.2, 20),
                        RowFactory.create(3, "C", 3.3, 30)
                ), schema);

        Dataset<Row> dataset2 = spark.createDataFrame(
                Arrays.asList(
                        RowFactory.create(1, "A", 1.1, 15),
                        RowFactory.create(2, "B2", 2.3, 20),
                        RowFactory.create(3, "C", 3.3, 35)
                ), schema);

        // Join the datasets on the ID field
        Dataset<Row> joined = dataset1.alias("ds1")
                .join(dataset2.alias("ds2"), col("ds1.id").equalTo(col("ds2.id")), "inner");

        // Get the list of fields to compare dynamically (excluding ID)
        List<String> fields = Arrays.asList(schema.fieldNames());
        fields.remove("id");

        // Select columns dynamically and compute differences
        Column[] selectColumns = new Column[fields.size() + 1];
        selectColumns[0] = col("ds1.id").alias("id");

        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            selectColumns[i + 1] = when(col("ds1." + field).equalTo(col("ds2." + field)), lit("No Change"))
                    .otherwise(concat_ws(" -> ", col("ds1." + field), col("ds2." + field))).alias(field + "_diff");
        }

        Dataset<Row> differences = joined.select(selectColumns);

        // Show the differences
        differences.show();

        // Stop Spark Session
        spark.stop();
    }
}
