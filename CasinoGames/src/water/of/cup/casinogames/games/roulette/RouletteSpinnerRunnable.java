package water.of.cup.casinogames.games.roulette;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.casinogames.games.poker.Poker;

public class RouletteSpinnerRunnable extends BukkitRunnable {
	private Roulette game;
	private int totalSpins;
	private int spinsLeft;
	private RouletteSpinner spinner;
	private static int WHEELSPINS = 30;

	public RouletteSpinnerRunnable(Roulette game, RouletteSpinner spinner) {
		spinsLeft = (int) (WHEELSPINS + Math.random() * 38);
		totalSpins = spinsLeft;
		this.game = game;
		this.spinner = spinner;

	}

	@Override
	public void run() {

		if (spinsLeft == 0) {
			game.endSpin();
			cancel();
			return;
		}
		if (totalSpins - spinsLeft < WHEELSPINS)
			spinner.spin();
		else 
			spinner.moveBall(1);
		game.updateSpinner();
		spinsLeft--;

	}
}
