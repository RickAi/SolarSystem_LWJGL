package top.navyblue.managers;

import java.nio.FloatBuffer;


import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.glu.Sphere;


public class RenderManager {
	
	private static Sphere sphere;
	
	public static void setupLighting() {
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(0.0f).put(0.0f).flip();
         
        FloatBuffer whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
         
        FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.4f).put(0.4f).put(0.4f).put(1.0f).flip();
		
		glShadeModel(GL_SMOOTH);
		glMaterial(GL_FRONT, GL_SPECULAR, whiteLight);
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
         
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);
         
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	}
	
	public static void setupView(int width, int height) {
		glPopAttrib();
		glPushAttrib(GL_ENABLE_BIT);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		setupLighting();
		
		glClearDepth(1D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glDepthFunc(GL_LEQUAL);
	    glViewport(0, 0, width, height);
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    gluPerspective(60F, (float)width / height, 0.1F, 1000F);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	}
	
	public static void init(int width, int height) {
		setupView(width, height);
		sphere = new Sphere();
		sphere.setDrawStyle(GLU_FILL);
		sphere.setTextureFlag(true);
		sphere.setNormals(GLU_SMOOTH);
	}
	
	public static int prepareSphere(int texId, float size) {
		int list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glBindTexture(GL_TEXTURE_2D, texId);
		sphere.draw(size, Constants.SPHERE_SLICES, Constants.SPHERE_SLICES);
		glEndList();
		return list;
	}
	
	public static int prepareSquare(int texId, float size) {
		int list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		glDisable(GL_CULL_FACE);
		glBindTexture(GL_TEXTURE_2D, texId);
		glTranslatef(-size/2, -size/2, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
        glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(size, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(size, size);
        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(0.0f, size);
		glEnd();
		
		glEnable(GL_CULL_FACE);
		glEndList();
		return list;
	}
	
}
