package top.navyblue.main;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import top.navyblue.managers.Constants;
import top.navyblue.managers.RenderManager;
import top.navyblue.objects.Craft;
import top.navyblue.objects.SolarSystem;
import top.navyblue.physics.KeyListenProvider;
import top.navyblue.physics.PhysicsException;
import top.navyblue.physics.PhysicsProvider;

public class SolarSystemMain {

	private long cachedMills;
	private long utime;
	private int fps;
	private float tickPart;
	
	private boolean isExit = false;
	
	public static boolean isRunning = true;
	public static boolean pauseMoment = false;
	public static boolean physTicked = false;
	
	private float distance = -50, rotX = 45, rotZ = 0;
	private int width, height;
	private int clickX = 0, clickY = 0;
	
	private Craft craft;
	private KeyListenProvider craftPhysicsProvider;

	private KeyBind[] keyBinds = new KeyBind[] {
			new KeyBind(Keyboard.KEY_ESCAPE) {
				@Override
				void pressed() {
					isExit = true;
				}
			}, new KeyBind(Keyboard.KEY_SPACE) {
				@Override
				void pressed() {
					pauseMoment = !pauseMoment;
				}
			}, new KeyBind(Keyboard.KEY_RIGHT){
				@Override
				void pressed() {
					craftPhysicsProvider.listen(1, 0, 0, 20);
				}
			}, new KeyBind(Keyboard.KEY_LEFT){
				@Override
				void pressed() {
					craftPhysicsProvider.listen(-1, 0, 0, 20);
				}
			}, new KeyBind(Keyboard.KEY_UP){
				@Override
				void pressed() {
					craftPhysicsProvider.listen(0, 0, -1, 20);
				}
			}, new KeyBind(Keyboard.KEY_DOWN){
				@Override
				void pressed() {
					craftPhysicsProvider.listen(0, 0, 1, 20);
				}
			}};


	private SolarSystemMain() {}

	public static void main(String[] args) {
		new SolarSystemMain().start();
	}

	private void start() {
		try {
			SolarSystem ss = initDisplay();

			initVariables();

			initEnvironment(ss);

			while (!(Display.isCloseRequested() || isExit)) {
				isRunning = Display.isVisible();

				if (isRunning) {
					updateMovement(ss, tickPart);
				}

				Display.update();

				if (!Constants.ENABLE_VSYNC)
					Display.sync(Constants.FPS_LIMIT);

				if (isRunning) {
					updateWindow();
				}

				if (ss.physics.error)
					throw new PhysicsException();
			}
		} catch (Throwable th) {
			JOptionPane.showMessageDialog(null, th.getMessage(),
					"Error, please check the console!", 0);
			if (!(th instanceof PhysicsException))
				th.printStackTrace();
		} finally {
			if (Display.isCreated())
				Display.destroy();
			System.exit(0);
		}
	}

	private void updateWindow() {
		long currentMills = System.currentTimeMillis();
		long delta = currentMills - cachedMills;
		if (delta >= 1000) {
			Display.setTitle(String.format("Solar system (%d fps)", ++fps));
			cachedMills = currentMills;
			fps = 0;
		} else {
			fps++;
		}
		currentMills = System.currentTimeMillis();
		if (physTicked) {
			physTicked = false;
			utime = currentMills;
		}
		delta = currentMills - utime;
		tickPart = (50 - delta) / 50f;
	}

	private void initEnvironment(SolarSystem ss) throws Exception,
			LWJGLException {
		RenderManager.init(width, height);
		ss.init();
		Keyboard.create();
		Keyboard.enableRepeatEvents(false);
		Mouse.create();
	}

	private void initVariables() {
		cachedMills = System.currentTimeMillis();
		utime = cachedMills;
		fps = 0;
		tickPart = 0;
		width = Display.getWidth();
		height = Display.getHeight();
	}

	private SolarSystem initDisplay() throws LWJGLException, Exception {
		SolarSystem ss = new SolarSystem();
		craft = ss.getCraft();
		craftPhysicsProvider = (KeyListenProvider) craft.getPhysics();
		
		Display.setTitle("Solar system");
		Display.setDisplayMode(new DisplayMode(Constants.DISPLAY_WIDTH,
				Constants.DISPLAY_HEIGHT));
		Display.setResizable(true);
		Display.create(new PixelFormat(8, 8, 0, Constants.SAMPLES_COUNT));
		if (Constants.ENABLE_VSYNC)
			Display.setVSyncEnabled(true);
		return ss;
	}

	private void updateMovement(SolarSystem ss, float tickPart) {
		checkSize();
		handleKeyboard();
		handleMouse();
		
		GL11.glPushMatrix();
		moveCamera();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		ss.render(tickPart);
		GL11.glPopMatrix();
	}

	private void checkSize() {
		if (!(width == Display.getWidth() && height == Display.getHeight())) {
			width = Display.getWidth();
			height = Display.getHeight();
			RenderManager.setupView(width, height);
		}
	}

	private void moveCamera() {
		GL11.glTranslatef(0, 0, distance);
		GL11.glRotatef(rotX, -1, 0, 0);
		GL11.glRotatef(rotZ, 0, 0, -1);
	}

	private void handleKeyboard() {
		for (KeyBind kb : keyBinds)
			kb.update();
	}

	private void handleMouse() {
		int dWheel = Mouse.getDWheel();
		if (dWheel != 0) {
			if (dWheel > 0)
				distance++;
			else
				distance--;
		}
		if (Mouse.isButtonDown(0)) {
			rotX += (clickY - Mouse.getY()) / 4f;
			rotZ += (clickX - Mouse.getX()) / 4f;
		} else if (Mouse.isButtonDown(1)) {
			distance += (clickY - Mouse.getY()) / 4f;
		}
		clickX = Mouse.getX();
		clickY = Mouse.getY();
		if (distance > -10)
			distance = -10;
		else if (distance < -100)
			distance = -100;
	}

	private static abstract class KeyBind {

		private final int key;
		private boolean spaceDown = false;

		KeyBind(int key) {
			this.key = key;
		}

		abstract void pressed();

		void update() {
			if (Keyboard.isKeyDown(key) && key == Keyboard.KEY_SPACE ) {
				if (!spaceDown) {
					spaceDown = true;
					pressed();
				}
			} else if(Keyboard.isKeyDown(key) && key != Keyboard.KEY_SPACE) {
					pressed();
			} else {
				spaceDown = false;
			}
		}
	}

}
