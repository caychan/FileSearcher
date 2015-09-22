package f_process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchHtmlKeyWord {
	
	/**
	 * ���ҹؼ��֣����ذ����ؼ������ھ��ӵ�List
	 * @param content Ҫ���ҵ��ַ���
	 * @param key �ؼ���
	 * @return ���ذ����ؼ������ھ��ӵ�List
	 */
	public List<String> searchKeyWordReturnSentence(File file, String keyWord) {
		List<String> sentence = new ArrayList<String>();
		String content = readFile(file);
		sentence = searchWordInSentence(file, content, keyWord);
			
		return sentence;
	}
	
	/**
	 * ���ҹؼ���λ�ã����ذ���λ�õ�List
	 * @param content Ҫ���ҵ��ַ���
	 * @param key �ؼ���
	 * @return λ��List
	 */
	public List<Integer> searchKeyWordReturnPosition(File file, String keyWord) {
		List<Integer> position = new ArrayList<Integer>();
		String content = readFile(file);
		position = searchWordPosition(content, keyWord);
		
		return position;
	}
	
	/**
	 * ���ҹؼ���λ��
	 * @param content Ҫ���ҵ��ַ���
	 * @param key �ؼ���
	 * @return λ��List
	 */
	private List<Integer> searchWordPosition(String content, String key){
		List<Integer> position = new ArrayList<Integer>();
		Pattern p = Pattern.compile(key);
		Matcher m = p.matcher(content);
		while(m.find()){
			position.add(m.start());
		}
		return position;
	}
	
	/**
	 * ���ҹؼ������ڵľ���
	 * @param content	Ҫ���ҵ��ַ���
	 * @param key	�ؼ���
	 * @return �ؼ������ھ��ӵļ���
	 */
	private List<String> searchWordInSentence(File file, String content, String key){
		List<String> sentence = new ArrayList<String>();
		Pattern pattern = Pattern.compile(key);
		Matcher matcher = pattern.matcher(content);
		int position = 0;
		int start, end;
		int length = content.length();
		sentence.add(file.getAbsolutePath());
		while(matcher.find()){
			position = matcher.start();
			//���ӵ�ǰλ�õ�ǰ200���ַ�λ�ÿ�ʼ������һ�����������ڵ�λ��
			start = position >= 200 ? position - 200 : 0;
			end = position < length ? position + 1 : position;
			
			String subString = content.substring(start, end);
			
			//������Ӣ�ĵľ�ţ���̾�ţ��ʺţ��ֺţ�ð�ţ����ۺźͿհ�
			//Pattern p = Pattern.compile("[��������������.?:;!-\\s]");
			Pattern p = Pattern.compile("[��������?\\s]");
			Matcher m = p.matcher(subString);
			//��ʼ��һ��lastPubctuation����Ϊ����������Ҳ����᷵��-1�����������Ϊ�˰�ȫ����ʼֵ��Ϊ��-2
			int lastPunctuation = -2;
			while (m.find()) {
				//��¼���һ��ƥ���λ��
				lastPunctuation = m.start();
			}
			if (lastPunctuation >= 0) {
				//����ҵ��ˣ���ʱ��lastPunctuation��subString�е�λ�ã��ĳ�content�е�λ�á�
				//+1����Ϊ����ʱָ����Ǳ�����
				lastPunctuation = position - (subString.length() - lastPunctuation) + 1;
			}
			if (lastPunctuation < 0 ) {
				//���û���ҵ�����Ҫ���λ�ã�����Ϊ��ǰλ�õ�ǰ100������0
				lastPunctuation = position >= 100 ? position - 100 : 0;
			}

			end = (length - position) >= 200 ? position + 200 : length;
			m = p.matcher(content.substring(position, end));
			int nextPunctuation = -2;
			if (m.lookingAt()) {
				nextPunctuation = m.start();
			}
			if (nextPunctuation >= 0) {
				nextPunctuation = nextPunctuation + position;
			}
			if (nextPunctuation < 0 ) {
				//���û���ҵ�����Ҫ���λ�ã�����Ϊ��ǰλ�õĺ�100�����ߵ���β
				//-1����ΪString.substring�ĵڶ������������lengthһ���󣬻�ָ��Խ��
				nextPunctuation = (length - position) > 100 ? position + 100 : length - 1;
			}

			sentence.add(content.substring(lastPunctuation + 1,nextPunctuation-1));
		}
		return sentence;
	}
	
	
	
	//ȥ�����е�html��ǩ
	private String removeTags(String content){
		String regex = "<.*?>";
		content = content.replaceAll(regex, "");
//		���������ֵ��������ϵĿհ�ȥ��
		regex = "\\s{2,}";
		content = content.replaceAll(regex, " ");
		
		return content;
	}
	
	private String readFile(File file){
		BufferedReader br = null;
		String str = null;
		try {
			String line = null;
			StringBuffer sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
	        while ((line = br.readLine()) != null){
	        	sb.append(line);
	        } 
	        
	        str = sb.toString();
	        str = removeTags(str);	
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		return str;
	}

}
