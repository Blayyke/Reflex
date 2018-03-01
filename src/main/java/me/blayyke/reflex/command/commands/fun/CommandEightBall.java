package me.blayyke.reflex.command.commands.fun;

import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandEightBall extends AbstractCommand {
    private String[] responses = new String[]{
            "It is certain", "It is decidedly so", "Without a doubt", "Yes, definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
            "Reply hazy, try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again",
            "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"eightball"};
    }

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public String getDesc() {
        return "Ask the 8-ball for an answer";
    }

    @Override
    public void onCommand(CommandEnvironment env) {
        EmbedBuilder embedBuilder = createEmbed();
        embedBuilder.setTitle("8-ball");
        embedBuilder.setDescription("\uD83C\uDFB1: " + responses[MiscUtils.getRandom(responses.length - 1)]);
        env.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }
}