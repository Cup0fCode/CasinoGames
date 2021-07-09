package water.of.cup.casinogames.games.gameutils.cards;

public class Card {
	int num; //ace is 0
	CardSuit suit;
	
	public Card(CardSuit suit, int num) {
		this.suit = suit;
		assert num >= 0 && num < 13;
		this.num = num;
	}
	
	public int getValue() {
		return getValue(true);
	}
	
	public int getValue(boolean aceHigh) {
		if (aceHigh && num == 0)
			return 13;
		return num;
	}
	
	public int getPoints() {
		// num * 10 + suit
		return getValue() * 10 + suit.getPoints();
	}
}
