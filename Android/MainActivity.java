package com.example.test_helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String publicFirstName;
    public String publicLastName;
    public String publicMessage;


    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void LogIn(View view) {
        Intent intent = new Intent(this, displayMessage.class);

        EditText firstname = (EditText) findViewById(R.id.firstName);
        EditText lastname = (EditText) findViewById(R.id.lastName);
//        EditText messagesent = (EditText) findViewById(R.id.message);

        publicFirstName = firstname.getText().toString();
        publicLastName = lastname.getText().toString();
//        publicMessage = messagesent.getText().toString();

        ArrayList<String> text = new ArrayList<String>();
        text.add(publicFirstName);
        text.add(publicLastName);
//        text.add(publicMessage);

        intent.putExtra(EXTRA_MESSAGE,text);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
