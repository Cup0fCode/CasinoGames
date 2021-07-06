package water.of.cup.casinogames;

import java.util.ArrayList;

import water.of.cup.boardgames.extension.BoardGamesExtension;
import water.of.cup.boardgames.game.Game;
import water.of.cup.casinogames.games.mines.Mines;
import water.of.cup.casinogames.games.slots.slotsgames.LibertyBell;

public class CasinoGames extends BoardGamesExtension {

	@Override
	public ArrayList<Class<? extends Game>> getGames() {
		ArrayList<Class<? extends Game>> games = new ArrayList<Class<? extends Game>>();
		games.add(LibertyBell.class);
		games.add(Mines.class);
		return games;
	}

	@Override
	public String getExtensionName() {
		// TODO Auto-generated method stub
		return "CasinoGames";
	}
}
