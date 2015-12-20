package top.navyblue.objects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import top.navyblue.basic.SpaceModel;
import top.navyblue.main.SolarSystemMain;
import top.navyblue.physics.PhysicsProvider;
import top.navyblue.physics.RotationPhysicsProvider;

public class AnonymousPlanet extends SpaceModel {
	
	private final PhysicsProvider physics;
	private static Random generator = new Random();
	private static HashMap<Integer, List<Float>> planetAttributes = new HashMap<Integer, List<Float>>();
	
	static{
		planetAttributes.put(0, Arrays.asList(10f, .04f, -5f));
		planetAttributes.put(1, Arrays.asList(14f, .041f, -5f));
		planetAttributes.put(2, Arrays.asList(19f, 0.01f, -5f));
		planetAttributes.put(3, Arrays.asList(26f, .0053f, -5f));
		planetAttributes.put(4, Arrays.asList(35f, .005f, -5f));
		planetAttributes.put(5, Arrays.asList(45f, .004f, -5f));
		planetAttributes.put(6, Arrays.asList(55f, .0035f, -5f));
		planetAttributes.put(7, Arrays.asList(65f, .003f, -5f));
	}

	public AnonymousPlanet(SpaceModel model, float size) {
		super(generateRandomTextrue(), generateRandomSize(size));
		boolean aroundSun = model instanceof Sun;
		if(aroundSun){
			// If add secondary planet for the sun, the attribute will be same as earth.
			List<Float> attrs = generateAttr();
			this.physics = new RotationPhysicsProvider(model, attrs.get(0), attrs.get(1), attrs.get(2));
		} else{
			// If not, the speed will be random but the radius will be fixed.
			float[] params = generateParams(model);
			this.physics = new RotationPhysicsProvider(model, 3.4f, params[1], params[2]);
			((RotationPhysicsProvider)this.physics).tilt = params[3];
		}
	}

	@Override
	public PhysicsProvider getPhysics() {
		return physics;
	}
	
	// I have write this method for the random radis, speed, speedself
	// However, it is hard to control the collision
	// Therefore, I give fix radius
	private float[] generateParams(SpaceModel model){
		// radius, speed, speedSelf, tilt
		RotationPhysicsProvider pp = (RotationPhysicsProvider) model.getPhysics();
		double radius = (Math.abs(generator.nextDouble()) * 100 % pp.radius)/3 + 1;
		
		double speed = (Math.abs(generator.nextDouble()) * 100 % pp.speed) + 0.2;
		
		double speedself = (Math.abs(generator.nextDouble()) * 100 % pp.speedSelf) + 0.5;
		
		double tilt = Math.abs(generator.nextDouble()) * 100 % 3.0 - 2.0 + pp.tilt;
		
		return new float[]{(float) (radius), (float) (speed),
				(float) (speedself), (float) (tilt)};
	}
	
	private static String generateRandomTextrue(){
		int nextInt = Math.abs(generator.nextInt());
		HashMap<Integer, String> pureColors = SolarSystemMain.pureColors;
		int number = pureColors.size()+1;
		String name = pureColors.get(Integer.valueOf(nextInt % number));
		return name;
	}
	
	private static List<Float> generateAttr(){
		int nextInt = Math.abs(generator.nextInt());
		int number = planetAttributes.size()+1;
		List<Float> list = planetAttributes.get(Integer.valueOf(nextInt % number));
		return list;
	}
	
	private static float generateRandomSize(float size){
		// size
		int divider = Math.abs(generator.nextInt(100)) % 3 + 3;
		return size/divider;
	}
	
}
