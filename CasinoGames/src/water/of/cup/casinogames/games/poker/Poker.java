package water.of.cup.casinogames.games.poker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.game.*;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.inventories.GameOption;
import water.of.cup.boardgames.game.inventories.GameOptionType;
import water.of.cup.boardgames.game.inventories.number.GameNumberInventory;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.casinogames.games.gameutils.cards.Card;
import water.of.cup.casinogames.games.gameutils.cards.Deck;
import water.of.cup.casinogames.games.gameutils.cards.Hand;

import java.util.*;

public class Poker extends Game {

    private Deck pokerDeck;
    private LinkedHashMap<GamePlayer, Hand> playerHands;
    private HashMap<GamePlayer, ArrayList<Button>> playerButtons;
    private HashMap<GamePlayer, Button> playerSelectedButtons;
    private LinkedHashMap<GamePlayer, Integer> playerBets;
    private ArrayList<Button> flopButtons;
    private HashMap<GamePlayer, Button> playerHandButtons;
    private LinkedHashMap<GamePlayer, Integer> playersAllIn;
    private ArrayList<SidePot> sidePots;
    private Hand flopCards;
    private int firstBetIndex;
    private int currentBet;
    private int gamePot;
    private int BIG_BLIND;
    private int RAISE_LIMIT;

    private static final int AMOUNT_OF_DECKS = 1;
    private final BoardGames instance = BoardGames.getInstance();

    // TODO: Join mid-game, player NPC, poker chips, translate messages
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
        if(hasGameData("minEntry") && hasGameData("raiseLimit")) {
            this.BIG_BLIND = (int) getGameData("minEntry");
            this.RAISE_LIMIT = (int) getGameData("raiseLimit");
        } else {
            this.BIG_BLIND = 0;
            this.RAISE_LIMIT = 0;
        }

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
        this.playerHands = new LinkedHashMap<>();
        this.flopCards = new Hand();
        this.playerSelectedButtons = new HashMap<>();
        this.playerBets = new LinkedHashMap<>();
        this.playersAllIn = new LinkedHashMap<>();
        this.sidePots = new ArrayList<>();
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

