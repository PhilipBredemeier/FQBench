package com.mycompany.app;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;
import java.util.*;

public class Benchmarker {
    public static void main(String[] args) throws SQLException, IOException {
        //Result csv erstellen und die Header schreiben
        String filename = "src/main/out/FQBenchResultPrestoTrino.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(filename), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        String[] header = {"FederatedEngine", "Query", "Runtime"};
        writer.writeNext(header);
        //writer.close();

        //Durch den Ordner FedEng iterieren und je Engine die Queries laufen lassen
        File fedEng = new File(Benchmarker.class.getClassLoader().getResource("fedEng_cluster/").getPath());
        File[] filesFedEng = fedEng.listFiles();
        for (File fileFedEng : filesFedEng) {
            String fedEngName = fileFedEng.getName();
            System.out.println(fedEngName);
            SystemsHandler sysHandler = new SystemsHandler(fedEngName);
            //sysHandler.printDrivers();
            sysHandler.initialize(Benchmarker.class.getClassLoader().getResource("fedEng_cluster/" + fedEngName).getFile());


            //Durch den Queries Ordner iterieren und alle Queries laufen lassen
            File f = new File(Benchmarker.class.getClassLoader().getResource("pr_tr_queries/run/").getPath());
            File[] files = f.listFiles();
            for (File file : files) {
                System.out.println(file.getName());
                String path = file.toString();
                String query = Files.readString(Paths.get(path));
                System.out.println(query);
                long runtime = 0;

                //5 mal die Query laufen lassen für eine Durchschnittszeit
                for (int i = 0; i < 5; i++) {
                    runtime = runtime + sysHandler.execQuery(sysHandler.getJDBCProperties(), query);
                }
                long avgRuntime = runtime / 5;

                //QueryPlan printen
                String queryPlan = "EXPLAIN " + query;
                sysHandler.execQuery(sysHandler.getJDBCProperties(), queryPlan);

                //Ergebnisse der queries je federated engine in csv datei schreiben
                String[] data = {sysHandler.jdbcProperties.getSystemName(), file.getName(), String.valueOf(avgRuntime)};
                writer.writeNext(data);
                //writer.writeAll(rs, true);
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("______________________________________________");
        }
        writer.close();

    }
}
