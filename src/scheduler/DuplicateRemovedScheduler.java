package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Remove duplicate urls and only push urls which are not duplicate.<br></br>
 *
 * @author code4crafer@gmail.com
 * @since 0.5.0
 */
public abstract class DuplicateRemovedScheduler  {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private HashSetDuplicateRemover duplicatedRemover = new HashSetDuplicateRemover();

    public HashSetDuplicateRemover getDuplicateRemover() {
        return duplicatedRemover;
    }

    public DuplicateRemovedScheduler setDuplicateRemover(HashSetDuplicateRemover duplicatedRemover) {
        this.duplicatedRemover = duplicatedRemover;
        return this;
    }

    public void push(String request) {
        if (!duplicatedRemover.isDuplicate(request) ) {
            pushWhenNoDuplicate(request);
        }
    }
    
    protected void pushWhenNoDuplicate(String request) {
    	//QueueScheduler中实现的。QueueScheduler继承了这个类。
    }

}
