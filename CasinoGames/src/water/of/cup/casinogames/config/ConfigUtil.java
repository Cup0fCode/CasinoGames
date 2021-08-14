package water.of.cup.casinogames.config;

import org.bukkit.ChatColor;
import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.config.ConfigInterface;

public enum ConfigUtil implements ConfigInterface {

    // CHAT

    // POKER
    CHAT_POKER_ALL_IN("settings.messages.chat.pokerallin", "%player% has gone all in!"),
    CHAT_POKER_BET_SMALL_BLIND("settings.messages.chat.pokersmallblind", "%player% bet small blind %num%"),
    CHAT_POKER_BET_BIG_BLIND("settings.messages.chat.pokerbigblind", "%player% bet big blind %num%"),
    CHAT_POKER_JOIN("settings.messages.chat.pokerjoin", "You have joined the Poker game."),
    CHAT_POKER_GAME_STARTING("settings.messages.chat.pokergamestarting", "Game starting!"),
    CHAT_POKER_PLAYER_CALL("settings.messages.chat.pokerplayercall", "%player% has called %num%"),
    CHAT_POKER_PLAYER_RAISE("settings.messages.chat.pokerplayerraise", "%player% has raised the bet to %num%"),
    CHAT_POKER_PLAYER_CHECK("settings.messages.chat.pokerplayercheck", "%player% has checked"),
    CHAT_POKER_PLAYER_FOLD("settings.messages.chat.pokerplayerfold", "%player% has folded"),
    CHAT_POKER_MONEY_BACK("settings.messages.chat.pokermoneyback", "You get %num% back!"),
    CHAT_POKER_NEXT_ROUND("settings.messages.chat.pokernextround", "Next round starting! Pot: %num%"),
    CHAT_POKER_SIDE_POT("settings.messages.chat.pokersidepot", "Side pot: %num% Players: %num2%"),
    CHAT_POKER_FLOP("settings.messages.chat.pokerflop", "Flop:"),
    CHAT_POKER_GAME_OVER("settings.messages.chat.pokergameover", "Game over!"),
    CHAT_POKER_WIN_POT("settings.messages.chat.pokerwinpot", "%player% won the pot worth %num%"),
    CHAT_POKER_WIN_SIDE_POT("settings.messages.chat.pokerwinsidepot", "%player% won the side pot worth %num%"),
    CHAT_POKER_NEXT_GAME("settings.messages.chat.pokernextgame", "Starting next game..."),
    CHAT_POKER_PLAYER_REMOVE("settings.messages.chat.pokerplayerremove", "%player% has been removed for not having enough money. "),
    CHAT_POKER_NOT_ENOUGH_PLAYERS("settings.messages.chat.pokernotenoughplayers", "Not enough players to start next game."),
    CHAT_POKER_GAME_START_TIMER("settings.messages.chat.pokergamestarttimer", "Time to next game: %num%:%num2%"),
    CHAT_POKER_GAME_PLAYER_TIMER("settings.messages.chat.pokergameplayertimer", "%player%'s time left: %num%:%num2%"),

    // MINES
    CHAT_MINES_CURRENT_MULT("settings.messages.chat.minescurrentmult", "Current multiplier: %num%x Cash out at: %num2%%"),
    CHAT_MINES_LOSE("settings.messages.chat.minelose", "You lost at %num%x!"),
    CHAT_MINES_WIN("settings.messages.chat.minewin", "You cashed out at %num%x! Payout: %num2%"),

    // PLINKO
    CHAT_PLINKO_WIN("settings.messages.chat.plinkowin", "Plinko: You won: %num%"),

