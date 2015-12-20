package top.navyblue.objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import top.navyblue.basic.SpaceModel;
import top.navyblue.physics.PhysicsThread;

public class SolarSystem {

	public final List<SpaceModel> models = new ArrayList<>();
	public SpaceModel selectedModel;
	public int selectedIndex = 0;
	public List<SpaceModel> selectModels = new ArrayList<SpaceModel>();
	public final PhysicsThread physics = new PhysicsThread(this);

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

		selectedModel = sun;
		initSelectModels();
	}

	private void initSelectModels() {
		selectModels.addAll(models);
		selectModels.remove(0);
	}
	
	public void init() throws Exception {
		physics.start();
		for (SpaceModel model : models)
			model.init();
		selectCurrent(0);
	}
	
	public void addSecondaryPlanet() throws Exception{
		AnonymousPlanet planet = new AnonymousPlanet(selectedModel, selectedModel.size);
		planet.init();
		models.add(planet);
		selectModels.add(planet);
	}
	
	public void selectNext() throws Exception {
		unselectCurrent();
		
		increaseIndex();

		selectCurrent(selectedIndex);
	}

	public void selectBefore() throws Exception {
		unselectCurrent();

		decreaseIndex();

		selectCurrent(selectedIndex);
	}
	
	public void selectPlanet(int toSelectIndex){
		unselectCurrent();
		selectCurrent(toSelectIndex);
	}
	
	public void selectCurrent(int toSelectIndex){
		selectedModel = selectModels.get(toSelectIndex);
		selectedModel.currentMode = SpaceModel.MODE_SELECT;
		selectedModel.updateSize();
	}
	
	private void unselectCurrent(){
		selectedModel.currentMode = SpaceModel.MODE_NORMAL;
		selectedModel.updateSize();
	}
	
	public void render(float framePart) {
		for (int i = 0; i < models.size(); i++) {
			SpaceModel model = models.get(i);
			GL11.glLoadName(i);
			model.render(framePart);
		}
	}

	public void updatePhysics() {
		for (SpaceModel model : models)
			model.update();
	}

	private void increaseIndex() {
		int size = selectModels.size();
		if (selectedIndex == size - 1) {
			selectedIndex = 0;
		} else {
			selectedIndex++;
		}
	}

	private void decreaseIndex() {
		int size = selectModels.size();
		if (selectedIndex == 0) {
			selectedIndex = size - 1;
		} else {
			selectedIndex--;
		}
	}

}
