package scheduler;

import org.apache.http.annotation.ThreadSafe;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ThreadSafe
public class QueueScheduler {  //��Ϊ�ļ�·�������ظ������Բ�Ҫȥ�صĲ���
							
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public void pushWhenNoDuplicate(String request) {
        queue.add(request);
    }

    public synchronized String poll() {
        return queue.poll();
    }
    
    public int getLeftRequestsCount() {
    	return queue.size();
    }

}
