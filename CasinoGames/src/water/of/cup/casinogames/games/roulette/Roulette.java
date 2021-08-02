package water.of.cup.casinogames.games.roulette;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.game.BoardItem;
import water.of.cup.boardgames.game.Button;
import water.of.cup.boardgames.game.Clock;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GameConfig;
import water.of.cup.boardgames.game.GameImage;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.storage.GameStorage;

public class Roulette extends Game {
	private RouletteSpinner spinner;
	private Button spinnerButton;
	private boolean spinning;
	private RouletteSpinnerRunnable spinnerRunnable;
	private int spinnerVal;

	public Roulette(int rotation) {
		super(rotation);
		spinner = new RouletteSpinner(this);
		spinnerButton = new Button(this, spinner.getGameImage(), new int[] { 0, 128 }, 0, "spinner");
		buttons.add(spinnerButton);
		spinning = false;
		spinnerVal = 0;
	}

	@Override
	protected void setMapInformation(int rotation) {
		this.mapStructure = new int[][] { { 5, 6 }, { 3, 4 }, { 1, 2 } };
		this.placedMapVal = 1;

	}

	@Override
	public void startGame() {
		super.startGame();
	}

	@Override
	protected void setGameName() {
		this.gameName = "Roulette";

	}

	@Override
	protected void setBoardImage() {
		this.gameImage = new GameImage("ROULETTE_BOARD");

	}

	@Override
	protected void clockOutOfTime() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Clock getClock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GameInventory getGameInventory() {
		// TODO Auto-generated method stub
		return new RouletteInventory(this);
	}

	@Override
	protected GameStorage getGameStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getTeamNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GameConfig getGameConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void click(Player player, double[] loc, ItemStack map) {
		if (spinning)
			return;

		spin();
	}

	private void spin() {
		spinning = true;
		spinnerRunnable = new RouletteSpinnerRunnable(this, spinner);
		spinnerRunnable.runTaskTimer(BoardGames.getInstance(), 3, 3);
	}

	protected void endSpin() {
		spinning = false;
		if (!spinnerRunnable.isCancelled())
			spinnerRunnable.cancel();
		spinnerVal = spinner.getValue();
		teamManager.getGamePlayers().get(0).getPlayer().sendMessage("value: " + spinnerVal);
	}

	protected void updateSpinner() {
		spinnerButton.setImage(spinner.getGameImage());
		mapManager.renderBoard();
	}

	@Override
	protected void gamePlayerOutOfTime(GamePlayer turn) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getBoardItem() {
		return new BoardItem(gameName, new ItemStack(Material.BLACK_CARPET, 1));
	}
}
