package testing;

import game.Entity;
import game.Game;
import game.PlayerEntity;
import game.WallEntity;

import org.junit.Assert;

import applet.main.Bomberapplet;

public class BombExplodeTest extends AppletTest{
	public void testBombExplode(){
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
			
			int numWallsPre = 0;
			int numWallsPost = 0;
			
			for (Entity i : game.getEntityList()){
				if (i instanceof WallEntity){
					++numWallsPre;
				}
			}
			
			// Place bomb
			player.placeBomb();
			Thread.sleep(4000);
			
			Assert.assertTrue("Player should be destroyed", player.isDestroyed());
			
			for (Entity i : game.getEntityList()){
				if (i instanceof WallEntity){
					++numWallsPost;
				}
			}
			
			Assert.assertTrue("Bomb should not destroy walls", numWallsPre == numWallsPost);
		}
		catch (Exception e){
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
