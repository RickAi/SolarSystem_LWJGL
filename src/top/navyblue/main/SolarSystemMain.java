package top.navyblue.main;

import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import top.navyblue.managers.Constants;
import top.navyblue.managers.RenderManager;
import top.navyblue.objects.SolarSystem;
import top.navyblue.physics.PhysicsException;

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
			}};
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
	
	private void renderScene(){
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
			
			int select = select(Mouse.getX(), Mouse.getY());
			System.out.println(select + "<---");
			
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
	
	public int select(int x, int y)
	{
		IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] buffer = new int[256];

		IntBuffer viewBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] viewport = new int[4];

		int hits;
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);
		viewBuffer.get(viewport);

		GL11.glSelectBuffer(selBuffer);
		GL11.glRenderMode(GL11.GL_SELECT);
		GL11.glInitNames();
		GL11.glPushName(0);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		{
			GL11.glLoadIdentity();
			GLU.gluPickMatrix( (float) x, (float) y, 5.0f, 5.0f,IntBuffer.wrap(viewport));
			GLU.gluPerspective(60f, 800/600f,  0.1F, 1000F);
			renderScene();
		}
		GL11.glPopMatrix();
		hits = GL11.glRenderMode(GL11.GL_RENDER);

		selBuffer.get(buffer);
		if (hits > 0)
		{
			int choose = buffer[3];
			int depth = buffer[1];

			for (int i = 1; i < hits; i++)
			{
				if ((buffer[i * 4 + 1] < depth || choose == 0) && buffer[i * 4 + 3] != 0)
				{
					choose = buffer[i * 4 + 3];
					depth = buffer[i * 4 + 1];
				}
			}

			if (choose > 0)
			{
				return choose - 1;
			}
		}
		return -1;
	}

	private static abstract class KeyBind {

		private final int key;
		private boolean spaceDown = false;
		private boolean leftDown = false;
		private boolean rightDown = false;

		KeyBind(int key) {
			this.key = key;
		}

		abstract void pressed();

		void update() {
			if (Keyboard.isKeyDown(key) && key == Keyboard.KEY_SPACE) {
				if (!spaceDown) {
					spaceDown = true;
					pressed();
				}
			} else if(Keyboard.isKeyDown(key) && key == Keyboard.KEY_LEFT){
				if (!leftDown) {
					leftDown = true;
					pressed();
				}
			} else if(Keyboard.isKeyDown(key) && key == Keyboard.KEY_RIGHT){
				if (!rightDown) {
					rightDown = true;
					pressed();
				}
			} else if (Keyboard.isKeyDown(key) && key != Keyboard.KEY_SPACE) {
				pressed();
			} else {
				spaceDown = false;
				leftDown = false;
				rightDown = false;
			}
		}
	}

}
