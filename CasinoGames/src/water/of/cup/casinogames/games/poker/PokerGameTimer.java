package water.of.cup.casinogames.games.poker;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.MathUtils;
import water.of.cup.casinogames.config.ConfigUtil;

public class PokerGameTimer extends BukkitRunnable {

//    private static final double TIME_UNTIL_START = 15;
    private double lastTimeChange;
    private double timeLeft;
    private final Poker game;

    public PokerGameTimer(Poker game) {
        lastTimeChange = System.currentTimeMillis() / 1000;
        timeLeft = Float.parseFloat(game.getConfigValue("game_timer") + "");
        this.game = game;
    }

    @Override
    public void run() {
        double time = System.currentTimeMillis() / 1000;
        double difference = time - lastTimeChange;
        lastTimeChange = time;

        timeLeft -= difference;

        if (timeLeft <= 0) {
            game.startGame();
            cancel();
            return;
        }

        sendPlayersClockTimes();
    }

    private void sendPlayersClockTimes() {
        String timeText = ConfigUtil.CHAT_POKER_GAME_START_TIMER.buildString((int) (timeLeft / 60), (int) (timeLeft % 60));
        for (GamePlayer gamePlayer : game.getGamePlayers())
            gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.YELLOW + timeText));

    }
}
