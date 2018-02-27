package by.md.gornak.homework.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class PageIndicatorView extends View{

    private static final int RADIUS = 6/*dp*/;

    private int currentPage;
    private int pageCount;

    private Paint activeDotColor;
    private Paint inactiveDotColor;

    private int dotRadius;
    private int dotPadding;
    private int width;
    private int height;

    private int previousIndicatorPosition;

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (dotPadding * (pageCount - 1)) + (dotRadius * 2 * pageCount);
        height = dotRadius * 2;

        // Try for a width based on our minimum
        int minWidth = getPaddingLeft() + getPaddingRight() + width;
        int width = resolveSizeAndState(minWidth, widthMeasureSpec, 0);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minHeight = getPaddingBottom() + getPaddingTop() + height;
        int height = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = (canvas.getWidth() - width) / 2;
        int top = canvas.getHeight() / 2;

        for (int i = 0; i < pageCount; i++) {
            canvas.drawCircle(
                    left + dotPadding * i + dotRadius * 2 * i + dotRadius,
                    top,
                    dotRadius,
                    inactiveDotColor
            );
        }

        canvas.drawCircle(
                left + previousIndicatorPosition,
                top,
                dotRadius,
                activeDotColor
        );
    }

    private void init() {
        float density = getResources().getDisplayMetrics().density;
        dotRadius = Math.round(RADIUS * density);
        dotPadding = dotRadius * 2;

        previousIndicatorPosition = dotRadius;

        activeDotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        activeDotColor.setColor(Color.DKGRAY);

        inactiveDotColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactiveDotColor.setColor(Color.GRAY);
    }

    public void setCurrentPage(@IntRange(from = 1) int currentPage) {
        setCurrentPage(currentPage, true);
    }

    public void setCurrentPage(@IntRange(from = 1) int currentPage, boolean animated) {
        this.currentPage = currentPage;

        if (animated) {
            int next = dotPadding * (this.currentPage - 1) + dotRadius * 2 * (this.currentPage - 1) + dotRadius;

            ValueAnimator animator = ValueAnimator.ofInt(previousIndicatorPosition, next);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    previousIndicatorPosition = (int) valueAnimator.getAnimatedValue();
                    invalidate();

                }
            });
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(250); //milliseconds
            animator.start();
        } else invalidate();
    }

    public void setPageCount(@IntRange(from = 1) int pageCount) {
        this.pageCount = pageCount;
        invalidate();
        requestLayout();
    }
}
