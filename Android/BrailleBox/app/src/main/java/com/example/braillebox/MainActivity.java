package com.example.braillebox;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText firstname, lastname, messagesent, ip, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();
    }
/**
 * MyServerThread is used to open a serversocket that allows information to be received from the server pi. 
 * The information sent from the server pi to the app is used to confirm that the message was received.
 * The Toast is the part where the confirmation message is displayed on the android device with the variable message received from the server pi.
 */
    class MyServerThread implements Runnable{
        Socket socket;
        ServerSocket serverSocket;
        InputStreamReader inputStreamReader;
        BufferedReader bufferReader;
        String message;
        Handler handler = new Handler();
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(1001);
                while(true) {
                    socket = serverSocket.accept();
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    bufferReader = new BufferedReader(inputStreamReader);
                    message = bufferReader.readLine();
                    System.out.println(message);

                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the input of the user to the MessageSender class
     *
     * @param v
     */
    public void send(View v){

        // Read the inputs
        firstname = (EditText) findViewById(R.id.firstName);
        lastname = (EditText) findViewById(R.id.lastName);
        messagesent = (EditText) findViewById(R.id.message);
        ip = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);


        ArrayList<String> text = new ArrayList<String>();

        // Convert inputs to strings
        text.add(firstname.getText().toString());
        text.add(lastname.getText().toString());
        text.add(messagesent.getText().toString());
        text.add(ip.getText().toString());
        text.add(port.getText().toString());

        // Send the packet
        MessageSender messagesender = new MessageSender();
        messagesender.execute(text);
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
