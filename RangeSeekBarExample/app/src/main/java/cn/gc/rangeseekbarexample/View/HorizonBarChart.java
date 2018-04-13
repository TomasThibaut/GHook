package cn.gc.rangeseekbarexample.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cn.gc.rangeseekbarexample.R;

/**
 * Created by 宫成 on 2018/3/27 上午10:06.
 */

public class HorizonBarChart extends View {
    Paint paint;
    RectF rectF;
    Drawable bgText;
    float heightBar;
    int barWidth;
    int barHeight;

    public HorizonBarChart(Context context) {
        this(context, null);
    }

    public HorizonBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectF = new RectF();
        heightBar = Utils.dip2px(context, 11);
        bgText = ContextCompat.getDrawable(context, R.mipmap.horizon_chart_bg);

        barWidth = (int) Utils.dip2px(context, 130);
        barHeight = (int) Utils.dip2px(context, 30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        rectF.bottom = height - getPaddingBottom();
        rectF.left = getPaddingLeft();
        rectF.right = width - getPaddingRight();
        rectF.top = rectF.bottom - heightBar;
    }

    int height;

    int width;
    float scale;
    float max;
    int[] colors;
    int colorEnd;
    Shader mShader;


    public void setNumber(float a, float b, float max) {
        scale = a / b;
        this.max = max;
        this.colors = colors;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.right = width * scale;
        mShader = new LinearGradient(rectF.right, rectF.top, rectF.left, rectF.bottom
                    , new int[]{Color.parseColor("#ffb02a"), Color.parseColor("#ffc738"), Color.YELLOW}
                    , null, Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        canvas.drawRect(rectF, paint);

        int left = (int) (rectF.right - barWidth / 2);
        int top = (int) (rectF.top - barHeight);
        bgText.setBounds(left, top, left + barWidth, top + barHeight);
        bgText.draw(canvas);
    }
}
