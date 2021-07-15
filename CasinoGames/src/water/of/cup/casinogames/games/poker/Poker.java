package water.of.cup.casinogames.games.poker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import water.of.cup.boardgames.game.*;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.casinogames.games.gameutils.cards.Card;
import water.of.cup.casinogames.games.gameutils.cards.Deck;
import water.of.cup.casinogames.games.gameutils.cards.Hand;

import java.util.ArrayList;
import java.util.HashMap;

public class Poker extends Game {

    private Deck pokerDeck;
    private HashMap<GamePlayer, Hand> playerHands;
    private HashMap<GamePlayer, ArrayList<Button>> playerButtons;

    private static final int AMOUNT_OF_DECKS = 1;

    public Poker(int rotation) {
        super(rotation);
    }

    @Override
    protected void setMapInformation(int i) {
        this.mapStructure = new int[][] { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 0, 13, 14 } };
        this.placedMapVal = 8;
    }

    @Override
    protected void startGame() {
        // Clear renderers
        super.startGame();

        this.pokerDeck = new Deck(AMOUNT_OF_DECKS);

        // deal cards to players
        this.playerHands = new HashMap<>();
        for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
            ArrayList<Card> cards = this.pokerDeck.draw(2);
            Hand playerHand = new Hand();
            playerHand.addCards(cards);
            playerHands.put(gamePlayer, playerHand);
        }

        this.setPokerButtons();

        // first round of blind bets
        // display cards
        // continue
        mapManager.renderBoard();

        super.setInGame();
    }

    // TODO: Fix them images
    private void setPokerButtons() {
        this.playerButtons = new HashMap<>();
        int posCounter = 0;
//        for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
        for(int i = 0; i < 7; i++) {
//            this.playerButtons.put(gamePlayer, new ArrayList<>());

            for(PokerButton pokerButton : PokerButton.values()) {
                if(pokerButton.getxDisplacement() == 0) continue;

                int rotation;
                int[] loc = new int[] { 0, 0 };

                if(posCounter > 4) {
                    rotation = 1;
                    loc[0] = 128 - pokerButton.getyDisplacement();
                    loc[1] = 256 * (posCounter - 5) + pokerButton.getxDisplacement();
                } else if(posCounter > 2){
                    rotation = 3;
                    loc[0] = (128 * 4) + pokerButton.getyDisplacement();
                    loc[1] = 256 * (posCounter - 3) + 128 - pokerButton.getxDisplacement();;
                } else {
                    rotation = 2;
                    loc[0] = (256 * posCounter) + 128 - pokerButton.getxDisplacement();
                    loc[1] = 128 - pokerButton.getyDisplacement();
                }


                Button b = new Button(this, pokerButton.getImageName(), loc, rotation, pokerButton.toString());
                b.changeLocationByRotation();
//                b.setVisibleForAll(false1);

//                b.addVisiblePlayer(gamePlayer);
                b.setClickable(true);
                buttons.add(b);
//                this.playerButtons.get(gamePlayer).add(b);
            }

            posCounter++;
        }
    }

    @Override
    protected void setGameName() {
        this.gameName = "Poker";
    }

    @Override
    protected void setBoardImage() {
        this.gameImage = new GameImage("POKER_BOARD");
    }

    @Override
    protected void clockOutOfTime() {

    }

    @Override
    protected Clock getClock() {
        return null;
    }

    @Override
    protected GameInventory getGameInventory() {
        return new PokerInventory(this);
    }

    @Override
    protected GameStorage getGameStorage() {
        return null;
    }

    @Override
    public ArrayList<String> getTeamNames() {
        return null;
    }

    @Override
    protected GameConfig getGameConfig() {
        return null;
    }

    @Override
    public void click(Player player, double[] doubles, ItemStack itemStack) {
        GamePlayer gamePlayer = getGamePlayer(player);
        if(!teamManager.getTurnPlayer().equals(gamePlayer)) return;

    }

    @Override
    protected void gamePlayerOutOfTime(GamePlayer gamePlayer) {

    }

    @Override
    public ItemStack getBoardItem() {
        return new BoardItem(gameName, new ItemStack(Material.ACACIA_TRAPDOOR, 1));
    }
}
