package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
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
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();
        List<Emote> emoteList = ParseUtils.getEmotes(getReflex().getShardManager(), env.getArgs()[0]);
        if (emoteList.isEmpty()) {
            //No emotes found. Let's do unicode stuff
            embedBuilder.setTitle("Character info");

            for (String s : env.getArgs()) {
                embedBuilder.appendDescription(MiscUtils.toHexString(s)).appendDescription(" | ")
                        .appendDescription(MiscUtils.getCharName(s)).appendDescription("\n");
            }

            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (emoteList.size() > 1) {
            embedBuilder.setColor(Colours.WARN);
            embedBuilder.setTitle("Multiple emotes found");
            embedBuilder.setDescription("I found more than one emotes from your input. Please specify emote ID or mention the emote.");

            env.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        env.getChannel().sendMessage(createEmbed().setTitle(emoteList.get(0).getName()).setImage(emoteList.get(0).getImageUrl()).build()).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}