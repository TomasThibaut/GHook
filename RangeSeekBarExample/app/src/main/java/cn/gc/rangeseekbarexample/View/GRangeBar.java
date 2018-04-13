package cn.gc.rangeseekbarexample.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.ArrayList;

import cn.gc.rangeseekbarexample.R;

/**
 * Created by G_C on 2018/3/13 下午5:49.
 */

public class GRangeBar extends View {

    /**
     * 画笔
     */
    private Paint mPaintCursor = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintBar = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Rect们
     */
    private Rect mRectView = new Rect();
    private RectF mRectBar = new RectF();
    private Rect mRectCursor = new Rect();
    private RectF mRectSelectedBar = new RectF();

    /**
     * 属性们
     */
    private Drawable mCursorImage;

    private int mTextSizeNormal;
    private int mTextSizeSelected;
    private int mTextColorNormal;
    private int mTextColorSelected;

    private int mBarColorNormal;
    private int mBarColorSelected;
    private int mBarHeight;

    private int mSpaceBetween;
    private String[] mTextArray;

    private int mIndex;

    private int mScrollerDuration;

    /**
     * constants
     */
    private Context mContext;
    private int mWidthCursor;
    private int mHeightCursor;
    private int mSpaceOfText;
    private int mFirstTextXLeft;
    private ArrayList<RectF> mNumberRectF = new ArrayList<>();
    private ArrayList<Float> mTextXCoordinateList = new ArrayList<>();
    /**
     * 刻度的列表
     */
    private ArrayList<Float> mDialList = new ArrayList<>();

    /**
     * 滑动相关
     */
    private Scroller mScroller;

