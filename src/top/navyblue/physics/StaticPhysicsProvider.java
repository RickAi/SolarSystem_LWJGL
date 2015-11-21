package top.navyblue.physics;

import top.navyblue.models.Coordinate3D;

public class StaticPhysicsProvider extends PhysicsProvider{

	public static StaticPhysicsProvider instance = new StaticPhysicsProvider();
	
	@Override
	public Coordinate3D updatePosition() {
		return null;
	}
	
}
