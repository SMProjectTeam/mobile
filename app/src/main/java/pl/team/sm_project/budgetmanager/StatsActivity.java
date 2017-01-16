package pl.team.sm_project.budgetmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private String json_string;

    private PieChart mChart, mChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart2 = (PieChart) findViewById(R.id.chart2);

        setupChart(mChart);
        setupChart(mChart2);

        getJSON();
    }

    private void setupChart(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        // getJSON

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(60f);
        l.setTextSize(18f);
        l.setFormLineWidth(20f);
        l.setWordWrapEnabled(true);
        l.setTextColor(Color.CYAN);

        // entry label styling
        chart.setEntryLabelColor(Color.MAGENTA);
        chart.setEntryLabelTextSize(16f);
    }

    private SpannableString generateCenterSpannableText(String title) {
        SpannableString s = new SpannableString("Budget Manager\n" + title);
        s.setSpan(new RelativeSizeSpan(1.3f), 0, 14, 0);

        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StatsActivity.this, getString(R.string.fetching), getString(R.string.wait), false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                json_string = response;
                showStats();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler rh = new RequestHandler();

                return rh.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.STATS_GET_ALL, "");
            }
        }

        GetJSON get_json_task = new GetJSON();
        get_json_task.execute();
    }

    private void showStats() {
        JSONObject json_object;
        JSONArray json_array;
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<ArrayList<PieEntry>> entriess = new ArrayList<>();

        Log.i("BM", "json_stringgg: " + json_string);

        try {
            json_array = new JSONArray(json_string);

            Log.i("BM", "json_array: " + json_array);
            Log.i("BM", "json_array.length(): " + json_array.length());

            for (int i = 0; i < json_array.length(); i++) {
                JSONObject stat = json_array.getJSONObject(i);

                Log.i("BM", "JSON stat in for i loop: " + stat);

                JSONArray data = stat.getJSONArray("js_chart_data");

                Log.i("BM", "js_chart_data: " + data);
                Log.i("BM", "js_chart_data.length(): " + data.length());

                entriess.add(new ArrayList<PieEntry>());

                for (int j = 0; j < data.length(); j++) {
                    Log.i("BM", "data.get(j): " + data.get(j));
                    Log.i("BM", "data.get(j).toString().length(): " + data.get(j).toString().length());
                    Log.i("BM", "data.optJSONArray(j).length(): " + data.optJSONArray(j).length());

                    Log.i("BM", "data.optJSONArray(j).get(0): " + data.optJSONArray(j).get(0));
                    Log.i("BM", "data.optJSONArray(j).get(1): " + data.optJSONArray(j).get(1));

                    entriess.get(i).add(new PieEntry((float) data.optJSONArray(j).getDouble(1), data.optJSONArray(j).getString(0)));

                    //entriess.get(i).add(new PieEntry((float) data.optJSONArray(j).getDouble(1), data.optJSONArray(j).getString(0)));

                    //entries.add(new PieEntry((float) data.optJSONArray(j).getDouble(1), data.optJSONArray(j).getString(0)));
                }

                //entries.add(new PieEntry((float) data, ((String) data)));
            }

            Log.i("BM", "entriess: " + entriess);

            /*json_object = new JSONObject(json_string);

            Log.i("BM", "json_object: " + json_object);
            Log.i("BM", "JSON_object.length(): " + json_object.length());

            JSONArray result = json_object.getJSONArray(WebConfig.TAG_JSON_ARRAY);

            Log.i("BM", "JSONArray result.length(): " + result.length());

            for (int i = 0; i < result.length(); i++) {
                JSONObject stat = result.getJSONObject(i);

                Log.i("BM", "JSON stat in for loop: " + stat);

                Object name = stat.getJSONObject("user").get("name");

                entries.add(new PieEntry((float) stat.getDouble("sum"), ((String) name)));
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setData(mChart, entriess.get(0), getString(R.string.expenditures_by_users));
        setData(mChart2, entriess.get(1), getString(R.string.expenditures_by_sources));
    }

    private void setData(PieChart chart, ArrayList<PieEntry> entries, String title) {
        PieDataSet dataSet = new PieDataSet(entries, title);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        chart.setCenterText(generateCenterSpannableText(title));

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLUE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_budgets) {
            startActivity(new Intent(this, ViewAllBudgets.class));
        }

        if(id == R.id.action_sources) {
            startActivity(new Intent(this, ViewAllSources.class));
        }

        if(id == R.id.action_logout) {
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}