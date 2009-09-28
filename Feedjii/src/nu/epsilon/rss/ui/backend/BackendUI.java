/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.backend;

/**
 *
 * @author Pär Sikö
 */
public interface BackendUI {

    void addResource(String resourceName, Object resource);
    
    Object getResource(String resourceName);
    
}
