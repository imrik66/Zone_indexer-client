package com.example.rcdsm.zone_indexer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rcdsm.zone_indexer.Entity.User;


public class Login_activity extends ActionBarActivity implements View.OnClickListener, User.UserManagement{

    EditText login;
    EditText password;
    Button connection;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        login = (EditText) findViewById(R.id.login_activity_login);
        password = (EditText) findViewById(R.id.login_activity_password);
        connection = (Button) findViewById(R.id.login_activity_connection);
        connection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("click event", "click");
        if(v.getId() == R.id.login_activity_connection) {
            String login = this.login.getText().toString();
            String password = this.password.getText().toString();
            if (login.length() > 1 && password.length() > 1) {
                User.getUserByMail(login, this, this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getUserFailed() {
        Toast.makeText(this, "connection failed : bad mail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void getUserSuccess(User user) {

        User.currentUser = user;
        if(user.checkAuth(this.password.getText().toString())) {
            Toast.makeText(this, "connection success ! :", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Login_activity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "connection failed : bad password", Toast.LENGTH_LONG).show();
        }

    }
}
