package pl.team.sm_project.budgetmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ViewAllSources extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView list_view;

    private String json_string;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_sources);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_view = (ListView) findViewById(R.id.listView);
        list_view.setOnItemClickListener(this);
        getJSON();
    }


    private void showSource() {
        JSONObject json_object = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            json_object = new JSONObject(json_string);
            JSONArray result = json_object.getJSONArray(WebConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject budget = result.getJSONObject(i);
                JSONObject contentObject = budget.getJSONObject(WebConfig.SOURCE_TAG_TYPE);
                String type = contentObject.getString("name");
                String name = budget.getString(WebConfig.SOURCE_TAG_NAME);
                String value = budget.getString(WebConfig.SOURCE_TAG_VALUE);
                String comment = budget.getString(WebConfig.SOURCE_TAG_COMMENT);

                //tymczasowe bezczelne hacki
                if(comment == "null"){
                    comment = "";
                }

                if(value == "null"){
                    value = "";
                }

                HashMap<String, String> budgets = new HashMap<>();
                budgets.put(WebConfig.SOURCE_TAG_TYPE, type);
                budgets.put(WebConfig.SOURCE_TAG_NAME, name);
                budgets.put(WebConfig.SOURCE_TAG_VALUE, value);
                budgets.put(WebConfig.SOURCE_TAG_COMMENT, comment);

                list.add(budgets);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewAllSources.this, list, R.layout.sources_list_items,
                new String[]{WebConfig.SOURCE_TAG_TYPE, WebConfig.SOURCE_TAG_NAME, WebConfig.SOURCE_TAG_VALUE, WebConfig.SOURCE_TAG_COMMENT},
                new int[]{R.id.source_type, R.id.source_name, R.id.source_value, R.id.source_comment});

        list_view.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllSources.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                json_string = response;
                showSource();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler rh = new RequestHandler();
                String response = rh.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.SOURCE_GET_ALL, "");
                return response;
            }
        }
        GetJSON get_json_task = new GetJSON();
        get_json_task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewBudget.class);
        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
        String source_id = map.get(WebConfig.SOURCE_TAG_ID);
        intent.putExtra(WebConfig.SOURCE_ID, source_id);
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

        return super.onOptionsItemSelected(item);
    }
}
