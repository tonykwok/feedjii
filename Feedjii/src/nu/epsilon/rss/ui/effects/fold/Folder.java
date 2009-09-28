package nu.epsilon.rss.ui.effects.fold;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.TimingTarget;

public class Folder {

    private Animator animator;
    private boolean folded = true;
    private Foldable foldable;

    public Folder(Foldable foldable, int duration) {
        this(foldable, createDefaultAnimator(duration));
    }

    public Folder(Foldable foldable, Animator animator) {
        this.foldable = foldable;
        this.animator = animator;
        animator.addTarget(new FoldTarget(foldable));
    }

    private static Animator createDefaultAnimator(int duration) {
        Animator defaultAnimator = new Animator(duration);
        defaultAnimator.setAcceleration(0.4F);
        defaultAnimator.setDeceleration(0.4F);
        return defaultAnimator;
    }

    public void fold() {
        if (animator.isRunning() || isFolded()) {
            return;
        }

        foldable.prepareFolding();
        animator.setStartFraction(1F);
        animator.setStartDirection(Direction.BACKWARD);
        animator.start();
    }

    public void unfold() {
        if (animator.isRunning() || !isFolded()) {
            return;
        }

        foldable.prepareUnfolding();
        animator.setStartFraction(0F);
        animator.setStartDirection(Direction.FORWARD);
        animator.start();
    }

    public void toggle() {
        if (folded) {
            unfold();
        } else {
            fold();
        }
    }

    boolean isFolded() {
        return folded;
    }

    void setFolded(boolean folded) {
        this.folded = folded;
        if (folded) {
            foldable.folded();
        } else {
            foldable.unfolded();
        }
    }

    private class FoldTarget implements TimingTarget {

        Foldable foldable;

        public FoldTarget(Foldable foldable) {
            this.foldable = foldable;
        }

        @Override
        public void timingEvent(float progress) {
            int height = Math.round(foldable.getOriginalHeight() * progress);
            foldable.setHeight(height);
        }

        @Override
        public void begin() {
        }

        @Override
        public void end() {
            setFolded(!isFolded());
        }

        @Override
        public void repeat() {
        }
    }
}

