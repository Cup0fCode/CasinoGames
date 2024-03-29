package water.of.cup.casinogames.games.roulette;

import water.of.cup.boardgames.game.npcs.GameNPC;
import water.of.cup.boardgames.game.npcs.NPCSkin;
import water.of.cup.casinogames.config.ConfigUtil;

public class RouletteNPC extends GameNPC {

    private static final String skinSig = ConfigUtil.NPC_ROULETTE_SKIN_SIG.toRawString();
    private static final String skinData = ConfigUtil.NPC_ROULETTE_SKIN_DATA.toRawString();

    public RouletteNPC(double[] loc) {
        super(loc);
    }

    @Override
    protected String getName() {
        return ConfigUtil.NPC_ROULETTE_NAME.toString();
    }

    @Override
    protected NPCSkin getSkin() {
        return new NPCSkin(skinSig, skinData);
    }
}
