package com.plantmer.soilsensor.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
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
import com.plantmer.soilsensor.util.Utils;
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


    public GraphFragment() {
        // Required empty public constructor
    }
    private LineChart mTemperatureChart;
    private LineChart mWindChart;
    private LineChart mRainChart;
    private LineChart mSnowChart;
    private String[] mDatesArray;

    private CustomValueFormatter mValueFormatter;
    private YAxisValueFormatter mYAxisFormatter;
    public List<DataObj> mForecastList=new ArrayList<>();
    Description ee = new Description();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ee.setText("");

        mValueFormatter = new CustomValueFormatter();
        mYAxisFormatter = new YAxisValueFormatter();
        mTemperatureChart = (LineChart) getView().findViewById(R.id.temperature_chart);
        mWindChart = (LineChart) getView().findViewById(R.id.wind_chart);
        mRainChart = (LineChart) getView().findViewById(R.id.rain_chart);
        mSnowChart = (LineChart) getView().findViewById(R.id.snow_chart);
        TextView temperatureLabel = (TextView) getView().findViewById(R.id.graphs_temperature_label);
        temperatureLabel.setText(getString(R.string.label_temperature) +
                ", " +
                Utils.getTemperatureScale(this.getContext()));
        TextView windLabel = (TextView) getView().findViewById(R.id.graphs_wind_label);
        windLabel.setText(getString(R.string.label_wind) + ", " + Utils.getSpeedScale(this.getContext()));
        TextView rainLabel = (TextView) getView().findViewById(R.id.graphs_rain_label);
        rainLabel.setText(getString(R.string.label_rain) + ", " + getString(R.string.millimetre_label));
        TextView snowLabel = (TextView) getView().findViewById(R.id.graphs_snow_label);
        snowLabel.setText(getString(R.string.label_snow) + ", " + getString(R.string.millimetre_label));

        updateUI();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false);
    }
    private void setTemperatureChart() {
        mTemperatureChart.setDescription(ee);
        mTemperatureChart.setDrawGridBackground(false);
        mTemperatureChart.setTouchEnabled(true);
        mTemperatureChart.setDragEnabled(true);
        mTemperatureChart.setMaxHighlightDistance(300);
        mTemperatureChart.setPinchZoom(true);
        mTemperatureChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mTemperatureChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mTemperatureChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mTemperatureChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float temperatureDay = mForecastList.get(i).getTemperatureDay();
            entries.add(new Entry(i, temperatureDay));
        }

        LineDataSet set;
        if (mTemperatureChart.getData() != null) {
            mTemperatureChart.getData().removeDataSet(mTemperatureChart.getData().getDataSetByIndex(
                    mTemperatureChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "Day");
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
            mTemperatureChart.setData(data);
        } else {
            set = new LineDataSet(entries, "Day");
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
            mTemperatureChart.setData(data);
        }
        mTemperatureChart.invalidate();
    }

    private void setWindChart() {
        mWindChart.setDescription(ee);
        mWindChart.setDrawGridBackground(false);
        mWindChart.setTouchEnabled(true);
        mWindChart.setDragEnabled(true);
        mWindChart.setMaxHighlightDistance(300);
        mWindChart.setPinchZoom(true);
        mWindChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mWindChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mWindChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mWindChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float wind = Float.parseFloat(mForecastList.get(i).getWindSpeed());
            entries.add(new Entry(i, wind));
        }

        LineDataSet set;
        if (mWindChart.getData() != null) {
            mWindChart.getData().removeDataSet(mWindChart.getData().getDataSetByIndex(
                    mWindChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "Wind");
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
            mWindChart.setData(data);
        } else {
            set = new LineDataSet(entries, "Wind");
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
            mWindChart.setData(data);
        }
        mWindChart.invalidate();
    }

    private void setRainChart() {
        mRainChart.setDescription(ee);
        mRainChart.setDrawGridBackground(false);
        mRainChart.setTouchEnabled(true);
        mRainChart.setDragEnabled(true);
        mRainChart.setMaxHighlightDistance(300);
        mRainChart.setPinchZoom(true);
        mRainChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mRainChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mRainChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mRainChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float values = Float.parseFloat(mForecastList.get(i).getRain());
            entries.add(new Entry(i, values));
        }

        LineDataSet set;
        if (mRainChart.getData() != null) {
            mRainChart.getData().removeDataSet(mRainChart.getData().getDataSetByIndex(
                    mRainChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "Rain");
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
            mRainChart.setData(data);
        } else {
            set = new LineDataSet(entries, "Rain");
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
            mRainChart.setData(data);
        }
        mRainChart.invalidate();
    }

    private void setSnowChart() {
        mSnowChart.setDescription(ee);
        mSnowChart.setDrawGridBackground(false);
        mSnowChart.setTouchEnabled(true);
        mSnowChart.setDragEnabled(true);
        mSnowChart.setMaxHighlightDistance(300);
        mSnowChart.setPinchZoom(true);
        mSnowChart.getLegend().setEnabled(false);

        formatDate();
        XAxis x = mSnowChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setValueFormatter(new XAxisValueFormatter(mDatesArray));

        YAxis yLeft = mSnowChart.getAxisLeft();
        yLeft.setEnabled(true);
        yLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawGridLines(true);
        yLeft.enableGridDashedLine(5f, 10f, 0f);
        yLeft.setGridColor(Color.parseColor("#333333"));
        yLeft.setXOffset(15);
        yLeft.setValueFormatter(mYAxisFormatter);

        mSnowChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mForecastList.size(); i++) {
            float values = Float.parseFloat(mForecastList.get(i).getSnow());
            entries.add(new Entry(i, values));
        }

        LineDataSet set;
        if (mSnowChart.getData() != null) {
            mSnowChart.getData().removeDataSet(mSnowChart.getData().getDataSetByIndex(
                    mSnowChart.getData().getDataSetCount() - 1));
            set = new LineDataSet(entries, "Snow");
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
            mSnowChart.setData(data);
        } else {
            set = new LineDataSet(entries, "Snow");
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
            mSnowChart.setData(data);
        }
        mSnowChart.invalidate();
    }

    private void formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE", Locale.getDefault());
        if (mForecastList != null) {
            int mSize = mForecastList.size();
            mDatesArray = new String[mSize];

            for (int i = 0; i < mSize; i++) {
                Date date = new Date(mForecastList.get(i).getDateTime() * 1000);
                String day = format.format(date);
                mDatesArray[i] = day;
            }
        }
    }



    private void toggleValues() {
        for (IDataSet set : mTemperatureChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mWindChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mRainChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        for (IDataSet set : mSnowChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        mTemperatureChart.invalidate();
        mWindChart.invalidate();
        mRainChart.invalidate();
        mSnowChart.invalidate();
    }

    private void toggleYAxis() {
        mTemperatureChart.getAxisLeft().setEnabled(!mTemperatureChart.getAxisLeft().isEnabled());
        mWindChart.getAxisLeft().setEnabled(!mWindChart.getAxisLeft().isEnabled());
        mRainChart.getAxisLeft().setEnabled(!mRainChart.getAxisLeft().isEnabled());
        mSnowChart.getAxisLeft().setEnabled(!mSnowChart.getAxisLeft().isEnabled());
        mTemperatureChart.invalidate();
        mWindChart.invalidate();
        mRainChart.invalidate();
        mSnowChart.invalidate();
    }


    private void updateUI() {
        setTemperatureChart();
        setWindChart();
        setRainChart();
        setSnowChart();
    }
}
