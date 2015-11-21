package top.navyblue.physics;

import top.navyblue.models.Coordinate3D;

public class KeyListenProvider extends PhysicsProvider {
	
	public Coordinate3D currentCoord;
	
	public KeyListenProvider(){
		currentCoord = new Coordinate3D();
	}

	@Override
	public Coordinate3D updatePosition() {
		return currentCoord;
	}
	
	public void listen(int internX, int internY, int internZ, int internRot){
		currentCoord.x += internX;
		currentCoord.y += internY;
		currentCoord.z += internZ;
		currentCoord.rot += internRot;
	}

}
