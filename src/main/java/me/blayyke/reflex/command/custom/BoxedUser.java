package me.blayyke.reflex.command.custom;

import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.entities.Member;

public class BoxedUser {
    private final Member member;

    BoxedUser(Member member) {
        this.member = member;
    }

    public String getName() {
        return member.getUser().getName();
    }

    public String getId() {
        return member.getUser().getId();
    }

    public String getDiscriminator() {
        return member.getUser().getDiscriminator();
    }

    public void sendMessage(String message) {
        member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    public String getAvatar() {
        return member.getUser().getEffectiveAvatarUrl();
    }

    public String mention() {
        return member.getAsMention();
    }

    @Override
    public String toString() {
        return UserUtils.formatUser(member.getUser());
    }
}