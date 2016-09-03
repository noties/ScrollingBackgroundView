/*
 *   Copyright 2016 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package ru.noties.sbv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * A simple View that will tile supplied background creating
 * an `endless` background feeling.
 * Does not scroll by itself, call {@link #scrollBy(int, int)}
 * or {@link #scrollTo(int, int)} methods. Negative values are OK.
 * If you wish to get current values of scrollX or scrollY, call
 * {@link #scrollX()} or {@link #scrollY()}. Another methods
 * are needed because system methods {@link #getScaleX()} and
 * {@link #getScrollY()} are final.
 * Does not animate between scroll positions, so it\'s better
 * to call {@link #scrollBy(int, int)} with relatively small values,
 * ideally redirected from your own scroll container
 */
public class ScrollingBackgroundView extends View {

    /**
     * Simple listener to be notified when this view size has changed
     */
    public interface OnSizeChangedListener {
        /**
         * Will be triggered after {@link android.view.View#onSizeChanged(int, int, int, int)} is called
         * @param width new width of the view
         * @param height new height of the view
         */
        void onSizeChanged(int width, int height);
    }

    private Drawable mDrawable;

    private int mScrollX;
    private int mScrollY;

    private OnSizeChangedListener mOnSizeChangedListener;

    public ScrollingBackgroundView(Context context) {
        this(context, null);
    }

    public ScrollingBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollingBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        if (attributeSet != null) {
            final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollingBackgroundView);
            try {

                mScrollX = array.getDimensionPixelSize(R.styleable.ScrollingBackgroundView_sbv_scrollX, 0);
                mScrollY = array.getDimensionPixelSize(R.styleable.ScrollingBackgroundView_sbv_scrollY, 0);

                final Drawable drawable = array.getDrawable(R.styleable.ScrollingBackgroundView_sbv_drawable);
                setDrawable(drawable);

            } finally {
                array.recycle();
            }
        }
    }

    /**
     * Set the drawable object manually. There is also an XML attribute `sbv_drawable`
     * If provided via XML the intrinsic bounds will be used.
     * The drawable will be <i>tiled</i> to fill this view
     * @param drawable to be tiled and drawn as background
     */
    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        if (mDrawable != null) {
            final Rect rect = mDrawable.getBounds();
            if (rect == null
                    || rect.isEmpty()) {
                mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
            }
            setWillNotDraw(false);
        }
        postInvalidateOnAnimation();
    }

    /**
     * @return current {@link #mDrawable}
     */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     *
     * @param listener to be notified when the size changes, or NULL to stop listening
     * @see OnSizeChangedListener
     */
    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.mOnSizeChangedListener = listener;
    }

    @Override
    public void scrollBy(int x, int y) {
        if (y != 0 || x != 0) {
            mScrollX += x;
            mScrollY += y;
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mScrollY != y
                || mScrollX != x) {
            mScrollX = x;
            mScrollY = y;
            postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mOnSizeChangedListener != null) {
            mOnSizeChangedListener.onSizeChanged(w, h);
        }
    }

    /**
     * Getter for current {@link #mScrollX}
     * @return current {@link #mScrollX}
     */
    public int scrollX() {
        return mScrollX;
    }

    /**
     * Getter for current {@link #mScrollY}
     * @return current {@link #mScrollY}
     */
    public int scrollY() {
        return mScrollY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // to draw possible background, etc
        super.onDraw(canvas);

        final Drawable drawable = mDrawable;
        if (drawable == null) {
            return;
        }

        final int scrollX = mScrollX;
        final int scrollY = mScrollY;

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        final Rect rect = drawable.getBounds();

        final int drawableWidth = rect.width();
        final int drawableHeight = rect.height();

        final int startX = start(scrollX, drawableWidth);
        final int iterationsX = iterations(width, startX, drawableWidth);

        final int startY = start(scrollY, drawableHeight);
        final int iterationsY = iterations(height, startY, drawableHeight);

        final int save = canvas.save();
        try {

            canvas.translate(startX, startY);

            for (int x = 0; x < iterationsX; x++) {
                for (int y = 0; y < iterationsY; y++) {
                    drawable.draw(canvas);
                    canvas.translate(.0F, drawableHeight);
                }
                canvas.translate(drawableWidth, -(drawableHeight * iterationsY));
            }

        } finally {
            canvas.restoreToCount(save);
        }
    }

    private static int start(int scroll, int side) {

        final int start;

        final int modulo = Math.abs(scroll) % side;
        if (modulo == 0) {
            start = 0;
        } else if (scroll < 0) {
            start = -(side - modulo);
        } else {
            start = -modulo;
        }

        return start;
    }

    private static int iterations(int total, int start, int side) {
        final int diff = total - start;
        final int base = diff / side;
        return base + (diff % side > 0 ? 1 : 0);
    }
}
