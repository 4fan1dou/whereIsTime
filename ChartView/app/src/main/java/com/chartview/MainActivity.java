package com.chartview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.chartview.model.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ChartView mCrtView;
    private PieChartView pieChartView;
    private List<Point> pieceDataHolders1 = new ArrayList<>();
    private List<Point> pieceDataHolders2 = new ArrayList<>();
    private List<Point> pieceDataHolders3 = new ArrayList<>();
    private List<Point> pieceDataHolders4 = new ArrayList<>();

    // X轴刻度
    private final static String[] mXValue = {"Mon", "Tue", "Wed", "Thu", "Fir", "Sta", "Sun"};
    // Y轴刻度
    private final static String[] mYValue = {"", "20", "40", "60", "80", "100"};

    //折线图，坐标轴文字大小
    public final static int COORDINATE_SIZE = 12;
    //折线图，坐标点文字大小
    public final static int POINT_SIZE = 16;
    //折线图，折线颜色
    public final static int POINT_COLOR = Color.RED;
    //折线图，坐标轴颜色
    public final static int COORDINATE_COLOR = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        mCrtView = (ChartView) findViewById(R.id.line_chart);
        mCrtView.SetInfo(mXValue, mYValue, pieceDataHolders1);
        mCrtView.postInvalidate();

        pieChartView = (PieChartView) findViewById(R.id.pie_chart);
        pieChartView.setData(pieceDataHolders1);
        //设置字体大小，默认14
        pieChartView.setTextSize(16);

    }

    /**
     * 模拟数据
     */
    private void initData() {

        //由于目前未知最大值，默认100。
        pieceDataHolders1.add(new Point(9, getResources().getColor(R.color.mon), "小美"));
        pieceDataHolders1.add(new Point(22, getResources().getColor(R.color.tue), "明天"));
        pieceDataHolders1.add(new Point(36, getResources().getColor(R.color.wen), "bug!"));
        pieceDataHolders1.add(new Point(46, getResources().getColor(R.color.thu), "呵呵"));
        pieceDataHolders1.add(new Point(20, getResources().getColor(R.color.fir), "小京"));
        pieceDataHolders1.add(new Point(66, getResources().getColor(R.color.sta), "大黄"));
        pieceDataHolders1.add(new Point(100, getResources().getColor(R.color.sum), "花花"));

        //由于目前未知最大值，默认100。
        pieceDataHolders2.add(new Point(34, getResources().getColor(R.color.mon), "小美"));
        pieceDataHolders2.add(new Point(22, getResources().getColor(R.color.tue), "明天"));
        pieceDataHolders2.add(new Point(55, getResources().getColor(R.color.wen), "bug!"));
        pieceDataHolders2.add(new Point(40, getResources().getColor(R.color.thu), "呵呵"));
        pieceDataHolders2.add(new Point(20, getResources().getColor(R.color.fir), "小京"));
        pieceDataHolders2.add(new Point(38, getResources().getColor(R.color.sta), "大黄"));
        pieceDataHolders2.add(new Point(77, getResources().getColor(R.color.sum), "花花"));

        //由于目前未知最大值，默认100。
        pieceDataHolders3.add(new Point(9, getResources().getColor(R.color.mon), "小美"));
        pieceDataHolders3.add(new Point(32, getResources().getColor(R.color.tue), "明天"));
        pieceDataHolders3.add(new Point(19, getResources().getColor(R.color.wen), "bug!"));
        pieceDataHolders3.add(new Point(30, getResources().getColor(R.color.thu), "呵呵"));
        pieceDataHolders3.add(new Point(55, getResources().getColor(R.color.fir), "小京"));
        pieceDataHolders3.add(new Point(67, getResources().getColor(R.color.sta), "大黄"));
        pieceDataHolders3.add(new Point(60, getResources().getColor(R.color.sum), "花花"));

        //由于目前未知最大值，默认100。
        pieceDataHolders4.add(new Point(4, getResources().getColor(R.color.mon), "小美"));
        pieceDataHolders4.add(new Point(22, getResources().getColor(R.color.tue), "明天"));
        pieceDataHolders4.add(new Point(55, getResources().getColor(R.color.wen), "bug!"));
        pieceDataHolders4.add(new Point(30, getResources().getColor(R.color.thu), "呵呵"));
        pieceDataHolders4.add(new Point(44, getResources().getColor(R.color.fir), "小京"));
        pieceDataHolders4.add(new Point(66, getResources().getColor(R.color.sta), "大黄"));
        pieceDataHolders4.add(new Point(88, getResources().getColor(R.color.sum), "花花"));
    }

    public void clickMe(View v) {

        switch (v.getId()) {
            case R.id.button1:
                mCrtView.SetInfo(mXValue, mYValue, pieceDataHolders1);
                mCrtView.postInvalidate();
                pieChartView.setData(pieceDataHolders1);
                break;

            case R.id.button2:
                mCrtView.SetInfo(mXValue, mYValue, pieceDataHolders2);
                mCrtView.postInvalidate();
                pieChartView.setData(pieceDataHolders2);
                break;

            case R.id.button3:
                mCrtView.SetInfo(mXValue, mYValue, pieceDataHolders3);
                mCrtView.postInvalidate();
                pieChartView.setData(pieceDataHolders3);
                break;

            case R.id.button4:
                mCrtView.SetInfo(mXValue, mYValue, pieceDataHolders4);
                mCrtView.postInvalidate();
                pieChartView.setData(pieceDataHolders4);
                break;

            default:
                break;
        }

    }
}
