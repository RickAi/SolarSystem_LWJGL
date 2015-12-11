package top.navyblue.main;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
public class Main
{
    public static void main(String[] args)
    {
            new Main();
    }
   
    public Main()
    {
            InitDisplay();
            mainLoop();
    }
   
    private void InitDisplay()
    {
            try
            {
                    Display.setDisplayMode(new DisplayMode(800,600));
                    Display.setTitle("LWJGL Picking");
                    Display.create();
            }catch(Exception e) { }
           
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(40f, 800/600f, 0.001f, 400f);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glEnable(GL11.GL_DEPTH_TEST);  
    }
   
    public static void render()
    {
            GL11.glLoadName(1);
            GL11.glBegin(GL11.GL_QUADS);
                    GL11.glColor3f(1, 1,0);  
                    GL11.glVertex3f(-5,-5,-80);
                    GL11.glVertex3f(5,-5,-80);
                    GL11.glVertex3f(5,5,-80);
                    GL11.glVertex3f(-5,5,-80);
            GL11.glEnd();
           
            GL11.glLoadName(3);
            GL11.glBegin(GL11.GL_QUADS);
                    GL11.glColor3f(1, 0,1);  
                    GL11.glVertex3f(-10,-10,-80);
                    GL11.glVertex3f(0,-10,-80);
                    GL11.glVertex3f(0,0,-80);
                    GL11.glVertex3f(-10,0,-80);
            GL11.glEnd();
    }
   
    private void select(int x, int y )
    {
            // The selection buffer
            IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
            int buffer[] = new int[256];
           
            IntBuffer vpBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
            // The size of the viewport. [0] Is <x>, [1] Is <y>, [2] Is <width>, [3] Is <height>
                int[] viewport = new int[4];
           
            // The number of "hits" (objects within the pick area).
            int hits;
            // Get the viewport info
                GL11.glGetInteger(GL11.GL_VIEWPORT, vpBuffer);
                vpBuffer.get(viewport);
           
            // Set the buffer that OpenGL uses for selection to our buffer
            GL11.glSelectBuffer(selBuffer);
           
            // Change to selection mode
            GL11.glRenderMode(GL11.GL_SELECT);
           
            // Initialize the name stack (used for identifying which object was selected)
            GL11.glInitNames();
            GL11.glPushName(0);
           
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
           
            /*  create 5x5 pixel picking region near cursor location */
            GLU.gluPickMatrix( (float) x, (float) y, 5.0f, 5.0f,IntBuffer.wrap(viewport));
           
            GLU.gluPerspective(40f, 800/600f, 0.001f, 400f);
            render();
            GL11.glPopMatrix();
            // Exit selection mode and return to render mode, returns number selected
            hits = GL11.glRenderMode(GL11.GL_RENDER);
            System.out.println("hits: " + hits);
           
            selBuffer.get(buffer);
                // Objects Were Drawn Where The Mouse Was
                if (hits > 0) {
                      // If There Were More Than 0 Hits
                      int choose = buffer[3]; // Make Our Selection The First Object
                      int depth = buffer[1]; // Store How Far Away It Is
                      for (int i = 1; i < hits; i++) {
                            // Loop Through All The Detected Hits
                            // If This Object Is Closer To Us Than The One We Have Selected
                            if (buffer[i * 4 + 1] <  depth) {
                                  choose = buffer[i * 4 + 3]; // Select The Closer Object
                                  depth = buffer[i * 4 + 1]; // Store How Far Away It Is
                            }
                      }
                      System.out.println("Chosen: " + choose);
                }
    }
   
   
    private void mainLoop()
    {
            while(!Display.isCloseRequested())
            {
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glLoadIdentity();
                   
                    render();
                   
                    Display.update();
                    Display.sync(60);
                   
                    if(Mouse.isButtonDown(0))
                    {
                            select(Mouse.getX(),Mouse.getY());
                    }
            }
    }
}