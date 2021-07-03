package water.of.cup.casinogames.games.slots;

import java.util.HashMap;
import java.util.Map.Entry;

public class SlotsSymbol {
	private String imageName;
	private SymbolType type;

	private HashMap<Integer, Double> payouts;
	private HashMap<HashMap<SlotsSymbol, Integer>, Double> specialPayouts;

	public SlotsSymbol(String imageName, SymbolType type) {
		this.imageName = imageName;
		this.type = type;
		payouts = new HashMap<Integer, Double>();
		specialPayouts = new HashMap<HashMap<SlotsSymbol, Integer>, Double>();
	}

	public String getGameImage() {
		return imageName;
	}

	public void setGameImage(String gameImage) {
		this.imageName = gameImage;
	}

	public SymbolType getType() {
		return type;
	}

	public void setType(SymbolType type) {
		this.type = type;
	}

	public void addPayout(int quantity, double payout) {
		payouts.put(quantity, payout);
	}

	private double getPayout(int quantity) {
		if (!payouts.containsKey(quantity))
			return 0;
		return payouts.get(quantity);
	}

	private boolean hasSpecialPayouts() {
		return specialPayouts.size() > 0;
	}

	public void addSpecialPayout(int quantity, HashMap<SlotsSymbol, Integer> symbolQuantities, double payout) {
		symbolQuantities.put(this, quantity);
		specialPayouts.put(symbolQuantities, payout);
	}

	private double getSpecialPayout(HashMap<SlotsSymbol, Integer> symbols) {
		if (!hasSpecialPayouts())
			return 0;
		double payout = 0;

		specialPayouts: for (Entry<HashMap<SlotsSymbol, Integer>, Double> specialPayout : specialPayouts.entrySet()) {
			for (Entry<SlotsSymbol, Integer> symbolQuantity : specialPayout.getKey().entrySet()) {
				SlotsSymbol symbol = symbolQuantity.getKey();
				// check that symbols has symbol
				if (!symbols.containsKey(symbol))
					continue specialPayouts;
				// check that quantities are the same
				if(symbolQuantity.getValue() != symbols.get(symbol))
					continue specialPayouts;
			}
			if (specialPayout.getValue() > payout)
				payout = specialPayout.getValue();
		}

		return payout;
	}
	
	public double getPayout(HashMap<SlotsSymbol, Integer> symbols) {
		int quantity = symbols.get(this);
		double payout = getPayout(quantity);
		double specialPayout = getSpecialPayout(symbols);
		if (specialPayout > payout)
			return specialPayout;
		return payout;
	}
}
