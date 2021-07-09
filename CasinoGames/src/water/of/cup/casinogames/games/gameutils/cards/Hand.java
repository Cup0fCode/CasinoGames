package water.of.cup.casinogames.games.gameutils.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Hand {
	ArrayList<Card> cards;

	public Hand() {
		cards = new ArrayList<Card>();
	}

	public void addCards(ArrayList<Card> newCards) {
		cards.addAll(newCards);
	}

	public void addCard(Card newCard) {
		cards.add(newCard);
	}

	public void draw(Deck deck) {
		cards.add(deck.draw());
	}

	public void draw(Deck deck, int amount) {
		cards.addAll(deck.draw(amount));
	}

	public int getHandPoints() {
		return getHandPoints(new ArrayList<Card>());
	}

	public int getHandPoints(ArrayList<Card> extraCards) {
		ArrayList<Card> handCards = new ArrayList<Card>();
		handCards.addAll(cards);
		handCards.addAll(extraCards);
		int points = 0; // ranking * 100 + highcard
		// high card
		points = getHighCard().getPoints();

		HashMap<Integer, Hand> multiples = new HashMap<Integer, Hand>();
		for (Card card : handCards) {
			int val = card.getValue();
			if (!multiples.containsKey(val)) {
				multiples.put(val, new Hand());
			}
			multiples.get(val).addCard(card);
		}
		
		ArrayList<Hand> pairs = new ArrayList<Hand>();
		ArrayList<Hand> threes = new ArrayList<Hand>();
		ArrayList<Hand> fours = new ArrayList<Hand>();
		for (Hand sHand : multiples.values()) {
			switch (sHand.getAmountOfCards()) {
			case 2:
				pairs.add(sHand);
			case 3:
				threes.add(sHand);
			case 4:
				fours.add(sHand);
			}
		}
		
		// Pair: 1
		if (pairs.size() == 1) {
			points = 100 + pairs.get(0).getHighCard().getPoints();
		}
		
		// Two Pair: 2
		if (pairs.size() == 2) {
			int highPoints = 0;
			for (Hand pair : pairs)
				if (pair.getHighCard().getPoints() > highPoints)
					highPoints = pair.getHighCard().getPoints();
			points = 200 + highPoints;
		}
		
		// Three of a kind: 3
		if (threes.size() >= 1) {
			int highPoints = 0;
			for (Hand three : threes)
				if (three.getHighCard().getPoints() > highPoints)
					highPoints = three.getHighCard().getPoints();
			points = 300 + highPoints;
		}
		
		// Straight: 4
		ArrayList<Hand> straits = new ArrayList<Hand>();
		int lastVal = -2;
		straits.add(new Hand());
		for (Entry<Integer, Hand> entry : multiples.entrySet()) {
			Hand strait = straits.get(straits.size() - 1);
			if (lastVal == entry.getKey() - 1) {
				strait.addCard(entry.getValue().getHighCard());
				
			} else {
				strait.clear();
			}
			lastVal = entry.getKey();
			
			if (strait.getAmountOfCards() == 5) {
				straits.add(new Hand());
			}
		}
		if (straits.get(straits.size() - 1).getAmountOfCards() != 5)
			straits.remove(straits.size() - 1);
		boolean hasStrait = straits.size() > 0;
		if (hasStrait) {
			Hand strait = straits.get(straits.size() - 1);
			points = 400 + strait.getHighCard().getPoints();
		}
		
		// Flush: 5
		HashMap<CardSuit, Hand> suitCards = new HashMap<CardSuit, Hand>();
		for (Card card : extraCards) {
			CardSuit suit = card.getSuit();
			if (suitCards.containsKey(suit)) {
				suitCards.put(suit, new Hand());
			}
			suitCards.get(suit).addCard(card);
		}
		int maxSuitPoints = 0;
		for (Hand suitHand : suitCards.values()) {
			if (suitHand.getAmountOfCards() >= 5) {
				int tempPoints = 500 + suitHand.getHighCard().getPoints();
				if (tempPoints > maxSuitPoints)
					maxSuitPoints = tempPoints;
			}
		}
		if (maxSuitPoints >= 500)
			points = maxSuitPoints;
		
		
		
		// Full House: 6
		if (threes.size() >= 1 && pairs.size() >= 1 ) {
			int highPoints = 0;
			for (Hand three : threes)
				if (three.getHighCard().getPoints() > highPoints)
					highPoints = three.getHighCard().getPoints();
			for (Hand pair : pairs)
				if (pair.getHighCard().getPoints() > highPoints)
					highPoints = pair.getHighCard().getPoints();
			points = 600 + highPoints;
		}
		
		// Four of a kind: 7
		if (fours.size() >= 1) {
			int highPoints = 0;
			for (Hand four : fours)
				if (four.getHighCard().getPoints() > highPoints)
					highPoints = four.getHighCard().getPoints();
			points = 700 + highPoints;
		}
		
		// Straight Flush: 8
		for (Hand tHand : suitCards.values()) {
			if (tHand.getAmountOfCards() < 5)
				continue;
			
			HashMap<Integer, Hand> tmultiples = new HashMap<Integer, Hand>();
			for (Card card : tHand.getCards()) {
				int val = card.getValue();
				if (!tmultiples.containsKey(val)) {
					tmultiples.put(val, new Hand());
				}
				tmultiples.get(val).addCard(card);
			}
			
			ArrayList<Hand> tstraits = new ArrayList<Hand>();
			int tlastVal = -2;
			straits.add(new Hand());
			for (Entry<Integer, Hand> entry : tmultiples.entrySet()) {
				Hand strait = tstraits.get(tstraits.size() - 1);
				if (tlastVal == entry.getKey() - 1) {
					strait.addCard(entry.getValue().getHighCard());
					
				} else {
					tstraits.clear();
				}
				tlastVal = entry.getKey();
				
				if (strait.getAmountOfCards() == 5) {
					tstraits.add(new Hand());
				}
			}
			if (tstraits.get(tstraits.size() - 1).getAmountOfCards() != 5)
				tstraits.remove(tstraits.size() - 1);
			boolean thasStrait = tstraits.size() > 0;
			if (thasStrait) {
				Hand strait = tstraits.get(tstraits.size() - 1);
				int tpoints = 800 + strait.getHighCard().getPoints();
				if (tpoints > points)
					points = tpoints;
			}
		}
		
		// Royal Flush: 9
		suitLoop: for (Hand tHand : suitCards.values()) {
			if (tHand.getAmountOfCards() < 5)
				continue;
			for (int n = 10; n < 13; n++) {
				if (!tHand.containsCardNum(n))
					continue  suitLoop;
			}
			int tpoints = 900 + tHand.getHighCard().getPoints();
			if (tpoints > points)
				points = tpoints;
		}

		return points;
	}

	private boolean containsCardNum(int n) {
		for (Card card : cards)
			if (card.getValue() == n)
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Card> getCards() {
		// TODO Auto-generated method stub
		return (ArrayList<Card>) cards.clone();
	}

	public String getHandName() {
		return "";
	}
	
	public int getAmountOfCards() {
		return cards.size();
	}
	
	public void clear() {
		cards = new ArrayList<Card>();
	}

	private Card getHighCard() {
		Card high = cards.get(0);
		int points = 0;
		for (Card card : cards) {
			if (card.getPoints() > points) {
				points = card.getPoints();
				high = card;
			}
		}
		return high;

	}

//	private Card getHighCard(ArrayList<Card> tcards) {
//		Card high = tcards.get(0);
//		int points = 0;
//		for (Card card : tcards) {
//			if (card.getPoints() > points) {
//				points = card.getPoints();
//				high = card;
//			}
//		}
//		return high;
//	}
}
