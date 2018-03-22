package me.blayyke.reflex.command.commands.developer;

import me.blayyke.reflex.Colours;
import me.blayyke.reflex.command.AbstractCommand;
import me.blayyke.reflex.command.CommandCategory;
import me.blayyke.reflex.command.CommandEnvironment;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CommandEval extends AbstractCommand {
    private ScriptEngine engine;

    public CommandEval() {
        super(CommandCategory.DEVELOPER, "evaluate", "Evaluate some JS code", new String[]{"eval", "js"});
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

    public void onCommand(CommandEnvironment env) {
        engine.put("environment", env);
        engine.put("env", env);

        try {
            Object out = engine.eval(
                    "(function() {" +
                            "with (imports) {" +
                            env.getMessage().getContentRaw().substring((env.getPrefixUsed() + env.getAlias()).length() + 1) +
                            "}" +
                            "})();");
            env.getChannel().sendMessage(out == null ? "Executed successfully." : out.toString()).queue();
        } catch (ScriptException e) {
            env.getChannel().sendMessage(createEmbed(Colours.ERROR).setTitle("Failure!").setDescription("Failed to onCommand code.").build()).queue();
            e.printStackTrace();
        }
    }
}