package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Neptune extends SpaceModel{
	
	private final PhysicsProvider physics;
	
	public Neptune(Sun sun) {
		super("neptune.png", 1.5f);
		this.physics = new RotationPhysicsProvider(sun, 65, .003f, -5f);
	}
	
	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
