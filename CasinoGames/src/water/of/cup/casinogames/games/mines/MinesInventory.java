package water.of.cup.casinogames.games.mines;

import org.bukkit.Material;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.inventories.GameOption;
import water.of.cup.boardgames.game.inventories.GameOptionType;

import java.util.ArrayList;
import java.util.HashMap;

public class MinesInventory extends GameInventory {
    private final Mines game;
    public MinesInventory(Mines game) {
        super(game);
        this.game = game;
    }

    @Override
    protected ArrayList<GameOption> getOptions() {
        ArrayList<GameOption> options = new ArrayList<GameOption>();//  economyRequired
        GameOption bet = new GameOption("betAmount", Material.GOLD_NUGGET, GameOptionType.COUNT, null, "0", true);
        options.add(bet);
        // TODO: Add translation
        GameOption bombAmount = new GameOption("bombAmount", Material.GOLD_NUGGET, GameOptionType.COUNT, "Bomb Amount: ", "1", false, 1, 24);
        options.add(bombAmount);
        return options;
    }

    @Override
    protected int getMaxQueue() {
        return 0;
    }

    @Override
    protected int getMaxGame() {
        return 1;
    }

    @Override
    protected int getMinGame() {
        return 1;
    }

    @Override
    protected boolean hasTeamSelect() {
        return false;
    }

    @Override
    protected boolean hasGameWagers() {
        return false;
    }

    @Override
    protected boolean hasWagerScreen() {
        return false;
    }

    @Override
    protected boolean hasForfeitScreen() {
        return false;
    }

    @Override
    protected void onGameCreate(HashMap<String, Object> hashMap, ArrayList<GamePlayer> arrayList) {
        game.startGame();
    }
}
