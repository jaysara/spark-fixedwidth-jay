package com.test.cobol;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.*;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SparkCsvParseMapSchema {

    public static void mainss(String[] args) {
        String line= "TU4R06231100000000000000000000060140KOX 0000000120241204145944PH0101207000SH01027101Y  N06USN20070302NM11123F1WILSON                          JUNE                E                                                             PI11025F43022912519300201AD11150F1   2305      N GARFIELD                      RD                PINCONNING                  MI486507469                              20170924 TR11332BC092WL002HSBC BANK                     R5X0X8X0X6X8X3X4X              CCC2021120520160427                 000001523000008767000015000MIN 000000037         20211111012021110511111111111111111111111111111111111111111111111 47000000000000000                                                                                     APP110972021110511111111111111111111111111111111111111111111111                                   AH11077202111N                                                               AH11077202110N                                                               AH11077202109N                                                               AH11077202108N                                                               AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101Y000001523000008767000015000000000037000000000                  TR11332BC092WL001HSBC BANK                     R5X3X0X0X2X0X8X1X              ACC2021120320210712                 000000000000000000000007000                              01                                                        01000000000000000                                                                                     APP1109720211103X                                                                                 AH11077202111X                                                               TR11332BC03681001FIRST CARD                    R4X7X0X3X7X1X0                 CCC202111272013083120200320C        000000000000000700000006200                      2017111201202110271XXXXXXXXXXXXXXXXXXXXXXXXXXX1X1XXXXXXXXXXXXXX11148000000000000000                                                                                  CBCAPP11097202110271XXXXXXXXXXXXXXXXXXXXXXXXXXX1X1XXXXXXXXXXXXXX111                                  AH11077202110N                                                               AH11077202109N                                                               AH11077202108N                                                               AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101N                                                               AH11077202012Y000000000000000700000006200         000000000      CBC         TR11332BC02676032MELLON BANK                   R4X2X4X2X0X5X1X9X              ICC2021112620170930         20180204000000000000000000000003000                              012021102611111111111111111111111111111111111111111111111 47000000000000000                                                                                     APP110972021102611111111111111111111111111111111111111111111111                                   AH11077202110N                                                               AH11077202109N                                                               AH11077202108N                                                               AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101N                                                               AH11077202012Y000000000000000000000003000         000000000                  TR11332AP0630Q005YO                            R1X6X3X1X                      ICH2021112520200605         20210202000000000000000176000001500                      202102100120211025111111111111111111111                           21000000000000000                                                                                     APP1109720211025111111111111111111111                                                             AH11077202110N                                                               AH11077202109N                                                               AH11077202108N                                                               AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101N                                                               AH11077202012Y000000000000000176000001500         000000000                  TR11332QZ02592688GMAC                          I4X3X0X3X6X5                   ILE202111122021060420211112C        000000000000002549         011M0000002120000002122021111001202110121                                               01000000000000000                                                                                     APP11097202110121                                                                                 AH11077202110X                                                               TR11332BC0152B013FCNB PRF CHG                  R6X0X9X4X4X                    I  2021101720130930         20180509000000000000000000000008300                      2018051301202109171111111111111111111111111                       25000000000000000                                                                                     APP11097202109171111111111111111111111111                                                         AH11077202109N                                                               AH11077202108N                                                               AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101N                                                               AH11077202012N                                                               AH11077202011Y000000000000000000000008300         000000000                  TR11332QZ02592688GMAC                          I4X3X0X8X6X5                   ILE202109132020071320210913C        000000000000002466         011M000000205000000205202109170120210813111111111                                       09000000000000000                                                                                     APP1109720210813111111111                                                                         AH11077202108X                                                               TR11332DZ0590S008RNB-FIELD1                    R1X8X3X8X5X1X                  ICH2021081720190606         20200703000000000000000800000000800                      202007200120210717111111111111                                    12000000000000000                                                                                     APP1109720210717111111111111                                                                      AH11077202107N                                                               AH11077202106N                                                               AH11077202105N                                                               AH11077202104N                                                               AH11077202103N                                                               AH11077202102N                                                               AH11077202101N                                                               AH11077202012N                                                               AH11077202011N                                                               AH11077202010N                                                               AH11077202009Y000000000000000800000000800         000000000                  TR11332JA0722D62UOSTERMAN JWL                  R9X0X3X0X6X                    ICH2021072520200714                 000000160000000810000004000                      202106160120210625111111111                                       09000000000000000                                                                                     APP1109720210625111111111                                                                         AH11077202106X                                                               TR11332DC01972010JCP-MCCBG                     R7X1X9X0X9X2X                  C  2020081820060930         20200705000000000000000385                               20200721012020071811XXXXXXXXXXXX111111111XXX1111X11XX111111X1111X148000000000000000                                                                                     APP110972020071811XXXXXXXXXXXX111111111XXX1111X11XX111111X1111X1                                  AH11077202007N                                                               AH11077202006N                                                               AH11077202005N                                                               AH11077202004N                                                               AH11077202003N                                                               AH11077202002N                                                               AH11077202001N                                                               AH11077201912N                                                               AH11077201911N                                                               AH11077201910N                                                               AH11077201909Y000000000000000385                  000000000                  TR11332CW08349001WFNNB/EXPRES                  R4X2X2X7X1                     I  2020081620200425                 000000000000000000000001000                              012020071611                                              02000000000000000                                                                                     APP110972020071611                                                                                AH11077202007X                                                               TR11332QZ02592688GMAC                          I4X3X0X3X5X4                   ILE202008102019071520200810C        000000000000003580         012M00000029800000029820201022012020071011111111                                        08000000000000000                                                                                     APP110972020071011111111                                                                          AH11077202007X                                                               TR11332DZ02736001GANTOS                        R7X6X9X6                       I  2019021920150430         20170205000000000000000335000000900                              0120190119111111111111111111111111XXXXXXXXXXXXXX111X1     43000000000000000                                                                                     APP1109720190119111111111111111111111111XXXXXXXXXXXXXX111X1                                       AH11077201901X                                                               TR11332CW0122P001WFNNB/LMITED                  R2X5X1X1X8                     I  2019021820181101         20190203000000000000000000000001000                              01                                                        01000000000000000                                                                                     APP1109720190118X                                                                                 AH11077201901X                                                               TR11332AP0630Q005YO                            R7X2X8X3X                      ICC2017072720150303                 000000000000000224000000600                              01                                                        13000000000000000                                                                                     APP1109720170627XXXXXXXXXXXXX                                                                     AH11077201706X                                                               TR11332BC01597029BK OF AMER                    R4X0X1X7X8X0X4X9X              ICC2017072620170426         20170704000000000000000000000005000                              01                                                        01000000000000000                                                                                     APP1109720170626X                                                                                 AH11077201706X                                                               TR11332QF02350975GECA AUT FIN                  R2X0X1X5X3X9                   I  2016060520150304         20160510000000000000000220                               20160514012016050511111111111                                     11000000000000000                                                                                     APP110972016050511111111111                                                                       AH11077201605X                                                               TR11332DC06256386CBUSASEARS                    R1X0X8X7X5X3X                  A  2014093020140517                 000000000000000000000003400                              01                                                        00      000000000                                                                                     AAH11077201408X                                                               SM120662NA NA 000000000NA NA        013009010009003NA NA NA NA NA SD12071R000040985000001683NA       000000000000000037000000000NA       SD12071T000040985000001683NA       000000000000000037000000000NA       DS11017R000000000DS11017I000000000HS11085R1007002000004800NA       NA       NA       NA       NA  NA       000000160T  HS11085R2007002NA       NA       NA       NA       NA       NA  NA       NA       NA HS11085R3007002NA       NA       NA       NA       NA       NA  NA       NA       NA HS11085R6007002NA       000001523NA       NA       NA       NA  NA       NA       NA HS11085RY009002000001800NA       NA       NA       NA       NA  NA       000000000I  HS11085IYNA NA          NA                NA       NA       NA  NA       000000000   HS11085T1007002         NA       NA       NA       NA       NA  NA       000000160   HS11085T2007002         NA       NA       NA       NA       NA  NA       NA          HS11085T3007002         NA       NA       NA       NA       NA  NA       NA          HS11085T6007002         000001523NA       NA       NA       NA  NA       NA          HS11085TY009002         NA       NA       NA       NA       NA  NA       000000000   END101200149";
        String inputString = "TU4R06231100000000000000000000060140KOX 0000000120241204145944PH0101207000SH01027101Y  N06USN20070302SH01027101Y  N06USN20070303";
        String csvConfigPath = "data/Sample.csv";
        List<Segment> csvConfigList = readCsvConfig(csvConfigPath);
//        Map<String, List<Field>> segmentConfig = csvConfigList.stream().collect(
//                Collectors.toMap(Segment:: getSegmentName, Segment::getFields )
//        );
        List<SegmentRow> segmentRows =new ArrayList<>();
        Map<String, List<Map<String, String>>> answer = parseString(inputString,csvConfigList);
        for (String s : answer.keySet()) {
            SegmentRow segmentRow = new SegmentRow();

            segmentRow.setSegmentType(s);
            segmentRow.setFields(answer.get(s));
            segmentRows.add(segmentRow);
            System.out.println("Key "+s);
           // Map<String, String> map = answer.get(s);
           // map.forEach((k,v)->System.out.println("Key "+k+" value "+v));
        }
       // System.out.println(segmentRows);
        segmentRows.stream().forEach(x -> System.out.println(x));
    }

    private static Map<String, Map<String, String>> parseStringToSegment(String input, List<Segment> segments) {
        Map<String, Map<String, String>> parsedData = new LinkedHashMap<>();
        int currentPosition = 0;

        while (currentPosition < input.length()) {
            boolean segmentMatched = false;

            for (int i=0;i<segments.size();i++) {
                Segment segment = segments.get(i) ;
                String segmentName = segment.segmentName.substring(0, 4);
                //  System.out.println("Segement name is "+segmentName);
                if (input.startsWith(segmentName, currentPosition)) {
                    segmentMatched = true;
                    System.out.println("Parsing for ****"+segmentName+"****");
                    // currentPosition += 4; // Move past the segment identifier
                    Map<String, String> segmentData = new LinkedHashMap<>();

                    for (Field field : segment.fields) {
                        int fieldStart = currentPosition + field.startPos - 1;
                        int fieldEnd = currentPosition + field.endPos;
                        if (fieldStart >= 0 && fieldEnd <= input.length()) {
                            String value = input.substring(fieldStart, fieldEnd).trim();

//                            System.out.println("Start "+ fieldStart+" End "+fieldEnd);
//                            System.out.println("Field name is "+field.outputName+" value is "+value);
                            segmentData.put(field.outputName, value);
                        }
                    }

                    parsedData.put(segment.segmentName, segmentData);
                    currentPosition += segment.fields.stream().mapToInt(f -> f.length).sum();
                    currentPosition -= 4; // Move back to the beginning of the segment
                    break;
                }
            }

            if (!segmentMatched) {
                // If no segment matched, move to the next position
                currentPosition++;
            }
        }

        return parsedData;
    }
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("SegmentParser")
                .master("local[*]")
                .config("spark.driver.bindAddress", "127.0.0.1")
                .getOrCreate();

        // Define paths for the CSV configuration and the input text file
        String csvConfigPath = "data/Sample.csv";
        String textFilePath = "data/sample.txt";

        // Read the configuration CSV
        Dataset<Row> configDf = spark.read()
                .option("header", true)
                .option("inferSchema", true)
                .csv(csvConfigPath);

        // Parse the configuration to a map of segment definitions
        //Map<String, List<Fieldinition>> segmentConfig = parseConfiguration(configDf);
        List<Segment> csvConfigList = readCsvConfig(csvConfigPath);
        Map<String, List<Field>> segmentConfig = csvConfigList.stream().collect(
                Collectors.toMap(Segment:: getSegmentName, Segment::getFields )
        );
        // Read the input text file
        Dataset<String> inputLines = spark.read().textFile(textFilePath);
        Dataset<ConsumerRecord> parsedData =
//        inputLines.flatMap(new FlatMapFunction<String, SegmentRow>() {
//            @Override
//            public Iterator<SegmentRow> call(String input) throws Exception {
//                List<SegmentRow> segmentRows =new ArrayList<>();
//                Map<String, List<Map<String, String>>> answer = parseString(input,csvConfigList);
//                for (String s  : answer.keySet()) {
//                    SegmentRow segmentRow = new SegmentRow();
//
//                    segmentRow.setSegmentType(s);
//                    segmentRow.setFields(answer.get(s));
//                    segmentRows.add(segmentRow);
//                    System.out.println("Key "+s);
//                   // Map<String, String> map = answer.get(s);
//                    //map.forEach((k,v)->System.out.println("Key "+k+" value "+v));
//                }
//                return segmentRows.iterator();
//            }
//        }, Encoders.bean(SegmentRow.class));

        inputLines.map(new MapFunction<String, ConsumerRecord>() {
            @Override
            public ConsumerRecord call(String input) throws Exception {
                List<SegmentRow> segmentRows =new ArrayList<>();
                ConsumerRecord consumerRecord = new ConsumerRecord();
                Map<String, List<Map<String, String>>> answer = parseString(input,csvConfigList);


                for (String s : answer.keySet()) {
                    SegmentRow segmentRow = new SegmentRow();
                    if(s.equals("TU4R"))
                    {
                        List<Map<String, String>> fields = answer.get(s);
                        String userRefNo = fields.get(0).get("User Reference Number");
                        System.out.println("user ref no "+userRefNo);
                        consumerRecord.setUserRefnumber(userRefNo);
                    }

                    segmentRow.setSegmentType(s);
                    segmentRow.setFields(answer.get(s));
                    segmentRows.add(segmentRow);
                    //System.out.println("Key "+s);
//                    Map<String, String> map = answer.get(s);
//                    map.forEach((k,v)->System.out.println("Key "+k+" value "+v));
                }
                consumerRecord.setSegmentRows(segmentRows);
                return consumerRecord;
            }
        }, Encoders.bean(ConsumerRecord.class));
        // Transform the input lines into structured data
//        Dataset<Row> parsedData = inputLines.flatMap(
//                (FlatMapFunction<String, SegmentRow>) line -> parseLine(line, segmentConfig).iterator(),
//                Encoders.bean(SegmentRow.class)
//        ).toDF();

        // Show the resulting dataset
        System.out.println(" Total Rows "+parsedData.count());
        parsedData.printSchema();
        parsedData.show(true);
        parsedData.foreach(new ForeachFunction<ConsumerRecord>() {
            @Override
            public void call(ConsumerRecord consumerRecord) throws Exception {
                System.out.println(" For User Ref No "+consumerRecord.getUserRefnumber()+ " total segment rows "+consumerRecord.getSegmentRows().size());
                List<SegmentRow> segmentRows = consumerRecord.getSegmentRows();
                for (SegmentRow segmentRow : segmentRows){

                    List<Map<String, String>> fields = segmentRow.getFields();
                    System.out.println("Segment Type "+segmentRow.getSegmentType()+ " Total fields "+fields.size());
//                    for (Map<String, String> field : fields){
//                        field.forEach((k,v)->System.out.println("Key "+k+" value "+v));
//                };
            }
        }});

        spark.stop();
    }

    public void modifyDataSet()
    {
        Dataset<ConsumerRecord> modifiedUserFefNo =
        parsedData.map(new MapFunction<ConsumerRecord, ConsumerRecord>() {
            @Override
            public ConsumerRecord call(ConsumerRecord consumerRecord) throws Exception {
                ConsumerRecord newConsumerRecord = new ConsumerRecord();
                newConsumerRecord.setUserRefnumber(consumerRecord.getUserRefnumber());
                if(consumerRecord.getUserRefnumber().contains("000000000000000000029101")) {
                    System.out.println("MODIFYING TR11 FOR USER REF NO " + consumerRecord.getUserRefnumber());

                    List<SegmentRow> segmentRows = consumerRecord.getSegmentRows();
                    List<SegmentRow> newsegmentRows = new ArrayList<>();
                    for (SegmentRow segmentRow : segmentRows) {

                        List<Map<String, String>> fields = segmentRow.getFields();
                        System.out.println("Segment Type " + segmentRow.getSegmentType() + " Total fields " + fields.size());
//                    for (Map<String, String> field : fields){
//                        field.forEach((k,v)->System.out.println("Key "+k+" value "+v));
//                };
                        if (segmentRow.getSegmentType().contains("TR11")) {
                            List<Map<String, String>> fieldsTR11 = segmentRow.getFields();
                            fieldsTR11.remove(0);
                            SegmentRow neweSegmentRow = new SegmentRow();
                            neweSegmentRow.setSegmentType(segmentRow.getSegmentType());
                            neweSegmentRow.setFields(fieldsTR11);
                            newsegmentRows.add(neweSegmentRow);

                        } else newsegmentRows.add(segmentRow);

                    }
                    newConsumerRecord.setSegmentRows(newsegmentRows);
                }else
                newConsumerRecord.setSegmentRows(consumerRecord.getSegmentRows());
                return newConsumerRecord;
               // return null;
            }
        }, Encoders.bean(ConsumerRecord.class));

        modifiedUserFefNo.write().mode("overwrite").parquet("data/output-tr11changed.parquet");
    }
     
    private static Map<String, List<Map<String, String>>> parseString(String input, List<Segment> segments) {
        Map<String, List<Map<String, String>>>  answer = new LinkedHashMap<>();
        Map<String, Map<String, String>> parsedData = new LinkedHashMap<>();
        int currentPosition = 0;

        while (currentPosition < input.length()) {
            boolean segmentMatched = false;

            for (int i=0;i<segments.size();i++) {
                Segment segment = segments.get(i) ;
                String segmentName = segment.segmentName.substring(0, 4);
                //  System.out.println("Segement name is "+segmentName);
                if (input.startsWith(segmentName, currentPosition)) {
                    segmentMatched = true;
                   // System.out.println("Parsing for ****"+segmentName+"****");
                    // currentPosition += 4; // Move past the segment identifier
                    Map<String, String> segmentData = new LinkedHashMap<>();

                    for (Field field : segment.fields) {
                        int fieldStart = currentPosition + field.startPos - 1;
                        int fieldEnd = currentPosition + field.endPos;
                        if (fieldStart >= 0 && fieldEnd <= input.length()) {
                            String value = input.substring(fieldStart, fieldEnd).trim();

//                            System.out.println("Start "+ fieldStart+" End "+fieldEnd);
//                            System.out.println("Field name is "+field.outputName+" value is "+value);
                            segmentData.put(field.outputName, value);
                        }
                    }

                    List<Map<String, String>> segmentDataList = new ArrayList() ;
                    if(answer.get(segment.segmentName)!=null)
                        segmentDataList = answer.get(segment.segmentName);
                    segmentDataList.add(segmentData);
                    parsedData.put(segment.segmentName, segmentData);
                    answer.put(segment.segmentName, segmentDataList);
                    currentPosition += segment.fields.stream().mapToInt(f -> f.length).sum();
                    currentPosition -= 4; // Move back to the beginning of the segment
                    break;
                }
            }

            if (!segmentMatched) {
                // If no segment matched, move to the next position
                currentPosition++;
            }
        }

        return answer;
    }

    private static List<Segment> readCsvConfig(String csvFilePath) {
        List<Segment> segments = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            Segment currentSegment = null;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 0 || Arrays.stream(nextLine).allMatch(String::isBlank)) {
                    // Blank line indicates the end of a segment definition
                    currentSegment = null;
                    continue;
                }

                String regex = "^[A-Za-z ]+\\([A-Za-z0-9]{4}\\)$";
                Pattern SegementPattern = Pattern.compile(regex);
                Matcher matcher = SegementPattern.matcher(nextLine[0]);

                //              if (nextLine[0].endsWith("Segment") && currentSegment == null) {
                if (matcher.matches() && currentSegment == null) {
                    // New segment starts
                    currentSegment = new Segment(nextLine[0].trim());
                    segments.add(currentSegment);
                    // Skip headers for the current segment
                    reader.readNext(); // Skip header line
                    reader.readNext(); // Skip blank line
                } else if (currentSegment != null && nextLine.length >= 7 && !nextLine[1].isBlank()) {
                    // Parse field definitions for the current segment
                    String outputName = nextLine[1];
                    int length = Integer.parseInt(nextLine[4].trim());
                    int startPos = Integer.parseInt(nextLine[5].trim());
                    int endPos = Integer.parseInt(nextLine[6].trim());
                    currentSegment.fields.add(new Field(outputName, length, startPos, endPos));
                }
            }
        } catch (IOException | CsvValidationException | NumberFormatException e) {
            e.printStackTrace();
        }
        return segments;
    }


    // Method to parse the configuration CSV
