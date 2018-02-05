package me.blayyke.reflex.database;

public enum DBEntryKey {
    LEAVE_MESSAGE("leave_message", true, false, false, false),
    JOIN_MESSAGE("join_message", true, false, false, false),
    GUILD_PREFIX("prefix", true, false, false, false),
    ANNOUNCEMENT_CHANNEL("announcement_channel", false, false, true, false),
    AUTOROLE_ID("autorole_id", false, false, true, false),
    COMMANDS("commands", false, false, false, true);

    private final String redisKey;
    private final boolean isString;
    private final boolean isBool;
    private final boolean isNumber;
    private boolean hash;

    DBEntryKey(String redisKey, boolean isString, boolean isBool, boolean isNumber, boolean hash) {
        this.redisKey = redisKey;
        this.isString = isString;
        this.isBool = isBool;
        this.isNumber = isNumber;
        this.hash = hash;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public boolean isBool() {
        return isBool;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public boolean isString() {
        return isString;
    }

    public boolean isHash() {
        return hash;
    }
}