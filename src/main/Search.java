package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search {

	private static final String FILE_PATH = "F:\\Clawer\\nen";

	public static void main(String[] args) {

		File file = new File(FILE_PATH);
		File[] files = new File[1000];
		files = file.listFiles();
		for (File file2 : files) {
			System.out.println(file2.isDirectory());
		}

		String[] lst = new String[100];
		lst = file.list();
		List<String> ls = new ArrayList<String>();
		ls = Arrays.asList(lst);
		for (String string : ls) {

			System.out.println(string);
		}
	}

}
