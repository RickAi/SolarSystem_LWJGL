package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Jupiter extends SpaceModel{
	
	private final PhysicsProvider physics;
	
	public Jupiter(Sun sun) {
		super("jupiter.png", 1.8f);
		this.physics = new RotationPhysicsProvider(sun, 35, .005f, -5f);
	}
	
	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
