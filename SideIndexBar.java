package com.haier.meetnewlife.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *  控件宽度最好大于100px，150为佳
 * Created by 27411 on 2018/8/2.
 */

public class SideIndexBar extends android.support.v7.widget.AppCompatTextView {

    private String words[] = {"A", "B","C", "D","E", "F","G", "H","I", "J","K", "L",
            "M", "N","O", "P","Q", "R","S", "T","U", "V","W", "X", "Y", "Z","#"};

    private Paint wordPaint, bgPaint, extraPaint, showPaint;
    private int wordWidth, wordHeight;
    private int currIndex = 0;  //默认索引位置
    private int offsetSpace = 80;    //偏移量--用来设置控件上下偏移量

    private OnWordChangeListener mListener;     //索引改变的监听
    public SideIndexBar(Context context) {
        super(context);
        init();
    }

    public SideIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wordPaint.setTextSize(getTextSize());
        wordPaint.setStyle(Paint.Style.FILL);   //必须设置，否则位置计算偏移
        wordPaint.setTextAlign(Paint.Align.CENTER);

        showPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        showPaint.setTextSize(50);
        showPaint.setColor(Color.WHITE);
        showPaint.setStyle(Paint.Style.FILL);   //必须设置，否则位置计算偏移
        showPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.rgb(255,121,0));

        extraPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        extraPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wordWidth = getMeasuredWidth();
        int height = getMeasuredHeight() - offsetSpace * 2;
        wordHeight = height / 27;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            if (i == currIndex){
//                canvas.drawCircle(wordWidth /2, wordHeight / 2 + i * wordHeight, wordHeight / 3, bgPaint);
                canvas.drawCircle(wordWidth / 4 * 3, wordHeight / 2 + i * wordHeight + offsetSpace, wordHeight / 3, bgPaint);
                wordPaint.setColor(Color.WHITE);
                if (showWord){
                    drawShowBg(canvas, i);
                }
            }else{
                wordPaint.setColor(Color.GRAY);
            }

            Paint.FontMetrics fontMetrics = wordPaint.getFontMetrics();
            float top = fontMetrics.top;    //为基线到字体上边框的距离
            float bottom = fontMetrics.bottom;  //为基线到字体下边框的距离
            int baseLineY = (int) (wordHeight / 2 - top/2 - bottom/2 + i * wordHeight);//基线中间点的y轴计算公式
            //x默认是这个字符串的左边在屏幕的位置，如果设置了paint.setTextAlign(Paint.Align.CENTER);那就是字符的中心，y是指定这个字符baseline在屏幕上的位置
//            canvas.drawText(words[i],wordWidth / 2,baseLineY,wordPaint);
            canvas.drawText(words[i],wordWidth / 4 * 3,baseLineY + offsetSpace,wordPaint);
        }

    }

    private boolean showWord = false;
    private void drawShowBg(Canvas canvas,int index){
        float x = wordWidth / 4 * 3 - wordHeight / 3;
        float y = wordHeight / 2 + index * wordHeight + offsetSpace;
        float radius = wordWidth / 4;
        RectF rectF = new RectF(radius + radius/4, y - radius, 3*radius + radius/4, y + radius);
        canvas.drawArc(rectF, 180, 90, true, extraPaint);
        canvas.drawCircle(radius + radius/4,y - 1*radius, radius, extraPaint);

        Paint.FontMetrics fontMetrics = showPaint.getFontMetrics();
        float top = fontMetrics.top;    //为基线到字体上边框的距离
        float bottom = fontMetrics.bottom;  //为基线到字体下边框的距离
        int baseLineY = (int) (y - top/2 - bottom/2 - radius);//基线中间点的y轴计算公式
        //x默认是这个字符串的左边在屏幕的位置，如果设置了paint.setTextAlign(Paint.Align.CENTER);那就是字符的中心，y是指定这个字符baseline在屏幕上的位置
        canvas.drawText(words[index],radius / 4 *5,baseLineY,showPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                showWord = true;
                float y = event.getY();
                int index = (int) ((y - offsetSpace) / wordHeight);
                if (index != currIndex){
                    currIndex = index;
                    if (mListener != null && index >= 0 && (index <= words.length -1)){
                        mListener.changeWord(words[index]);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                showWord = false;
                invalidate();   //用于取消显示的索引
                break;
        }
        return true;
    }

    public interface OnWordChangeListener {
        void changeWord(String word);
    }

    public void setWordChangeListener(OnWordChangeListener listener){
        this.mListener = listener;
    }

    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                currIndex = i;
                invalidate();
                return;
            }
        }
    }
}

