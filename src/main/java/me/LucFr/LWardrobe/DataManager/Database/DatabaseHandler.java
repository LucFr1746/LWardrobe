package me.LucFr.LWardrobe.DataManager.Database;

import me.LucFr.LWardrobe.DataManager.Database.MySQL.MySQLConnect;

import java.sql.Connection;

public class DatabaseHandler {

    private String type;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseHandler() {
    }

    public DatabaseHandler(String type, String host, String port, String database, String username, String password) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        switch (type) {
            case "MySQL":
                MySQLConnect mySQLConnect = new MySQLConnect(host, port, database, username, password);
                connection = mySQLConnect.getConnection();
            case "SQLite":
        }
        prepareDatabase();
    }
    private void prepareDatabase() {
        switch (type) {
            case "MySQL":
            case "SQLite":
        }
    }
    private String getType() {
        return type;
    }
    private String getHost() {
        return host;
    }
    private String getPort() {
        return port;
    }
    private String getDatabase() {
        return database;
    }
    private String getUsername() {
        return username;
    }
    private String getPassword() {
        return password;
    }
    private Connection getConnection() {
        return connection;
    }
    private Boolean isConnected() {
        return connection != null;
    }
}
