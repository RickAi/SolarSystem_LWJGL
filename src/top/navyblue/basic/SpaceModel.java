package top.navyblue.basic;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

import top.navyblue.managers.RenderManager;
import top.navyblue.managers.TextureUtil;
import top.navyblue.models.Coordinate3D;
import top.navyblue.physics.PhysicsProvider;

public abstract class SpaceModel {
	
	public float prevPosX = 0, posX = 0, prevPosY = 0, posY = 0, prevPosZ = 0, posZ = 0;
	public float prevRot = 0, rot = 0;
	
	public final float size;
	public final TextureUtil modelTexture;
	protected List<Integer> glLists = new ArrayList<>();
	
	public final boolean hasAtmosphere = this instanceof IHasAtmosphere;
	public final boolean hasRings = this instanceof IHasRings;
	
	public boolean ignoreLight = false, renderInside = false;
	public static final int MODE_NORMAL = 0, MODE_SELECT = 1;
	public int currentMode = MODE_NORMAL;
	
	private TextureUtil atmTexture;
	private TextureUtil ringTexture;
	private IHasAtmosphere atm;
	private IHasRings rng;
	
	public SpaceModel(String textureName, float size) {
		this.modelTexture = new TextureUtil(textureName);
		this.size = size;
	}
	
	public abstract PhysicsProvider getPhysics();
	
	public void update() {
		Coordinate3D pos = getPhysics().updatePosition();
		if(pos!=null){
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			prevRot = rot;
			
			posX = pos.x;
			posY = pos.y;
			posZ = pos.z;
			rot = pos.rot;
		}
	}
	
	public void init() throws Exception{
		modelTexture.prepare();
		glLists.add(RenderManager.prepareSphere(modelTexture.id, size));
		if(hasAtmosphere){
			atm = (IHasAtmosphere)this;
			atmTexture = atm.getAtmosphereTexture();
			atmTexture.prepare();
			glLists.add(RenderManager.prepareSphere(atmTexture.id, atm.getAtmosphereSize()));
		} 
		if(hasRings){
			rng = (IHasRings)this;
			ringTexture = rng.getRingsTexture();
			ringTexture.prepare();
			glLists.add(RenderManager.prepareSquare(ringTexture.id, rng.getRingsSize()));
		} 
	}
	
	public void updateSize(){
		glLists.clear();
		float cross;
		if(currentMode == MODE_NORMAL){
			cross = 1.0f;
		} else{
			cross = 1.5f;
		}
		
		glLists.add(RenderManager.prepareSphere(modelTexture.id, size*cross));
		if(hasAtmosphere){
			glLists.add(RenderManager.prepareSphere(atmTexture.id, atm.getAtmosphereSize()*cross));
		}
		if(hasRings){
			glLists.add(RenderManager.prepareSquare(ringTexture.id, rng.getRingsSize()*cross));
		}
	}
	
	public void render(float framePart) {
		float x = prevPosX + (prevPosX - posX) * framePart;
		float y = prevPosY + (prevPosY - posY) * framePart;
		float z = prevPosZ + (prevPosZ - posZ) * framePart;
		
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(-x).put(-y).put(-z).put(0.0f).flip();
		float rot = prevRot + (prevRot - this.rot) * framePart;
		
		glPushMatrix();
		if(renderInside)
			glDisable(GL_CULL_FACE);
		if(ignoreLight)
			glDisable(GL_LIGHTING);
		else
			glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		
		// change the location and the rotate angle
		glTranslatef(x, y, z);
		glRotatef(rot, 0, 0, 1);
		
		// call the display list
		for(int list : glLists)
			glCallList(list);
		
		if(renderInside)
			glEnable(GL_CULL_FACE);
		
		glPopMatrix();
	}
	
}
