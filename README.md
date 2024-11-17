# spark-fixedwidth-jay
Create a custom Spark Relation to read fixedwidth file.

https://harness-developer-hub.s3.us-east-2.amazonaws.com/labs/Harness_CI_CD_Dev_Days_Lab_Guide.pdf
When capturing key performance profiles and metrics for **ETL (Extract, Transform, Load) batch jobs**, it's important to monitor various dimensions to ensure data pipelines are optimized for efficiency, scalability, and reliability. Here are the **key performance metrics** across the stages of an ETL batch process:

```
// Remove the first 10 characters of the 'message' column
Dataset<Row> updatedLogDf = logDf.withColumn("message", substring(col("message"), 11, length(col("message")).minus(10)));

// Display the updated DataFrame
updatedLogDf.show(false);
```

### **1. Execution Time**
   - **Total Execution Time**: The total time taken for the ETL batch job to complete (start-to-end).
   - **Stage-wise Time**:
     - **Extraction Time**: Time taken to extract data from the source system.
     - **Transformation Time**: Time spent processing and transforming data.
     - **Load Time**: Time spent loading the data into the target system.
   - **Parallelism**: If jobs are parallelized, measure the efficiency of parallel task execution.

### **2. Data Volume Metrics**
   - **Data Volume Processed**: The amount of data (in GB/TB) processed during the job. Helps in scaling operations based on data size.
   - **Record Count**: The number of records processed through each phase—extracted, transformed, and loaded.
   - **Data Growth Rate**: How much the data volume grows over time (e.g., daily/weekly).

### **3. Resource Utilization**
   - **CPU Usage**: Percentage of CPU resources consumed during the ETL process.
   - **Memory Usage**: Average and peak memory utilization by the ETL jobs.
   - **Disk I/O**: The amount of data read and written to disk during ETL processes (particularly important for large datasets).
   - **Network I/O**: Volume of data transferred over the network, particularly in distributed systems.

### **4. Throughput**
   - **Records Per Second**: The number of records processed per second.
   - **Data Processed Per Minute/Hour**: Volume of data processed in a specific time interval (MB/s or GB/h).
   - **ETL Pipeline Latency**: The time it takes for data to move from source to destination through the pipeline.

### **5. Failure Metrics**
   - **Job Success Rate**: The percentage of ETL jobs that complete successfully vs. those that fail.
   - **Error Rate**: Number of records that fail to process or load correctly.
   - **Retry Count**: Number of retries required to successfully complete the job (useful for batch jobs with fault tolerance).
   - **Failure Causes**: Categorization of reasons for job failures (data quality issues, resource limits, timeouts, etc.).

### **6. Data Quality Metrics**
   - **Data Accuracy**: Number of accurate records vs. those with errors (based on validation rules).
   - **Data Completeness**: Percentage of missing or incomplete data across datasets.
   - **Duplicate Records**: Percentage of duplicate records found during the transformation phase.

### **7. Scheduling and Timing**
   - **Job Start and End Times**: When ETL jobs start and end. Helps to monitor batch windows and determine whether jobs are completing within the allotted time.
   - **Job Frequency**: How often the batch jobs run (e.g., daily, hourly, etc.).
   - **Batch Window Utilization**: Percentage of batch time used versus the allowed batch window.

### **8. Scalability and Elasticity**
   - **Horizontal Scalability**: How well the system scales with the addition of more servers or nodes (in distributed ETL environments).
   - **Vertical Scalability**: Ability to handle increased workloads with more powerful resources (e.g., more CPU/memory).
   - **Elastic Resource Usage**: How well the job uses cloud-based resources that scale up and down dynamically.

### **9. Performance Bottlenecks**
   - **Top Time-Consuming Tasks**: Identify which tasks take the longest within the ETL pipeline (e.g., slow transformation steps).
   - **Resource Contention**: Monitor contention for shared resources like disk or network bandwidth, especially in multi-tenant environments.
   - **Queue and Wait Time**: Time spent waiting for resources (CPU, I/O, or database access), or in job scheduling queues.

### **10. SLA Compliance**
   - **Service-Level Agreement (SLA) Metrics**: Monitor if ETL jobs meet the SLAs for data availability, freshness, or processing times.
   - **SLA Violations**: Count of violations where the ETL batch did not meet the agreed timelines.

### **11. Historical Trends and Anomalies**
   - **Job Performance Over Time**: Track the performance of ETL jobs historically to identify trends in degradation or improvement.
   - **Anomaly Detection**: Set baselines for job performance and detect anomalies (e.g., significant increase in processing time or resource usage).

### **12. Dependency Metrics**
   - **Downstream Impacts**: The impact of ETL jobs on downstream systems, like BI tools or data warehouses (e.g., delay in availability).
   - **Dependency Bottlenecks**: Measure time waiting on upstream systems to provide the data or dependencies that delay ETL execution.

---

### **Examples of Tools for Monitoring ETL Performance**:
- **Apache Airflow**, **AWS Glue**, and **Google Cloud Dataflow**: Provide native monitoring for ETL job execution time, success rates, and resource usage.
- **Prometheus/Grafana**: Used for capturing detailed metrics like CPU, memory, disk, and network usage.
- **Custom dashboards** in tools like **Tableau** or **Power BI** can track data volume processed, job success rates, and SLA compliance over time.

Monitoring these metrics ensures that the ETL batch process is optimized, scalable, and delivers reliable data processing for your business needs.

