package com.noveogroup.highlightify;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.StateSet;

import java.util.Arrays;
import java.util.Map;


public class HighlightDrawable extends StateListDrawable {

    private ColorFilter filter;
    private boolean highlighted = false;

    public HighlightDrawable(final Drawable drawable, final ColorFilter filter) {
        super();
        this.filter = filter;

        final PaintDrawable paintDrawable;
        if ((drawable instanceof ColorDrawable)) {
            final int color = ColorUtils.getColor(((ColorDrawable) drawable));
            paintDrawable = new PaintDrawable(color);
            paintDrawable.setShape(new RectShape());
        } else {
            paintDrawable = null;
        }

        if (drawable instanceof StateListDrawable) {
            final StateListDrawable stateListDrawable = (StateListDrawable) drawable;
            final Map<int[], Drawable> states = HighlightifyUtils.pullDrawableStates(stateListDrawable);
            for (final int[] state : states.keySet()) {
                addState(state, states.get(state));
            }
        } else {
            final Drawable source = paintDrawable == null ? drawable : paintDrawable;
            final Rect padding = new Rect();
            source.getPadding(padding);
            final InsetDrawable inset = new InsetDrawable(source, padding.left, padding.top, padding.right, padding.bottom);
            addState(StateSet.WILD_CARD, inset);
        }
    }

    public void setHighlightFilter(ColorFilter filter) {
        this.filter = filter;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean result = super.onStateChange(stateSet);

        Arrays.sort(stateSet);
        final boolean shouldHighlight = Arrays.binarySearch(stateSet, android.R.attr.state_pressed) > 0;
        if (highlighted != shouldHighlight) {
            highlighted = shouldHighlight;
            if (shouldHighlight) {
                setColorFilter(filter);
            } else {
                clearColorFilter();
            }
            invalidateSelf();
            return true;
        }

        return result;
    }
}
