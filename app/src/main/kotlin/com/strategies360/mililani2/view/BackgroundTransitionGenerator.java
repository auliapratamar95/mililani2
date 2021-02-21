package com.strategies360.mililani2.view;

import android.graphics.RectF;
import android.view.animation.Interpolator;

import com.flaviofaria.kenburnsview.Transition;
import com.flaviofaria.kenburnsview.TransitionGenerator;

public class BackgroundTransitionGenerator implements TransitionGenerator {

    /** Default value for the transition duration in milliseconds. */
    public static final int DEFAULT_TRANSITION_DURATION = 10000;

    /** Minimum rect dimension factor, according to the maximum one. */
    private static final float MIN_RECT_FACTOR = 0.75f;

    /** The duration, in milliseconds, of each transition. */
    private long mTransitionDuration;

    /** The {@link Interpolator} to be used to create transitions. */
    private Interpolator mTransitionInterpolator;

    private Transition lastTransition;

    private RectF lastDrawableBounds;

    private boolean forward;

    public BackgroundTransitionGenerator(long transitionDuration, Interpolator transitionInterpolator) {
        setTransitionDuration(transitionDuration);
        setTransitionInterpolator(transitionInterpolator);
    }

    @Override
    public Transition generateNextTransition(RectF drawableBounds, RectF viewport) {
        float drawableRatio = getRectRatio(drawableBounds);
        float viewportRectRatio = getRectRatio(viewport);
        RectF startRect;
        RectF endRect;
        if (drawableRatio >= viewportRectRatio) {
            float w = drawableBounds.height() * viewportRectRatio;
            float h = drawableBounds.height();
            startRect = new RectF(0, 0, w, h);
            endRect =generateRect(drawableBounds,viewport); //new RectF((drawableBounds.width() - w), drawableBounds.height(),drawableBounds.width(), h);
        } else {
            float w = drawableBounds.width();
            float h = drawableBounds.width() / viewportRectRatio;
            startRect = new RectF(0, 0, w, h);
            endRect =generateRect(drawableBounds,viewport); //new RectF(0, drawableBounds.height() - h, w, drawableBounds.height());
        }

        if (!drawableBounds.equals(lastDrawableBounds) || !haveSameAspectRatio(lastTransition.getDestinyRect(), viewport)) {
            forward = false;
        }
        forward = !forward;

        if (forward) {
            lastTransition = new Transition(endRect, startRect, mTransitionDuration, mTransitionInterpolator);
        } else {
            lastTransition = new Transition(startRect, endRect, mTransitionDuration, mTransitionInterpolator);
        }

        lastDrawableBounds = new RectF(drawableBounds);
        return lastTransition;
    }

    private RectF generateRect(RectF drawableBounds, RectF viewportRect) {
        float drawableRatio = getRectRatio(drawableBounds);
        float viewportRectRatio = getRectRatio(viewportRect);
        RectF maxCrop;

        if (drawableRatio > viewportRectRatio) {
            float r = (drawableBounds.height() / viewportRect.height()) * viewportRect.width();
            float b = drawableBounds.height();
            maxCrop = new RectF(0, 0, r, b);
        } else {
            float r = drawableBounds.width();
            float b = (drawableBounds.width() / viewportRect.width()) * viewportRect.height();
            maxCrop = new RectF(0, 0, r, b);
        }

        float factor = MIN_RECT_FACTOR;

        float width = factor * maxCrop.width();
        float height = factor * maxCrop.height();
        int widthDiff = (int) (drawableBounds.width() - (width - (viewportRect.width()/4)));
        int heightDiff = (int) (drawableBounds.height() - (height + (viewportRect.height()/4)));
        int left = widthDiff;// > 0 ? mRandom.nextInt(widthDiff) : 0;
        int top = heightDiff;// > 0 ? mRandom.nextInt(heightDiff) : 0;
        return new RectF(left, top, left + width, top + height);
    }

    private static boolean haveSameAspectRatio(RectF r1, RectF r2) {
        return (Math.abs(getRectRatio(r1) - getRectRatio(r2)) <= 0.01f);
    }

    private static float getRectRatio(RectF rect) {
        return rect.width() / rect.height();
    }

    public void setTransitionDuration(long transitionDuration) {
        mTransitionDuration = transitionDuration;
    }


    public void setTransitionInterpolator(Interpolator interpolator) {
        mTransitionInterpolator = interpolator;
    }
}
