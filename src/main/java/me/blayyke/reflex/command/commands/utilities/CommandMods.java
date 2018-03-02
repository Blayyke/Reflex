package me.blayyke.reflex.command.commands.utilities;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.UserUtils;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandMods extends AbstractCommand {
    private Map<OnlineStatus, String> statusMap = new HashMap<>();

    public CommandMods() {
        statusMap.put(OnlineStatus.DO_NOT_DISTURB, "<:rDnd:419043487195987968>");
        statusMap.put(OnlineStatus.IDLE, "<:rIdle:419043486830952469>");
        statusMap.put(OnlineStatus.OFFLINE, "<:rOffline:419043487065964555>");
        statusMap.put(OnlineStatus.ONLINE, "<:rOnline:419043487238062081>");
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILITIES;
    }

    @Override
    public String getName() {
        return "mods";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"staff"};
    }

    @Override
    public String getDesc() {
        return "Show the server moderators";
    }

    @Override
    protected void onCommand(CommandEnvironment env) {
        StringBuilder builder = new StringBuilder();
        List<Member> members = env.getGuild().getMembers().stream().filter(m -> !m.getUser().isBot()).filter(m -> m.hasPermission(Permission.KICK_MEMBERS, Permission.MESSAGE_MANAGE)).collect(Collectors.toList());

        if (members.isEmpty()) replyError(env, "No moderators are currently online!");

        List<Member> online = new ArrayList<>();
        List<Member> offline = new ArrayList<>();
        List<Member> dnd = new ArrayList<>();
        List<Member> idle = new ArrayList<>();

        members.forEach(m -> {
            switch (m.getOnlineStatus()) {
                case OFFLINE:
                    offline.add(m);
                    break;
                case IDLE:
                    idle.add(m);
                    break;
                case ONLINE:
                    online.add(m);
                    break;
                case DO_NOT_DISTURB:
                    dnd.add(m);
                    break;
            }
        });

        online.forEach(m -> builder.append(getStatus(m)).append(" ").append(UserUtils.formatUser(m.getUser())).append('\n'));
        idle.forEach(m -> builder.append(getStatus(m)).append(" ").append(UserUtils.formatUser(m.getUser())).append('\n'));
        dnd.forEach(m -> builder.append(getStatus(m)).append(" ").append(UserUtils.formatUser(m.getUser())).append('\n'));
        offline.forEach(m -> builder.append(getStatus(m)).append(" ").append(UserUtils.formatUser(m.getUser())).append('\n'));

        env.getChannel().sendMessage("Server moderators: \n" + builder).queue();
    }

    private String getStatus(Member m) {
        return statusMap.get(m.getOnlineStatus());
    }
}