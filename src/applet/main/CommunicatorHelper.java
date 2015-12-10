package applet.main;

import game.Game;
import game.IUpdatable;
import game.PlayerEntity;
import graphics.SpriteAnimation;

import org.json.JSONArray;
import org.json.JSONObject;

import web.Communicator;
import web.Communicator.ERequestType;

public class CommunicatorHelper extends Thread implements IUpdatable{
	private static final float PLAYER_UPDATE_DELTA = 1.0f;
	private static final float MOVE_DELTA = 1.0f;
	private static final long SLEEP_DELTA = 1000;
	private static final float SLEEP_DELTA_FLOAT = SLEEP_DELTA * 0.001f;
	
	private WebEntityHelper entityHelper;
	private Game game;
	private PlayerEntity localPlayer;
	private int localPid;
	private int currentGameRoom = 1;
	private float playerUpdate = 0.0f;
	private float moveUpdate = 0.0f;
	private boolean isFinished = false;
	
	public CommunicatorHelper(Game game){
		this.game = game;
		entityHelper = new WebEntityHelper(game);
	}
	
	public void init(){
		JSONObject obj = Communicator.sendRequest(ERequestType.GET_PID);
		localPid = obj.getInt("pid");
		localPlayer = newPlayer(localPid, true);
	}
	
	public void update(float deltaTime){
		playerUpdate += deltaTime;
		if (playerUpdate > PLAYER_UPDATE_DELTA){
			playerUpdate = 0.0f;
			updatePlayers();
		}
		
		moveUpdate += deltaTime;
		if (moveUpdate > MOVE_DELTA){
			moveUpdate = 0.0f;
			movePlayer();
		}
	}
	
	public PlayerEntity newPlayer(int pid, boolean isLocal){
		PlayerEntity player = new PlayerEntity(game);		
		player.setSprite(new SpriteAnimation("ranger_f.json"));
		player.initEntity();
		player.setPosition(1, 1);
		if (isLocal){
			player.readKeybindings();
		}
		entityHelper.addPlayer(pid, player);
		return player;
	}

	public void updatePlayers(){
		JSONObject obj = Communicator.sendRequest(ERequestType.UPDATE_PLAYERS, currentGameRoom, localPid);
		JSONArray pids = obj.getJSONArray("pids");
		int pid;
		PlayerEntity player;
		for (int i = 0; i < pids.length(); i++){
			pid = pids.getInt(i);
			if (pid == localPid){
				continue;
			}
			player = entityHelper.getPlayerById(pid);
			if (player == null){
				player = newPlayer(pid, false);
			}
			JSONObject coordinates = obj.getJSONObject(pid + "");
			player.setPosition((float)coordinates.getDouble("x"), (float)coordinates.getDouble("y"));
		}
	}
	
	public void movePlayer(){
		Communicator.sendRequest(ERequestType.MOVE, currentGameRoom, localPid, 0, localPlayer.getPosition());
	}
	
	public void setFinished(boolean finished){
		isFinished = finished;
	}
	
	@Override
	public void run() {
		//init();
		while (!isFinished){
			try {
				Thread.sleep(SLEEP_DELTA);
				update(SLEEP_DELTA_FLOAT);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
