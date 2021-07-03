package water.of.cup.casinogames.games.slots;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import water.of.cup.boardgames.game.Button;
import water.of.cup.boardgames.game.Clock;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.storage.GameStorage;

public abstract class SlotsGame extends Game {
	private int[] dimensions; // {x , y}
	protected Button[][] slotsButtons;
	private ArrayList<SlotsSymbol> symbols;

	private SlotsSymbol[][] currentSymbols;

	private int initialBet;

	public SlotsGame(int rotation) {
		super(rotation);

		dimensions = getDimensions();

		// verify dimensions are accurate
		assert dimensions.length == 2 && dimensions[0] != 0 && dimensions[1] != 0;

		slotsButtons = getSlotsButtons();

		for (Button[] row : slotsButtons)
			for (Button b : row)
				buttons.add(b);

		// verify slots size is consistent
		assert dimensions[1] == slotsButtons.length && dimensions[0] == slotsButtons[0].length;
		symbols = getSlotsSymbols();
		
		currentSymbols = new SlotsSymbol[dimensions[1]][dimensions[0]];

		spin();
		setButtonImages();
	}

	@Override
	protected void startGame() {
		initialBet = 5; // TODO: get initial bet
		spin();
		givePayout();
		setButtonImages();
		mapManager.renderBoard();
	}

	private void spin() {
		for (int y = 0; y < dimensions[1]; y++)
			for (int x = 0; x < dimensions[0]; x++) {
				int r = (int) (Math.random() * symbols.size());
				currentSymbols[y][x] = symbols.get(r);
			}
	}

	private void givePayout() {
		// calculate payout
		double payout = 0;
		
		for (SlotsSymbol[] line : currentSymbols) {
			
			int tempPayout = 0;
			double multiplier = 1;
			
			HashMap<SlotsSymbol, Integer> symbolQuantity = new HashMap<SlotsSymbol, Integer>();

			// calculate symbolQuantity:
			for (SlotsSymbol symbol : line)
				if (symbolQuantity.containsKey(symbol))
					symbolQuantity.put(symbol, 1);
				else
					symbolQuantity.put(symbol, symbolQuantity.get(symbol) + 1);

			// add each symbols payout
			for (SlotsSymbol symbol : symbolQuantity.keySet()) {
				if (symbol.getType() == SymbolType.STANDARD)
					tempPayout += symbol.getPayout(symbolQuantity);
				if (symbol.getType() == SymbolType.MULTIPLIER)
					multiplier = symbol.getPayout(symbolQuantity);
			}
			
			payout += multiplier * tempPayout;
		}
		payout = initialBet * payout;
		

		// give payout
		Player player = teamManager.getTurnPlayer().getPlayer();
		player.sendMessage("You won $" + payout);
	}

	private void setButtonImages() {
		for (int y = 0; y < dimensions[1]; y++)
			for (int x = 0; x < dimensions[0]; x++)
				slotsButtons[y][x].setImage(currentSymbols[y][x].getGameImage());
	}

	protected abstract int[] getDimensions();

	protected abstract Button[][] getSlotsButtons();

	protected abstract ArrayList<SlotsSymbol> getSlotsSymbols();

	@Override
	protected void setMapInformation(int rotation) {
		this.mapStructure = new int[][] { { 1 } };
		this.placedMapVal = 1;

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
		return new SlotsInventory(this);
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
	public void click(Player player, double[] loc, ItemStack map) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void gamePlayerOutOfTime(GamePlayer turn) {
		// TODO Auto-generated method stub

	}

}
