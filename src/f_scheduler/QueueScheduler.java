package f_scheduler;

import org.apache.http.annotation.ThreadSafe;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ThreadSafe
public class QueueScheduler {  //��Ϊ�ļ�·�������ظ������Բ�Ҫȥ�صĲ���
							
    private BlockingQueue<File> queue = new LinkedBlockingQueue<File>();

    public void push(File file) {
        queue.add(file);
    }

    public synchronized File poll() {
        return queue.poll();
    }
    
    public int getLeftRequestsCount() {
    	return queue.size();
    }

}
