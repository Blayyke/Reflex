package me.blayyke.reflex.command.custom;

import net.dv8tion.jda.core.entities.VoiceChannel;

public class BoxedVoiceChannel {
    private VoiceChannel channel;

    BoxedVoiceChannel(VoiceChannel channel) {
        this.channel = channel;
    }

    public String getName() {
        return channel.getName();
    }

    public String getId() {
        return channel.getId();
    }
}