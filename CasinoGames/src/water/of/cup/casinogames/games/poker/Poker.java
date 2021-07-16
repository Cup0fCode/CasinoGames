package water.of.cup.casinogames.games.poker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.config.ConfigUtil;
import water.of.cup.boardgames.game.*;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.inventories.GameOption;
import water.of.cup.boardgames.game.inventories.GameOptionType;
import water.of.cup.boardgames.game.inventories.number.GameNumberInventory;
import water.of.cup.boardgames.game.inventories.number.GameNumberInventoryCallback;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.casinogames.games.gameutils.cards.Card;
import water.of.cup.casinogames.games.gameutils.cards.Deck;
import water.of.cup.casinogames.games.gameutils.cards.Hand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Poker extends Game {

    private Deck pokerDeck;
    private HashMap<GamePlayer, Hand> playerHands;
    private HashMap<GamePlayer, ArrayList<Button>> playerButtons;
    private HashMap<GamePlayer, Button> playerSelectedButtons;
    private LinkedHashMap<GamePlayer, Integer> playerBets;
    private Hand flopCards;
    private int currentBet;
    private int gamePot;

    private static final int AMOUNT_OF_DECKS = 1;
    private static final int BIG_BLIND = 10;
    private static final int BET_LIMIT = 50;
    private final BoardGames instance = BoardGames.getInstance();

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
        buttons.clear();
        super.startGame();

        // Initiate poker deck
        this.pokerDeck = new Deck(AMOUNT_OF_DECKS);

        // Deal cards to players, init flop cards
        this.dealCards();

        // Render in game buttons
        this.setPokerButtons();

        super.setInGame();

        this.takeBlindBets();

        mapManager.renderBoard();
    }

    private void dealCards() {
        this.playerHands = new HashMap<>();
        this.flopCards = new Hand();
        this.playerSelectedButtons = new HashMap<>();
        this.playerBets = new LinkedHashMap<>();
        this.currentBet = 0;
        this.gamePot = 0;

        for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
            ArrayList<Card> cards = this.pokerDeck.draw(2);
            Hand playerHand = new Hand();
            playerHand.addCards(cards);
            playerHands.put(gamePlayer, playerHand);
            playerBets.put(gamePlayer, -1);
        }
    }

    // TODO: add bets, set blinds in inventory, min entry
    private void setPokerButtons() {
        this.playerButtons = new HashMap<>();
        int posCounter = 0;
        for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
            this.playerButtons.put(gamePlayer, new ArrayList<>());

            for(PokerButton pokerButton : PokerButton.values()) {
                if(pokerButton.getxDisplacement() == 0) continue;

                int rotation;
                int[] loc = new int[] { 0, 0 };

                if(posCounter > 4) {
                    rotation = 1;
                    loc[0] = 128 - pokerButton.getyDisplacement();
                    loc[1] = 256 * (posCounter - 5) + pokerButton.getxDisplacement(); // Might want to change to 6 - posCounter to fix rotation
                } else if(posCounter > 2){
                    rotation = 3;
                    loc[0] = (128 * 4) + pokerButton.getyDisplacement();
                    loc[1] = 256 * (posCounter - 3) + 128 - pokerButton.getxDisplacement();;
                } else {
                    rotation = 2;
                    loc[0] = (256 * posCounter) + 128 - pokerButton.getxDisplacement();
                    loc[1] = 128 - pokerButton.getyDisplacement();
                }

                Button b = new Button(this, pokerButton.getImageName(true), loc, rotation, pokerButton.toString());
                b.changeLocationByRotation();
                b.setVisibleForAll(false);
                b.addVisiblePlayer(gamePlayer);
                b.setClickable(true);

                buttons.add(b);
                this.playerButtons.get(gamePlayer).add(b);
            }

            posCounter++;
        }

        // TODO: See where the cards should be
        for(int i = 0; i < 5; i++) {
            Button b = new Button(this, "POKER_FLOP", new int[]{ 128 * 2 + 42 + (i * 9), 128 + 57 }, 0, "FLOP_" + i);
            buttons.add(b);
        }
    }

    // TODO: Add all-in check
    private boolean placeBet(GamePlayer gamePlayer, int amount) {
        if(amount < currentBet) return false;
        if(amount > currentBet) {
            // Player raise
        } else {
            // Player calls
        }

        this.playerBets.put(gamePlayer, amount);

        currentBet = amount;
        return true;
    }

    private void takeBlindBets() {
        GamePlayer smallBlindPlayer = teamManager.getTurnPlayer();
        GamePlayer bigBlindPlayer = teamManager.nextTurn();

        this.placeBet(smallBlindPlayer, BIG_BLIND/2);
        this.placeBet(bigBlindPlayer, BIG_BLIND);

        this.sendGameMessage(smallBlindPlayer.getPlayer().getDisplayName() + " bet small blind " + BIG_BLIND/2);
        this.sendGameMessage(bigBlindPlayer.getPlayer().getDisplayName() + " bet big blind " + BIG_BLIND);

        this.nextTurn();
    }

    private void sendGameMessage(String message) {
        teamManager.getGamePlayers().forEach((GamePlayer player) -> {
           player.getPlayer().sendMessage(message);
        });
    }

    private void reRenderPokerButtons() {
        for(GamePlayer gamePlayer : playerBets.keySet()) {
            this.renderPokerPlayerButtons(gamePlayer);
        }
    }

    private void renderPokerPlayerButtons(GamePlayer gamePlayer) {
        for(Button button : playerButtons.get(gamePlayer)) {
            button.getImage().setImage("POKER_" + button.getName() + "_DARK");
        }

        boolean canCheck = playerBets.get(gamePlayer) - currentBet == 0 || currentBet == 0;

        // if its their turn, bet
        if(teamManager.getTurnPlayer().equals(gamePlayer)) {
            this.setPokerButton(playerButtons.get(gamePlayer), "BET", true);
            this.setPokerButton(playerButtons.get(gamePlayer), "CALL", true);
            this.setPokerButton(playerButtons.get(gamePlayer), "FOLD", true);

            if(canCheck) {
                this.setPokerButton(playerButtons.get(gamePlayer), "CALL", "POKER_CHECK");
            }
        }

        this.setPokerButton(playerButtons.get(gamePlayer), "CHECK_FOLD", true);

        this.setPokerButton(playerButtons.get(gamePlayer), "CALL_ANY", true);
    }

    private void nextTurn() {
        if(playerBets.size() > 1) {
            GamePlayer nextPlayer = teamManager.nextTurn();

            if(!playerBets.containsKey(nextPlayer)) {
                nextTurn();
                return;
            }

            // If they have already selected a button
            if(this.playerSelectedButtons.containsKey(nextPlayer)) {
                this.playPokerMove(nextPlayer, this.playerSelectedButtons.get(nextPlayer).getName());
                this.playerSelectedButtons.remove(nextPlayer);
                return;
            }
        }

        this.reRenderPokerButtons();
    }

    private void setPokerButton(ArrayList<Button> buttons, String buttonName, boolean isToggled) {
        buttons.forEach((Button button) -> {
            if(button.getName().equals(buttonName)) {
                button.getImage().setImage(isToggled ? "POKER_" + buttonName : "POKER_" + buttonName + "_DARK");
            }
        });
    }

    private void setPokerButton(ArrayList<Button> buttons, String buttonName, String imageName) {
        buttons.forEach((Button button) -> {
            if(button.getName().equals(buttonName)) {
                button.getImage().setImage(imageName);
            }
        });
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
    public void click(Player player, double[] loc, ItemStack map) {
        GamePlayer gamePlayer = getGamePlayer(player);
        int[] clickLoc = mapManager.getClickLocation(loc, map);

        Button b = getClickedButton(gamePlayer, clickLoc);
        if(b == null) return;

        // If they are out of the round, ignore clicks
        if(!playerBets.containsKey(gamePlayer)) return;

        if(!teamManager.getTurnPlayer().equals(gamePlayer)) {
            this.selectPokerButton(gamePlayer, b);
            return;
        }

        this.playPokerMove(gamePlayer, b.getName());
    }

    private void playPokerMove(GamePlayer gamePlayer, String move) {
        Player player = gamePlayer.getPlayer();
        switch (move) {
            case "BET": {
                this.handlePokerBet(gamePlayer);
                return;
            }
            case "CALL_ANY":
            case "CALL": {
                if(currentBet > 0) {
                    sendGameMessage(player.getDisplayName() + " has called " + currentBet);

                    this.placeBet(gamePlayer, currentBet);
                } else {
                    // Check takes place of call when available
                    this.checkGamePlayer(gamePlayer);
                }
                this.nextTurn();
                break;
            }
            case "CHECK_FOLD": {
                if(playerBets.get(gamePlayer) == currentBet || currentBet == 0) {
                    this.checkGamePlayer(gamePlayer);
                } else {
                    this.foldGamePlayer(gamePlayer);
                }
                this.nextTurn();
                break;
            }
            case "FOLD": {
                this.foldGamePlayer(gamePlayer);
                this.nextTurn();
                break;
            }
        }

        boolean roundOver = checkRoundOver();
        if(roundOver) {
            this.startNextRound();
        }

        mapManager.renderBoard();
    }

    private void handlePokerBet(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        GameOption betOption = new GameOption("bet", Material.GOLD_INGOT, GameOptionType.COUNT, "Bet Amount:", currentBet + "", false, Math.max(1, currentBet), BET_LIMIT);
        new GameNumberInventory(gameInventory).build(player, (s, betAmount) -> {
            if(betAmount > currentBet) {
                sendGameMessage(player.getDisplayName() + " has raised the bet to " + betAmount);

                this.placeBet(gamePlayer, betAmount);
                this.nextTurn();

                boolean roundOver = checkRoundOver();
                if(roundOver) {
                    this.startNextRound();
                }

                mapManager.renderBoard();
            }
        }, betOption, currentBet);
    }

    // TODO: Add selected color
    private void selectPokerButton(GamePlayer gamePlayer, Button selectedButton) {
        String buttonName = selectedButton.getName();

        // check if which one they select
        if(this.playerSelectedButtons.containsKey(gamePlayer)) {
            if(!selectedButton.getName().equals(this.playerSelectedButtons.get(gamePlayer).getName())) return;

            this.renderPokerPlayerButtons(gamePlayer);
            this.playerSelectedButtons.remove(gamePlayer);

            mapManager.renderBoard(gamePlayer.getPlayer());
            return;
        }

        if(!buttonName.equals("CALL_ANY") && !buttonName.equals("CHECK_FOLD")) return;

        // Set their buttons to black
        for(Button button : playerButtons.get(gamePlayer)) {
            button.getImage().setImage("POKER_" + button.getName() + "_DARK");
        }

        this.setPokerButton(playerButtons.get(gamePlayer), buttonName, true);

        this.playerSelectedButtons.put(gamePlayer, selectedButton);

        mapManager.renderBoard(gamePlayer.getPlayer());
    }

    private void checkGamePlayer(GamePlayer gamePlayer) {
        sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has checked");

        if(this.playerBets.get(gamePlayer) < 0)
            this.playerBets.put(gamePlayer, 0);
    }

    private void foldGamePlayer(GamePlayer gamePlayer) {
        // TODO: Pot stuff
        sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has folded");

        this.playerBets.remove(gamePlayer);

        // Set their buttons to black
        for(Button button : playerButtons.get(gamePlayer)) {
            button.getImage().setImage("POKER_" + button.getName() + "_DARK");
        }
    }

    private void startNextRound() {
        // If its the last betting round or everyone has folded
        if(this.flopCards.getAmountOfCards() == 5 || playerBets.size() <= 1) {
            // End game
            sendGameMessage("Game over!");
            this.endGame();
            return;
        }

        // TODO: Draw flops
        // Draw flop card(s)
        int drawFlopNum = (this.flopCards.getAmountOfCards() == 0) ? 3 : 1;
        ArrayList<Card> flopCards = this.pokerDeck.draw(drawFlopNum);
        this.flopCards.addCards(flopCards);

        teamManager.setTurn(playerBets.keySet().iterator().next());

        int roundPot = 0;
        for(int bet : this.playerBets.values()) {
            if(bet >= 0)
                roundPot += bet;
        }

        this.gamePot += roundPot;
        this.currentBet = 0;
        this.playerBets.replaceAll((p, v) -> -1);
        this.playerSelectedButtons.clear();
        this.reRenderPokerButtons();

        sendGameMessage("Next round starting! Pot: " + gamePot);
        sendGameMessage("Flop:");
        for(Card card : this.flopCards.getCards()) {
            sendGameMessage(card.getValue() + "" + card.getSuit().toString());
        }
    }

    private void endGame() {
        // Draw the remaining flop cards
        for(int i = this.flopCards.getAmountOfCards(); i < 5; i++) {
            Card flopCard = this.pokerDeck.draw();
            this.flopCards.addCard(flopCard);
        }

        // TODO: Show everyones cards
        // TODO: Get best hand
        super.endGame(null);
    }

    private boolean checkRoundOver() {
        if(playerBets.size() <= 1) return true;

        boolean roundOver = true;
        for(int playerBet : playerBets.values()) {
            if(playerBet != currentBet) {
                roundOver = false;
                break;
            }
        }

        return roundOver;
    }

    @Override
    protected void gamePlayerOutOfTime(GamePlayer gamePlayer) {

    }

    @Override
    public ItemStack getBoardItem() {
        return new BoardItem(gameName, new ItemStack(Material.ACACIA_TRAPDOOR, 1));
    }
}
