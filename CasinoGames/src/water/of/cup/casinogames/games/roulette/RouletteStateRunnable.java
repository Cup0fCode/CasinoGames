package water.of.cup.casinogames.games.roulette;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.casinogames.config.ConfigUtil;

public class RouletteStateRunnable extends BukkitRunnable {
	private Roulette game;
	private RouletteState state;
	private static final double BETTING_TIME = 30;
	private static final double PAYOUT_TIME = 10;
	private double lastTimeChange;
	private double timeLeft;

	public RouletteStateRunnable(Roulette game) {
		this.game = game;
		state = RouletteState.BETTING;
		timeLeft = BETTING_TIME;
		lastTimeChange = System.currentTimeMillis() / 1000;

	}

	public boolean canBet() {
		return state == RouletteState.BETTING;
	}

	public void endSpin() {
		state = RouletteState.PAYOUT;
		timeLeft = PAYOUT_TIME;
	}

	public void resetTime() {
		if (state == RouletteState.BETTING)
			timeLeft = BETTING_TIME;
		else if (state == RouletteState.PAYOUT)
			timeLeft = PAYOUT_TIME;
	}

	@Override
	public void run() {
		double time = System.currentTimeMillis() / 1000;
		double difference = time - lastTimeChange;
		lastTimeChange = time;

		if (state == RouletteState.SPINNING)
			return;

		if (state == RouletteState.BETTING) {
			if (!game.betMade())
				return;

			timeLeft -= difference;
			sendPlayersClockTimes();
			
			if (timeLeft <= 0) {
				game.spin();
				state = RouletteState.SPINNING;
				
			}
			return;
		}

		if (state == RouletteState.PAYOUT) {
			timeLeft -= difference;
			if (timeLeft <= 0) {
				state = RouletteState.BETTING;
				timeLeft = BETTING_TIME;
				game.dealerSendAll("Place your bets!");
				game.clearBetButtons();
			}

			return;
		}
	}

	public void sendPlayersClockTimes() {
		String timeText = "Bets close in " + (int) (timeLeft / 60) + ":" + (int) (timeLeft % 60);
		for (GamePlayer gamePlayer : game.getGamePlayers())
			gamePlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
					TextComponent.fromLegacyText(ChatColor.YELLOW + timeText));
		return;
	}

	private enum RouletteState {
		BETTING, SPINNING, PAYOUT
	}
}
