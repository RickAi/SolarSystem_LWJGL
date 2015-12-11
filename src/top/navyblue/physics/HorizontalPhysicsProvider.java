package top.navyblue.physics;

import java.util.Random;

import top.navyblue.main.SolarSystemMain;
import top.navyblue.managers.Constants;
import top.navyblue.models.Coordinate3D;
import top.navyblue.objects.AutoCraft;

public class HorizontalPhysicsProvider extends PhysicsProvider {

	private Coordinate3D coord = new Coordinate3D();
	private int speed = 0;
	private int flyType;
	private int randomInt;

	public HorizontalPhysicsProvider(int speed, int flyType) {
		this.speed = speed;
		this.flyType = flyType;
		generateOther();
	}
	
	public void generateOther(){
		Random generator = new Random();
		randomInt = generator.nextInt(100) - 50;
	}

	@Override
	public Coordinate3D updatePosition() {
		if(SolarSystemMain.pauseMoment){
			return coord;
		}
		
		if (flyType == AutoCraft.FLY_X) {
			coord.x += speed;
			coord.y = randomInt;
			coord.z = randomInt;
		} else if (flyType == AutoCraft.FLY_Y) {
			coord.x = randomInt;
			coord.y += speed;
			coord.z = randomInt;
		} else if (flyType == AutoCraft.FLY_Z) {
			coord.x = randomInt;
			coord.y = randomInt;
			coord.z += speed;
		}

		coord.rot += speed * 2;

		if (coord.x >= Constants.DISPLAY_WIDTH+randomInt*5) {
			coord.x = 0;
			generateOther();
		}
		if (coord.y >= Constants.DISPLAY_WIDTH+randomInt*5) {
			coord.y = 0;
			generateOther();
		}
		if (coord.z >= Constants.DISPLAY_WIDTH+randomInt*5) {
			coord.z = 0;
			generateOther();
		}
		return coord;
	}

}
