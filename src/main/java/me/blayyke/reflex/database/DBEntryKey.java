package me.blayyke.reflex.database;

public enum DBEntryKey {
    LEAVE_MESSAGE("leave_message", true, false, false),
    JOIN_MESSAGE("join_message", true, false, false),
    GUILD_PREFIX("prefix", true, false, false),
    ANNOUNCEMENT_CHANNEL("announcement_channel", false, false, true);

    private final String redisKey;
    private final boolean isString;
    private final boolean isBool;
    private final boolean isNumber;

    DBEntryKey(String redisKey, boolean isString, boolean isBool, boolean isNumber) {
        this.redisKey = redisKey;
        this.isString = isString;
        this.isBool = isBool;
        this.isNumber = isNumber;
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
}