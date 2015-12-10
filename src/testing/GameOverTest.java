package testing;

import game.Entity;
import game.Game;
import game.PlayerEntity;
import game.WallEntity;

import org.junit.Assert;

import applet.main.Bomberapplet;

public class GameOverTest extends AppletTest{
	public void testGameOver(){
		Bomberapplet applet = startApplet();
		Game game = applet.gameRef;
		try {
			game.setDialogVisible("InitialDialog", false);
			PlayerEntity player = null;
			for (Entity i : game.getEntityList()){
				if (i instanceof PlayerEntity){
					player = (PlayerEntity)i;
				}
			}
			Assert.assertTrue("Player should be in the game", player != null);
			
			// Place bomb
			Thread.sleep(200);
			player.placeBomb();
			Thread.sleep(4000);
			
			Assert.assertTrue("Game should be over", game.isGameOver());
		}
		catch (Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
