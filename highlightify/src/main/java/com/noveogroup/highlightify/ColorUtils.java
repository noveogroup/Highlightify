package com.noveogroup.highlightify;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException();
    }


    // This is just awful.
    // Please tell me if you know how to make this right.
    public static int filter(final ColorFilter filter, final int color) {
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setColorFilter(filter);
        final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawPaint(paint);
        return bitmap.getPixel(0, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getColor(final ColorDrawable colorDrawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            colorDrawable.draw(canvas);
            return bitmap.getPixel(0, 0);
        } else {
            return colorDrawable.getColor();
        }
    }

    public static int withAlpha(final int color, final int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
