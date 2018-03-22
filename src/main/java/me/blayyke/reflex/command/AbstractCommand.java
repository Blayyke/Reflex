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
    private final CommandCategory category;
    private final String name;
    private final String[] aliases;
    private final Permission[] requiredPermissions;
    private String description;
    private Map<String, Long> cooldownMap = new HashMap<>();
    private Map<String, Boolean> sentCooldownMessage = new HashMap<>();

    private Reflex reflex;

    public AbstractCommand(CommandCategory category, String name, String description, String[] aliases, Permission... requiredPermissions) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.aliases = aliases == null ? new String[0] : aliases;
        this.requiredPermissions = requiredPermissions;
    }

    protected final Reflex getReflex() {
        return reflex;
    }

    public final void setReflex(Reflex reflex) {
        this.reflex = reflex;
    }

    protected void init() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }

    public String[] getAliases() {
        return aliases;
    }

    public final void execute(CommandEnvironment env) {
        if (env.getArgs().length < getRequiredArgs()) {
            replyError(env, "This command requires at least " + getRequiredArgs() + " arguments.");
            return;
        }
        if (hasCooldown(env.getMember().getUser())) {
            if (sentCooldownMessage.get(env.getMember().getUser().getId())) return;
            replyError(env, "Please wait " + getRemainingCooldown(env.getMember().getUser()) + " seconds before executing this command.");
            sentCooldownMessage.put(env.getMember().getUser().getId(), true);
            return;
        }
        sentCooldownMessage.remove(env.getMember().getUser().getId());

        onCommand(env);
        activateCooldown(env.getMember().getUser());
    }

    public void replyError(CommandEnvironment env, String s) {
        env.getChannel().sendMessage(MiscUtils.ERROR + " " + s).queue();
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