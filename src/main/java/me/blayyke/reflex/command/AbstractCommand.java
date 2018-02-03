package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.time.Instant;

public abstract class AbstractCommand {
    private Reflex reflex;

    protected final Reflex getReflex() {
        return reflex;
    }

    public Permission[] getRequiredPermissions() {
        return new Permission[0];
    }

    public final void setReflex(Reflex reflex) {
        this.reflex = reflex;
    }

    public String[] getAliases() {
        return new String[0];
    }

    public abstract CommandCategory getCategory();

    public Permission[] getBotRequiredPermissions() {
        return new Permission[0];
    }

    public abstract String getName();

    public abstract String getDesc();

    public abstract void execute(CommandContext context);

    public static EmbedBuilder createEmbed() {
        return new EmbedBuilder()
                .setColor(Colours.INFO)
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()));
    }

    public static EmbedBuilder createEmbed(Color colour) {
        return createEmbed().setColor(colour);
    }

    protected void notEnoughArgs(CommandContext context) {
        context.getChannel().sendMessage(createEmbed(Colours.WARN).setTitle("Not enough arguments").setDescription("This command requires one or more arguments!").build()).queue();
    }
}