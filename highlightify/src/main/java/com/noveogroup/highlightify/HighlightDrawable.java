/*
 * Copyright (c) 2014. Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
