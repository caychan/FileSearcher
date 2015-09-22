package f_process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import f_pipeline.ConsolePipeline;
import f_search.Searcher;

public class Search implements Processor {

	private static final String FILE_PATH = "F:\\Clawer\\LNU_TIEBA\\2005-08";
	private String keyWord = "������ѧ";
	
//	���洦����ļ���Ϣ��list�У���ǰ��ʹ��
	List<List<String>> lstFiles = new ArrayList<List<String>>();
	
	
	public static void main(String[] args) {
		Searcher sch = new Searcher(new Search());
		File file = new File(FILE_PATH);
		if (file.exists()) {
			sch.startFile(file)
			//��console����ʾ��ǰ������ļ�
			.addPipeline(new ConsolePipeline())
			//�������߳�ͬ������
			//.thread(5)
			.run();
		}
	}
	
	@Override
	public void process(File file) {

		if (file.isDirectory()) {
			ExtraFiles ef = new ExtraFiles();
			ef.getExtraFiles(file);
		} else if (file.isFile() && file.canRead()) {
			SearchHtmlKeyWord shkw = new SearchHtmlKeyWord();
			List<String> sentence = new ArrayList<String>();
			sentence = shkw.searchKeyWordReturnSentence(file, keyWord);
			if (sentence.size() > 1) {
				System.out.println("------" + file.getAbsolutePath());
				addInfoToList(sentence);
				for (String str : sentence) {
					System.out.println(str);
				}
			}
		}
	}

	
	private void addInfoToList(List<String> sentence){
		synchronized (lstFiles) {
			lstFiles.add(sentence);
		}
	}
	
	private void clearList(){
		synchronized (lstFiles) {
			lstFiles.clear();
		}
	}
	
}
