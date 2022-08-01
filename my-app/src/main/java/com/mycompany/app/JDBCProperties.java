package com.mycompany.app;

import java.util.Properties;

public class JDBCProperties {

    private String systemName;
    private String url;
    private String db;
    private String port;
    private String user;
    private String password;
    private String tablenameStart;
    private String driverName;
    private String driverJar;


    public JDBCProperties() {
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTablenameStart() {
        return tablenameStart;
    }

    public void setTablenameStart(String tablenameStart) {
        this.tablenameStart = tablenameStart;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverJar() {
        return driverJar;
    }

    public void setDriverJar(String driverJar) {
        this.driverJar = driverJar;
    }


    @Override
    public String toString() {
        return "JDBCProperties{" +
                "systemName='" + systemName + '\'' +
                ", url='" + url + '\'' +
                ", db='" + db + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", tablenameStart='" + tablenameStart + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverJar='" + driverJar + '\'' +
                '}';
    }
}