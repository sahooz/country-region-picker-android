package com.sahooz.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by android on 3/14/2018.
 */

public class SideBar extends View {

    public final ArrayList<String> indexes = new ArrayList<>();
    private OnLetterChangeListener onLetterChangeListener;
    private Paint paint;
    private float textHeight;
    private int cellWidth;
    private int cellHeight;
    private int currentIndex = -1;
    private int letterColor;
    private int selectColor;
    private int letterSize;

    public SideBar(Context context) { this(context, null); }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SideBar, defStyleAttr, 0);
        letterColor = ta.getColor(R.styleable.SideBar_letterColor, Color.BLACK);
        selectColor = ta.getColor(R.styleable.SideBar_selectColor, Color.CYAN);
        letterSize = ta.getDimensionPixelSize(R.styleable.SideBar_letterSize, 24);
        ta.recycle();
        paint = new Paint();
        //消除锯齿
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);  //1.1---2   2.1--3
        String[] letters = {"A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        indexes.addAll(Arrays.asList(letters));
    }

    public void addIndex(String indexStr, int position) {
        indexes.add(position, indexStr);
        invalidate();
    }

    public void removeIndex(String indexStr) {
        indexes.remove(indexStr);
        invalidate();
    }

    public void setLetterSize(int letterSize) {
        if(this.letterSize == letterSize) return;
        this.letterSize = letterSize;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = getMeasuredWidth();
        cellHeight = getMeasuredHeight() / indexes.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setTextSize(letterSize);
        for (int i = 0; i < indexes.size(); i++) {
            String letter = indexes.get(i);
            float textWidth = paint.measureText(letter);
            float x = (cellWidth - textWidth) * 0.5f;
            float y = (cellHeight + textHeight) * 0.5f + cellHeight * i;

            if (i == currentIndex) {
                paint.setColor(selectColor);
            } else {
                paint.setColor(letterColor);
            }

            canvas.drawText(letter, x, y, paint);
        }
    }

    public OnLetterChangeListener getOnLetterChangeListener() {
        return onLetterChangeListener;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public String getLetter(int position) {
        if(position < 0 || position >= indexes.size()) return "";
        return indexes.get(position);
    }

    public interface OnLetterChangeListener {
        void onLetterChange(String letter);

        //手指抬起
        void onReset();
    }

    /**
     * 处理 按下 移动 手指抬起
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int downY = (int) event.getY();
                //获取当前索引
                currentIndex = downY / cellHeight;
                if (currentIndex < 0 || currentIndex > indexes.size() - 1) {

                } else {
                    if (onLetterChangeListener != null) {
                        onLetterChangeListener.onLetterChange(indexes.get(currentIndex));
                    }
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getY();
                //获取当前索引
                currentIndex = moveY / cellHeight;
                if (currentIndex < 0 || currentIndex > indexes.size() - 1) {

                } else {
                    if (onLetterChangeListener != null) {
                        onLetterChangeListener.onLetterChange(indexes.get(currentIndex));
                    }
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                currentIndex = -1;
                //手动刷新
                invalidate();
                //表示手指抬起了
                if (onLetterChangeListener != null) {
                    onLetterChangeListener.onReset();
                }
                break;
        }
        // 为了 能够接受  move+up事件
        return true;
    }
}
