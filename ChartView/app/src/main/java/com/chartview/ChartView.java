package com.chartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.chartview.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线图View
 * <p/>
 * Created By: wushan
 */
public class ChartView extends View {

	public int XPoint; // 原点的X坐标
	public int YPoint; // 原点的Y坐标
	public int XScale = 55; // X的刻度长度
	public int YScale = 40; // Y的刻度长度
	public int XLength; // X轴的长度
	public int YLength; // Y轴的长度
	public String[] XLabel; // X的刻度
	public String[] YLabel; // Y的刻度
	// public String[] Data; // 数据
	private List<Point> Data = new ArrayList<Point>();

	public ChartView(Context context) {
		super(context);

	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, List<Point> AllData) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// 获取父级控件对象
		View parentView = (View) getParent();
		int parentWidth = parentView.getRight() - parentView.getLeft();
		int parentHeight = parentView.getBottom() - parentView.getTop();

		// 设置X轴相关
		if (XLabel != null) {
			XLength = XScale * XLabel.length;
		} else {
			XLength = widthSize - 60;
		}
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		XPoint = parentView.getLeft() + (parentWidth - XLength) / 2;

		// 设置Y轴相关
		if (YLabel != null) {
			YLength = YScale * YLabel.length;
		} else {
			YLength = heightSize;
		}
		YPoint = heightSize - 120;

		// 重新设置框高
		setMeasuredDimension(widthSize, heightSize);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 重写onDraw方法

		// 坐标轴
		// canvas.drawColor(Color.WHITE);//设置背景颜色
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(MainActivity.COORDINATE_COLOR);// 颜色
		paint.setTextSize(MainActivity.COORDINATE_SIZE);

		Paint paintData = new Paint();
		paintData.setStyle(Paint.Style.STROKE);
		paintData.setAntiAlias(true);// 去锯齿
		paintData.setColor(MainActivity.POINT_COLOR);
		paintData.setTextSize(MainActivity.POINT_SIZE);
		// 设置Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 轴线
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, paint); // 刻度
			try {
				canvas.drawText(YLabel[i], XPoint - 22, YPoint - i * YScale + 5, paint); // 文字
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 6, paint); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 6, paint);
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线

		for (int i = 0; i * XScale < XLength; i++) {
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 5, paint); // 刻度
			try {
				
				canvas.drawText(XLabel[i], XPoint + i * XScale - 10, YPoint + 20, paint); // 文字
				// 数据值--折线
				paint.setTextSize(MainActivity.POINT_SIZE);
				if (i > 0 && YCoord(Data.get(i - 1).getValue()) != -999 && YCoord(Data.get(i).getValue()) != -999) // 保证有效数据
					canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data.get(i - 1).getValue()), XPoint + i * XScale,
							YCoord(Data.get(i).getValue()), paintData);
				canvas.drawCircle(XPoint + i * XScale, YCoord(Data.get(i).getValue()), 2, paintData);

				canvas.drawText(Data.get(i).getMarker(), XPoint + i * XScale - 10, YCoord(Data.get(i).getValue()) - 10,
						paint); // 文字
				paint.setTextSize(MainActivity.COORDINATE_SIZE);
			} catch (Exception e) {
			}
		}

		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, paint); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, paint);
	}

	private float YCoord(float y0) // 计算绘制时的Y坐标，无数据时返回-999
	{
		float y = -999;

		try {
			return YPoint - y0 * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}
