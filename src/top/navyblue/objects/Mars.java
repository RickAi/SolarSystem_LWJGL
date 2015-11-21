package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Mars extends SpaceModel{
	
	private final PhysicsProvider physics;
	
	public Mars(Sun sun) {
		super("mars.png", .8f);
		this.physics = new RotationPhysicsProvider(sun, 26, .0053f, -5f);
	}
	
	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
