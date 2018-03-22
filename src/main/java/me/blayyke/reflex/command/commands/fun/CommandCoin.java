package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandCoin extends AbstractCommand {
    public CommandCoin() {
        super(CommandCategory.FUN, "coin", "Flip a coin", new String[]{"coinflip", "cf"});
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        int random = MiscUtils.getRandom(100) + 1;
        EmbedBuilder embedBuilder = createEmbed();

        embedBuilder.setTitle("Coin flip");
        embedBuilder.setDescription("I flipped a coin. The result was " + (random >= 50 ? "Heads" : "Tails") + ".");

        env.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}