package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand {
    private Map<String, Long> cooldownMap = new HashMap<>();

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

    public final void execute(CommandContext context) {
        if (hasCooldown(context.getMember().getUser())) {
            replyError(context, "Please wait " + getRemainingCooldown(context.getMember().getUser()) + " seconds before executing this command.");
            return;
        }

        onCommand(context);
        activateCooldown(context.getMember().getUser());
    }

    public void replyError(CommandContext context, String s) {
        context.getChannel().sendMessage(MiscUtils.ERROR + " " + s).queue();
    }

    private void activateCooldown(User user) {
        cooldownMap.put(user.getId(), System.currentTimeMillis());
    }

    private boolean hasCooldown(User user) {
        return cooldownMap.containsKey(user.getId()) && cooldownMap.get(user.getId()) >= (System.currentTimeMillis() - getCooldown() * 1000);
    }

    private int getRemainingCooldown(User user) {
        long started = cooldownMap.get(user.getId());
        long current = System.currentTimeMillis();

        return Math.toIntExact(((started + (getCooldown() * 1000) - current) / 1000) + 1);
    }

    protected abstract void onCommand(CommandContext context);

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

    /**
     * The cooldown, in seconds, before this command can be executed again.
     *
     * @return The cooldown before this command can be used again.
     */
    public int getCooldown() {
        return 0;
    }
}