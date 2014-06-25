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

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class HighlightifyUtils {

    private HighlightifyUtils() {
        throw new UnsupportedOperationException();
    }


    public static Map<int[], Integer> pullColorStates(final ColorStateList colorStateList) {
        final Map<int[], Integer> states = new HashMap<int[], Integer>();
        final Class<ColorStateList> clazz = ColorStateList.class;
        try {
            final Field mColors = clazz.getDeclaredField("mColors");
            final Field mStateSpecs = clazz.getDeclaredField("mStateSpecs");

            mColors.setAccessible(true);
            mStateSpecs.setAccessible(true);

            final int[] colors = (int[]) mColors.get(colorStateList);
            final int[][] stateSpecs = (int[][]) mStateSpecs.get(colorStateList);
            for (int i = 0; i < colors.length; i++) {
                states.put(stateSpecs[i], colors[i]);
            }
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        return states;
    }

    public static Map<int[], Drawable> pullDrawableStates(final StateListDrawable stateListDrawable) {
        final Map<int[], Drawable> states = new HashMap<int[], Drawable>();
        final Class<StateListDrawable> clazz = StateListDrawable.class;
        try {
            final Method getStateCount = clazz.getDeclaredMethod("getStateCount");
            final Method getStateSet = clazz.getDeclaredMethod("getStateSet", int.class);
            final Method getStateDrawable = clazz.getDeclaredMethod("getStateDrawable", int.class);

            final Integer statesCount = (Integer) getStateCount.invoke(stateListDrawable);
            for (int i = 0; i < statesCount; i++) {
                final Drawable stateDrawable = (Drawable) getStateDrawable.invoke(stateListDrawable, i);
                final int[] state = (int[]) getStateSet.invoke(stateListDrawable, i);
                states.put(state, stateDrawable);
            }
        } catch (NoSuchMethodException ignore) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        return states;
    }
}
