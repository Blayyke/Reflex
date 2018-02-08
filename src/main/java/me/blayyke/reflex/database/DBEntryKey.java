package me.blayyke.reflex.database;

public enum DBEntryKey {
    LEAVE_MESSAGE("leave_message", true),
    JOIN_MESSAGE("join_message", true),
    GUILD_PREFIX("prefix", true),
    ANNOUNCEMENT_CHANNEL("announcement_channel", false, false, true),
    AUTOROLE_ID("autorole_id", false, false, true),
    COMMANDS("commands", false, false, false, true),
    CUSTOM_COMMAND("cc", false, false, false, false, true);

    private final String redisKey;
    private final boolean isString;
    private final boolean isBool;
    private final boolean isNumber;
    private final boolean isHash;
    private boolean isSet;

    DBEntryKey(String redisKey, boolean isString) {
        this(redisKey, isString, false);
    }

    DBEntryKey(String redisKey, boolean isString, boolean isBool) {
        this(redisKey, isString, isBool, false, false);
    }

    DBEntryKey(String redisKey, boolean isString, boolean isBool, boolean isNumber) {
        this(redisKey, isString, isBool, isNumber, false);
    }

    DBEntryKey(String redisKey, boolean isString, boolean isBool, boolean isNumber, boolean isSet) {
        this(redisKey, isString, isBool, isNumber, isSet, false);
    }


    DBEntryKey(String redisKey, boolean isString, boolean isBool, boolean isNumber, boolean isSet, boolean isHash) {
        this.redisKey = redisKey;
        this.isString = isString;
        this.isBool = isBool;
        this.isNumber = isNumber;
        this.isSet = isSet;
        this.isHash = isHash;
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

    public boolean isSet() {
        return isSet;
    }

    public boolean isHash() {
        return isHash;
    }
}