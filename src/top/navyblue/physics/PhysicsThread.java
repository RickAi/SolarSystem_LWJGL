package top.navyblue.physics;

import top.navyblue.main.SolarSystemMain;
import top.navyblue.objects.SolarSystem;

public class PhysicsThread extends Thread {
	
	public boolean error = false;
	private final SolarSystem ss;
	
	public PhysicsThread(SolarSystem ss) {
		super("Physics");
		this.ss = ss;
	}
	
	@Override
	public void run() {
		try{
			long utime = System.currentTimeMillis();
			while(true){
				long time = System.currentTimeMillis();
				if(SolarSystemMain.isRunning){
					ss.updatePhysics();
					SolarSystemMain.physTicked = true;
					if (System.currentTimeMillis() - utime >= 1000) {
						utime = System.currentTimeMillis();
					}
				}
				
				long sleep = time + 49 - System.currentTimeMillis();
				if(sleep>0)
					Thread.sleep(sleep);
				else
					System.out.println(String.format("Physics is updating too slow. Missed %d ms", -sleep));
			}
		}catch(Throwable th){
			error = true;
			th.printStackTrace();
		}
	}
	
}