    // NPC
    NPC_POKER_NAME("settings.messages.npc.pokernpcname", "Dealer"),
    NPC_POKER_SKIN_SIG("settings.messages.npc.pokerskinsig", "T5QGS3fQ9wWvsjmD6l9b/nZMkfOfYW1X3c1xvDdZQ5WHvPmew//3Q86+yfgQqIjPvEcXiDilr71p3WDrz/itsLb5mf9wLU5P4X18x5c6bmmv49TDLUCH5mEIUXu1jiQ8Kog/vzZNGZAAxadTGQPJ7BdII/+OpHDLS+WiCPRMnjCs/1h5RTE7I1OOPQnsh+yk+gOpaxCxgVFMLnMqNnL3mJP05qajHI6OKKXnyyXPwV0xxA3XT2WPbtCPsux3CjNCPP7fA1mYL4dPtdTaju9kP+6jeuf0IkS0jZ31bHKx324cM/W4xiSbR/2OSyYepHdS7TxWPZIYpkMPbaHMLXao7Ok209LD7p3GWZ5RDNvnZTcvGlF10wKoHJ9xy7lHoSfy4NfRAD3doATK5meRo7/JQCCo8M8Mw6dnBvYC9bcb3zCrvTkwQz2dfjkHvmH/QcWkJS5iqYCS6Uk67PJsFtYxa5a9ZBiZGUVxhprrB0hoZem0vfsnzGgzbwjpw0VxDSN1ndXSIJZ4yXB2KI58NE0HMjkVL9OcmOItoS4fqLqdo7CqrntdHsRcDZ7lSaCFVphBMsJI3AbrWAyIM54N9SSMJgpQkrbJ1tWhO1jp8mTXGqW1YlbmCEFS+LRR6sk/F3YK6FtSucJlhlrOdeKGHVaESWLVFzTMBgfVS3TfKSxSRI8="),
    NPC_POKER_SKIN_DATA("settings.messages.npc.pokerskindata", "ewogICJ0aW1lc3RhbXAiIDogMTYxODIxNTU4MTc1OCwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA0NGNiODMyZDg3Y2RlNmFmNDJhMGRlNDdiYzg1YTY3YzdkNGU1OWEyZDc0NjY2MTc2ZDFjYTQxYWJkMGEyZCIKICAgIH0KICB9Cn0="),

    // GUI
    GUI_BOMB_AMOUNT_LABEL("settings.messages.gui.bombamountlabel", "&2Bomb Amount: "),
    GUI_BET_AMOUNT_LABEL("settings.messages.gui.betamountlabel", "&2Bet Amount: "),
    GUI_MINES_CASH_OUT("settings.messages.gui.minescashout", "&aCash-Out"),
    GUI_POKER_MIN_ENTRY_LABEL("settings.messages.gui.pokerminentrylabel", "&aMinimum Entry: "),
    GUI_POKER_RAISE_LIMIT_LABEL("settings.messages.gui.pokerraiselimitlabel", "&aRaise Limit: "),
    GUI_PLINKO_LOW_RISK("settings.messages.gui.plinkolowrisk", "Low Risk"),
    GUI_PLINKO_NORMAL_RISK("settings.messages.gui.plinkonormalrisk", "Normal Risk"),
    GUI_PLINKO_HIGH_RISK("settings.messages.gui.plinkohighrisk", "High Risk");

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

    public String toRawString() {
        return ChatColor.stripColor(this.toString());
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String buildString(String replaceWith) {
        String formatted = this.toString();

        formatted = formatted.replace("%player%", replaceWith)
                .replace("%game%", replaceWith)
                .replace("%num%", replaceWith);
        return formatted;
    }

    public String buildString(String replaceWith, int num) {
        String formatted = this.toString();

        formatted = formatted
                .replace("%player%", replaceWith)
                .replace("%game%", replaceWith)
                .replace("%num%", num + "");

        return formatted;
    }

    public String buildString(String replaceWith, int num, int num2) {
        String formatted = this.toString();

        formatted = formatted
                .replace("%player%", replaceWith)
                .replace("%game%", replaceWith)
                .replace("%num%", num + "")
                .replace("%num2%", num2 + "");

        return formatted;
    }

    public String buildString(Number num, Number num2) {
        String formatted = this.toString();

        formatted = formatted
                .replace("%num%", num + "")
                .replace("%num2%", num2 + "");

        return formatted;
    }
}
