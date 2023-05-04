package me.LucFr.LWardrobe.DataManager.Database.MySQL;

import me.LucFr.LWardrobe.LWardrobe;

import java.sql.*;

public class MySQLConnect {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public MySQLConnect(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        try {
            connect();
        } catch (SQLException e) {
            LWardrobe.getInstance.getLogger().warning("Failed to connect to MySQL!");
        }
        if (isConnected()) {
            try {
                if (!hasDatabase()){
                    LWardrobe.getInstance.getLogger().warning("There is no database named: " + database);
                }
            } catch (SQLException e) {
                LWardrobe.getInstance.getLogger().warning("Failed to connect to MySQL!");
            }
        }
    }

    public static void shutdown() {
        disconnect();
    }

    private static Connection connection;

    private static boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
            LWardrobe.getInstance.getLogger().info("Successfully connected to MySQL!");
        }
    }

    private boolean hasDatabase() throws SQLException {
        //Getting the connection
        Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port, username, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW DATABASES");
        while(rs.next()) {
            if (rs.getString(1).equals(database)) return true;
        }
        return false;
    }

    private static void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                LWardrobe.getInstance.getLogger().info("Successfully disconnected MySQL!");
            } catch (SQLException e) {
                LWardrobe.getInstance.getLogger().warning("Failed to disconnect MySQL!");
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
