package water.of.cup.casinogames.games.gameutils.cards;

public enum CardSuit {
	CLUBS, DIAMONDS, HEARTS, SPADES;
	
	public int getPoints() {
		switch (this) {
		case CLUBS:
			return 0;
		case DIAMONDS:
			return 1;
		case HEARTS:
			return 2;
		case SPADES:
			return 3;
		default:
			return 0;
		
		}
	}
}
