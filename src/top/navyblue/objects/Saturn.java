package top.navyblue.objects;

import top.navyblue.basic.IHasRings;
import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.TextureUtil;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Saturn extends SpaceModel implements IHasRings{

	private final TextureUtil rings = new TextureUtil("saturn_rings.png");
	
	private final PhysicsProvider physics;
	
	public Saturn(Sun sun) {
		super("saturn.png", 1.7f);
		this.physics = new RotationPhysicsProvider(sun, 45, .004f, -5f);
	}

	@Override
	public TextureUtil getRingsTexture() {
		return rings;
	}

	@Override
	public float getRingsSize() {
		return 10f;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
	
	
}
