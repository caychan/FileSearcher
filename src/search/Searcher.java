package search;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pipeline.ConsolePipeline;
import pipeline.Pipeline;
import process.Processor;
import scheduler.QueueScheduler;
import thread.CountableThreadPool;

public class Searcher implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ReentrantLock newFileLock = new ReentrantLock();

	private Condition newFileCondition = newFileLock.newCondition();

	private QueueScheduler scheduler = new QueueScheduler();
	
	private Pipeline pipeline;
	
	private Processor processor;
	
	private int threadNum = 1;

	private CountableThreadPool threadPool;
	
	private ExecutorService executorService;

	private int emptySleepTime = 30000;
	
	
    public Searcher(Processor processor) {
    	System.out.println("processor");
        this.processor = processor;
    }
    
    public Searcher startFile(File... files) {
        for (File file : files) {
            addFile(file);
        }
        signalNewFile();
        System.out.println("start file");
        return this;
    }
    
    private void addFile(File file){
        scheduler.push(file);
    }
    
    public Searcher thread(int threadNum) {
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        System.out.println("thread");
        return this;
    }
    
    public Searcher addPipeline(Pipeline pipeline) {
    	System.out.println("pipeline");
        this.pipeline = pipeline;
        return this;
    }

    protected void initComponent() {
        if (pipeline == null) {
            pipeline = new ConsolePipeline();
        }
//        downloader.setThread(threadNum);
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
    }
    
	@Override
	public void run() {
		initComponent();
		logger.info("Searcher started!");
		while (!Thread.currentThread().isInterrupted()) {
			File file = scheduler.poll();
			if (file == null) {
				if (threadPool.getThreadAlive() == 0) {
					break;
				}
				waitNewUrl(); // wait until new url added
			} else {
				final File fileFinal = file;//不加final会出错
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							processFile(fileFinal);
						} catch (Exception e) {
							logger.error("process file " + fileFinal + " error", e);
						} finally {
							signalNewFile();
						}
					}
				});
			}
		}
	}

	protected void processFile(File file) {
		pipeline.process(file);
		processor.process(file);
	}

	// signal 唤醒线程
	private void signalNewFile() {
		try {
			newFileLock.lock();
			newFileCondition.signalAll();
		} finally {
			newFileLock.unlock();
		}
	}

	private void waitNewUrl() {
		newFileLock.lock();
		try {
			if (threadPool.getThreadAlive() == 0) {
				return;
			}
			newFileCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.warn("waitNewUrl - interrupted, error {}", e);
		} finally {
			newFileLock.unlock();
		}
	}

}
