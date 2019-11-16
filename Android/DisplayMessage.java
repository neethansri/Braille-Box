package com.example.test_helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        ArrayList<String> message = intent.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);
        String display = "First Name: "+message.get(0)+"\n"+"Last Name: "+message.get(1)+"\n"+"IP Address: "+message.get(3)+"\n"+"Port Number: "+message.get(4)+"\n"+"Message Sent: "+message.get(2);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(display);


    }
}