    public GRangeBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initFields();
    }

    private void initFields() {
        mTextColorNormal = Color.GRAY;
        mTextColorSelected = Color.BLACK;

        mBarColorNormal = Color.GRAY;
        mBarColorSelected = Color.RED;

        mScrollerDuration = 1000;

        int size = 11;
        mTextArray = new String[size];
        for (int i = 0; i < size; i++) {
            mTextArray[i] = String.valueOf(i);
            mNumberRectF.add(new RectF());
            mTextXCoordinateList.add(0f);
            mDialList.add(0f);
        }

        mTextSizeNormal = Math.round(Utils.dip2px(mContext, 12));
        mTextSizeSelected = Math.round(Utils.dip2px(mContext, 16));
        mBarHeight = Math.round(Utils.dip2px(mContext, 5));
        mSpaceBetween = Math.round(Utils.dip2px(mContext, 10));

        //mBarBackgroundImage素材自行添加
        mCursorImage = mContext.getDrawable(R.mipmap.slider);

        mPaintCursor.setColor(mBarColorNormal);

        mPaintText.setColor(mTextColorNormal);
        mPaintText.setTextSize(mTextSizeNormal);

        mPaintBar.setStrokeCap(Paint.Cap.ROUND);
        mPaintBar.setStrokeWidth(mBarHeight);
        mPaintBar.setStyle(Paint.Style.FILL);


        mWidthCursor = mCursorImage.getIntrinsicWidth();
        mHeightCursor = mCursorImage.getIntrinsicHeight();

        mScroller = new Scroller(mContext);
    }


    /**
     * 父View的限制
     *
     * @param widthMeasureSpec  宽度限制
     * @param heightMeasureSpec 高度限制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: 2018/3/20 onMeasure执行了很多次， 所以用add不行
        //过筛是否符合父View的限制
//        resolveSize()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //赋值View的范围
        mRectView.left = getLeft() - getPaddingLeft();
        mRectView.top = getTop() - getPaddingTop();
        mRectView.right = getRight() - getPaddingRight();
        mRectView.bottom = getBottom() - getPaddingBottom();


        //没有考虑mWidthCursor和字体宽度谁大的问题， 默认mWidthCursor大
        mRectBar.left = mRectView.left + mWidthCursor / 2;
        mRectBar.right = mRectView.right - mWidthCursor / 2;
        mRectBar.top = mRectView.top + Math.abs(mHeightCursor / 2 - mBarHeight / 2);
        mRectBar.bottom = mRectBar.top + mBarHeight;

        mRectCursor.left = Math.round((mRectBar.left - mWidthCursor / 2));
        mRectCursor.right = mRectCursor.left + mWidthCursor;
        mRectCursor.top = mRectView.top;
        mRectCursor.bottom = mRectCursor.top + mHeightCursor;


        //计算字体Rect
        float barWidth = mRectBar.right - mRectBar.left;
        float pointSpace = barWidth / (mTextArray.length - 1);
        //根据Cursor的宽度和 字体的宽度， 决定Bar的Rect
        for (int i = 0; i < mTextArray.length; i++) {
            //画字体需要计算每个数字的左侧坐标，而数字有1位2位等，所以宽度不一样，数字之前的空格，宽度也不一样，
            //但是bar上的每个标尺点位是确定的，再根据该点位的字体宽度，推导出字体的左侧坐标
            String numberText = mTextArray[i];
            float numberWidth = mPaintText.measureText(numberText);
            //每个点位的x坐标
            float pointX = mRectBar.left + i * pointSpace;
            mDialList.set(i, pointX);
            RectF rectF = mNumberRectF.get(i);
            //mNumberRectF代表标尺的数字范围，可用于判断点击范围等
            rectF.left = pointX - pointSpace / 2;
            rectF.right = pointX + pointSpace / 2;
            rectF.top = mRectBar.top;
            rectF.bottom = mRectBar.bottom + mSpaceBetween + mTextSizeNormal;
            mNumberRectF.set(i, rectF);
            // TODO: 2018/3/20 onMeasure执行了很多次， 所以用add不行
            mTextXCoordinateList.set(i, pointX - numberWidth / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画未选中Bar
        mPaintBar.setColor(mTextColorNormal);
        canvas.drawRoundRect(mRectBar, mBarHeight / 2, mBarHeight / 2, mPaintBar);

        //画数字
        for (int i = 0; i < mTextArray.length; i++) {
            canvas.drawText(mTextArray[i], mTextXCoordinateList.get(i), mTextSizeNormal + mSpaceBetween + mRectBar.bottom, mPaintText);
        }
        //根据游标的位置计算mRectSelectedBar， 画选中Bar
        int x = getCursorXCoordinate();
        Log.i("G_C", "onDraw: " + x);
        Log.i("G_C", "onDraw mRectBar: " + mRectBar.right / 2);
        mRectSelectedBar.left = mRectBar.left;
        mRectSelectedBar.right = x;
        mRectSelectedBar.top = mRectBar.top;
        mRectSelectedBar.bottom = mRectBar.bottom;
        mPaintBar.setColor(mBarColorSelected);
        canvas.drawRoundRect(mRectSelectedBar, mBarHeight / 2, mBarHeight / 2, mPaintBar);

        //画游标
        mCursorImage.setBounds(mRectCursor);
        mCursorImage.draw(canvas);
    }

    private int getCursorXCoordinate() {
        return mRectCursor.right - mWidthCursor / 2;
    }

    /**
     * TODO: 2018/3/15 为毛有个warning   performClick()?
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(event);
                break;
            default:
        }
        return true;
    }

    private boolean isDownInCursor;

    private void onActionDown(MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY();
        if (mRectCursor.contains(Math.round(downX), Math.round(downY))) {
            isDownInCursor = true;
        }

    }

    private void onActionMove(MotionEvent event) {
        if (isDownInCursor) {
            //把x作为cursor的初始中心点， deltaX移动后的中心点
            float x = event.getX();
            int halfCursorWidth = mWidthCursor / 2;

            if (x - halfCursorWidth <= mRectView.left) {
                x = halfCursorWidth;
            }
            if (x + halfCursorWidth >= mRectView.right) {
                x = mRectView.right - halfCursorWidth;
            }
            mRectCursor.left = Math.round(x - halfCursorWidth);
            mRectCursor.right = mRectCursor.left + mWidthCursor;

            //重绘， 来模拟Cursor滑动
            invalidate();
        }
    }

    private void onActionUp(MotionEvent event) {
        float xUp = event.getX();
        float yUp = event.getY();
        if (isDownInCursor) {
            int xCoordinate = getCursorXCoordinate();
            for (int i = 0; i < mNumberRectF.size(); i++) {
                float right = mNumberRectF.get(i).right;
                float left = mNumberRectF.get(i).left;
                if (xCoordinate >= left & xCoordinate < right) {
                    mIndex = i;
//                    int deltaX = (int) (xCoordinate - (right - left));
                    scrollTomDial(xCoordinate);
                }
            }
        }


        isDownInCursor = false;
    }

    /**
     * 游标平移到对应刻度
     *
     * @param x 手指up时候滑动到的位置
     */
    private void scrollTomDial(int x) {
        Float dial = mDialList.get(mIndex);
        int dx = Math.round((dial- mWidthCursor - x));
        Log.i("G_C", "scrollTomDial: " + dx);
        mScroller.startScroll(x, 0, dx, 0, mScrollerDuration);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mRectCursor.left = mScroller.getCurrX();
            mRectCursor.right = mRectCursor.left + mWidthCursor;
            Log.i("G_C", "computeScroll: " + mScroller.getCurrX());
            invalidate();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public interface OnCursorChangeListener {
        void onCursorChanged(int index);
    }

    private OnCursorChangeListener mOnCursorChangeListener;

    public void setOnCursorChangeListener(OnCursorChangeListener listener) {
        this.mOnCursorChangeListener = listener;
    }
}
