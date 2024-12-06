import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

import java.util.*;

public class SparckCSVParseFlatSchema {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("SegmentParser")
                .master("local[*]")
                .getOrCreate();

        // Define paths for the CSV configuration and the input text file
        String csvConfigPath = "path/to/config.csv";
        String textFilePath = "path/to/input.txt";

        // Read the configuration CSV
        Dataset<Row> configDf = spark.read()
                .option("header", true)
                .option("inferSchema", true)
                .csv(csvConfigPath);

        // Parse the configuration to a map of segment definitions
        Map<String, List<FieldDefinition>> segmentConfig = parseConfiguration(configDf);

        // Read the input text file
        Dataset<String> inputLines = spark.read().textFile(textFilePath);

        // Transform the input lines into structured data with a flattened schema
        Dataset<Row> parsedData = inputLines.flatMap(
                line -> parseLine(line, segmentConfig).iterator(),
                Encoders.bean(SegmentRow.class)
        ).toDF();

        // Show the resulting dataset and print its schema
        parsedData.show(false);
        parsedData.printSchema();

        spark.stop();
    }

    // Method to parse the configuration CSV
    private static Map<String, List<FieldDefinition>> parseConfiguration(Dataset<Row> configDf) {
        Map<String, List<FieldDefinition>> segmentMap = new LinkedHashMap<>();
        List<Row> rows = configDf.collectAsList();
        String currentSegment = null;
        List<FieldDefinition> fields = new ArrayList<>();

        for (Row row : rows) {
            String dataGroup = row.getAs("Data Group:");
            String outputName = row.getAs("Output Name:");
            Integer len = row.getAs("Len:");
            Integer startPos = row.getAs("Start Pos:");
            Integer endPos = row.getAs("End Pos:");

            if (dataGroup != null && !dataGroup.isEmpty() && !dataGroup.startsWith(",")) {
                // New segment begins
                if (currentSegment != null) {
                    segmentMap.put(currentSegment, fields);
                }
                currentSegment = dataGroup.trim();
                fields = new ArrayList<>();
            } else if (outputName != null && startPos != null && endPos != null) {
                // Field definition
                fields.add(new FieldDefinition(outputName.trim(), startPos - 1, endPos, len));
            }
        }

        // Add the last segment
        if (currentSegment != null) {
            segmentMap.put(currentSegment, fields);
        }

        return segmentMap;
    }

    // Method to parse a single line based on the segment configuration
    private static List<Row> parseLine(String line, Map<String, List<FieldDefinition>> segmentConfig) {
        List<Row> parsedRows = new ArrayList<>();
        
        for (Map.Entry<String, List<FieldDefinition>> entry : segmentConfig.entrySet()) {
            String segmentType = entry.getKey();
            List<FieldDefinition> fields = entry.getValue();

            // Check if the line starts with the segment type
            if (line.startsWith(segmentType)) {
                List<String> fieldValues = new ArrayList<>();
                for (FieldDefinition field : fields) {
                    if (field.getEndPos() <= line.length()) {
                        String value = line.substring(field.getStartPos(), field.getEndPos()).trim();
                        fieldValues.add(value);
                    }
                }
                
                // Construct a Row for this segment with flattened fields
                Row segmentRow = RowFactory.create(
                        segmentType, 
                        fieldValues.toArray(new Object[0])
                );
                parsedRows.add(segmentRow);
            }
        }
        return parsedRows;
    }
}

// Class representing a field definition
class FieldDefinition {
    private String name;
    private int startPos;
    private int endPos;
    private int length;

    public FieldDefinition(String name, int startPos, int endPos, int length) {
        this.name = name;
        this.startPos = startPos;
        this.endPos = endPos;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public int getLength() {
        return length;
    }
}

// Class representing a flattened segment row
class SegmentRow {
    private String segmentType;
    private List<String> fields;

    public SegmentRow(String segmentType, List<String> fields) {
        this.segmentType = segmentType;
        this.fields = fields;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public List<String> getFields() {
        return fields;
    }
}
