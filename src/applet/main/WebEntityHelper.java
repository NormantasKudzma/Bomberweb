package applet.main;

import game.Game;
import game.PlayerEntity;

import java.util.HashMap;

public class WebEntityHelper {
	public static final String SKINS[] = new String[] { "healer_f.json", "mage_m.json", "ninja_f.json", "ranger_m.json", "townfolk_f.json", "warrior_m.json" };

	private HashMap<Integer, PlayerEntity> playerMap = new HashMap<Integer, PlayerEntity>(4);
	private Game game;

	public WebEntityHelper(Game game) {
		this.game = game;
	}

	public void addPlayer(int id, PlayerEntity player) {
		playerMap.put(id, player);
		game.addEntity(player);
	}

	public PlayerEntity getPlayerById(int id) {
		return playerMap.get(id);
	}
}
