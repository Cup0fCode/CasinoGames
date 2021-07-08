package water.of.cup.casinogames.config;

import org.bukkit.ChatColor;
import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.config.ConfigInterface;

public enum ConfigUtil implements ConfigInterface {
    GUI_TEAM_LABEL("settings.messages.gui.teamlabel", "&2Team: ");

    private final String path;
    private final String defaultValue;
    private static final BoardGames instance = BoardGames.getInstance();

    ConfigUtil(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        String configString = instance.getConfig().getString(this.path);

        if(configString == null) return "";

        return ChatColor.translateAlternateColorCodes('&', configString);
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }
}
