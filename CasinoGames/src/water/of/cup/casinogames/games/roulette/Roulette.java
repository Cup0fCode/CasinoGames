package water.of.cup.casinogames.games.roulette;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

	private HashMap<GamePlayer, ArrayList<RouletteBet>> playerBets;

	private Button[] straitButtons;
	private Button[] strait0Buttons;
	private Button[] splitButtons;
	private Button[] streetButtons;
	private Button[] sixLineButtons;
	private Button[] cornerButtons;
	private Button[] trioButtons;
	private Button basketButton;
	private Button[] redBlackButtons;
	private Button[] oddEvenButtons;
	private Button[] eighteenButtons;
	private Button[] dozensButtons;
	private Button[] columnsButtons;

	private Button[][] betButtons;

	public Roulette(int rotation) {
		super(rotation);
		spinner = new RouletteSpinner(this);
		spinnerButton = new Button(this, spinner.getGameImage(), new int[] { 0, 256 }, 0, "spinner");
		buttons.add(spinnerButton);
		setBetButtons();

		spinning = false;
		spinnerVal = 0;
	}

	private void setBetButtons() {
		Button betsButton = new Button(this, "ROULETTE_BETS", new int[] { 0, 0 }, 2, "bets");
		buttons.add(betsButton);

//		private Button[] cornerButtons;
		BufferedImage cornerImage = new BufferedImage(7, 7, BufferedImage.TYPE_INT_RGB);
		cornerButtons = new Button[22];
		for (int i = 0; i < 22; i++) {
			int[] loc = { 113 - (i % 2) * 23, 207 - (i / 2) * 17 - i / 8};
			cornerButtons[i] = new Button(this, new GameImage(cornerImage, 0), loc, 0, "corner");
			cornerButtons[i].setClickable(true);
			cornerButtons[i].setVisible(false);
			buttons.add(cornerButtons[i]);
		}

//		private Button[] straitButtons;
		BufferedImage straitImage = new BufferedImage(18, 12, BufferedImage.TYPE_INT_RGB);
		straitButtons = new Button[36];
		for (int i = 0; i < 36; i++) {
			int[] loc = { 119 - (i % 3) * 23, 213 - (i / 3) * 17 - i / 12};
			straitButtons[i] = new Button(this, new GameImage(straitImage, 0), loc, 0, "strait");
			straitButtons[i].setClickable(true);
			straitButtons[i].setVisible(false);
			buttons.add(straitButtons[i]);
		}

//		private Button[] splitButtons;
		BufferedImage splitHImage = new BufferedImage(16, 5, BufferedImage.TYPE_INT_RGB);
		BufferedImage splitVImage = new BufferedImage(5, 10, BufferedImage.TYPE_INT_RGB);

		splitButtons = new Button[57];
		for (int i = 0; i < 24; i++) {
			int[] loc = { 114 - (i % 2) * 23, 214 - (i / 2) * 17 - i / 8};
			splitButtons[i] = new Button(this, new GameImage(splitVImage, 0), loc, 0, "split");
			splitButtons[i].setClickable(true);
			splitButtons[i].setVisible(false);
			buttons.add(splitButtons[i]);
		}
		for (int i = 24; i < 57; i++) {
			int[] loc = { 120 - ((i - 24) % 3) * 23, 208 - ((i - 24) / 3) * 17 - (i - 24) / 12};
			splitButtons[i] = new Button(this, new GameImage(splitHImage, 0), loc, 0, "split");
			splitButtons[i].setClickable(true);
			splitButtons[i].setVisible(false);
			buttons.add(splitButtons[i]);
		}

//		private Button[] streetButtons;
		BufferedImage streetImage = new BufferedImage(6, 10, BufferedImage.TYPE_INT_RGB);
		streetButtons = new Button[12];
		for (int i = 0; i < 12; i++) {
			int[] loc = { 137, 214 - i * 17 - i / 4 };
			streetButtons[i] = new Button(this, new GameImage(streetImage, 0), loc, 0, "street");
			streetButtons[i].setClickable(true);
			streetButtons[i].setVisible(false);
			buttons.add(streetButtons[i]);
		}

//		private Button[] sixLineButtons;

		// use cornerImage
		sixLineButtons = new Button[11];
		for (int i = 0; i < 11; i++) {
			int[] loc = { 136, 207 - i * 17 - i / 4 };
			sixLineButtons[i] = new Button(this, new GameImage(cornerImage, 0), loc, 0, "six line");
			sixLineButtons[i].setClickable(true);
			sixLineButtons[i].setVisible(false);
			buttons.add(sixLineButtons[i]);
		}

//		private Button[] trioButtons;

		// use cornerImage
		trioButtons = new Button[2];
		for (int i = 0; i < 2; i++) {
			int[] loc = { 113 - i * 23, 224 };
			trioButtons[i] = new Button(this, new GameImage(cornerImage, 0), loc, 0, "trio");
			trioButtons[i].setClickable(true);
			trioButtons[i].setVisible(false);
			buttons.add(trioButtons[i]);
		}

//		private Button basketButton;

		// use cornerImage
		int[] basketLoc = { 136, 224 };
		basketButton = new Button(this, new GameImage(cornerImage, 0), basketLoc, 0, "basket");
		basketButton.setClickable(true);
		basketButton.setVisible(false);
		basketButton.setCanClickInvisible(true);
		buttons.add(basketButton);

//		private Button[] redBlackButtons;
		BufferedImage outsideSmallImage = new BufferedImage(23, 33, BufferedImage.TYPE_INT_RGB);
		redBlackButtons = new Button[2];
		for (int i = 0; i < 2; i++) {
			int[] loc = { 162, 125 - i * 34 };
			redBlackButtons[i] = new Button(this, new GameImage(outsideSmallImage, 0), loc, 0, "red or black");
			redBlackButtons[i].setClickable(true);
			redBlackButtons[i].setVisible(false);
			buttons.add(redBlackButtons[i]);
		}

//		private Button[] oddEvenButtons;
		oddEvenButtons = new Button[2];
		for (int i = 0; i < 2; i++) {
			int[] loc = { 162, 160 - i * 106 };
			oddEvenButtons[i] = new Button(this, new GameImage(outsideSmallImage, 0), loc, 0, "odd or even");
			oddEvenButtons[i].setClickable(true);
			oddEvenButtons[i].setVisible(false);
			buttons.add(oddEvenButtons[i]);
		}

//		private Button[] eighteenButtons;
		eighteenButtons = new Button[2];
		for (int i = 0; i < 2; i++) {
			int[] loc = { 162, 194 - i * 172 };
			eighteenButtons[i] = new Button(this, new GameImage(outsideSmallImage, 0), loc, 0, "1 to 18 or 19 to 36");
			eighteenButtons[i].setClickable(true);
			eighteenButtons[i].setVisible(false);
			buttons.add(eighteenButtons[i]);
		}

//		private Button[] dozensButtons;
		BufferedImage dozensImage = new BufferedImage(20, 67, BufferedImage.TYPE_INT_RGB);

		dozensButtons = new Button[3];
		for (int i = 0; i < 3; i++) {
			int[] loc = { 141, 160 - i * 69 };
			dozensButtons[i] = new Button(this, new GameImage(dozensImage, 0), loc, 0, "dozens");
			dozensButtons[i].setClickable(true);
			dozensButtons[i].setVisible(false);
			buttons.add(dozensButtons[i]);
		}

//		private Button[] columnsButtons;
		BufferedImage columnsImage = new BufferedImage(22, 15, BufferedImage.TYPE_INT_RGB);

		columnsButtons = new Button[3];
		for (int i = 0; i < 3; i++) {
			int[] loc = { 117 - i * 23, 6 };
			columnsButtons[i] = new Button(this, new GameImage(columnsImage, 0), loc, 0, "columns");
			columnsButtons[i].setClickable(true);
			columnsButtons[i].setVisible(false);
			buttons.add(columnsButtons[i]);
		}

		// private Button[] strait0Buttons;
		BufferedImage strait0Image = new BufferedImage(34, 18, BufferedImage.TYPE_INT_RGB);

		strait0Buttons = new Button[2];
		for (int i = 0; i < 2; i++) {
			int[] loc = { 105 - i * 35, 229 };
			strait0Buttons[i] = new Button(this, new GameImage(strait0Image, 0), loc, 0, "strait0");
			strait0Buttons[i].setClickable(true);
			strait0Buttons[i].setVisible(false);
			buttons.add(strait0Buttons[i]);
		}

		betButtons = new Button[][] { straitButtons, strait0Buttons, splitButtons, streetButtons, sixLineButtons,
				cornerButtons, trioButtons, redBlackButtons, oddEvenButtons, eighteenButtons,
				dozensButtons, columnsButtons };
		for (Button[] line : betButtons)
			for (Button b : line)
				b.setCanClickInvisible(true);

	}

	@Override
	protected void setMapInformation(int rotation) {
		this.mapStructure = new int[][] { { 7, 8 }, { 5, 6 }, { 3, 4 }, { 1, 2 } };
		this.placedMapVal = 1;

	}

	protected void startRound() {
		spin();
	}

	protected void prepareNextRound() {
		playerBets = new HashMap<GamePlayer, ArrayList<RouletteBet>>();

	}

	@Override
	public void startGame() {
		super.startGame();
		this.setInGame();
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

	private int getButtonPosition(Button b) {
		if (b == basketButton)
			return 1;
		String type = b.getName();
		for (Button[] typeButtons : betButtons) {
			if (!type.equals(typeButtons[0].getName()))
				continue;
			for (int i = 0; i < typeButtons.length; i++)
				if (b == typeButtons[i])
					return i + 1;
			
		}
		return 1;
	}

	@Override
	public void click(Player player, double[] loc, ItemStack map) {
		if (spinning)
			return;
		GamePlayer gamePlayer = getGamePlayer(player);
		// player.sendMessage("clicked");

		int[] clickLoc = mapManager.getClickLocation(loc, map);

		Button b = getClickedButton(gamePlayer, clickLoc);
		if (b != null) {
			// bet Button clicked
			String type = b.getName();
			int position = getButtonPosition(b);
			RouletteBet bet = new RouletteBet(type, position, 10, clickLoc, "RED");
			player.sendMessage("Your numbers for " + type + " are: " + Arrays.toString(bet.getWinningNums().toArray()));
			return;
		}

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
