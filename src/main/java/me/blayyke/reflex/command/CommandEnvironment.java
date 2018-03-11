package me.blayyke.reflex.command;

import me.blayyke.reflex.Reflex;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandEnvironment {
    private final Message message;
    private final Member member;
    private final Reflex reflex;
    private final JDA jda;
    private final Guild guild;
    private final String[] args;
    private final String alias;
    private final String prefixUsed;

    public CommandEnvironment(Message message, Reflex reflex, Guild guild, String alias, String[] args, String prefixUsed) {
        this.message = message;
        this.member = this.message.getMember();
        this.reflex = reflex;
        this.guild = guild;
        this.jda = this.guild.getJDA();
        this.args = args;
        this.alias = alias;
        this.prefixUsed = prefixUsed;
    }

    public boolean hasArgs() {
        return args.length > 0;
    }

    public Guild getGuild() {
        return guild;
    }

    public JDA getJDA() {
        return jda;
    }

    public Member getMember() {
        return member;
    }

    public Message getMessage() {
        return message;
    }

    public Reflex getReflex() {
        return reflex;
    }

    public String[] getArgs() {
        return args;
    }

    public TextChannel getChannel() {
        return message.getTextChannel();
    }

    public String getAlias() {
        return alias;
    }

    public String getPrefixUsed() {
        return prefixUsed;
    }
}
