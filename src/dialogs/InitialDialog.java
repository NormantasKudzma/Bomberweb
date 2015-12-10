package dialogs;

import game.Game;
import graphics.BaseDialog;
import graphics.Button;
import graphics.Label;
import graphics.SpriteAnimation;

import org.json.JSONObject;

import utils.Vector2;
import web.Communicator;
import web.Communicator.ERequestType;

public class InitialDialog extends BaseDialog {
	private static final String SKINS [] = new String[]{
		"healer_f.json",
		"mage_m.json",
		"ninja_f.json",
		"ranger_m.json",
		"townfolk_f.json",
		"warrior_m.json"
	};
	
	private Label version;
	private Button changeSkin;	
	private SpriteAnimation skin;
	private Vector2 skinPos = new Vector2(0.5f, 1.25f);
	private int localPid = -1;
	private Label pidLabel;
	private Label first32;
	private Label last32;
	private Game game;
	
	public InitialDialog(Game game){
		super("InitialDialog");
		this.game = game;
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		version = new Label("Bomberweb v0.9");
		version.getPosition().set(1.0f, 1.7f);
		
		// Set up skin button and sprite
		skin = new SpriteAnimation(SKINS[0]);
		changeSkin = new Button(){
			private int i = 0;
			
			@Override
			public boolean onClick(Vector2 pos) {
				boolean isMouseOver = isMouseOver(pos);
				if (!isMouseOver){
					return false;
				}
				skin.loadSpriteSheet(SKINS[++i % SKINS.length]);
				return true;
			}
		};
		changeSkin.setText("Change skin");
		changeSkin.setScale(new Vector2(9.0f, 2.0f));
		changeSkin.setPosition(0.5f, 1.0f);
		addClickable(changeSkin);

		// Set up pid label
		JSONObject obj = Communicator.sendRequest(ERequestType.GET_PID);
		localPid = obj.getInt("pid");
		
		pidLabel = new Label("Your pid is " + localPid);
		pidLabel.getPosition().set(1.5f, 1.15f);
		pidLabel.getScale().set(0.48f, 0.48f);
		
		// Set up room buttons
		JSONObject rooms = Communicator.sendRequest(ERequestType.UPDATE_ROOMS);
		JSONObject newRoom = new JSONObject();
		newRoom.put("id", -1);
		newRoom.put("name", "create new");
		rooms.put((rooms.length() - 1) + "", newRoom);
		for (int i = 0; i < rooms.length() - 1; i++){
			JSONObject room = rooms.getJSONObject("" + i);
			final int roomId = room.getInt("id");
			String roomName = room.getString("name");
			Button b = new Button(){
				int id = roomId;
				
				@Override
				public boolean onClick(Vector2 pos) {
					boolean isMouseOver = isMouseOver(pos);
					if (!isMouseOver){
						return false;
					}

					GameRoomDialog dialog = new GameRoomDialog(id);
					game.addDialog(dialog);
					dialog.setVisible(true);
					InitialDialog.this.setVisible(false);
					return true;
				}
			};
			if (roomName == null || roomName.isEmpty()){
				b.setText("Room " + roomId);
			}
			else {
				b.setText("Room " + roomName);
			}
			
			b.setScale(new Vector2(8.0f, 2.0f));
			b.setPosition(1.5f, 1.0f - i * 0.15f);
			addClickable(b);
		}
		
		// All printable characters
		first32 = new Label("0123456789.ABCDEFGHIJKLMOPQRSTU");
		first32.getPosition().set(1.0f, 0.2f);
		first32.getScale().set(0.6f, 0.6f);
		last32 =  new Label("VWXYZabcdefghijklmnopqrstuvwxyz");
		last32.getPosition().set(1.0f, 0.1f);
		last32.getScale().set(0.6f, 0.6f);
	}
	
	@Override
	public void render() {
		super.render();
		version.render();
		skin.render(skinPos, 0, Vector2.one);
		pidLabel.render();
		first32.render();
		last32.render();
	}
}
