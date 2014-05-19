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
