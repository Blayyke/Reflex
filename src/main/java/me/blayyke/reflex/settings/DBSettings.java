package me.blayyke.reflex.settings;

public class DBSettings {
    private String databaseHost;
    private int databasePort;
    private int databaseNumber;
    private String databasePassword;

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabaseNumber(int databaseNumber) {
        this.databaseNumber = databaseNumber;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getDatabaseNumber() {
        return databaseNumber;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}