package testing;

import game.Entity;
import game.Game;
import game.PlayerEntity;

import org.junit.Assert;

import utils.Vector2;
import applet.main.Bomberapplet;

public class CollisionTest extends AppletTest{
	public void testCollisions(){
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
			Assert.assertTrue(player != null);
			
			// Move left until wall is hit or time limit is met
			int sleepTime = 0;
			Vector2 position = player.getPosition().copy();
			boolean isWallHit = false;
			while (sleepTime < 2000){
				player.moveLeft();
				Thread.sleep(50);
				if (player.getPosition().equals(position)){
					isWallHit = true;
					break;
				}
				position = player.getPosition().copy();
				sleepTime += 50;
			}
			Assert.assertFalse(!isWallHit);
			
			// Try to move right
			position = player.getPosition().copy();
			player.moveRight();
			Thread.sleep(200);
			if (player.getPosition().equals(position)){
				Assert.fail();
			}
			Thread.sleep(500);
		}
		catch (Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}	
}
