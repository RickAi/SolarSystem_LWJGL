package top.navyblue.objects;

import java.util.HashMap;
import java.util.Random;

import top.navyblue.basic.SpaceModel;
import top.navyblue.main.SolarSystemMain;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class AnonymousPlanet extends SpaceModel {
	
	private final PhysicsProvider physics;
	private static Random generator = new Random(100);

	public AnonymousPlanet(SpaceModel model, float size) {
		super(generateRandomTextrue(), generateRandomSize(size));
		float[] params = generateParams(model);
//		this.physics = new RotationPhysicsProvider(model, params[0], params[1], params[2]);
		this.physics = new RotationPhysicsProvider(model, 3.4f, .5f, -.1f);
		((RotationPhysicsProvider)this.physics).tilt = params[3];
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
	private float[] generateParams(SpaceModel model){
		// radius, speed, speedSelf, tilt
		RotationPhysicsProvider pp = (RotationPhysicsProvider) model.getPhysics();
		double radius = (generator.nextDouble() * 100 % pp.radius)/2 + 1;
		
		double speed = (generator.nextDouble() * 100 % pp.speed) * 3  + 1;
		
		double speedself = generator.nextDouble() * 100 % pp.speedSelf + 0.5;
		
		double tilt = generator.nextDouble() * 100 % 3.0 - 2.0 + pp.tilt;
		
		return new float[]{(float) (radius), (float) (speed),
				(float) (speedself), (float) (tilt)};
	}
	
	private static String generateRandomTextrue(){
		int nextInt = generator.nextInt();
		HashMap<Integer, String> pureColors = SolarSystemMain.pureColors;
		int number = pureColors.size();
		String name = pureColors.get(Integer.valueOf(nextInt % number));
		return name;
	}
	
	private static float generateRandomSize(float size){
		// size
		return size/3;
	}
	
}