//    private static Map<String, List<Fieldinition>> parseConfiguration(Dataset<Row> configDf) {
//        Map<String, List<Fieldinition>> segmentMap = new LinkedHashMap<>();
//        List<Row> rows = configDf.collectAsList();
//        String currentSegment = null;
//        List<Fieldinition> fields = new ArrayList<>();
//
//        for (Row row : rows) {
//            String dataGroup = row.getAs("Data Group:");
//            String outputName = row.getAs("Output Name:");
//            Integer len = row.getAs("Len:");
//            Integer startPos = row.getAs("Start Pos:");
//            Integer endPos = row.getAs("End Pos:");
//
//            if (dataGroup != null && !dataGroup.isEmpty() && !dataGroup.startsWith(",")) {
//                // New segment begins
//                if (currentSegment != null) {
//                    segmentMap.put(currentSegment, fields);
//                }
//                currentSegment = dataGroup.trim();
//                fields = new ArrayList<>();
//            } else if (outputName != null && startPos != null && endPos != null) {
//                // Field definition
//                fields.add(new Fieldinition(outputName.trim(), startPos - 1, endPos, len));
//            }
//        }
//
//        // Add the last segment
//        if (currentSegment != null) {
//            segmentMap.put(currentSegment, fields);
//        }
//
//        return segmentMap;
//    }

    // Method to parse a single line based on the segment configuration
    private static List<SegmentRow> parseLineNotused(String line, Map<String, List<Field>> segmentConfig) {
        System.out.println("Line : " + line);
        System.out.println("Segment Config : " + segmentConfig);
        System.out.println("");
        List<SegmentRow> parsedSegments = new ArrayList<>();
        for (Map.Entry<String, List<Field>> entry : segmentConfig.entrySet()) {
            String segmentType = entry.getKey();
            List<Field> fields = entry.getValue();

            // Check if the line starts with the segment type
            if (line.startsWith(segmentType)) {
                Map<String, String> fieldValues = new LinkedHashMap<>();
                for (Field field : fields) {
                    if (field.getEndPos() <= line.length()) {
                        String value = line.substring(field.getStartPos(), field.getEndPos()).trim();
                        fieldValues.put(field.getOutputName(), value);
                    }
                }
                List<Map<String,String>> fieldValuesList = new ArrayList<>();
                fieldValuesList.add(fieldValues);
                parsedSegments.add(new SegmentRow(segmentType, fieldValuesList));
            }
        }
        return parsedSegments;
    }
}
 @Getter
@Setter
 class Field implements Serializable {
    String outputName;
    int length;
    int startPos;
    int endPos;

    Field(String outputName, int length, int startPos, int endPos) {
        this.outputName = outputName;
        this.length = length;
        this.startPos = startPos;
        this.endPos = endPos;
    }
}
@Getter
@Setter
class Segment  implements Serializable{
    String segmentName;
    List<Field> fields;

    String segmentFullName;


    Segment(String segmentName) {
        this.segmentFullName = segmentName;
        int startIndex = segmentName.indexOf('(');
        int endIndex = segmentName.indexOf(')');
        this.segmentName = segmentName.substring(startIndex + 1, endIndex);
        this.fields = new ArrayList<>();
    }
}
// Class representing a field definition
//class Fieldinition {
//    private String name;
//    private int startPos;
//    private int endPos;
//    private int length;
//
//    public Fieldinition(String name, int startPos, int endPos, int length) {
//        this.name = name;
//        this.startPos = startPos;
//        this.endPos = endPos;
//        this.length = length;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getStartPos() {
//        return startPos;
//    }
//
//    public int getEndPos() {
//        return endPos;
//    }
//
//    public int getLength() {
//        return length;
//    }
//}



