/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.backend;

import java.util.HashMap;
import java.util.Map;
import nu.epsilon.rss.persistence.FeedStorageImpl;

/**
 *
 * @author Pär Sikö
 */
public class BackendUIImpl implements BackendUI{

    private Map<String, Object> resources = new HashMap<String, Object>();
    
    private static final BackendUIImpl INSTANCE = new BackendUIImpl();
    
    public static BackendUI getInstance() {
        return INSTANCE;
    }
    
    private BackendUIImpl() {
        
    }
    
    @Override
    public void addResource(String resourceName, Object resource) {
        resources.put(resourceName, resource);
    }

    @Override
    public Object getResource(String resourceName) {
        return resources.get(resourceName);
    }

    
    
}
