package search;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scheduler.QueueScheduler;
import thread.CountableThreadPool;

public class Searcher implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private ReentrantLock newFileLock = new ReentrantLock();

	private Condition newFileCondition = newFileLock.newCondition();

	protected QueueScheduler scheduler = new QueueScheduler();

	private CountableThreadPool threadPool;

	private int emptySleepTime = 30000;

	@Override
	public void run() {
		logger.info("Searcher started!");
		while (!Thread.currentThread().isInterrupted()) {
			String file = scheduler.poll();
			if (file == null) {
				if (threadPool.getThreadAlive() == 0) {
					break;
				}
				waitNewUrl(); // wait until new url added
			} else {
				final String fileFinal = file;
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							processRequest(fileFinal);
						} catch (Exception e) {
							logger.error("process request " + fileFinal
									+ " error", e);
						} finally {
							signalNewUrl();
						}
					}
				});
			}
		}
	}

	protected void processRequest(String request) {

		// pageProcessor.process(page);
		// extractAndAddRequests(page, spawnUrl);

	}

	public void close() {
		threadPool.shutdown();
	}

	// signal »½ÐÑÏß³Ì
	private void signalNewUrl() {
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
