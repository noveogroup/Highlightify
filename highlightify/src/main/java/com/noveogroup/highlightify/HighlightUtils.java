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

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.StateSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Map;

public final class HighlightUtils {

    private HighlightUtils() {
        throw new UnsupportedOperationException();
    }

    public static void highlightBackground(final ColorFilter filter, final View view) {
        final Drawable background = view.getBackground();
        if (background == null) {
            return;
        }
        final HighlightDrawable highlightDrawable = wrapDrawable(background, filter);
        setBackgroundPreservePadding(view, highlightDrawable);
    }

    public static void highlightText(final ColorFilter filter, final TextView textView) {
        final ColorStateList colorStateList = textView.getTextColors();
        final Map<int[], Integer> colorStates = HighlightifyUtils.pullColorStates(colorStateList);
        final int[][] states;
        final int[] colors;
        if (colorStates == null) {
            final int color = colorStateList.getDefaultColor();
            states = new int[][]{{android.R.attr.state_pressed}, StateSet.WILD_CARD};
            colors = new int[]{ColorUtils.filter(filter, color), color};
        } else {
            // We'll just add "pressed" states on top, doesn't matter if they already exist
            final int statesCount = colorStates.size();
            states = new int[statesCount * 2][];
            colors = new int[states.length];
            int i = 0;
            for (final int[] state : colorStates.keySet()) {
                final int color = colorStates.get(state);

                final int[] pressedState = Arrays.copyOf(state, state.length + 1);
                pressedState[state.length] = android.R.attr.state_pressed;
                states[i] = pressedState;
                states[statesCount + i] = state;

                colors[i] = ColorUtils.filter(filter, color);
                colors[statesCount + i] = color;

                i++;
            }
        }
        textView.setTextColor(new ColorStateList(states, colors));
    }

    @SuppressWarnings("PMD.NPathComplexity")
    public static void highlightCompound(final ColorFilter filter, final TextView textView) {
        final Drawable[] drawables = textView.getCompoundDrawables();
        if (drawables == null) {
            return;
        }
        final int left = 0;
        final int top = 1;
        final int right = 2;
        final int bottom = 3;
        drawables[left] = drawables[left] == null ? null : wrapDrawable(drawables[left], filter);
        drawables[top] = drawables[top] == null ? null : wrapDrawable(drawables[top], filter);
        drawables[right] = drawables[right] == null ? null : wrapDrawable(drawables[right], filter);
        drawables[bottom] = drawables[bottom] == null ? null : wrapDrawable(drawables[bottom], filter);
        textView.setCompoundDrawablesWithIntrinsicBounds(drawables[left], drawables[top], drawables[right], drawables[bottom]);
    }

    public static void highlightImage(final ColorFilter filter, final ImageView imageView) {
        final Drawable source = imageView.getDrawable();
        if (source == null) {
            return;
        }
        final HighlightDrawable highlightDrawable = wrapDrawable(source, filter);
        imageView.setImageDrawable(highlightDrawable);
    }

    public static ColorFilter newFilter(final int color) {
        // Add alpha chanel
        final int highlightColor = Color.alpha(color) == 0xFF ? ColorUtils.withAlpha(color, 0x50) : color;
        return new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
    }

    private static HighlightDrawable wrapDrawable(final Drawable drawable, final ColorFilter filter) {
        final HighlightDrawable highlightDrawable;
        if (drawable instanceof HighlightDrawable) {
            highlightDrawable = (HighlightDrawable) drawable;
            highlightDrawable.setHighlightFilter(filter);
        } else {
            highlightDrawable = new HighlightDrawable(drawable, filter);
        }
        return highlightDrawable;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void setBackgroundPreservePadding(final View view, final Drawable drawable) {
        final Rect padding = new Rect(
                view.getPaddingLeft(),
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom()
        );
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
        view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }
}
