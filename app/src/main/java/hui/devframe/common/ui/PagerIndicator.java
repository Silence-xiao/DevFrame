package hui.devframe.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import hui.devframe.R;
import hui.devframe.common.util.ScreenUtil;

/**
 * 指示器
 * Created by wanghui on 16/5/31.
 */
public class PagerIndicator extends View {
    private static final int DEFAULT_DOT_SIZE = 10;
    private static final int DEFAULT_DOT_SPACE = 20;
    private static final int DEFAULT_BACK_COLOR = 0xff000000;
    private static final int DEFAULT_FORE_COLOR = 0xffffffff;

    private Context context;
    private int number = 0;
    private int size;
    private int space;
    private int back_color;
    private int fore_color;

    private Paint mBackPaint;
    private Paint mForePaint;
    private int mPos = 0;
    private float mOffset = 0;

    public PagerIndicator(Context context) {
        this(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
        intPaint();

        setVisibility(GONE);
    }

    private void init(AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pager_indicator);
        size = ScreenUtil.dp2px(context, a.getInt(R.styleable.pager_indicator_dot_size, DEFAULT_DOT_SIZE));
        space = ScreenUtil.dp2px(context, a.getInt(R.styleable.pager_indicator_dot_space, DEFAULT_DOT_SPACE));
        back_color = a.getColor(R.styleable.pager_indicator_dot_back, DEFAULT_BACK_COLOR);
        fore_color = a.getColor(R.styleable.pager_indicator_dot_fore, DEFAULT_FORE_COLOR);
        a.recycle();
    }

    private void intPaint() {
        mBackPaint = new Paint();
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStrokeWidth(ScreenUtil.dp2px(context, 1.0F));
        mBackPaint.setColor(back_color);

        mForePaint = new Paint();
        mForePaint.setStyle(Paint.Style.FILL);
        mForePaint.setAntiAlias(true);
        mForePaint.setStrokeWidth(ScreenUtil.dp2px(context, 1.0F));
        mForePaint.setColor(fore_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pos_x, pos_y;
        for (int i = 0; i < number; i++) {
            pos_x = size + (size * 2 + space) * i;
            pos_y = size;
            canvas.drawCircle(pos_x, pos_y, size, mBackPaint);
        }
        pos_x = (mOffset + mPos) * (size * 2 + space) + size;
        canvas.drawCircle(pos_x, size, size, mForePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(size * 2 * number + space * (number - 1), size * 2);
    }

    /**
     * 设置对应ViewPager，如果不调用则不显示
     */
    public void setViewPager(ViewPager pager) {
        if (pager != null && pager.getAdapter() != null && pager.getAdapter().getCount() > 0) {
            setVisibility(VISIBLE);
            this.number = pager.getAdapter().getCount();
            invalidate();

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setOffset(position, positionOffset);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    /**
     * 设置前景小圆点位置
     *
     * @param position       当前位置
     * @param positionOffset 当前偏移的百分比
     */
    private void setOffset(int position, float positionOffset) {
        this.mPos = position;
        this.mOffset = positionOffset;
        postInvalidate();
    }
}
