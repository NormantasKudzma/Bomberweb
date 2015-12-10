package dialogs;

import graphics.BaseDialog;

public class GameRoomDialog extends BaseDialog{
	protected int gameRoom;
	
	public GameRoomDialog(int gameRoomId) {
		super("GameRoomDialog");
		gameRoom = gameRoomId;
	}

	@Override
	protected void initialize() {
		super.initialize();
	}
	
}
