package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandCoin extends AbstractCommand {
    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String getName() {
        return "coin";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"coinflip"};
    }

    @Override
    public String getDesc() {
        return "Flip a coin and show the side";
    }

    @Override
    public void execute(CommandContext context) {
        int random = MiscUtils.getRandom(100) + 1;
        EmbedBuilder embedBuilder = createEmbed();

        embedBuilder.setTitle("Coin flip");
        embedBuilder.setDescription("I flipped a coin. The result was " + (random >= 50 ? "Heads" : "Tails") + ".");

        context.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}