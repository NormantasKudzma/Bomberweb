package applet.main;

import game.Game;
import graphics.TextureLoader;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

import org.json.JSONObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import utils.ConfigManager;
import utils.Paths;
import utils.Vector2;
import audio.AudioManager;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;
import controls.LwjglMouseController;
import dialogs.InitialDialog;

public class Bomberapplet extends Applet {
	private static final int TARGET_FPS = 60;
	public static Game gameRef;	// for testing
	
	private int frameHeight = 480;
	private int frameWidth = 640;
	private float deltaTime;
	private Game game;
	private long t0, t1; // Frame start (t0) and frame end (t1) time
	private Canvas canvas;
	private Thread gameThread;
	private boolean running;
	private CommunicatorHelper communicatorHelper;

	public void destroy() {
		game.destroy();
		AudioManager.destroy();
		communicatorHelper.setFinished(true);

		remove(canvas);
		Display.destroy();
		super.destroy();
	}
	
	public void init() {		
		setLayout(new BorderLayout());
		try {
			canvas = new Canvas();
			canvas.setSize(frameWidth, frameHeight);
			setSize(frameWidth, frameHeight);
            add(canvas);
            canvas.setFocusable(true);
            canvas.requestFocus();
            canvas.setIgnoreRepaint(true);
            setVisible(true);
			
			gameThread = new Thread(){
				@Override
				public void run() {
					running = true;
					loop();
				}
			};
			gameThread.start();
		}
		catch (Exception e) {
			e.printStackTrace();
			destroy();
		}

		//AudioManager.playMusic("menu.ogg");
		//AudioManager.playSound(SoundType.BOMB_EXPLODE);

		/*
		 * loop(); destroy();
		 */
	}

	private final void loop() {
		try {
			Display.setParent(canvas);
			Display.create();
			Display.setDisplayMode(new DisplayMode(frameWidth, frameHeight));
		}
		catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	
		// Create and initialize game
		game = new Game();
		game.init();
		gameRef = game;

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glViewport(0, 0, frameWidth, frameHeight);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		preloadTextures();
		
		communicatorHelper = new CommunicatorHelper(game);
		//communicatorHelper.start();
		InitialDialog initialDialog = new InitialDialog(game);
		initialDialog.setVisible(true);
		game.addDialog(initialDialog);
		
		// Set up the mouse controller
		LwjglMouseController c = (LwjglMouseController) ControllerManager.getInstance().getController(EController.LWJGLMOUSECONTROLLER);
		c.addKeybind(0, new ControllerEventListener(){

			@Override
			public void handleEvent(long eventArg, Vector2 pos, int... params) {
				if (params[0] == 1){
					game.onClick(pos);
				}
			}			
		});
		c.setMouseMoveListener(new ControllerEventListener(){
			@Override
			public void handleEvent(long eventArg, Vector2 pos, int... params) {
				game.onHover(pos);
			}
		});
		
		while (running) {
			t0 = System.currentTimeMillis();
			deltaTime = (t0 - t1) * 0.001f;
			t1 = t0;

			// Poll controllers for input
			ControllerManager.getInstance().pollControllers();

			// Update game logic
			game.update(deltaTime);

			// Prepare for rendering
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glOrtho(0, frameWidth, 0, frameHeight, 0, 20);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(-1.0f, -1.0f, 0.0f);
			//GL11.glScalef(1.0f, -1.0f, 1.0f);

			// Render game and swap buffers
			game.render();

			Display.update();
			Display.sync(TARGET_FPS);
		}
		
		destroy();
	}

	/**
	 * Preload textures from current context, so other contexts can use them
	 */
	private void preloadTextures() {
		JSONObject json = ConfigManager.loadConfigAsJson(WebPaths.PRELOADTEX);
		JSONObject animations = json.getJSONObject("animations");
		String path = null;
		try {
			for (int i = 0; i < animations.length(); i++){
				path = animations.getString("" + i);
				TextureLoader.getInstance().getTexture(Paths.ANIMATIONS + path);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		JSONObject textures = json.getJSONObject("textures");
		path = null;
		try {
			for (int i = 0; i < textures.length(); i++){
				path = textures.getString("" + i);
				TextureLoader.getInstance().getTexture(Paths.TEXTURES + path);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
