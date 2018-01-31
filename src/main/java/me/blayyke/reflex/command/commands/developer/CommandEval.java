package me.blayyke.reflex.command.commands.developer;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CommandEval extends AbstractCommand {
    private ScriptEngine engine;

    public CommandEval() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(" +
                    "java.io," +
                    "java.lang," +
                    "java.util," +
                    "Packages.net.dv8tion.jda.core," +
                    "Packages.net.dv8tion.jda.core.entities," +
                    "Packages.net.dv8tion.jda.core.entities.impl," +
                    "Packages.net.dv8tion.jda.core.managers," +
                    "Packages.net.dv8tion.jda.core.managers.impl," +
                    "Packages.net.dv8tion.jda.core.utils);");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"eval"};
    }

    @Override
    public String getName() {
        return "evaluate";
    }

    @Override
    public void execute(CommandContext context) {
        if (!context.hasArgs()) {
            notEnoughArgs(context);
            return;
        }

        engine.put("context", context);

        try {
            Object out = engine.eval(
                    "(function() {" +
                            "with (imports) {" +
                            context.getMessage().getContentRaw().substring((context.getPrefixUsed() + context.getAlias()).length() + 1) +
                            "}" +
                            "})();");
            context.getChannel().sendMessage(out == null ? "Executed successfully." : out.toString()).queue();
        } catch (ScriptException e) {
            context.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Failure!").setDescription("Failed to execute code.").build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getDesc() {
        return "Evaluates some javascript code (Nashorn)";
    }
}
