package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.RenderManager;
import top.navyblue.physics.KeyListenProvider;
import top.navyblue.physics.PhysicsProvider;

public class Craft extends SpaceModel{
	
	private PhysicsProvider physicsProvider;

	public Craft() {
		super("craft.png", 2.0F);
		physicsProvider = new KeyListenProvider();
	}

	@Override
	public void init() throws Exception {
		modelTexture.prepare();
		glLists.add(RenderManager.prepareSquare(modelTexture.id, size));
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physicsProvider;
	}
	
}
