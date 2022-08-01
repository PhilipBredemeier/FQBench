package com.mycompany.app;

import com.opencsv.CSVWriter;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

public class SystemsHandler implements InteractionInterface {

    public JDBCProperties jdbcProperties;
    public String systemName;

    public SystemsHandler() {
    }

    ;

    public SystemsHandler(String systemName) {
        this.systemName = systemName;
        this.jdbcProperties = new JDBCProperties();
    }

    public static void printDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();

        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println(driver.getClass().getName());
        }
    }

    public long execQuery(JDBCProperties jdbcProperties, String query) {
        try {

            /*Class.forName("io.trino.jdbc.TrinoDriver");
            // URL parameters
            String jdbcUrl = "jdbc:trino://localhost:8081";
            Properties properties = new Properties();
            properties.setProperty("user", "test");
            //properties.setProperty("password", "secret");
            //properties.setProperty("SSL", "true");
            properties.setProperty("sessionProperties", "resource_overcommit:true");
            Connection connection = DriverManager.getConnection(jdbcUrl, properties);
*/
            long start = System.nanoTime();
            Connection connection = DriverManager.getConnection(jdbcProperties.getUrl());
            Statement stmt = connection.createStatement();
            //int timeOut = 999999999;
            //stmt.setQueryTimeout(timeOut);
            ResultSet rs = stmt.executeQuery(query);
            long finish = System.nanoTime();
            long runtime = (finish - start) / 1000000;
            //System.out.println("Query: "+query+" on "+jdbcProperties.getUrl());
            System.out.println("Querying " + jdbcProperties.getUrl());
            System.out.println("Total Time: " + (finish - start) / 1000000 + " ms");
            System.out.println("Resultset: " + rs);
            System.out.println("______________________________________________");
            printResultSet(rs);
            return runtime;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void execQueryOnDB(JDBCProperties jdbcProperties, String query) {
        try {
            long start = System.nanoTime();
            Connection connection = DriverManager.getConnection(jdbcProperties.getUrl(), jdbcProperties.getUser(), jdbcProperties.getPassword());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            long finish = System.nanoTime();
            System.out.println("Query: " + query + " on " + jdbcProperties.getUrl());
            System.out.println("Total Time: " + (finish - start) / 1000000 + " ms");
            System.out.println("Resultset: " + rs);
            printResultSet(rs);
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void initialize(String propertiesFile) throws IOException {

        try (InputStream input = new FileInputStream(propertiesFile)) {

            Properties prop = new Properties();
            prop.load(input);

            this.jdbcProperties.setSystemName(prop.getProperty("systemName"));
            //System.out.println(prop.getProperty("systemName"));
            this.jdbcProperties.setUrl(prop.getProperty("url"));
            //System.out.println(prop.getProperty("url"));
            this.jdbcProperties.setDb(prop.getProperty("db"));
            //System.out.println(prop.getProperty("db"));
            this.jdbcProperties.setPort(prop.getProperty("port"));
            //System.out.println(prop.getProperty("port"));
            this.jdbcProperties.setUser(prop.getProperty("user"));
            //System.out.println(prop.getProperty("user"));
            this.jdbcProperties.setPassword(prop.getProperty("password"));
            //System.out.println(prop.getProperty("password"));
            this.jdbcProperties.setTablenameStart(prop.getProperty("tablenameStart"));
            //System.out.println(prop.getProperty("tablenameStart"));
            this.jdbcProperties.setDriverName(prop.getProperty("driverName"));
            //System.out.println(prop.getProperty("driverName"));
            this.jdbcProperties.setDriverJar(prop.getProperty("driverJar"));
            //System.out.println(prop.getProperty("driverJar"));

            System.out.println("Initialize done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*public static void printResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        if (!rs.next()) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Empty ResultSet");
            System.out.println("------------------------------------------------------------------------");
        } else {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Query Result:");
            System.out.println("------------------------------------------------------------------------");

            while (rs.next()) {
                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

                    if (i != 1)
                        sb.append("|");
                    sb.append(rs.getString(i));

                }
                System.out.println(sb.toString());
            }
            System.out.println("------------------------------------------------------------------------");
        }
    }*/

    public static void printResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        while (rs.next()) {
            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

                if (i != 1)
                    sb.append("|");
                sb.append(rs.getString(i));

            }
            System.out.println(sb.toString());
        }
    }


    @Override
    public JDBCProperties getJDBCProperties() {
        return this.jdbcProperties;
    }
}