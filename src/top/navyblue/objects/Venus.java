package top.navyblue.objects;

import top.navyblue.basic.IHasAtmosphere;
import top.navyblue.basic.SpaceModel;
import top.navyblue.managers.TextureUtil;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class Venus extends SpaceModel implements IHasAtmosphere{

private final TextureUtil clouds = new TextureUtil("venus_atm.png");
	
	private final PhysicsProvider physics;
	
	public Venus(Sun sun) {
		super("venus.png", 1f);
		this.physics = new RotationPhysicsProvider(sun, 14, .041f, -5f);
	}

	@Override
	public TextureUtil getAtmosphereTexture() {
		return clouds;
	}

	@Override
	public float getAtmosphereSize() {
		return 1.08f;
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
}
