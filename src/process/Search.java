package process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pipeline.ConsolePipeline;
import pipeline.FilePipeline;
import search.Searcher;

public class Search implements Processor {

	private static final String FILE_PATH = "F:\\Clawer\\nen - use";

	private String keyWord = "ÁÉÄþ´óÑ§";

	SearchKeyWord skw = new SearchKeyWord();
	List<Integer> position = new ArrayList<Integer>();
	
	static Searcher sch = new Searcher(new Search());
	
	FilePipeline fp = new FilePipeline("F:\\Clawer\\search\\record.txt");
	
	@Override
	public void process(File file) {

		if (file.isDirectory()) {
			sch.getExtraFiles(file);
		} else if (file.isFile() && file.canRead()) {
			position = skw.searchKeyWord(file, keyWord);
			if (position.size() > 0) {
				fp.process(file, position);
			}
		}
	}

	public static void main(String[] args) {

		File file = new File(FILE_PATH);
		if (file.exists()) {
			sch.thread(5).startFile(file)
			.addPipeline(new ConsolePipeline())
			.run();
		}
	}
}
