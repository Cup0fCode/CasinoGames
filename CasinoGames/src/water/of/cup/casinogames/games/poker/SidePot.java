package water.of.cup.casinogames.games.poker;

import water.of.cup.boardgames.game.GamePlayer;

import java.util.ArrayList;

public class SidePot {

    private int potAmount;
    private ArrayList<GamePlayer> potPlayers;

    public SidePot() {
        this.potPlayers = new ArrayList<>();
        this.potAmount = 0;
    }

    public void addPotPlayers(ArrayList<GamePlayer> potPlayers) {
        this.potPlayers.addAll(potPlayers);
    }

    public void addPotPlayer(GamePlayer gamePlayer) {
        this.potPlayers.add(gamePlayer);
    }

    public void setPotAmount(int potAmount) {
        this.potAmount = potAmount;
    }
}
