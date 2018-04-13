package cn.gc.rangeseekbarexample.View;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 宫成 on 2018/3/14 下午3:22.
 */

public class Utils {
    private static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null)
            return null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * dp值转化为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dip2px(Context context, float dp) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        if (metrics == null)
            return 0;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
