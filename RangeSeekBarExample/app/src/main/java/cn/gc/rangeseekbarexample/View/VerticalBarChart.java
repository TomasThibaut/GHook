package cn.gc.rangeseekbarexample.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 宫成 on 2018/3/26 下午2:56.
 */

public class VerticalBarChart extends View {

    private Paint aLittleMagicalPaint;
    private Rect rectView;
    /**
     * 此列表顺序为， 一个我的， 一个平均， 一个我的，一个平均...etc
     */
    private List<RectF> chartRects;
    private List<String> chartNames;
    private List<Float> barPresents;
    private int width;
    private int height;
    private int topBar;
    private int bottomBar;
    private Context context;
    private List<Integer> cursorPoints;
    private int widthDot;
    private int barWidth;
    private float space;
    //说明矩形的范围集合
    private List<RectF> rectFDescs;


    private List<float[]> presenList = new ArrayList<>();

    public VerticalBarChart(Context context) {
        this(context, null);
    }

    public VerticalBarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        aLittleMagicalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectView = new Rect();
        chartRects = new ArrayList<>();
        chartNames = new ArrayList<>();
        barPresents = new ArrayList<>();
        cursorPoints = new ArrayList<>();
        rectFDescs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cursorPoints.add(i);
            RectF rectFDesc = new RectF();
            rectFDescs.add(rectFDesc);
        }
        setChartNames("上课时长", "刷题数量", "做题正确率");
        widthDot = getPxFromDip(1.5f);
        barWidth = getPxFromDip(15);
    }

    public void setChartNames(String... names) {
        chartNames.addAll(Arrays.asList(names));
    }

    /**
     * 顺序不可逆， 第一个是上课时长， 第二个是刷题数量， 第三个是正确率
     *
     * @param list
     */
    public void setBarPersents(List<float[]> list) {
        int listSize = 3;
        if (list == null || list.size() != listSize) {
            return;
        }
        this.presenList = list;

        invalidate();
    }

    private int getPxFromDip(float dip) {
        return Math.round(Utils.dip2px(context, dip));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectView.left = getPaddingLeft();
        rectView.right = getMeasuredWidth() - getPaddingRight();
        rectView.top = getPaddingTop();
        rectView.bottom = getMeasuredHeight() - getPaddingBottom();
        width = rectView.right - rectView.left;
        height = rectView.bottom - rectView.top;
        topBar = rectView.top + getPxFromDip(5);
        bottomBar = rectView.bottom - getPxFromDip(10) - height / 3;
        space = width / 3;
        for (int i = 0; i < 3; i++) {
            int cursorPoint = (int) (rectView.left + space * (i + 0.5));
            cursorPoints.set(i, cursorPoint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画分割线
        aLittleMagicalPaint.setColor(Color.parseColor("#CBCBCB"));
        aLittleMagicalPaint.setStrokeWidth(getPxFromDip(1));
        aLittleMagicalPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(rectView.left, bottomBar, rectView.right, bottomBar, aLittleMagicalPaint);


        //画虚线
        aLittleMagicalPaint.setStrokeWidth(getPxFromDip(0.5f));
        aLittleMagicalPaint.setColor(Color.parseColor("#E5E5E5"));
        int widthDottedLine = (rectView.right - getPxFromDip(10)) - (rectView.left + getPxFromDip(10));
        int size = widthDottedLine / widthDot / 2;
        int maxHeightBar = bottomBar - topBar;
        int dottedBaseLine = topBar + (maxHeightBar) / 2;
        for (int i = 0; i < size; i++) {
            int startX = (rectView.left + getPxFromDip(10)) + widthDot * i * 2;
            int stopX = startX + widthDot;
            canvas.drawLine(startX, topBar, stopX, topBar, aLittleMagicalPaint);
            canvas.drawLine(startX, dottedBaseLine, stopX, dottedBaseLine, aLittleMagicalPaint);
        }
        //画统计图
        for (int i = 0; i < presenList.size(); i++) {
            float[] array = presenList.get(i);
            Float averageP = array[0];
            Float mineP = array[1];
            RectF rectFMine = new RectF();
            int cursorPoint = cursorPoints.get(i);
            rectFMine.left = cursorPoint - barWidth;
            rectFMine.right = cursorPoint;
            rectFMine.bottom = bottomBar;

            if (mineP >= averageP) {
                rectFMine.top = topBar;
            } else {
                if (averageP != 0) {
                    float mineHeight = (maxHeightBar) * (mineP / averageP);
                    rectFMine.top = bottomBar - mineHeight;
                }
            }

            RectF rectFAverage = new RectF();
            rectFAverage.left = cursorPoint;
            rectFAverage.right = cursorPoint + barWidth;
            rectFAverage.bottom = bottomBar;
            if (averageP >= mineP) {
                rectFAverage.top = topBar;
            } else {
                if (mineP != 0) {
                    float averageHeight = maxHeightBar * averageP / mineP;
                    rectFAverage.top = bottomBar - averageHeight;
                }
            }

            if (averageP == 0 && mineP == 0) {
                rectFAverage.top = rectFAverage.bottom = rectFMine.top = rectFMine.bottom = bottomBar;
            }
            chartRects.add(rectFAverage);
            chartRects.add(rectFMine);

            RectF rf = rectFDescs.get(i);
            rf.left = rectFMine.right;
            rf.right = rf.left + space - barWidth / 2;
            rf.bottom = dottedBaseLine;
            rf.top = rf.bottom - (dottedBaseLine - topBar) * 2 / 3;
            if (i == 2) {
                rf.right = rectFMine.left;
                rf.left = rf.right - space + barWidth / 2;
            }
            rectFDescs.set(i, rf);
        }

        for (int i = 0; i < chartRects.size(); i++) {
            RectF rectF = chartRects.get(i);
            if (i % 2 == 0) {
                //黄色，人均
                aLittleMagicalPaint.setColor(Color.parseColor("#FFCE3C"));
            } else {
                //红色，我的
                aLittleMagicalPaint.setColor(Color.parseColor("#FF2F25"));
            }
            canvas.drawRect(rectF, aLittleMagicalPaint);
        }

        //文字
        aLittleMagicalPaint.setTextSize(getPxFromDip(14));
        aLittleMagicalPaint.setColor(Color.parseColor("#323232"));
        float widthText = aLittleMagicalPaint.measureText("上");
        int baseLine = bottomBar + getPxFromDip(7) + getPxFromDip(16);
        for (int i = 0; i < cursorPoints.size(); i++) {
            int point = cursorPoints.get(i);
            String s = chartNames.get(i);
            float x = point - widthText * s.length() / 2f;
            canvas.drawText(s, x, baseLine, aLittleMagicalPaint);
        }

        //画注释
        int annotationCenterY = baseLine + (rectView.bottom - baseLine) / 2;
        int mid = rectView.left + (rectView.right - rectView.left) / 2;
        int spaceBetween = getPxFromDip(5);
        float leftRectFR = mid - getPxFromDip(20) - spaceBetween - widthText * chartNames.get(0).length();
        float leftRectFL = leftRectFR - barWidth;
        float leftRectFT = annotationCenterY - barWidth / 2;
        float leftRectFB = annotationCenterY + barWidth / 2;
        float rightRectFL = mid;
        float rightRectFR = mid + barWidth;
        float rightRectFT = leftRectFT;
        float rightRectFB = leftRectFB;
        float leftTextX = leftRectFR + spaceBetween;
        float rightTextX = rightRectFR + spaceBetween;
        aLittleMagicalPaint.setColor(Color.parseColor("#FF2F25"));
        canvas.drawRect(leftRectFL, leftRectFT, leftRectFR, leftRectFB, aLittleMagicalPaint);
        aLittleMagicalPaint.setColor(Color.parseColor("#FFCE3C"));
        canvas.drawRect(rightRectFL, rightRectFT, rightRectFR, rightRectFB, aLittleMagicalPaint);
        aLittleMagicalPaint.setColor(Color.parseColor("#888888"));
        aLittleMagicalPaint.setTextSize(getPxFromDip(13));
        canvas.drawText("我的学习", leftTextX, leftRectFB - getPxFromDip(2), aLittleMagicalPaint);
        canvas.drawText("通过的人均学习", rightTextX, leftRectFB - getPxFromDip(2), aLittleMagicalPaint);


        //画说明flag
        aLittleMagicalPaint.setTextSize(getPxFromDip(12));
        for (int i = 0; i < rectFDescs.size(); i++) {
            if (indexClick != i) {
                continue;
            }
            //画背景
            RectF rf = rectFDescs.get(i);
            aLittleMagicalPaint.setStyle(Paint.Style.FILL);
            aLittleMagicalPaint.setColor(Color.WHITE);
            canvas.drawRect(rf, aLittleMagicalPaint);
            //描边
            aLittleMagicalPaint.setStyle(Paint.Style.STROKE);
            aLittleMagicalPaint.setStrokeWidth(getPxFromDip(0.5f));
            aLittleMagicalPaint.setColor(Color.parseColor("#888888"));
            canvas.drawRect(rf, aLittleMagicalPaint);

            int radius = getPxFromDip(2);
            float rfHeight = rf.bottom - rf.top;
            float offsetBaseline = getPxFromDip(2);
            float betweenTextAndImage = getPxFromDip(3);
            float baselineUp = rf.top + rfHeight / 3 * 1 + offsetBaseline;
            float baselineDown = rf.top + rfHeight / 3 * 2 + offsetBaseline;
            float xUp = rf.left + betweenTextAndImage * 2 + radius * 2;

            float[] array = presenList.get(i);
            int averageP = (int) array[0];
            int mineP = (int) array[1];
            String contentMine = "";
            String contentAvg = "";

            if (i == 0) {
                contentMine = "我的：" + mineP + "分钟";
                contentAvg = "人均：" + averageP + "分钟";
            } else if (i == 1) {
                contentMine = "我的：" + mineP + "道";
                contentAvg = "人均：" + averageP + "道";
            } else if (i == 2) {
                contentMine = "我的：" + mineP + "%";
                contentAvg = "人均：" + averageP + "%";
            }
            aLittleMagicalPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(contentMine, xUp, baselineUp, aLittleMagicalPaint);
            canvas.drawText(contentAvg, xUp, baselineDown, aLittleMagicalPaint);
            aLittleMagicalPaint.setColor(Color.parseColor("#FF2F25"));
            canvas.drawCircle(rf.left + betweenTextAndImage + radius, baselineUp - offsetBaseline, radius, aLittleMagicalPaint);
            aLittleMagicalPaint.setColor(Color.parseColor("#FFCE3C"));
            canvas.drawCircle(rf.left + betweenTextAndImage + radius, baselineDown - offsetBaseline, radius, aLittleMagicalPaint);
        }
    }

    private int indexClick = 0;
    private int index = -1;
    float downX;
    float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            actionDown(event);
        } else if (action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            int minMove = getPxFromDip(4);
            if (index == -1 || Math.abs(x - downX) > minMove || Math.abs(y - downY) > minMove) {
                index = -1;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (index != -1) {
                indexClick = index;
                invalidate();
            }
        }
        return true;
    }

    private void actionDown(MotionEvent event) {

        downX = event.getX();
        downY = event.getY();
        if (downY > bottomBar) {
            index = -1;
            return;
        }
        if (downX <= space) {
            index = 0;
        } else if (downX > space && downX <= space * 2) {
            index = 1;
        } else if (downX > space * 2 && downX <= space * 3) {
            index = 2;
        }
    }
}
