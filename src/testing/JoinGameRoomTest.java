package testing;

import game.Game;
import graphics.Button;
import graphics.IClickable;

import org.junit.Assert;

import applet.main.Bomberapplet;
import dialogs.InitialDialog;

public class JoinGameRoomTest extends AppletTest{	
	public void testJoinGameRoom(){
		Bomberapplet applet = startApplet();
		Game game = applet.gameRef;
		try {
			InitialDialog dialog = (InitialDialog)game.getDialog("InitialDialog");
			for (IClickable i : dialog.getClickables()){
				if (i instanceof Button){
					if (((Button)i).getText().equals("Room 999999")){
						Assert.fail("There should be no gameroom with id 999999.");
					}
				}
			}
			for (IClickable i : dialog.getClickables()){
				if (i instanceof Button){
					if (((Button)i).getText().equals("Room 123456")){
						return;
					}
				}
			}
			Assert.fail("Room with expected id 123456 was not found.");
		}
		catch (Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
}
