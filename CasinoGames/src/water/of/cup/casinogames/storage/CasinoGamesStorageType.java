package water.of.cup.casinogames.storage;

import water.of.cup.boardgames.game.storage.StorageType;

import java.sql.JDBCType;

public enum CasinoGamesStorageType implements StorageType {

    MONEY_WON ("money_won", "double default 0", JDBCType.DOUBLE, true),
    MONEY_LOST ("money_lost", "double default 0", JDBCType.DOUBLE, true);

    private final String key;
    private final JDBCType dataType;
    private final String query;
    private final boolean orderByDescending;

    CasinoGamesStorageType(String key, String query, JDBCType dataType, boolean orderByDescending) {
        this.key = key;
        this.dataType = dataType;
        this.query = query;
        this.orderByDescending = orderByDescending;
    }

    public String getKey() {
        return key;
    }

    public JDBCType getDataType() {
        return dataType;
    }

    public boolean isOrderByDescending() {
        return orderByDescending;
    }

    public String getQuery() {
        return this.query;
    }
}
