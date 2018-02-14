package me.blayyke.reflex;

import me.blayyke.reflex.command.custom.CustomCommand;
import me.blayyke.reflex.game.MineSweeperManager;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildStorage {
    private Guild guild;
    private Map<String, CustomCommand> customCommandMap = new HashMap<>();
    private MineSweeperManager mineSweeperManager;

    public GuildStorage(Guild guild) {
        this.guild = guild;
        mineSweeperManager = new MineSweeperManager(guild);
    }

    public MineSweeperManager getMineSweeperManager() {
        return mineSweeperManager;
    }

    public Map<String, CustomCommand> getCustomCommandMap() {
        return customCommandMap;
    }
}