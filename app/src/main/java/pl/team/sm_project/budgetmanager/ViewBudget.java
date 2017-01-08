package pl.team.sm_project.budgetmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewBudget extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_text_name;
    private EditText edit_text_date;

    private Button button_update;
    private Button button_delete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        Intent intent = getIntent();

        id = intent.getStringExtra(WebConfig.BUDGET_ID);

        edit_text_name = (EditText) findViewById(R.id.editTextName);
        edit_text_date = (EditText) findViewById(R.id.editTextDate);

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
                String response = request_handler.sendPostRequest(WebConfig.GLOBAL_URL + WebConfig.BUDGET_ADD, parameters, null);
                return response;
            }
        }

        AddBudget add_budget_task = new AddBudget();
        add_budget_task.execute();
    }

    private void updateBudget() {
        final String name = edit_text_name.getText().toString().trim();
        final String date = edit_text_date.getText().toString().trim();

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
}
