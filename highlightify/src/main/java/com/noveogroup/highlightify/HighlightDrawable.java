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

        // ColorDrawable has no bounds and must be wrapped in shape before applying ColorFilter
        final Drawable sourceDrawable;
        if (drawable instanceof ColorDrawable) {
            final int color = ColorUtils.getColor(((ColorDrawable) drawable));
            final PaintDrawable paintDrawable = new PaintDrawable(color);
            paintDrawable.setShape(new RectShape());
            sourceDrawable = paintDrawable;
        } else {
            sourceDrawable = drawable;
        }

        // Preserve all states that initial drawable might have
        final Map<int[], Drawable> states = drawable instanceof StateListDrawable
                ? HighlightifyUtils.pullDrawableStates((StateListDrawable) drawable)
                : null;
        if (states == null) {
            final Rect padding = new Rect();
            sourceDrawable.getPadding(padding);
            final InsetDrawable inset = new InsetDrawable(sourceDrawable, padding.left, padding.top, padding.right, padding.bottom);
            addState(StateSet.WILD_CARD, inset);
        } else {
            for (final int[] state : states.keySet()) {
                addState(state, states.get(state));
            }
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
