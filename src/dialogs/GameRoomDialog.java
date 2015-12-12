package dialogs;

import game.PlayerEntity;
import graphics.BaseDialog;
import graphics.Button;
import graphics.Label;
import graphics.SpriteAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Vector2;
import web.Communicator;
import web.Communicator.ERequestType;
import applet.main.CommunicatorHelper;
import applet.main.WebEntityHelper;

public class GameRoomDialog extends BaseDialog {
	protected final float PLAYER_REFRESH = 1.0f;
	protected final float GAME_START_REFRESH = 1.0f;
	protected final Vector2 START_POSITIONS[] = {new Vector2(0.125f, 0.125f), new Vector2(0.125f, 1.75f), new Vector2(1.75f, 0.125f), new Vector2(1.75f, 1.75f)};
	
	protected int gameRoom;
	protected Label room;
	protected ArrayList<Label> players = new ArrayList<Label>(4);
	protected Button start;
	protected SpriteAnimation skin;
	
	protected float playerRefresh = 0.0f;
	protected float gameStartRefresh = 0.0f;

	public GameRoomDialog() {
		super("GameRoomDialog");
	}
	
	public void setInfo(int id, SpriteAnimation skin){
		gameRoom = id;
		room.setText("Room " + id);
		this.skin = skin;
	}

	@Override
	protected void initialize() {
		super.initialize();
		room = new Label("Room");
		room.getPosition().set(1.0f, 1.7f);
		start = new Button(){
			boolean isStarted = false;
			
			@Override
			public boolean onClick(Vector2 pos) {
				boolean isMouseOver = isMouseOver(pos);
				if (!isMouseOver) {
					return false;
				}
				if (!isStarted){
					isStarted = true;
					setText("Starting..");
					Communicator.sendRequest(ERequestType.GAME_START, gameRoom, Communicator.playerPid);
				}
				return true;
			}
		};
		start.setText("Start");
		start.getScale().set(9.5f, 2.5f);
		start.setPosition(1.5f, 1.0f);
		addClickable(start);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		playerRefresh += deltaTime;
		if (playerRefresh > PLAYER_REFRESH){
			playerRefresh = 0.0f;
			recheckPlayers(Communicator.sendRequest(ERequestType.UPDATE_PLAYERS, gameRoom, Communicator.playerPid));
		}
		
		gameStartRefresh += deltaTime;
		if (gameStartRefresh > GAME_START_REFRESH){
			gameStartRefresh = 0.0f;
			recheckGameState(Communicator.sendRequest(ERequestType.GAME_STATE, gameRoom, Communicator.playerPid));
		}
	}
	
	protected void recheckGameState(JSONObject json){
		if (json == null || json.isNull("state")){
			return;
		}
		if (json.getString("state").equals("STARTED")){
			Collections.sort(players, new Comparator<Label>(){
				@Override
				public int compare(Label i, Label j) {
					return i.getText().compareTo(j.getText());
				}				
			});
			for (int i = 0; i < players.size(); ++i){
				int pid = Integer.parseInt(players.get(i).getText().split(" ")[1]);
				boolean isLocal = pid == Communicator.playerPid;
				PlayerEntity player = CommunicatorHelper.getInstance().newPlayer(pid, isLocal);
				if (isLocal){
					player.setSprite(skin);
				}
				else {
					player.setSprite(new SpriteAnimation(WebEntityHelper.SKINS[(new Random()).nextInt(WebEntityHelper.SKINS.length)]));
				}
				player.setPosition(START_POSITIONS[i]);
			}
			CommunicatorHelper.getInstance().start();
			setVisible(false);
		}
	}
	
	protected void recheckPlayers(JSONObject json){
		if (json.isNull("pids")){
			return;
		}
		JSONArray pids = json.getJSONArray("pids");
		int pid;
		Label label;
		
		if (players.size() == pids.length()){
			return;
		}
		
		players.clear();
		
		for (int i = 0; i < pids.length(); i++) {
			pid = pids.getInt(i);
			label = new Label("Player " + pid + (pid == Communicator.playerPid ? " you" : ""));
			label.getPosition().set(0.5f, 1.4f - i * 0.17f);
			label.getScale().set(0.48f, 0.48f);
			players.add(label);
		}
	}
	
	@Override
	public void render() {
		super.render();
		room.render();
		start.render();
		for (int i = 0; i < players.size(); ++i){
			players.get(i).render();
		}
	}
}
