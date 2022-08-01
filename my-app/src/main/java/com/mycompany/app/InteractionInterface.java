package com.mycompany.app;

import java.sql.Connection;
import java.sql.SQLException;

public interface InteractionInterface {

    //Connection getConnection(String URL) throws SQLException;//, String USER, String PASSWORD) throws SQLException;//ggfs Try Catch in methode

    JDBCProperties getJDBCProperties();

    long execQuery(JDBCProperties jdbcProperties, String query);

}
