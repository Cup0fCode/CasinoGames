package water.of.cup.casinogames;

import java.util.ArrayList;

import water.of.cup.boardgames.extension.BoardGamesConfigOption;
import water.of.cup.boardgames.extension.BoardGamesExtension;
import water.of.cup.boardgames.game.Game;
import water.of.cup.casinogames.config.ConfigUtil;
import water.of.cup.casinogames.games.hilo.HiLo;
import water.of.cup.casinogames.games.mines.Mines;
import water.of.cup.casinogames.games.slots.slotsgames.LibertyBell;
import water.of.cup.casinogames.games.slots.slotsgames.MoneyHoney;

public class CasinoGames extends BoardGamesExtension {

	@Override
	public ArrayList<Class<? extends Game>> getGames() {
		ArrayList<Class<? extends Game>> games = new ArrayList<Class<? extends Game>>();
		games.add(LibertyBell.class);
		games.add(Mines.class);
		games.add(MoneyHoney.class);
		games.add(HiLo.class);
		return games;
	}

	@Override
	public String getExtensionName() {
		// TODO Auto-generated method stub
		return "CasinoGames";
	}

	@Override
	public ArrayList<BoardGamesConfigOption> getExtensionConfig() {
		ArrayList<BoardGamesConfigOption> configOptions = new ArrayList<>();
		for(ConfigUtil configUtil : ConfigUtil.values()) {
			configOptions.add(new BoardGamesConfigOption(configUtil.getPath(), configUtil.getDefaultValue()));
		}
		return configOptions;
	}
}
