package pl.team.sm_project.budgetmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAllBudgets extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView list_view;

    private String json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_budgets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        list_view = (ListView) findViewById(R.id.listView);
        list_view.setOnItemClickListener(this);
        getJSON();
    }


    private void showBudget() {
        JSONObject json_object;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            json_object = new JSONObject(json_string);
            JSONArray result = json_object.getJSONArray(WebConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject budget = result.getJSONObject(i);
                JSONObject contentObject = budget.getJSONObject(WebConfig.BUDGET_TAG_TYPE);
                String type = contentObject.getString("name");
                String id = budget.getString(WebConfig.BUDGET_TAG_ID);
                String name = budget.getString(WebConfig.BUDGET_TAG_NAME);
                String value = budget.getString(WebConfig.BUDGET_TAG_VALUE);
                String date = budget.getString(WebConfig.BUDGET_TAG_DATE);
                String comment = budget.getString(WebConfig.BUDGET_TAG_COMMENT);

                // FIXME: tymczasowe bezczelne hacki
                if(comment == "null"){
                    comment = "";
                }

                HashMap<String, String> budgets = new HashMap<>();
                budgets.put(WebConfig.BUDGET_TAG_ID, id);
                budgets.put(WebConfig.BUDGET_TAG_TYPE, type);
                budgets.put(WebConfig.BUDGET_TAG_NAME, name);
                budgets.put(WebConfig.BUDGET_TAG_VALUE, value);
                budgets.put(WebConfig.BUDGET_TAG_DATE, date);
                budgets.put(WebConfig.BUDGET_TAG_COMMENT, comment);
                list.add(budgets);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewAllBudgets.this, list, R.layout.budget_list_items,
                new String[]{WebConfig.BUDGET_TAG_ID ,WebConfig.BUDGET_TAG_TYPE, WebConfig.BUDGET_TAG_NAME, WebConfig.BUDGET_TAG_VALUE, WebConfig.BUDGET_TAG_DATE, WebConfig.BUDGET_TAG_COMMENT},
                new int[]{R.id.budget_id, R.id.budget_type, R.id.budget_name, R.id.budget_value, R.id.budget_date, R.id.budget_comment});

        list_view.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllBudgets.this, getString(R.string.fetching), getString(R.string.wait), false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                json_string = response;
                showBudget();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler rh = new RequestHandler();

                return rh.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_GET_ALL, "");
            }
        }
        GetJSON get_json_task = new GetJSON();
        get_json_task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewBudget.class);
        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
        String budget_id = map.get(WebConfig.BUDGET_TAG_ID);
        intent.putExtra(WebConfig.BUDGET_ID, budget_id);
        startActivity(intent);
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
