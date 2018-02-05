package me.blayyke.reflex.database;

public enum CustomCommandDBKey {
    NAME("name"),
    ACTION_CODE("action"),
    DESCRIPTION("desc");

    private String key;

    CustomCommandDBKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}