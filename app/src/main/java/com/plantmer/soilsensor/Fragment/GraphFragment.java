package com.plantmer.soilsensor.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;

import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment  implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;
    }
    private Button genButton;
    private Button genPrev;
    private Button genNext;
    private Random rnd= new Random();
    boolean last = true;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.genButton:
                DataObj dob = new DataObj(System.currentTimeMillis(),rnd.nextInt(20)+10,rnd.nextInt(20)+10,rnd.nextInt(20)+10,rnd.nextInt(20)+10);
                updatez(dob);
                break;
            case R.id.genPrev:
                end = start;
                start = start - range;
                last= false;
                updateRange();
                break;
            case R.id.genNext:
                start = end;
                end = end+range;
                if(end>System.currentTimeMillis()){
                    end = System.currentTimeMillis();
                    start = end - range;
                    last = true;
                }
                updateRange();
                break;
        }
    }
    public void updateRange(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mForecastList = main.getDb().dataDao().getRange(start,end);
                Log.i("GF","mForecastList.size:"+mForecastList.size()+" start:"+new Date(start)+" send:"+new Date(end));
                if(mForecastList.size()==0){
                    mForecastList.add(new DataObj(System.currentTimeMillis()));
                }
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
                return null;
            }
        }.execute();
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

    private void updatez(final DataObj dob) {
        if(last) {
            mForecastList.add(dob);
            if (mForecastList.size() > 200) {
                mForecastList.remove(0);
            }
            updateUI();
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                main.getDb().dataDao().insertAll(dob);
                return null;
            }
        }.execute();
    }
//        <string-array name="time_range_array">
//            <item>5m</item>
//            <item>15m</item>
//            <item>1h</item>
//            <item>6h</item>
//            <item>12h</item>
//            <item>24h</item>
//            <item>2d</item>
//            <item>7d</item>
//            <item>30d</item>
//        </string-array>
    long min = 1000*60*60;
    long range = 5*min;;
    long end=System.currentTimeMillis();
    long start=end-range;
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (pos){
            case 0://5m
                range = 5*min;
                break;
            case 1://15m
                range = 15*min;
                break;
            case 2://1h
                range = 60*min;
                break;
            case 3://6h
                range = 6*60*min;
                break;
            case 4://12h
                range = 12*60*min;
                break;
            case 5://24h
                range = 24*60*min;
                break;
            case 6://2d
                range = 2*24*60*min;
                break;
            case 7://7d
                start = 7*24*60*min;
                break;
            case 8://30d
                range = 30*24*60*min;
                break;
        }
        end=System.currentTimeMillis();
        start = end - range;
        updateRange();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
        genButton = getActivity().findViewById(R.id.genButton);
        genButton.setOnClickListener(this);
        genPrev = getActivity().findViewById(R.id.genPrev);
        genPrev.setOnClickListener(this);
        genNext = getActivity().findViewById(R.id.genNext);
        genNext.setOnClickListener(this);

        ee.setText("");

        Spinner spinner = getActivity().findViewById(R.id.time_range_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_range_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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
        updateRange();
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
            mTempChart.getData().removeDataSet(0);
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
        mTempChart.notifyDataSetChanged();
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
            mDpChart.getData().removeDataSet(mDpChart.getData().getDataSetByIndex(0));
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
        mDpChart.notifyDataSetChanged();
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
            mEcChart.getData().removeDataSet(0);
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
        mEcChart.notifyDataSetChanged();
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
            mVwcChart.getData().removeDataSet(0);
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
        mVwcChart.notifyDataSetChanged();
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
