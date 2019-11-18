package com.example.test_helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.Message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Logged In", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void LogIn(View view) {
        Intent intent = new Intent(this, DisplayMessage.class);

        EditText firstname = (EditText) findViewById(R.id.firstName);
        EditText lastname = (EditText) findViewById(R.id.lastName);
        EditText messagesent = (EditText) findViewById(R.id.message);
        EditText ip = (EditText) findViewById(R.id.ip);
        EditText port = (EditText) findViewById(R.id.port);

        String s1 = firstname.getText().toString();
        String s2 = lastname.getText().toString();
        String s3 = messagesent.getText().toString();
        String s4 = ip.getText().toString();
        String s5 = port.getText().toString();


        ArrayList<String> text = new ArrayList<String>();
        text.add(s1);
        text.add(s2);
        text.add(s3);
        text.add(s4);
        text.add(s5);

        if (s3.length()!=0) {
            try {
                DatagramSocket socket1 = new DatagramSocket();
                byte[] data = s3.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(s4), Integer.getInteger(s5));
                socket1.send(packet);
                packet = new DatagramPacket(new byte[200], 200);
                socket1.receive(packet);
                socket1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
