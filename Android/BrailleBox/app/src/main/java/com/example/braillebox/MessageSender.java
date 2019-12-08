package com.example.braillebox;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author Issa
 */
public class MessageSender extends AsyncTask<ArrayList,Void,Void> {
    /*
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * 
     * The function is executed in the background where the information packet is sent using serversockets.
     * The serversocket is already opened from the server Pi, which connects with this socket using the port number and ip address entered from the user.
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
            printWriter.write(list.get(0)+","+list.get(1)+","+list.get(3)+","+list.get(4)+","+list.get(2));
            printWriter.flush();
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
