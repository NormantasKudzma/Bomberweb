package testing;

import game.BombEntity;
import game.Entity;
import game.Game;
import game.PlayerEntity;

import org.junit.Assert;

import applet.main.Bomberapplet;

public class BombPlaceTest extends AppletTest {
	public void testBombPlace(){
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
			
			int numBombs = 0;
			
			// Place bomb
			player.placeBomb();
			Thread.sleep(150);
			for (Entity i : game.getEntityList()){
				if (i instanceof BombEntity){
					++numBombs;
				}
			}
			Assert.assertFalse("Player should be allowed to place 1 bomb", numBombs != 1);
			
			numBombs = 0;
			player.placeBomb();
			Thread.sleep(150);
			player.placeBomb();
			Thread.sleep(150);
			for (Entity i : game.getEntityList()){
				if (i instanceof BombEntity){
					++numBombs;
				}
			}
			Assert.assertFalse("Player should be allowed to place ONLY 1 bomb", numBombs != 1);
		}
		catch (Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
