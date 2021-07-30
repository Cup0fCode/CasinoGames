package water.of.cup.casinogames.games.poker;

import org.bukkit.Location;
import water.of.cup.boardgames.game.npcs.GameNPC;

public class PokerNPC extends GameNPC {

    public PokerNPC(double[] loc) {
        super(loc);
    }

    @Override
    protected String getName() {
        return "Dealer";
    }
}
