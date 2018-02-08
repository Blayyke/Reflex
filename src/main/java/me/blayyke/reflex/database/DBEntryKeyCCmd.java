package me.blayyke.reflex.database;

public enum DBEntryKeyCCmd {
    ACTION("action"),
    CREATOR("creator"),
    DESCRIPTION("desc");

    private String key;

    DBEntryKeyCCmd(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}