package pl.team.sm_project.budgetmanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ViewSource extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_text_name;
    private EditText edit_text_value;

    private Button button_update;
    private Button button_delete;

    private String id;
    private String type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_source);

        Intent intent = getIntent();

        id = intent.getStringExtra(WebConfig.SOURCE_ID);

        edit_text_name = (EditText) findViewById(R.id.editTextSourceName);
        edit_text_value = (EditText) findViewById(R.id.editTextSourceValue);

        button_update = (Button) findViewById(R.id.buttonSourceUpdate);
        button_delete = (Button) findViewById(R.id.buttonSourceDelete);

        button_update.setOnClickListener(this);
        button_delete.setOnClickListener(this);

        getSource();
    }

    private void getSource() {
        class GetSource extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewSource.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                showSource(response);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler request_handler = new RequestHandler();

                return request_handler.sendGetRequest(WebConfig.GLOBAL_URL + WebConfig.SOURCE_GET, id);
            }
        }
        GetSource get_budget_task = new GetSource();
        get_budget_task.execute();
    }

    private void showSource(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(WebConfig.TAG_JSON_ARRAY);
            JSONObject source = result.getJSONObject(0);
            String name = source.getString(WebConfig.SOURCE_TAG_NAME);

            edit_text_name.setText(name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateSource() {
        final String name = edit_text_name.getText().toString().trim();
        final String value = edit_text_value.getText().toString().trim();

        class UpdateSource extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewSource.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                Toast.makeText(ViewSource.this, response, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                HashMap<String, String> hash_map = new HashMap<>();
                hash_map.put(WebConfig.KEY_SOURCE_ID, id);
                hash_map.put(WebConfig.KEY_SOURCE_NAME, name);
                hash_map.put(WebConfig.KEY_SOURCE_TYPE, type_id);
                hash_map.put(WebConfig.KEY_SOURCE_VALUE, value);
                RequestHandler request_handler = new RequestHandler();

                return request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.SOURCE_UPDATE, hash_map, id);
            }
        }

        UpdateSource update_source_task = new UpdateSource();
        update_source_task.execute();
    }

    private void deleteSource() {
        class DeleteSource extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewSource.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();
                Toast.makeText(ViewSource.this, response, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... parameters) {
                RequestHandler request_handler = new RequestHandler();
                HashMap<String, String> hash_map = new HashMap<>();
                hash_map.put(WebConfig.KEY_SOURCE_ID,id);

                return request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.SOURCE_DELETE, hash_map, id);
            }
        }

        DeleteSource delete_source_task = new DeleteSource();
        delete_source_task.execute();
    }

    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this record?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSource();
                        startActivity(new Intent(ViewSource.this, ViewAllSources.class));
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
            updateSource();
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

}
