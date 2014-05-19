package com.noveogroup.highlightify;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class HighlightifyUtils {

    private HighlightifyUtils() {
        throw new UnsupportedOperationException();
    }


    static Map<int[], Drawable> pullDrawableStates(final StateListDrawable stateListDrawable) {
        final Map<int[], Drawable> states = new HashMap<int[], Drawable>();
        try {
            final Class<StateListDrawable> clazz = StateListDrawable.class;
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
