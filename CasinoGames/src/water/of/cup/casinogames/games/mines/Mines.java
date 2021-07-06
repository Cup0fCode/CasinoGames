package water.of.cup.casinogames.games.mines;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import water.of.cup.boardgames.game.*;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.casinogames.games.MathUtils;

import java.util.ArrayList;

public class Mines extends Game {

    private int[][] selectedTiles;
    private int[][] bombLocations;
    private Button[][] tileButtons;

    public Mines(int rotation) {
        super(rotation);
    }

    @Override
    protected void setMapInformation(int i) {
        this.mapStructure = new int[][] { { 1 } };
        this.placedMapVal = 1;
    }

    @Override
    protected void startGame() {
        super.startGame();
        buttons.clear();
        setInGame();
        createBoard();
        mapManager.renderBoard();
    }

    private void createBoard() {
        this.selectedTiles = new int[5][5];
        this.bombLocations = new int[5][5];
        this.tileButtons = new Button[5][5];

        for(int y = 0; y < this.tileButtons.length; y++) {
            for(int x = 0; x < this.tileButtons[y].length; x++) {
                Button tileButton = new Button(this, "MINE_UNSELECTED", new int[] { x * 26, y * 26 }, 0, "UNSELECTED");
                tileButton.setClickable(true);
                this.tileButtons[y][x] = tileButton;
                buttons.add(tileButton);
            }
        }

        // TODO: place bombs
    }

    @Override
    protected void setGameName() {
        this.gameName = "Mines";
    }

    @Override
    protected void setBoardImage() {
        this.gameImage = new GameImage("MINES_BOARD");
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
        return null;
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
//        TODO: Add back once game inv is added
//        if(!teamManager.getTurnPlayer().equals(gamePlayer)) return;

        int[] clickLoc = mapManager.getClickLocation(loc, map);

        Button b = getClickedButton(gamePlayer, clickLoc);
        if(b == null) return;

        boolean didSelect = selectMine(b);
        if(didSelect) {
            player.sendMessage("You selected a tile!");
        }

        mapManager.renderBoard();
    }

    private boolean selectMine(Button b) {
        int[] buttonLoc = getButtonLocation(b);
        if(buttonLoc == null) return false;

        if(b.getName().equals("UNSELECTED")) {
            // TODO: Check if bomb is located
            this.selectedTiles[buttonLoc[1]][buttonLoc[0]] = 1;

            b.getImage().setImage("MINE_SELECTED");
            b.setName("SELECTED");
            return true;
        }

        return false;
    }

    private long getWinChance(int bombCount, int tilesOpened) {
        int tileTotal = 25;
        int availTiles = 25 - bombCount;

        long first = MathUtils.combination(tileTotal, tilesOpened);
        long second = MathUtils.combination(availTiles, tilesOpened);

//        double result = 0.99 * (first/second);
        // TODO: Finish this
        return 0;
    }

    private int[] getButtonLocation(Button b) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (b == tileButtons[y][x])
                    return new int[] { x, y };
            }
        }
        return null;
    }

    @Override
    protected void gamePlayerOutOfTime(GamePlayer gamePlayer) {

    }

    @Override
    public ItemStack getBoardItem() {
        return new BoardItem(gameName, new ItemStack(Material.ACACIA_TRAPDOOR, 1));
    }
}
