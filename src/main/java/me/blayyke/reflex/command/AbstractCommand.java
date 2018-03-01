package me.blayyke.reflex.command;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.Reflex;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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

    public final void execute(CommandEnvironment context) {
        if (context.getArgs().length < getRequiredArgs()) {
            replyError(context, "This command requires at least " + getRequiredArgs() + " arguments.");
            return;
        }
        if (hasCooldown(context.getMember().getUser())) {
            replyError(context, "Please wait " + getRemainingCooldown(context.getMember().getUser()) + " seconds before executing this command.");
            return;
        }

        onCommand(context);
        activateCooldown(context.getMember().getUser());
    }

    public void replyError(CommandEnvironment context, String s) {
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

    protected abstract void onCommand(CommandEnvironment env);

    public static EmbedBuilder createEmbed() {
        return new EmbedBuilder()
                .setColor(Colours.INFO)
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()));
    }

    /**
     * Handle a list of members. Sends an error if none or multiple were found.
     *
     * @param env  The {@link me.blayyke.reflex.command.CommandEnvironment}
     * @param list The list of members to check.
     * @return True if the list contains only a single member.
     */
    protected boolean handleMemberList(CommandEnvironment env, List<Member> list) {
        if (list == null) {
            replyError(env, "Something went wrong (null list)!");
            return false;
        } else if (list.isEmpty()) {
            replyError(env, "No members were found with your input. Accepted inputs are either username, ID, DiscordTag(Name#9999) or mention.");
            return false;
        } else if (list.size() > 1) {
            replyError(env, "Multiple members were found with your input. Please be more specific (DiscordTag, ID or mention).");
            return false;
        }
        return true;
    }

    /**
     * Handle a list of members. Sends an error if none or multiple were found.
     *
     * @param env  The {@link me.blayyke.reflex.command.CommandEnvironment}
     * @param list The list of members to check.
     * @return True if the list contains only a single member.
     */
    protected boolean handleChannelList(CommandEnvironment env, List<TextChannel> list) {
        if (list == null) {
            replyError(env, "Something went wrong (null list)!");
            return false;
        } else if (list.isEmpty()) {
            replyError(env, "No channels were found with your input. Accepted inputs are either name, ID or mention.");
            return false;
        } else if (list.size() > 1) {
            replyError(env, "Multiple channels were found with your input. Please be more specific (ID or mention).");
            return false;
        }
        return true;
    }

    /**
     * Handle a list of members. Sends an error if none or multiple were found.
     *
     * @param env  The {@link me.blayyke.reflex.command.CommandEnvironment}
     * @param list The list of members to check.
     * @return True if the list contains only a single member.
     */
    protected boolean handleRoleList(CommandEnvironment env, List<Role> list) {
        if (list == null) {
            replyError(env, "Something went wrong (null list)!");
            return false;
        } else if (list.isEmpty()) {
            replyError(env, "No roles were found with your input. Accepted inputs are either name, ID or mention.");
            return false;
        } else if (list.size() > 1) {
            replyError(env, "Multiple roles were found with your input. Please be more specific (ID or mention).");
            return false;
        }
        return true;
    }

    public static EmbedBuilder createEmbed(Color colour) {
        return createEmbed().setColor(colour);
    }

    /**
     * The cooldown, in seconds, before this command can be executed again.
     *
     * @return The cooldown before this command can be used again.
     */
    public int getCooldown() {
        return 0;
    }

    public int getRequiredArgs() {
        return 0;
    }
}