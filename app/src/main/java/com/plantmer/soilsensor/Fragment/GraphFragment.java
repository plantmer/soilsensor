package com.plantmer.soilsensor.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;

import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.plantmer.soilsensor.util.CustomValueFormatter;
import com.plantmer.soilsensor.util.DataObj;
import com.plantmer.soilsensor.util.XAxisValueFormatter;
import com.plantmer.soilsensor.util.YAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;
    }

    public void append(String[] split){
        if(split.length==4){ // readings dateTime, float dp, float ec, float temp, float vwc
            DataObj dob = new DataObj(System.currentTimeMillis(),Float.valueOf(split[0]),Float.valueOf(split[1]),Float.valueOf(split[2]),Float.valueOf(split[3]));
            updatez(dob);
        }else if(split.length==5){ //readings with date
            DataObj dob = new DataObj(System.currentTimeMillis(),Float.valueOf(split[1]),Float.valueOf(split[2]),Float.valueOf(split[3]),Float.valueOf(split[4]));
            updatez(dob);
        }
    }

    private void updatez(DataObj dob) {
        mForecastList.add(dob);
        if(mForecastList.size()>200){
            mForecastList.remove(0);
        }
        updateUI();
    }

    public GraphFragment() {
        // Required empty public constructor
    }
    private LineChart mDpChart;
    private LineChart mEcChart;
    private LineChart mTempChart;
    private LineChart mVwcChart;
    private String[] mDatesArray;

    private CustomValueFormatter mValueFormatter;
    private YAxisValueFormatter mYAxisFormatter;
    public List<DataObj> mForecastList=new ArrayList<>();
    Description ee = new Description();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }


    @Override
    public void onViewCreated(View vv, Bundle savedInstanceState){

            ee.setText("");
            mForecastList.add(new DataObj(System.currentTimeMillis()));
        mValueFormatter = new CustomValueFormatter();
        mYAxisFormatter = new YAxisValueFormatter();
        mTempChart = (LineChart) getActivity().findViewById(R.id.temp_chart);
        mDpChart = (LineChart) getActivity().findViewById(R.id.dp_chart);
        mEcChart = (LineChart) getActivity().findViewById(R.id.ec_chart);
        mVwcChart = (LineChart) getActivity().findViewById(R.id.vwc_chart);
        TextView temperatureLabel = (TextView) getActivity().findViewById(R.id.graphs_temp_label);
        temperatureLabel.setText("Temperature");
        TextView dpLabel = (TextView) getActivity().findViewById(R.id.graphs_dp_label);
        dpLabel.setText("Dielectric permittivity");
        TextView ecLabel = (TextView) getActivity().findViewById(R.id.graphs_ec_label);
        ecLabel.setText("Electrical Conductivity");
        TextView vwcLabel = (TextView) getActivity().findViewById(R.id.graphs_vwc_label);
        vwcLabel.setText("Water Content");

        updateUI();
        // Inflate the layout for this fragment
    }
    private void setTempChart() {
        mTempChart.setDescription(ee);
        mTempChart.setDrawGridBackground(false);
        mTempChart.setTouchEnabled(true);
        mTempChart.setDragEnabled(true);
        mTempChart.setMaxHighlightDistance(300);
        mTempChart.setPinchZoom(true);
        mTempChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mTempChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mTempChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mTempChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float temperatureDay = mForecastList.get(i).getTemp();
            entries.add(new Entry(i, temperatureDay));
        }

        LineDataSet set;
        if (mTempChart.getData() != null) {
            mTempChart.getData().removeDataSet(mTempChart.getData().getDataSetByIndex(
                    mTempChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "Temp");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setDrawValues(false);
            set.setValueTextSize(12f);
            set.setColor(Color.parseColor("#E84E40"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mTempChart.setData(data);
        } else {
            set = new LineDataSet(entries, "Temp");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#E84E40"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mTempChart.setData(data);
        }
        mTempChart.invalidate();
    }

    private void setDpChart() {
        mDpChart.setDescription(ee);
        mDpChart.setDrawGridBackground(false);
        mDpChart.setTouchEnabled(true);
        mDpChart.setDragEnabled(true);
        mDpChart.setMaxHighlightDistance(300);
        mDpChart.setPinchZoom(true);
        mDpChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mDpChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mDpChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mDpChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float dp = mForecastList.get(i).getDp();
            entries.add(new Entry(i, dp));
        }

        LineDataSet set;
        if (mDpChart.getData() != null) {
            mDpChart.getData().removeDataSet(mDpChart.getData().getDataSetByIndex(
                    mDpChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "DP");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#00BCD4"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mDpChart.setData(data);
        } else {
            set = new LineDataSet(entries, "DP");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#00BCD4"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mDpChart.setData(data);
        }
        mDpChart.invalidate();
    }

    private void setEcChart() {
        mEcChart.setDescription(ee);
        mEcChart.setDrawGridBackground(false);
        mEcChart.setTouchEnabled(true);
        mEcChart.setDragEnabled(true);
        mEcChart.setMaxHighlightDistance(300);
        mEcChart.setPinchZoom(true);
        mEcChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mEcChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mEcChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mEcChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float values = mForecastList.get(i).getEc();
            entries.add(new Entry(i, values));
        }

        LineDataSet set;
        if (mEcChart.getData() != null) {
            mEcChart.getData().removeDataSet(mEcChart.getData().getDataSetByIndex(
                    mEcChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "EC");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#5677FC"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mEcChart.setData(data);
        } else {
            set = new LineDataSet(entries, "EC");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#5677FC"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mEcChart.setData(data);
        }
        mEcChart.invalidate();
    }

    private void setVwcChart() {
        mVwcChart.setDescription(ee);
        mVwcChart.setDrawGridBackground(false);
        mVwcChart.setTouchEnabled(true);
        mVwcChart.setDragEnabled(true);
        mVwcChart.setMaxHighlightDistance(300);
        mVwcChart.setPinchZoom(true);
        mVwcChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mVwcChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mVwcChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mVwcChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float values = mForecastList.get(i).getVwc();
            entries.add(new Entry(i, values));
        }

        LineDataSet set;
        if (mVwcChart.getData() != null) {
            mVwcChart.getData().removeDataSet(mVwcChart.getData().getDataSetByIndex(
                    mVwcChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "VWC");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#009688"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mVwcChart.setData(data);
        } else {
            set = new LineDataSet(entries, "VWC");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setLineWidth(2f);
            set.setValueTextSize(12f);
            set.setDrawValues(false);
            set.setColor(Color.parseColor("#009688"));
            set.setHighlightEnabled(false);
            set.setValueFormatter(mValueFormatter);

            LineData data = new LineData(set);
            mVwcChart.setData(data);
        }
        mVwcChart.invalidate();
    }

    private void formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        if (mForecastList != null) {
            int mSize = mForecastList.size();
            mDatesArray = new String[mSize];

            for (int i = 0; i < mSize; i++) {
                Date date = new Date(mForecastList.get(i).getDateTime());
                String day = format.format(date);
                mDatesArray[i] = day;
            }
        }
    }



    private void toggleValues() {
        for (IDataSet set : mTempChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mDpChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mEcChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mVwcChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        mTempChart.invalidate();
        mDpChart.invalidate();
        mEcChart.invalidate();
        mVwcChart.invalidate();
    }

    private void toggleYAxis() {
        mTempChart.getAxisLeft().setEnabled(!mTempChart.getAxisLeft().isEnabled());
        mDpChart.getAxisLeft().setEnabled(!mDpChart.getAxisLeft().isEnabled());
        mEcChart.getAxisLeft().setEnabled(!mEcChart.getAxisLeft().isEnabled());
        mVwcChart.getAxisLeft().setEnabled(!mVwcChart.getAxisLeft().isEnabled());
        mTempChart.invalidate();
        mDpChart.invalidate();
        mEcChart.invalidate();
        mVwcChart.invalidate();
    }


    private void updateUI() {
        setTempChart();
        setDpChart();
        setEcChart();
        setVwcChart();
    }
}
