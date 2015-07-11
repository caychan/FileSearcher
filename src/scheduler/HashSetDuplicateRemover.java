package scheduler;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class HashSetDuplicateRemover  {

    private Set<String> urls = Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public boolean isDuplicate(String request) {
        return !urls.add(request);
    }
    
    protected String getPath(String request) {
        return request;
    }

    public int getTotalRequestsCount() {
    	return urls.size();
    }
    
}
