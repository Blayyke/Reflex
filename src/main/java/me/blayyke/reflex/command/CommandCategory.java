package me.blayyke.reflex.command;

public enum CommandCategory {
    MODERATION("Moderation", "Commands used for assisting server moderators"),
    DEVELOPER("Developer", "Commands that can only be used for the bot's developer"),
    UTILITIES("Other", "Commands that don't fit into the other categories"),
    CUSTOMIZATION("Customization", "Commands that change things about the bot for your guild. These commands require the Manage Server permission"),
    CUSTOM("Custom Commands", "Commands that are unique to this guild"),
    FUN("Fun", "Commands that are just for fun");

    private String name;
    private String description;

    CommandCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}