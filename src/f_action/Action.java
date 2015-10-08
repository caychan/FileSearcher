package f_action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import f_pipeline.ConsolePipeline;
import f_process.Processor;
import f_process.RecordToFile;
import f_process.SearchHtmlKeyWord;
import f_process.Searcher;

public class Action implements Processor {

	private static final String FILE_PATH = "F:\\Clawer\\LNU_BBS_test\\";
	private String keyWord = "辽宁大学";
	static Searcher searcher = new Searcher(new Action());
	
	RecordToFile fwPipeline = new RecordToFile("F:\\Clawer\\LNU_TBBS\\record");
	List<String> sentence = new ArrayList<String>();
	
	public static void main(String[] args) {
		File file = new File(FILE_PATH);
		if (file.exists()) {
			searcher.startFile(file)
//			.setScheduler(new FileCacheScheduler(FILECACHEPATH))
			//在console中显示当前处理的文件
			.addPipeline(new ConsolePipeline())
			//.thread(5)
//			.setSleepTime(500)
			.run();
		}
	}
		


	@Override
	public void process(File file) {
		if (file.isDirectory()) {
			searcher.getExtraFiles(file);
		} else if (file.isFile() && file.canRead()) {
			SearchHtmlKeyWord shkw = new SearchHtmlKeyWord();
			sentence = shkw.searchKeyWordReturnSentence(file, keyWord);
			if (sentence.size() > 1) {
				System.out.println("------" + file.getAbsolutePath());
				for (String str : sentence) {
					System.out.println(str);
				}
			}
		}
	}
	
	
	public void stopSearcher(){
		searcher.stop();
	}
	
}
