package top.navyblue.objects;

import top.navyblue.basic.IHasRings;
import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.TextureUtil;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Uranus extends SpaceModel implements IHasRings{

	private final TextureUtil rings = new TextureUtil("uranus_rings.png");
	
	private final PhysicsProvider physics;
	
	public Uranus(Sun sun) {
		super("uranus.png", 1.4f);
		this.physics = new RotationPhysicsProvider(sun, 55, .0035f, -5f);
	}

	@Override
	public TextureUtil getRingsTexture() {
		return rings;
	}

	@Override
	public float getRingsSize() {
		return 8f;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
