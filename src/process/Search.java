package process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pipeline.ConsolePipeline;
import pipeline.FilePipeline;
import search.Searcher;

public class Search implements Processor {

	private static final String FILE_PATH = "F:\\Clawer\\LNU_BBS_NEED\\ѧϰ����\\329-���㿼BEC����ͬѧ������һ�¾�����.txt";

	private String keyWord = "������ѧ";

	SearchKeyWord skw = new SearchKeyWord();
	SearchHtmlKeyWord shkw = new SearchHtmlKeyWord();
	ExtraFiles ef = new ExtraFiles();
	List<Integer> position = new ArrayList<Integer>();
	
	FilePipeline fp = new FilePipeline("F:\\Clawer\\search\\bbs_record.txt");
	
	@Override
	public void process(File file) {

		if (file.isDirectory()) {
			ef.getExtraFiles(file);
		} else if (file.isFile() && file.canRead()) {
			position = shkw.searchKeyWord(file, keyWord);
			if (position.size() > 0) {
				fp.process(file, position);
			}
		}
	}

	public static void main(String[] args) {
		Searcher sch = new Searcher(new Search());
		File file = new File(FILE_PATH);
		if (file.exists()) {
			sch.startFile(file)
				.addPipeline(new ConsolePipeline())
				.thread(5)
				.run();
		}
	}
}
