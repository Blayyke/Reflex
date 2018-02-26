package me.blayyke.reflex;

import me.blayyke.reflex.command.custom.CustomCommand;
import me.blayyke.reflex.database.keys.KeyModLogChannel;
import me.blayyke.reflex.database.keys.guild.*;
import me.blayyke.reflex.game.MineSweeperManager;
import me.blayyke.reflex.utils.MiscUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class GuildStorage {
    private Guild guild;
    private Map<String, CustomCommand> customCommandMap = new HashMap<>();
    private MineSweeperManager mineSweeperManager;
    private Reflex reflex;

    public GuildStorage(Reflex reflex, Guild guild) {
        this.reflex = reflex;
        this.guild = guild;
        mineSweeperManager = new MineSweeperManager(guild);
    }

    public MineSweeperManager getMineSweeperManager() {
        return mineSweeperManager;
    }

    public Map<String, CustomCommand> getCustomCommandMap() {
        return customCommandMap;
    }

    public Reflex getReflex() {
        return reflex;
    }

    public TextChannel getAnnouncerChannel() {
        return guild.getTextChannelById(reflex.getDBManager().get(new KeyAnnouncerChannel(guild)));
    }

    public String getPrefix() {
        if (!reflex.getDBManager().keyExists(new KeyPrefix(guild)))
            reflex.getDBManager().set(new KeyPrefix(guild), reflex.getDataManager().getSettings().getDefaultPrefix());
        return getReflex().getDBManager().get(new KeyPrefix(guild));
    }

    public String getWelcomeMessage() {
        return getReflex().getDBManager().get(new KeyWelcomeMessage(guild));
    }

    public String getLeaveMessage() {
        return getReflex().getDBManager().get(new KeyLeaveMessage(guild));
    }

    public Role getAutoRole() {
        String s = reflex.getDBManager().get(new KeyAutoRole(guild));
        if (s == null || s.isEmpty() || !MiscUtils.isId(s)) return null;
        return guild.getRoleById(s);
    }

    public TextChannel getModLogChannel() {
        String id = reflex.getDBManager().get(new KeyModLogChannel(guild));

        if (id == null || id.isEmpty() || !MiscUtils.isId(id)) return null;
        return reflex.getShardManager().getTextChannelById(id);
    }

    public void setModLogChannel(TextChannel modLogChannel) {
        reflex.getDBManager().set(new KeyModLogChannel(guild), modLogChannel == null ? null : modLogChannel.getId());
    }
}