package process;

import java.io.File;

import scheduler.QueueScheduler;


public class ExtraFiles{
	
	private QueueScheduler scheduler = new QueueScheduler();
	
	public void getExtraFiles(File file){
		
		File[] files = file.listFiles();
		synchronized (scheduler) {
			System.out.println("add extra files");
			for (File f : files) {			
				scheduler.push(f);
			}
		}
	}
}
