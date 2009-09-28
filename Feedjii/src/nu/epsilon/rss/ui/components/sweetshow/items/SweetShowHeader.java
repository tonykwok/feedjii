/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.epsilon.rss.ui.components.sweetshow.items;

import javax.swing.JComponent;

/**
 *
 * @author Pär Sikö
 */
public class SweetShowHeader extends SweetShowItem{

    public SweetShowHeader(String header) {
        type = Type.HEADER;
        setListText(header);
    }

    @Override
    public JComponent getDisplayItem() {
        return null;
    }

    
    
}
