package applet.main;

import game.Game;
import game.PlayerEntity;

import java.util.HashMap;

public class WebEntityHelper {
	private HashMap<Integer, PlayerEntity> playerMap = new HashMap<Integer, PlayerEntity>(4);
	private Game game;
	
	public WebEntityHelper(Game game){
		this.game = game;
	}
	
	public void addPlayer(int id, PlayerEntity player){
		playerMap.put(id, player);
		game.addEntity(player);	
	}
	
	public PlayerEntity getPlayerById(int id){
		return playerMap.get(id);
	}
}
