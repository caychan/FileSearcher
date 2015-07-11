package process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pipeline.ConsolePipeline;
import search.Searcher;

public class Search implements Processor {

	private static final String FILE_PATH = "F:\\Clawer\\search";

	private String keyWord = "ÁÉÄþ´óÑ§";

	ExtraFiles ef = new ExtraFiles();
	SearchKeyWord skw = new SearchKeyWord();
	List<Integer> position = new ArrayList<Integer>();

	@Override
	public void process(File file) {

		System.out.println("main function");
		System.out.println(file.getAbsolutePath());

		if (file.isDirectory()) {
			System.out.println("is directory");
			ef.getExtraFiles(file);
		} else if (file.isFile() && file.canRead()) {
			System.out.println("is file");
			position = skw.searchKeyWord(file, keyWord);
			System.out.println(position.size());
			for (Integer lst : position) {
				System.out.print(lst + " ");
			}

		}
	}

	public static void main(String[] args) {

		File file = new File(FILE_PATH);

		Searcher sch = new Searcher(new Search());
		sch.thread(3).startFile(file).addPipeline(new ConsolePipeline()).run();
	}
}
