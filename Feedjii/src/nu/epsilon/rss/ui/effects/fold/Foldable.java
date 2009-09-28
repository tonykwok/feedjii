package nu.epsilon.rss.ui.effects.fold;

public interface Foldable {

    public int getOriginalHeight();

    public void setHeight(int height);

    public void prepareFolding();

    public void prepareUnfolding();

    public void unfolded();

    public void folded();
}
