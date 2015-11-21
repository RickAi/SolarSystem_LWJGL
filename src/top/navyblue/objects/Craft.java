package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.RenderManager;
import top.navyblue.models.Coordinate3D;
import top.navyblue.physics.KeyListenProvider;
import top.navyblue.physics.PhysicsProvider;

public class Craft extends SpaceModel{
	
	public Coordinate3D coord;
	private PhysicsProvider physicsProvider;

	public Craft() {
		super("craft.png", 3.0F);
		physicsProvider = new KeyListenProvider();
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void init() throws Exception {
		texture.prepare();
		glLists.add(RenderManager.prepareSquare(texture.id, size));
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physicsProvider;
	}
	
	
	
}
