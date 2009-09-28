/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.sweetshow.items;

import java.awt.Image;
import javax.swing.JComponent;
import nu.epsilon.rss.ui.components.sweetshow.listener.SweetShowCreationListener;

/**
 *
 * @author Pär Sikö
 */
public abstract class SweetShowItem {

    private String listText;
    private Image listImage;
    protected Type type;
    protected SweetShowCreationListener listener;
    protected long id;

    public enum Type {

        PROFILE, SUBSCRIPTION, HEADER
    }

    public SweetShowItem() {
    }

    public void setCreationListener(SweetShowCreationListener listener) {
        this.listener = listener;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getListText() {
        return listText;
    }

    public Image getListImage() {
        return listImage;
    }

    public void setListImage(Image listImage) {
        this.listImage = listImage;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

    public long getId() {
        return id;
    }

    public abstract JComponent getDisplayItem();
}
