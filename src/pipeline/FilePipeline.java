package pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FilePipeline implements Pipeline {

	public String path;

	public static String pathSeperator = "\\";

	static {
		String property = System.getProperties().getProperty("file.separator");
		if (property != null) {
			pathSeperator = property;
		}
	}

	public FilePipeline(String path) {
		setPath(path);
	}

	@Override
	public void process(File file) {
		
	}
	
	public void process(File file, List<Integer> list) {
		 try {
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
			 		getPath(),true),"gbk"));
			
			printWriter.println(file.getAbsolutePath());
			printWriter.print("每一个的起始位置：");
			for (Integer integer : list) {
				printWriter.print(integer + " ");
			}
			printWriter.println();
			printWriter.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	private void setPath(String path) {
		this.path = path;
	}

	private String getPath() {
		return path;
	}

}
