package com.example.simon_000.buddy;

import android.util.Log;

import com.example.simon_000.buddy.Fragments.communication;
import com.example.simon_000.buddy.customs.members;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by tsroax on 2014-09-30.
 */

public class TCPConnection {
    private RunOnThread thread;
    private Receive receive;
    private MainActivity ma;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private InetAddress address;
    private int connectionPort;
    private String ip;
    private Exception exception;
    public static String id;
    public static ArrayList<members> memberList = new ArrayList<members>();


    public TCPConnection(String ip, int connectionPort, MainActivity ma) {
        this.ip = ip;
        this.connectionPort = connectionPort;
        thread = new RunOnThread();
        this.ma = ma;

    }

    public void connect() {
        thread.start();
        thread.execute(new Connect());
    }

    public void disconnect() {
        thread.execute(new Disconnect());
    }

    public void send(String expression) {
        thread.execute(new Send(expression));
    }

    private class Receive extends Thread {
        public void run() {
            String result;
            try {
                while (receive != null) {
                    result = (String) input.readUTF();
                    newMessage(result);
                }
            } catch (Exception e) { // IOException, ClassNotFoundException
                receive = null;
            }
        }
    }

    public void newMessage(final String answer) {
        ma.runOnUiThread(new Runnable() {
            public void run() {
                String message = answer;
                String type;

                JSONObject jObj = null;
                try {
                    Log.d("TEST", message);
                    jObj = new JSONObject(message);
                    type = jObj.getString("type");

                    if (type.equals("groups")) {
                        recevieGroups(jObj);
                    } else if (type.equals("register")) {
                        recevieID(jObj);
                    } else if (type.equals("members")) {
                        receiveMembers(jObj);
                    } else if (type.equals("location")) {
                        Log.d("TEST", "Location    "+message);

                    } else if (type.equals("locations")) {
                        receiveLocations(jObj);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void receiveLocations(JSONObject jObj) throws JSONException {
//        { ”type”:”locations”, ”group”:”NAME”, ”locations”:[ {”member”:”NAME”, ”longitude”:”LONGITUDE”, ”latitude”:”LATITUDE” }, … ] }
        JSONArray jArray = jObj.getJSONArray("location");

        for (int i = 0; i < jArray.length(); i++) {
            members m = new members();

            JSONObject jRealObject = jArray.getJSONObject(i);
            m.setName(jRealObject.getString("member"));
            m.setLongitude(Double.parseDouble(jRealObject.getString("longitude")));
            m.setLatitude(Double.parseDouble(jRealObject.getString("latitude")));
            memberList.add(m);
            Log.d("TEST", " memberNAMES : " + m.getName() + " lng: " + m.getLongitude() + " lat: " + m.getLatitude());
        }
        ma.updateMapMarkers(memberList);
    }

    public void receiveMembers(JSONObject jObj) throws JSONException {
        //        { “type”:”members”, “group”:”NAME”, “members”:[ {“member”:”NAME”},…] }

        JSONArray jArray = jObj.getJSONArray("members");

        for (int i = 0; i < jArray.length(); i++) {
            String n;

            JSONObject jRealObject = jArray.getJSONObject(i);
            n = jRealObject.getString("member");
            Log.d("TEST", " MembernamesBEFORE_ADD : " + n);

            communication.namesList.add(n);

            Log.d("TEST", " memberNAMES : " + n);
        }

    }

    public void recevieID(JSONObject jObj) throws JSONException {
        id = (jObj.getString("id"));
        Log.d("TEST", " ID : " + id);
    }

    public void recevieGroups(JSONObject jObj) throws JSONException {
        JSONArray jArray = jObj.getJSONArray("groups");

        for (int i = 0; i < jArray.length(); i++) {
            String g;
            JSONObject jRealObject = jArray.getJSONObject(i);
            g = (jRealObject.getString("group"));
            communication.groupsList.add(g);
            Log.d("TEST", " groupNames : " + g);
        }
    }

    public Exception getException() {
        Exception result = exception;
        exception = null;
        return result;
    }

    private class Connect implements Runnable {
        public void run() {
            try {
                Log.d("TCPConnection", "Connect-run");
                address = InetAddress.getByName(ip);
                Log.d("TCPConnection-Connect", "Skapar socket");
                socket = new Socket(address, connectionPort);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                output.flush();
                Log.d("TCPConnection-Connect", "Strömmar klara");
                newMessage("CONNECTED");
                receive = new Receive();
                receive.start();
            } catch (Exception e) { // SocketException, UnknownHostException
                Log.d("TCPConnection-Connect", e.toString());
                exception = e;
                newMessage("EXCEPTION");
            }
        }
    }

    public class Disconnect implements Runnable {
        public void run() {
            try {
                if (socket != null)
                    socket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                thread.stop();
                newMessage("CLOSED");
            } catch (IOException e) {
                exception = e;
                newMessage("EXCEPTION");
            }
        }
    }

    public class Send implements Runnable {
        private String exp;

        public Send(String exp) {
            this.exp = exp;
        }

        public void run() {
            try {
                output.writeUTF(exp);
                output.flush();
            } catch (IOException e) {
                exception = e;
                newMessage("EXCEPTION");
            }
        }
    }

}
