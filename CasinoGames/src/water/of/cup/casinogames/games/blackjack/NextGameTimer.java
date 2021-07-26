package water.of.cup.casinogames.games.blackjack;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import water.of.cup.boardgames.game.GamePlayer;

public class NextGameTimer extends BukkitRunnable {
	private static double TIME_UNTIL_START = 20;
	private double lastTimeChange;
	private double timeLeft;
	private Blackjack game;

	public NextGameTimer(Blackjack game) {
		lastTimeChange = System.currentTimeMillis() / 1000;
		timeLeft = TIME_UNTIL_START;
		this.game = game;
		
	}

	@Override
	public void run() {
		double time = System.currentTimeMillis() / 1000;
		double difference = time - lastTimeChange;
		lastTimeChange = time;
		
		if (game.getAmountOfBets() == 0)
			return;
		
		timeLeft -= difference;

		if (timeLeft <= 0) {
			// game.star;
			game.startRound();
			cancel();
			return;
		}
		sendPlayersClockTimes();

		
	}

	public void sendPlayersClockTimes() {
		String timeText = "Time to next game: " + (int) (timeLeft / 60) + ":" + (int) (timeLeft % 60);		
		for (GamePlayer gamePlayer : game.getGamePlayers())
			gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
					TextComponent.fromLegacyText(ChatColor.YELLOW + timeText));
		return;

	}
}
