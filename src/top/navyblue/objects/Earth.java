package top.navyblue.objects;

import top.navyblue.basic.IHasAtmosphere;
import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.TextureUtil;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Earth extends SpaceModel implements IHasAtmosphere{

	private final TextureUtil clouds = new TextureUtil("clouds.png");
	
	private final PhysicsProvider physics;
	
	public Earth(Sun sun) {
		super("earth.png", 1.2f);
		this.physics = new RotationPhysicsProvider(sun, 19, .01f, -5f);
	}
	
	@Override
	public TextureUtil getAtmosphereTexture() {
		return clouds;
	}

	@Override
	public float getAtmosphereSize() {
		return 1.26f;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
