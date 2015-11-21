package top.navyblue.objects;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.StaticPhysicsProvider;

public class Stars extends SpaceModel {

	public Stars(){
		super("stars.png", 101F);
		renderInside = true;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return StaticPhysicsProvider.instance;
	}

}
