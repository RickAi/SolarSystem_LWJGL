package top.navyblue.physics;

import top.navyblue.basic.SpaceModel;
import top.navyblue.main.SolarSystemMain;
import top.navyblue.models.Coordinate3D;

public class RotationPhysicsProvider extends PhysicsProvider{

	public final SpaceModel center;
	private Coordinate3D coord = new Coordinate3D();
	
	public final float radius, speed, speedSelf;
	private float angle = 0;
	public float tilt = 0;
	
	public RotationPhysicsProvider(SpaceModel center, float radius, float speed, float speedSelf) {
		this.center = center;
		this.radius = radius;
		this.speed = speed;
		this.speedSelf = speedSelf;
	}
	
	@Override
	public Coordinate3D updatePosition() {
		if (!SolarSystemMain.pauseMoment){
			angle+=speed;
			coord.x = center.posX + (float)Math.cos(angle)*radius;
			coord.y = center.posY + (float)Math.sin(angle)*radius;
			coord.z = center.posZ + (float)Math.cos(angle)*radius*tilt;
			coord.rot+=speedSelf;
		}
		return coord;
	}

}