    private void setPokerButtons() {
        // Sets player buttons
        this.playerButtons = new HashMap<>();
        this.playerHandButtons = new HashMap<>();
        int posCounter = 0;
        for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
            this.playerButtons.put(gamePlayer, new ArrayList<>());

            for(PokerButton pokerButton : PokerButton.values()) {
                if(pokerButton.getxDisplacement() == 0) continue;

                int rotation = getPokerButtonRotation(posCounter);
                int[] loc = getPokerButtonPos(posCounter);

                if(posCounter > 4) {
                    loc[0] = loc[0] - pokerButton.getyDisplacement();
                    loc[1] = loc[1] + pokerButton.getxDisplacement(); // Might want to change to 6 - posCounter to fix rotation
                } else if(posCounter > 2){
                    loc[0] = loc[0] + pokerButton.getyDisplacement();
                    loc[1] = loc[1] - pokerButton.getxDisplacement();;
                } else {
                    loc[0] = loc[0] - pokerButton.getxDisplacement();
                    loc[1] = loc[1] - pokerButton.getyDisplacement();
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

        // Sets player cards
        posCounter = 0;
        for(GamePlayer gamePlayer : playerHands.keySet()) {
            Hand hand = playerHands.get(gamePlayer);
            int rotation = getPokerButtonRotation(posCounter);
            int[] loc = getPokerButtonPos(posCounter);

            GameImage handImage = hand.getGameImage(false);
            Button handButton = new Button(this, handImage, loc, rotation, "HAND");
            handButton.changeLocationByRotation();
            handButton.setVisibleForAll(false);
            handButton.addVisiblePlayer(gamePlayer);
            handButton.setClickable(false);

            playerHandButtons.put(gamePlayer, handButton);
            buttons.add(handButton);
            posCounter++;
        }

        // Sets flop cards
        this.setFlopCards();
    }

    private void setFlopCards() {
        int[][] flopCords = new int[][] {
                { 128 * 3, 128 * 2, 2 },
                { 128 * 2, 128, 1 },
                { 128 * 3, 128 * 2, 3 }
        };

        if(this.flopButtons != null) {
            buttons.removeAll(this.flopButtons);
        }

        this.flopButtons = new ArrayList<>();

        GameImage flopImage = flopCards.getGameImage(true);
        for(int[] flopCord : flopCords) {
            Button b = new Button(this, flopImage, flopCord, flopCord[2], "FLOP");
            b.changeLocationByRotation();
            this.flopButtons.add(b);
            buttons.add(b);
        }
    }

    private int[] getPokerButtonPos(int posCounter) {
        int[] loc = new int[] { 0, 0 };
        if(posCounter > 4) {
            loc[0] = 128;
            loc[1] = 256 * (posCounter - 5); // Might want to change to 6 - posCounter to fix rotation
        } else if(posCounter > 2){
            loc[0] = (128 * 4);
            loc[1] = 256 * (posCounter - 3) + 128;
        } else {
            loc[0] = (256 * posCounter) + 128;
            loc[1] = 128;
        }
        return loc;
    }

    private int getPokerButtonRotation(int posCounter) {
        if(posCounter > 4) {
            return 1;
        } else if(posCounter > 2) {
            return 3;
        } else {
            return 2;
        }
    }

    private boolean placeBet(GamePlayer gamePlayer, int amount) {
        int playerBalance = (int) instance.getEconomy().getBalance(gamePlayer.getPlayer());
        boolean allIn = playerBalance <= amount;

        if(amount < currentBet) return false;

        // if a player has gone all in, start counting new pot
        // new pot is smallest stack
        if(allIn) {
            sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has gone all in!");

            instance.getEconomy().withdrawPlayer(gamePlayer.getPlayer(), playerBalance);

            playersAllIn.put(gamePlayer, playerBalance);
            playerBets.remove(gamePlayer);

            this.disablePokerButtons(gamePlayer);

            if(playerBalance > currentBet)
                currentBet = playerBalance;
            return true;
        }

        int withdrawAmount = playerBets.get(gamePlayer) == -1 ? amount : amount - playerBets.get(gamePlayer);
        instance.getEconomy().withdrawPlayer(gamePlayer.getPlayer(), withdrawAmount);

        this.playerBets.put(gamePlayer, amount);

        currentBet = amount;
        return true;
    }

    private void takeBlindBets() {
        GamePlayer smallBlindPlayer = teamManager.getTurnPlayer();
        GamePlayer bigBlindPlayer = teamManager.nextTurn();

        this.firstBetIndex = teamManager.getGamePlayers().indexOf(smallBlindPlayer);

        this.placeBet(smallBlindPlayer, BIG_BLIND/2);
        this.sendGameMessage(smallBlindPlayer.getPlayer().getDisplayName() + " bet small blind " + BIG_BLIND/2);

        this.placeBet(bigBlindPlayer, BIG_BLIND);
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
        this.disablePokerButtons(gamePlayer);

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
        if(playerBets.size() >= 1) {
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

        boolean roundOver = checkRoundOver();
        if(roundOver) {
            this.startNextRound();
            return;
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

        mapManager.renderBoard();
    }

    private void handlePokerBet(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        GameOption betOption = new GameOption("bet", Material.GOLD_INGOT, GameOptionType.COUNT, "Bet Amount:", currentBet + "", false, Math.max(1, currentBet), RAISE_LIMIT + currentBet);
        new GameNumberInventory(gameInventory).build(player, (s, betAmount) -> {
            if(betAmount > currentBet) {
                int playerBalance = (int) instance.getEconomy().getBalance(gamePlayer.getPlayer());
                if(betAmount >= playerBalance)
                    betAmount = playerBalance;

                sendGameMessage(player.getDisplayName() + " has raised the bet to " + betAmount);

                this.placeBet(gamePlayer, betAmount);
                this.nextTurn();

                mapManager.renderBoard();
            }
        }, betOption, currentBet);
    }

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
        this.disablePokerButtons(gamePlayer);

        this.setPokerButton(playerButtons.get(gamePlayer), buttonName, "POKER_" + buttonName + "_SELECTED");

        this.playerSelectedButtons.put(gamePlayer, selectedButton);

        mapManager.renderBoard(gamePlayer.getPlayer());
    }

    private void checkGamePlayer(GamePlayer gamePlayer) {
        sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has checked");

        if(this.playerBets.get(gamePlayer) < 0)
            this.playerBets.put(gamePlayer, 0);
    }

    private void foldGamePlayer(GamePlayer gamePlayer) {
        sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has folded");

        // Add their bet to the pot
        if(this.playerBets.containsKey(gamePlayer) && this.playerBets.get(gamePlayer) > 0) {
            gamePot += this.playerBets.get(gamePlayer);
        }

        this.playerBets.remove(gamePlayer);

        // Set their buttons to black
        this.disablePokerButtons(gamePlayer);
    }

    private void disablePokerButtons(GamePlayer gamePlayer) {
        for(Button button : playerButtons.get(gamePlayer)) {
            button.getImage().setImage("POKER_" + button.getName() + "_DARK");
        }
    }

    private void startNextRound() {
        if(this.playersAllIn.size() > 0) {
            // Find smallest all in
            ArrayList<Integer> allInBets = new ArrayList<>(this.playersAllIn.values());
            allInBets.sort(Comparator.naturalOrder());

            int smallestBet = allInBets.get(0);
            // all players can win roundPot
            int roundPot = smallestBet * (this.playerBets.size() + this.playersAllIn.size());

            SidePot firstPot = new SidePot();
            firstPot.addPotPlayers(new ArrayList<>(this.playerBets.keySet()));
            firstPot.addPotPlayers(new ArrayList<>(this.playersAllIn.keySet()));
            firstPot.setPotAmount(roundPot + gamePot);
            this.sidePots.add(firstPot);

            // other side pots
            if(this.playersAllIn.size() > 1) {
                for(int i = 1; i < this.playersAllIn.size(); i++) {
                    int bet = allInBets.get(i) - allInBets.get(i - 1);
                    int sidePot = bet * ((this.playersAllIn.size() - i) + this.playerBets.size());
                    // can be won by i-playersAllIn.size() + playerBets

                    // if everyone went all in and is last side pot, return money (no one can match)
                    if(playerBets.size() == 0 && i == this.playersAllIn.size() - 1) {
                        if(bet > 0) {
                            GamePlayer returnPlayer = getLastElement(this.playersAllIn.keySet());
                            returnPlayer.getPlayer().sendMessage("You get " + bet + " back!");
                            instance.getEconomy().depositPlayer(returnPlayer.getPlayer(), bet);
                        }
                    } else {
                        SidePot newSidePot = new SidePot();
                        newSidePot.addPotPlayers(new ArrayList<>(this.playerBets.keySet()));
                        newSidePot.setPotAmount(sidePot);
                        for(int j = i; j < this.playersAllIn.size(); j++) {
                            newSidePot.addPotPlayer(new ArrayList<>(this.playersAllIn.keySet()).get(j));
                        }

                        this.sidePots.add(newSidePot);
                    }
                }
            }

            // new side pot with playerBets (becomes new game pot)
            gamePot = (currentBet - allInBets.get(allInBets.size() - 1)) * this.playerBets.size();
            this.playersAllIn.clear();
        } else {
            int roundPot = 0;
            for(int bet : this.playerBets.values()) {
                if(bet >= 0)
                    roundPot += bet;
            }

            this.gamePot += roundPot;
        }

        // If its the last betting round or everyone has folded
        if((this.flopCards.getAmountOfCards() == 5 || playerBets.size() <= 1)) {
            // End game
            this.endRound(false);
            return;
        }

        // Draw flop card(s)
        int drawFlopNum = (this.flopCards.getAmountOfCards() == 0) ? 3 : 1;
        ArrayList<Card> flopCards = this.pokerDeck.draw(drawFlopNum);
        this.flopCards.addCards(flopCards);

        this.setFlopCards();

        teamManager.setTurn(playerBets.keySet().iterator().next());

        this.currentBet = 0;
        this.playerBets.replaceAll((p, v) -> -1);
        this.playerSelectedButtons.clear();
        this.reRenderPokerButtons();

        sendGameMessage("Next round starting! Pot: " + gamePot);
        for(SidePot sidePot : this.sidePots) {
            sendGameMessage("Side pot: " + sidePot.getPotAmount() + " Players: " + sidePot.getPotPlayers().size());
        }
        sendGameMessage("Flop:");
        for(Card card : this.flopCards.getCards()) {
            sendGameMessage(card.getValue() + "" + card.getSuit().toString());
        }
    }

    private void endRound(boolean endGame) {
        sendGameMessage("Game over!");

        // Draw the remaining flop cards
        for(int i = this.flopCards.getAmountOfCards(); i < 5; i++) {
            Card flopCard = this.pokerDeck.draw();
            this.flopCards.addCard(flopCard);
        }

        this.setFlopCards();

        HashMap<GamePlayer, Integer> finalPlayers = new HashMap<>();

        // Only playerBets can win gamePot
        if(playerBets.size() > 0) {
            GamePlayer winner = getBestHand(new ArrayList<>(playerBets.keySet()));
            sendGameMessage(winner.getPlayer().getDisplayName() + " won the pot worth " + gamePot);
            finalPlayers.put(winner, gamePot);
        }

        for(SidePot sidePot : this.sidePots) {
            GamePlayer winner = getBestHand(sidePot.getPotPlayers());
            sendGameMessage(winner.getPlayer().getDisplayName() + " won a side pot worth " + sidePot.getPotAmount());
            finalPlayers.put(winner, sidePot.getPotAmount());
        }

        // Show winning cards / Send money to players
        for(GamePlayer inGamePlayer : finalPlayers.keySet()) {
            playerHandButtons.get(inGamePlayer).setVisibleForAll(true);
            instance.getEconomy().depositPlayer(inGamePlayer.getPlayer(), finalPlayers.get(inGamePlayer));
        }

        mapManager.renderBoard();

        if(!endGame) {
            sendGameMessage("Starting next game...");

            // Make sure players still have enough money
            for(GamePlayer gamePlayer : teamManager.getGamePlayers()) {
                double playerBalance = instance.getEconomy().getBalance(gamePlayer.getPlayer());
                if(playerBalance < this.BIG_BLIND) {
                    sendGameMessage(gamePlayer.getPlayer().getDisplayName() + " has been removed for not having enough money.");
                    teamManager.removeTeamByPlayer(gamePlayer.getPlayer());
                }
            }

            if(teamManager.getGamePlayers().size() <= 1) {
                sendGameMessage("Not enough players to start next game.");
                clearGamePlayers();
                super.endGame(null);
                return;
            }

            int playerIndex = firstBetIndex + 1 >= teamManager.getGamePlayers().size() ? 0 : firstBetIndex + 1;
            teamManager.setTurn(teamManager.getGamePlayers().get(playerIndex));

            startGame();
        }
    }

    private boolean checkRoundOver() {
        // Everyone has folded
        if(playerBets.size() <= 1 && this.playersAllIn.size() == 0) return true;

        // Everyone has gone all in
        if(playerBets.size() == 0) return true;

        boolean roundOver = true;
        for(int playerBet : playerBets.values()) {
            if(playerBet != currentBet) {
                roundOver = false;
                break;
            }
        }

        return roundOver;
    }

    private GamePlayer getBestHand(ArrayList<GamePlayer> gamePlayers) {
        HashMap<Hand, GamePlayer> potHands = new HashMap<>();
        for(GamePlayer sidePotPlayer : gamePlayers) {
            potHands.put(playerHands.get(sidePotPlayer), sidePotPlayer);
        }

        Hand bestHand = Hand.getBestHand(new ArrayList<>(potHands.keySet()), flopCards.getCards());
        return potHands.get(bestHand);
    }

    @Override
    public void exitPlayer(Player player) {
        this.foldGamePlayer(teamManager.getGamePlayer(player));

        super.exitPlayer(player);

        if (!this.isIngame())
            return;

        boolean roundOver = checkRoundOver();
        if(roundOver) {
            this.startNextRound();
            return;
        }

        while (!playerBets.containsKey(teamManager.getTurnPlayer())) {
            teamManager.nextTurn();
        }

        this.reRenderPokerButtons();

        mapManager.renderBoard();
    }

    @Override
    public void endGame(GamePlayer gamePlayer) {
        this.endRound(true);

        clearGamePlayers();
        super.endGame(null);
    }

    @Override
    protected void gamePlayerOutOfTime(GamePlayer gamePlayer) {

    }

    @Override
    public ItemStack getBoardItem() {
        return new BoardItem(gameName, new ItemStack(Material.ACACIA_TRAPDOOR, 1));
    }

    private <T> T getLastElement(final Iterable<T> elements) {
        T lastElement = null;

        for (T element : elements) {
            lastElement = element;
        }

        return lastElement;
    }
}
