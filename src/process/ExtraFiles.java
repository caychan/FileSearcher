package process;

import java.io.File;

import scheduler.QueueScheduler;
import search.Searcher;


public class ExtraFiles{
	
	QueueScheduler scheduler = Searcher.getScheduler();
	
	public void getExtraFiles(File file){
		
		File[] files = file.listFiles();
		synchronized (scheduler) {
			for (File f : files) {			
				scheduler.push(f);
			}
		}
	}
}
