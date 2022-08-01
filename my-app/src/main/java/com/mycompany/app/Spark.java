package com.mycompany.app;

import com.opencsv.CSVWriter;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Spark {
    public static void main(String[] args) throws ClassNotFoundException, IOException {


        // Mit Billig SQL nochmal durchlaufen lassen!! (erst kleines sql, dann mit join, dann mit join und aggregate)
        // DOCKER IMAGE MIT SPARK 3.2.0, dafür alle images löschen und Dockerfile vorm Make Build anpassen!
        // Clean Problem irgendwie abfangen!

        //Docker ordner in Projektordner kopieren
        //Code für Explain Plans hinzufügen im Benchmarker und bei SPark (Explain plan vor die SQL queries concatenieren


        SparkSession spark = SparkSession
                .builder()
                .master("spark://spark-master:7077")
                .appName("FQ-Bench Spark")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.executor.memory", "4g")

                .config("spark.driver.memory", "4g")
                .config("spark.task.maxDirectResultSize", "2g")
                //.config("spark.sql.autoBroadcastJoinThreshold ", "1g")
                .config("spark.sql.broadcastTimeout", "5555555")
                .config("spark.sql.autoBroadcastJoinThreshold", "-1")

                //.config("spark.sql.codegen.aggregate.map.twolevel.enabled", "false")

                /*.config("spark.deploy.defaultCores", "8")
                .config("spark.cores.max", "8")
                .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                .config("spark.executor.cores", "2")
                //.config("spark.task.cpus", "2")

                .config("spark.driver.memory", "1g")
                .config("spark.network.timeout", "5000000")
                .config("spark.rpc.askTimeout", "5000000")
                .config("fetchSize", "250000")
                .config("numPartitions", "8")
                //.config("spark.databricks.driver.disableScalaOutput", "true")
                //.config("spark.default.parallelism", "20")
                //.config("spark.submit.deployMode", "client")
                //.config("spark.driver.bindAddress", "0.0.0.0")
                //.config("spark.eventLog.enabled", "true")
                //.config("spark.logConf", "true")
                //.config("spark.eventLog.dir", "/meinLog.log")*/

                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");


        //Tabellenverteilung holen
        File f = new File(Spark.class.getClassLoader().getResource("tabledistribution/td1.properties").getFile());
        //String tdName = f.getName();

        //Result csv erstellen und die Header schreiben
        String filename = "src/main/out/FQBenchResult_spark.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(filename), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        String[] header = {"FederatedEngine", "Query", "Runtime"};
        writer.writeNext(header);

        if (f != null) {

            try (InputStream input = new FileInputStream(f)) {

                Properties properties = new Properties();
                properties.load(input);

                //Tabellenverteilung als Properties speichern um anschließend die Tabellen je DB als Views bekannt zu machen
                Set<String> propertieNames = properties.stringPropertyNames();
                for (String i : propertieNames) {
                    properties.put(i, properties.getProperty(i));
                    System.out.println("TD Properties für " + i);

                    //für jedes System PG1/PG2/MDB1 die Properties File laden und
                    File sysFile = new File(Spark.class.getClassLoader().getResource("tabledistribution/" + i + ".properties").getFile());
                    if (sysFile != null) {

                        InputStream sysInput = new FileInputStream(sysFile);
                        Properties connectionProperties = new Properties();
                        connectionProperties.load(sysInput);
                        Set<String> sysPropertieNames = connectionProperties.stringPropertyNames();
                        for (String l : sysPropertieNames) {
                            connectionProperties.put(l, connectionProperties.getProperty(l));
                        }

                        //connectionProperties.setProperty("fetchsize", "250000");

                        //Tabellen laden
                        List<String> tableLoaded = new ArrayList<String>();

                        String[] tables = properties.getProperty(i).split(",");
                        for (String table : tables) {
                            if (!tableLoaded.contains(table)) {
                                //System.out.println(connectionProperties);
                                // Loading data from a JDBC source
                                Dataset<Row> jdbcDF = spark.read()
                                        .option("fetchSize", "250000")
                                        .option("numPartitions", "8")
                                        //.option("spark.default.parallelism", "50")
                                        .jdbc(connectionProperties.getProperty("url"), "(select * from " + table + ") as " + table, connectionProperties);
                                jdbcDF.createTempView(table);
                                tableLoaded.add(table);
                                System.out.println(table + " geladen");
                            }
                        }
                    }
                }

                //Durch den Queries Ordner iterieren und alle Queries laufen lassen
                File fi = new File(Spark.class.getClassLoader().getResource("sparkqueries/run/").getPath());
                File[] queryFiles = fi.listFiles();
                for (File queryfile : queryFiles) {
                    System.out.println(queryfile.getName());
                    String path = queryfile.toString();
                    String sqlQuery = Files.readString(Paths.get(path));
                    System.out.println(sqlQuery);
                    //String outputPath = "/tmp/test.csv";

                    long avgRuntime = 0;
                    //5 mal die Query laufen lassen für eine Durchschnittszeit
                    for (int i = 0; i < 5; i++) {
                        long start = System.nanoTime();
                        spark.sql(sqlQuery).show();
                        long finish = System.nanoTime();
                        long runtime = (finish - start) / 1000000;
                        System.out.println("query" + queryfile.getName() + " done in " + runtime + " ms");
                        avgRuntime = avgRuntime + runtime;
                    }
                    avgRuntime = avgRuntime / 5;

                    //QueryPlan printen
                    spark.sql(sqlQuery).explain();

                    /* Dataset<Row> d = spark.sql(sqlQuery);
                    d.write()
                            .mode(SaveMode.Overwrite)
                            .format("com.databricks.spark.csv")
                            .option("header", "true")
                            .save(outputPath);
                    d.show();*/

                    //Ergebnisse der queries je federated engine in csv datei schreiben
                    String[] data = {"spark", queryfile.getName(), String.valueOf(avgRuntime)};
                    writer.writeNext(data);

                }
                writer.close();
                spark.stop();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AnalysisException e) {
                e.printStackTrace();
            }

        }
    }
}