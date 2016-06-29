package com.me.whereistime.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qwtangwenqiang on 2016/5/19.
 */
public class CircleProgressBar extends View {
    private String center_text;
    private int maxProgress = 100;
    private int progress = 0;
    private int progressStrokeWidth = 2;
    private int marxArcStorkeWidth = 20;
    // 画圆所在的距形区域
    RectF oval;
    Paint paint;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        width = (width > height) ? height : width;
        height = (width > height) ? height : width;

        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(Color.WHITE); // 设置画笔颜色
        canvas.drawColor(Color.TRANSPARENT); // 白色背景
        paint.setStrokeWidth(progressStrokeWidth); // 线宽
        paint.setStyle(Paint.Style.STROKE); //空心 FILL为实心

        oval.left = marxArcStorkeWidth / 2; // 左上角x
        oval.top = marxArcStorkeWidth / 2; // 左上角y
        oval.right = width - marxArcStorkeWidth / 2; // 左下角x
        oval.bottom = height - marxArcStorkeWidth / 2; // 右下角y

        canvas.drawArc(oval, -90, 360, false, paint); // 绘制白色圆圈，即进度条背景
        paint.setColor(Color.rgb(0x57, 0x87, 0xb6));
        paint.setStrokeWidth(marxArcStorkeWidth);
        canvas.drawArc(oval, 90, ((float) progress / maxProgress) * 360,
                false, paint); // 绘制进度圆弧，这里是蓝色
//        //显示文字
//        paint.setStrokeWidth(1);
//        center_text = progress + "%";
//        int textHeight = height / 4;
//        paint.setTextSize(textHeight);
//        int textWidth = (int) paint.measureText(center_text, 0, center_text.length());
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawText(center_text, width / 2 - textWidth / 2, height / 2
//                + textHeight / 2, paint);

    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * 设置进度
     *
     * @param progress
     *            进度百分比
     */
    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }
}
