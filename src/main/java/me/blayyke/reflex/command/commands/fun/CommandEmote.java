package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.MiscUtils;
import me.blayyke.reflex.utils.ParseUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;

import java.util.List;

public class CommandEmote extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "emote";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"emoji"};
    }

    @Override
    public String getDesc() {
        return "Sends the picture of an emote (NOT inbuilt discord emojis).";
    }

    @Override
    public void onCommand(CommandContext context) {
        if (!context.hasArgs()) {
            notEnoughArgs(context);
            return;
        }

        EmbedBuilder embedBuilder = createEmbed();
        List<Emote> emoteList = ParseUtils.getEmotes(getReflex().getShardManager(), context.getArgs()[0]);
        if (emoteList.isEmpty()) {
            //No emotes found. Let's do unicode stuff
            embedBuilder.setTitle("Character info");

            for (String s : context.getArgs()) {
                embedBuilder.appendDescription(MiscUtils.toHexString(s)).appendDescription(" | ")
                        .appendDescription(MiscUtils.getCharName(s)).appendDescription("\n");
            }

            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (emoteList.size() > 1) {
            embedBuilder.setColor(Colours.WARN);
            embedBuilder.setTitle("Multiple emotes found");
            embedBuilder.setDescription("I found more than one emotes from your input. Please specify emote ID or mention the emote.");

            context.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        context.getChannel().sendMessage(createEmbed().setTitle(emoteList.get(0).getName()).setImage(emoteList.get(0).getImageUrl()).build()).queue();
    }
}