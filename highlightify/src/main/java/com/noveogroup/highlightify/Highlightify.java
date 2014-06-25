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

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Highlightify {

    private static final ColorFilter FILTER = HighlightUtils.newFilter(Color.BLACK);
    private static final Map<Target, ColorFilter> TARGETS = new HashMap<Target, ColorFilter>();

    static {
        TARGETS.put(Target.Background, FILTER);
        TARGETS.put(Target.Text, FILTER);
        TARGETS.put(Target.Compound, FILTER);
        TARGETS.put(Target.Image, FILTER);
    }

    public static class Builder {

        private final Map<Target, ColorFilter> targets = new HashMap<Target, ColorFilter>();

        public Builder addTargets(final Target... targets) {
            addTargets(FILTER, targets);
            return this;
        }

        public Builder addTargets(final int color, final Target... targets) {
            addTargets(HighlightUtils.newFilter(color), targets);
            return this;
        }

        public Builder addTargets(final ColorFilter filter, final Target... targets) {
            for (final Target target : targets) {
                this.targets.put(target, filter);
            }
            return this;
        }

        public Highlightify build() {
            return new Highlightify(targets.isEmpty() ? TARGETS : targets);
        }
    }

    private final Map<Target, ColorFilter> targets;

    protected Highlightify(final Map<Target, ColorFilter> targets) {
        this.targets = targets;
    }

    public Highlightify() {
        this(TARGETS);
    }

    public Set<Target> getTargets() {
        return new HashSet<Target>(targets.keySet());
    }

    private void highlight(final TextView textView) {
        if (targets.containsKey(Target.Text)) {
            HighlightUtils.highlightText(targets.get(Target.Text), textView);
        }
        if (targets.containsKey(Target.Compound)) {
            HighlightUtils.highlightCompound(targets.get(Target.Compound), textView);
        }
    }

    // Seems like a PMD bug
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void highlight(final ImageView imageView) {
        if (targets.containsKey(Target.Image)) {
            HighlightUtils.highlightImage(targets.get(Target.Image), imageView);
        }
        if (targets.containsKey(Target.ImageBackground)) {
            HighlightUtils.highlightBackground(targets.get(Target.ImageBackground), imageView);
        }
    }

    /**
     * Highlight views
     *
     * @param views View objects to highlight
     */
    public void highlight(final View... views) {
        for (final View view : views) {
            if (view instanceof ImageView) {
                highlight((ImageView) view);
            } else {
                if (view instanceof TextView) {
                    highlight((TextView) view);
                }
                if (targets.containsKey(Target.Background)) {
                    HighlightUtils.highlightBackground(targets.get(Target.Background), view);
                }
            }
        }
    }

    /**
     * Highlight clickable views recursively
     *
     * @param view View object to highlight
     */
    public void highlightClickable(final View view) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                final View child = viewGroup.getChildAt(i);
                highlightClickable(child);
            }
        }
        if (view.isClickable()) {
            highlight(view);
        }
    }

    /**
     * Highlight supplied View and all its childred
     *
     * @param view View object to highlight
     */
    public void highlightWithChildren(final View view) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.setAddStatesFromChildren(false);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                final View child = viewGroup.getChildAt(i);
                child.setDuplicateParentStateEnabled(true);
                highlightWithChildren(child);
            }
        }
        highlight(view);
    }
}
