package me.blayyke.reflex.command.custom;

import net.dv8tion.jda.core.entities.TextChannel;

public class BoxedTextChannel {
    private final TextChannel channel;

    BoxedTextChannel(TextChannel channel) {
        this.channel = channel;
    }

    public void sendMessage(String message) {
        channel.sendMessage(message).queue();
    }

    public String getName() {
        return channel.getName();
    }

    public String getId() {
        return channel.getId();
    }

    public String mention() {
        return channel.getAsMention();
    }
}