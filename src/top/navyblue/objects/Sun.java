package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.StaticPhysicsProvider;

public class Sun extends SpaceModel {
	
	public Sun() {
		super("sun.png", 5F);
		this.ignoreLight = true;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return StaticPhysicsProvider.instance;
	}
	
}
