package top.navyblue.main;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;

import top.navyblue.managers.Constants;
import top.navyblue.managers.RenderManager;
import top.navyblue.objects.SolarSystem;
import top.navyblue.physics.PhysicsException;

public class SolarSystemMain {

	public static HashMap<Integer, String> pureColors = new HashMap<Integer, String>();

	static {
		pureColors.put(0, "blue.png");
		pureColors.put(1, "deep_blue.png");
		pureColors.put(2, "green.png");
		pureColors.put(3, "light_blue.png");
		pureColors.put(4, "pink.png");
		pureColors.put(5, "red.png");
		pureColors.put(6, "yellow.png");
	}

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
			}, new KeyBind(Keyboard.KEY_RIGHT) {
				@Override
				void pressed() {
					try {
						ss.selectNext();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new KeyBind(Keyboard.KEY_LEFT) {
				@Override
				void pressed() {
					try {
						ss.selectBefore();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new KeyBind(Keyboard.KEY_EQUALS) {
				@Override
				void pressed() {
					try {
						ss.addSecondaryPlanet();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} };

	private SolarSystem ss;

	private SolarSystemMain() {
	}

	public static void main(String[] args) {
		new SolarSystemMain().start();
	}

	private void start() {
		try {
			ss = initDisplay();

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

		Display.setTitle("Solar system");
		Display.setDisplayMode(new DisplayMode(Constants.DISPLAY_WIDTH,
				Constants.DISPLAY_HEIGHT));
		Display.setResizable(true);
		Display.create(new PixelFormat(8, 8, 0, Constants.SAMPLES_COUNT));
		if (Constants.ENABLE_VSYNC)
			Display.setVSyncEnabled(true);
		Display.setVSyncEnabled(true);
		return ss;
	}

	private void updateMovement(SolarSystem ss, float tickPart) {
		handleKeyboard();
		handleMouse();

		checkSize();
		renderScene();
	}

	private void renderScene() {
		glPushMatrix();
		moveCamera();
		glClear(GL_DEPTH_BUFFER_BIT);
		ss.render(tickPart);
		glPopMatrix();
	}

	private void checkSize() {
		if (!(width == Display.getWidth() && height == Display.getHeight())) {
			width = Display.getWidth();
			height = Display.getHeight();
			RenderManager.setupView(width, height);
		}
	}

	private void moveCamera() {
		glTranslatef(0, 0, distance);
		glRotatef(rotX, -1, 0, 0);
		glRotatef(rotZ, 0, 0, -1);
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

			int index = select(Mouse.getX(), Mouse.getY());
			if (index != -1) {
				ss.selectPlanet(index);
			}
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

	public int select(int x, int y) {
		IntBuffer selBuffer = ByteBuffer.allocateDirect(1024)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] buffer = new int[256];

		IntBuffer viewBuffer = ByteBuffer.allocateDirect(64)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		// The size of the viewport. [0] Is <x>, [1] Is <y>, [2] Is <width>, [3] Is <height> 
		int[] viewport = new int[4];

		// The number of "hits" (objects within the pick area). 
		int hits;
		// Get the viewport info 
		glGetInteger(GL_VIEWPORT, viewBuffer);
		viewBuffer.get(viewport);

		// Set the buffer that OpenGL uses for selection to our buffer 
		glSelectBuffer(selBuffer);
		// Change to selection mode 
		glRenderMode(GL_SELECT);
		// Initialize the name stack (used for identifying which object was selected) 
		glInitNames();
		glPushName(0);

		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		{
			glLoadIdentity();
			/*  create 5x5 pixel picking region near cursor location */ 
			GLU.gluPickMatrix((float) x, (float) y, 5.0f, 5.0f,
					IntBuffer.wrap(viewport));
			GLU.gluPerspective(60f, 800 / 600f, 0.1F, 1000F);
			renderScene();
		}
		glPopMatrix();
		// Exit selection mode and return to render mode, returns number selected 
		hits = glRenderMode(GL_RENDER);

		selBuffer.get(buffer);
		// Objects Were Drawn Where The Mouse Was 
		if (hits > 0) {
			// If There Were More Than 0 Hits 
			int choose = buffer[3];
			int depth = buffer[1];

			for (int i = 1; i < hits; i++) {
				// Loop Through All The Detected Hits 
                // If This Object Is Closer To Us Than The One We Have Selected 
				if ((buffer[i * 4 + 1] < depth || choose == 0)
						&& buffer[i * 4 + 3] != 0) {
					choose = buffer[i * 4 + 3];
					depth = buffer[i * 4 + 1];
				}
			}

			if (choose > 0) {
				return choose - 1;
			}
		}
		return -1;
	}

	private static abstract class KeyBind {

		private final int key;
		private boolean isKeyDown = false;
		private boolean isAddDown = false;

		KeyBind(int key) {
			this.key = key;
		}

		abstract void pressed();

		void update() {
			if (Keyboard.isKeyDown(key) && key == Keyboard.KEY_EQUALS) {
				if (!isAddDown) {
					isAddDown = true;
					pressed();
				}
			} else if (Keyboard.isKeyDown(key)) {
				if (!isKeyDown) {
					isKeyDown = true;
					pressed();
				}
			} else {
				isKeyDown = false;
				isAddDown = false;
			}
		}
	}

}
