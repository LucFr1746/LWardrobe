package me.LucFr.LWardrobe.DataManager.Database;

import me.LucFr.LWardrobe.FileManager.Config;

public class DatabaseHandler {

    private final String type;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public DatabaseHandler() {
        this.type = Config.dbType;
        this.host = Config.dbHost;
        this.port = Config.dbPort;
        this.database = Config.dbDatabase;
        this.username = Config.dbUsername;
        this.password = Config.dbPassword;
    }
}
