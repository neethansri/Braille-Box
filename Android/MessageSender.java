package com.example.braillebox;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MessageSender extends AsyncTask<ArrayList,Void,Void> {
    /*
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     */

    Socket socket;
    PrintWriter printWriter;

    @Override
    protected Void doInBackground(ArrayList... voids) {

        ArrayList<String> list = voids[0];
        System.out.println(list);

        try {
            socket = new Socket(InetAddress.getByName(list.get(3)), Integer.parseInt(list.get(4)));
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write("First Name: "+list.get(0)+", Last Name: "+list.get(1)+", IP Address: "+list.get(3)+", Port Number: "+list.get(4)+", Message Sent: "+list.get(2));
            printWriter.flush();
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
