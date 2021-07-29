package water.of.cup.casinogames.games.poker;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import water.of.cup.boardgames.game.GamePlayer;

public class PokerTurnTimer extends BukkitRunnable {

    private static double CLOCKTIME = 15;
    private double lastTimeChange;
    private double timeLeft;
    private Poker game;
    private Player player;

    public PokerTurnTimer(Poker game) {
        lastTimeChange = System.currentTimeMillis() / 1000;
        timeLeft = CLOCKTIME;
        this.game = game;

    }

    public void start(Player player) {
        lastTimeChange = System.currentTimeMillis() / 1000;
        timeLeft = CLOCKTIME;
        this.player = player;
    }

    @Override
    public void run() {
        if (player == null)
            return;

        double time = System.currentTimeMillis() / 1000;
        double difference = time - lastTimeChange;
        lastTimeChange = time;
        timeLeft -= difference;

        if (timeLeft <= 0) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if(gamePlayer != null)
                game.foldGamePlayer(gamePlayer);

            player = null;
            game.nextTurn();
            game.reRenderBoard();
            return;
        }


        sendPlayersClockTimes();
    }

    public void sendPlayersClockTimes() {
        String timeText = player.getDisplayName() + "'s time left: " + (int) (timeLeft / 60) + ":" + (int) (timeLeft % 60);
        for (GamePlayer gamePlayer : game.getGamePlayers())
            gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.YELLOW + timeText));
        return;
    }

}
