import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

public class CompareParquetFiles {
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length < 2) {
            System.err.println("Usage: CompareParquetFiles <s3_path_file1> <s3_path_file2>");
            System.exit(1);
        }

        String s3PathFile1 = args[0];
        String s3PathFile2 = args[1];

        // Initialize Spark session
        SparkSession spark = SparkSession.builder()
                .appName("Compare Parquet Files")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .getOrCreate();

        // Read Parquet files into DataFrames
        Dataset<Row> df1 = spark.read().parquet(s3PathFile1);
        Dataset<Row> df2 = spark.read().parquet(s3PathFile2);

        // Check if schemas match
        StructType schema1 = df1.schema();
        StructType schema2 = df2.schema();

        if (!schema1.equals(schema2)) {
            System.err.println("Schemas do not match!");
            System.out.println("Schema of File 1: " + schema1.prettyJson());
            System.out.println("Schema of File 2: " + schema2.prettyJson());
            System.exit(1);
        }

        // Find differences between the two DataFrames
        Dataset<Row> df1Only = df1.except(df2);
        Dataset<Row> df2Only = df2.except(df1);

        // Show differences if any
        if (!df1Only.isEmpty()) {
            System.out.println("Rows present in File 1 but not in File 2:");
            df1Only.show(false);
        } else {
            System.out.println("No rows are exclusive to File 1.");
        }

        if (!df2Only.isEmpty()) {
            System.out.println("Rows present in File 2 but not in File 1:");
            df2Only.show(false);
        } else {
            System.out.println("No rows are exclusive to File 2.");
        }

        // Stop the Spark session
        spark.stop();
    }
}
