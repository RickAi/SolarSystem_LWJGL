package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Moon extends SpaceModel{

	private final PhysicsProvider physics;
	
	public Moon(Earth earth) {
		super("moon.png", .35f);
		this.physics = new RotationPhysicsProvider(earth, 2.4f, .1f, -.1f);
		((RotationPhysicsProvider)this.physics).tilt = -0.65f;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}

}
