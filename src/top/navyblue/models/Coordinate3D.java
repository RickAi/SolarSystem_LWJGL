package top.navyblue.models;

public class Coordinate3D {
	
	public float x, y, z, rot;
	
	public Coordinate3D(float x, float y, float z, float rot) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rot = rot;
	}
	
	public Coordinate3D(){ 
		this(0, 0, 0, 0);
	}
}
