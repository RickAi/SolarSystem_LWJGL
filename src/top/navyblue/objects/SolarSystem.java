package top.navyblue.objects;

import java.util.ArrayList;
import java.util.List;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsThread;

public class SolarSystem {
	
	public final List<SpaceModel> models = new ArrayList<>();
	public final PhysicsThread physics = new PhysicsThread(this);
	private Craft craft;
	
	public SolarSystem() {
		models.add(new Stars());
		Sun sun = new Sun();
		models.add(sun);
		models.add(new Mercury(sun));
		models.add(new Venus(sun));
		Earth earth = new Earth(sun);
		models.add(earth);
		models.add(new Moon(earth));
		models.add(new Mars(sun));
		models.add(new Jupiter(sun));
		models.add(new Saturn(sun));
		models.add(new Uranus(sun));
		models.add(new Neptune(sun));
		craft = new Craft();
		models.add(craft);
	}
	
	public void init() throws Exception{
		physics.start();
		for(SpaceModel model : models)
			model.init();
	}
	
	public void render(float framePart) {
		for(SpaceModel model : models)
			model.render(framePart);
	}
	
	public void updatePhysics() {
		for(SpaceModel model : models)
			model.update();
	}
	
	public Craft getCraft(){
		return craft;
	}
	
}
