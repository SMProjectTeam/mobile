package pl.team.sm_project.budgetmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_text_email;
    private EditText edit_text_password;

    private Button button_login;
    private Button button_view;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_text_email = (EditText) findViewById(R.id.editTextEmail);
        edit_text_password = (EditText) findViewById(R.id.editTextPassword);

        button_login = (Button) findViewById(R.id.buttonLogin);
        button_login.setOnClickListener(this);
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v == button_login){
            return;
        }
    }
}
