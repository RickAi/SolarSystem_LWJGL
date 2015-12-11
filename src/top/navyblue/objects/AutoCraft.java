package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.Constants;
import top.navyblue.managers.RenderManager;
import top.navyblue.physics.HorizontalPhysicsProvider;
import top.navyblue.physics.PhysicsProvider;

public class AutoCraft extends SpaceModel {
	
	public static final int FLY_X = 0;
	public static final int FLY_Y = 1;
	public static final int FLY_Z = 2;
	
	private PhysicsProvider physicsProvider;

	public AutoCraft(int flyType) {
		super("craft.png", 0.5F);
		physicsProvider = new HorizontalPhysicsProvider(Constants.AUTOCRAFT_SPEED, flyType);
	}
	
	@Override
	public void init() throws Exception {
		modelTexture.prepare();
		glLists.add(RenderManager.prepareSphere(modelTexture.id, size));
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physicsProvider;
	}

	
}
