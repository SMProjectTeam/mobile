package pl.team.sm_project.budgetmanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ViewBudget extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_text_name;
    private TextView edit_text_date;
    private EditText edit_text_value;

    private Button button_update;
    private Button button_delete;

    private String id;
    private String type_id;
    private String source_id;

    private final List<String> sources_names = new ArrayList<String>();
    private final List<String> sources_ids = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        Intent intent = getIntent();

        id = intent.getStringExtra(WebConfig.BUDGET_ID);

        edit_text_name = (EditText) findViewById(R.id.editTextName);
        edit_text_date = (EditText) findViewById(R.id.editTextDate);
        edit_text_value = (EditText) findViewById(R.id.editTextValue);

        Spinner edit_spiner_source = (Spinner) findViewById(R.id.editBudgetSource);
        getSources();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sources_names);
        edit_spiner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.v("test", Integer.toString(position));
                source_id = sources_ids.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        edit_spiner_source.setAdapter(adapter);

        button_update = (Button) findViewById(R.id.buttonUpdate);
        button_delete = (Button) findViewById(R.id.buttonDelete);

        button_update.setOnClickListener(this);
        button_delete.setOnClickListener(this);

        getBudget();
    }

    private void getBudget() {
        class GetBudget extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBudget.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                showBudget(response);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler request_handler = new RequestHandler();

                return request_handler.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_GET, id);
            }
        }
        GetBudget get_budget_task = new GetBudget();
        get_budget_task.execute();
    }

    private void showBudget(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(WebConfig.TAG_JSON_ARRAY);
            JSONObject budget = result.getJSONObject(0);
            String name = budget.getString(WebConfig.BUDGET_TAG_NAME);
            String date = budget.getString(WebConfig.BUDGET_TAG_DATE);

            edit_text_name.setText(name);
            edit_text_date.setText(date);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSources() {
        class GetSources extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBudget.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                JSONObject json_object;

                try {
                    json_object = new JSONObject(json_string);
                    JSONArray result = json_object.getJSONArray(WebConfig.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject budget = result.getJSONObject(i);
                        String id = budget.getString(WebConfig.SOURCE_TAG_ID);
                        String name = budget.getString(WebConfig.SOURCE_TAG_NAME);
                        sources_ids.add(id);
                        sources_names.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler rh = new RequestHandler();

                return rh.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.SOURCE_GET_ALL, "");
            }
        }
        GetSources get_json_task = new GetSources();
        get_json_task.execute();
    }

    private void addBudget() {

        final String name = edit_text_name.getText().toString().trim();
        final String date = edit_text_date.getText().toString().trim();

        class AddBudget extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBudget.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                Toast.makeText(ViewBudget.this, response, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put(WebConfig.KEY_BUDGET_NAME, name);
                parameters.put(WebConfig.KEY_BUDGET_DATE, date);

                RequestHandler request_handler = new RequestHandler();
                return request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_ADD, parameters, null);
            }
        }

        AddBudget add_budget_task = new AddBudget();
        add_budget_task.execute();
    }

    private void updateBudget() {
        final String name = edit_text_name.getText().toString().trim();
        final String date = edit_text_date.getText().toString().trim();
        final String value = edit_text_value.getText().toString().trim();

        class UpdateBudget extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBudget.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                Toast.makeText(ViewBudget.this, response, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                HashMap<String, String> hash_map = new HashMap<>();
                hash_map.put(WebConfig.KEY_BUDGET_ID, id);
                hash_map.put(WebConfig.KEY_BUDGET_NAME, name);
                hash_map.put(WebConfig.KEY_BUDGET_DATE, date);
                hash_map.put(WebConfig.KEY_BUDGET_TYPE, type_id);
                hash_map.put(WebConfig.KEY_BUDGET_VALUE, value);
                hash_map.put(WebConfig.KEY_BUDGET_SOURCE, source_id);
                Log.v(WebConfig.KEY_BUDGET_ID, id);
                Log.v(WebConfig.KEY_BUDGET_NAME, name);
                Log.v(WebConfig.KEY_BUDGET_DATE, date);
                Log.v(WebConfig.KEY_BUDGET_SOURCE, source_id);
                Log.v(WebConfig.KEY_BUDGET_VALUE, value);
                Log.v(WebConfig.KEY_BUDGET_TYPE, type_id);
                RequestHandler request_handler = new RequestHandler();

                return request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_UPDATE, hash_map, id);
            }
        }

        UpdateBudget update_budget_task = new UpdateBudget();
        update_budget_task.execute();
    }

    private void deleteBudget() {
        class DeleteBudget extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBudget.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                Toast.makeText(ViewBudget.this, response, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler request_handler = new RequestHandler();
                HashMap<String, String> hash_map = new HashMap<>();
                hash_map.put(WebConfig.KEY_BUDGET_ID,id);

                return request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_DELETE, hash_map, id);
            }
        }

        DeleteBudget delete_budget_task = new DeleteBudget();
        delete_budget_task.execute();
    }

    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this record?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteBudget();
                        startActivity(new Intent(ViewBudget.this, ViewAllBudgets.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view == button_update) {
            updateBudget();
        }

        if(view == button_delete) {
            confirmDelete();
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_income:
                if (checked)
                    type_id = WebConfig.INCOME_ID;
                    break;
            case R.id.radio_expense:
                if (checked)
                    type_id = WebConfig.EXPENSE_ID;
                    break;
        }
    }

    public void showDatePickerDialog(View v) {
        showDatePicker();
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    final DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            edit_text_date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

}
