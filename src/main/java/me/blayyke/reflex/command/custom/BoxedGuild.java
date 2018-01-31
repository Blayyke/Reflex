package me.blayyke.reflex.command.custom;

import net.dv8tion.jda.core.entities.Guild;

public class BoxedGuild {
    private final Guild guild;

    BoxedGuild(Guild guild) {
        this.guild = guild;
    }

    public BoxedTextChannel getTextChannel(String id) {
        return guild.getTextChannelById(id) == null ? null : new BoxedTextChannel(guild.getTextChannelById(id));
    }

    public BoxedVoiceChannel getVoiceChannel(String id) {
        return guild.getVoiceChannelById(id) == null ? null : new BoxedVoiceChannel(guild.getVoiceChannelById(id));
    }

    public BoxedRole getRole(String id) {
        return guild.getRoleById(id) == null ? null : new BoxedRole(guild.getRoleById(id));
    }

    public String getName() {
        return guild.getName();
    }

    public String getId() {
        return guild.getId();
    }
}