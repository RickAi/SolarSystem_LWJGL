package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Mercury extends SpaceModel{
	
	private final PhysicsProvider physics;
	
	public Mercury(Sun sun) {
		super("mercury.png", .6f);
		this.physics = new RotationPhysicsProvider(sun, 10, .04f, -5f);
	}
	
	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
