package me.blayyke.reflex.listeners;

import me.blayyke.reflex.Reflex;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private Reflex reflex;

    public MessageListener(Reflex reflex) {
        this.reflex = reflex;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event == null || event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage())
            return;
        if (!event.getChannel().canTalk()) return;

        String cmdPrefix = reflex.getPrefix(event.getGuild());
        String content = event.getMessage().getContentRaw().replaceAll(" +", " ");

        if (content == null) {
            reflex.getLogger().debug("Received null-content for message " + event.getMessageId());
            return;
        }

        String selfId = reflex.getShardManager().getShards().get(0).getSelfUser().getId();
        if (content.equalsIgnoreCase("<@" + selfId + ">") || content.equals("<@!" + selfId + ">")) {
            content = event.getGuild().getSelfMember().getAsMention() + " help";
        }

        if (cmdPrefix == null) {
            reflex.getLogger().warn("Prefix is null in guild " + event.getGuild().getIdLong() + "!");
            return;
        }

        if (!content.toLowerCase().startsWith(cmdPrefix)) {
            if (!content.startsWith(event.getGuild().getSelfMember().getAsMention() + " ")) return;
            cmdPrefix = event.getGuild().getSelfMember().getAsMention() + " ";
        }

        content = content.substring(cmdPrefix.length()).trim();

        String command = content;
        String[] args = new String[0];

        if (command.contains(" ")) {
            command = content.split(" ")[0];
            args = content.substring(command.length() + 1).split(" ");
        }

        reflex.getCommandManager().dispatchCommand(command, args, event, cmdPrefix);
    }
}