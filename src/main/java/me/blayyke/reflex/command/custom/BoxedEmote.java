package me.blayyke.reflex.command.custom;

import net.dv8tion.jda.core.entities.Emote;

public class BoxedEmote {
    private final Emote emote;

    public BoxedEmote(Emote emote) {
        this.emote = emote;
    }

    public String getName() {
        return emote.getName();
    }

    public String getId() {
        return emote.getId();
    }

    public String mention() {
        return emote.getAsMention();
    }
}