package water.of.cup.casinogames.games.mines;

import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.boardgames.game.storage.StorageType;
import water.of.cup.casinogames.storage.CasinoGamesStorageType;

public class MinesStorage extends GameStorage {

    public MinesStorage(Game game) {
        super(game);
    }
    @Override
    protected String getTableName() {
        return "mines";
    }

    @Override
    protected StorageType[] getGameStores() {
        return new StorageType[] {
                CasinoGamesStorageType.MONEY_WON,
                CasinoGamesStorageType.MONEY_LOST
        };
    }
}
