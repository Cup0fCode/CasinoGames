package water.of.cup.casinogames.games.gameutils.cards;

import water.of.cup.boardgames.game.GameImage;
import water.of.cup.boardgames.image_handling.ImageManager;

public class Card {
	int num; // ace is 0
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
			return 14;
		return num + 1;
	}

	public int getBlackJackValue(boolean aceHigh) {
		if (aceHigh && num == 0)
			return 11;
		if (num + 1 > 10)
			return 10;
		return num + 1;
	}

	public int getPoints() {
		// num * 10 + suit
		return getValue() * 10 + suit.getPoints();
	}

	public CardSuit getSuit() {
		return suit;
	}

	public GameImage getGameImage() {
		GameImage image = new GameImage(ImageManager.getImage("PLAYINGCARDS_" + suit.toString() + "_" + getValue()), 0);
		return image;
	}

	public String getName() {
		return getShortName() + " of " + suit.getName();
	}

	public String getShortName() {
		switch (getValue(false)) {
		case 1:
			return "Ace";
		case 2:
			return "Two";
		case 3:
			return "Three";
		case 4:
			return "Four";
		case 5:
			return "Five";
		case 6:
			return "Six";
		case 7:
			return "Seven";
		case 8:
			return "Eight";
		case 9:
			return "Nine";
		case 10:
			return "Ten";
		case 11:
			return "Jack";
		case 12:
			return "Queen";
		case 13:
			return "King";
		}

		return "";
	}
}
