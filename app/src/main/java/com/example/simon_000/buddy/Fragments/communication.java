package com.example.simon_000.buddy.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.simon_000.buddy.MainActivity;
import com.example.simon_000.buddy.R;
import com.example.simon_000.buddy.TCPConnection;
import com.example.simon_000.buddy.customs.GroupAdapter;
import com.example.simon_000.buddy.customs.NameAdapter;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class communication extends Fragment {
    private ListView list;
    private EditText groupet;
    private EditText nameet;
    private ListView memberList;
    private Button btsend, btUpdate;
    private GroupAdapter Groupadapter;
    private NameAdapter Nameadapter;
    public static ArrayList<String> groupsList = new ArrayList<String>();
    public static ArrayList<String> namesList = new ArrayList<String>();


    public communication() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        getGroupInfo();
        initiateVariables(view);

        return view;
    }

    private void initiateVariables(View view) {

        list = (ListView) view.findViewById(R.id.grouplistView);
//        groupsList = new ArrayList<String>();
//        namesList = new ArrayList<String>();
        groupet = (EditText) view.findViewById(R.id.etgroupName);
        nameet = (EditText) view.findViewById(R.id.etUsername);
        btsend = (Button) view.findViewById(R.id.btSend);
        btUpdate = (Button) view.findViewById(R.id.btUpdate);
        groupet.setText("MadKim");

        (getActivity()).runOnUiThread(new Runnable() {
            public void run() {
        Groupadapter = new GroupAdapter(getActivity(), R.layout.row, groupsList);
        list.setAdapter(Groupadapter);
        list.setOnItemClickListener(new listListener());
        btsend.setOnClickListener(new Listener());
        btUpdate.setOnClickListener(new ListenerUpdate());
            }
        });
    }

    public void getGroupInfo() {
        ((MainActivity) getActivity()).getGroups();
    }

    private class Listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            sc.startThreadCommunication();
            String group = groupet.getText().toString();
            String name = nameet.getText().toString();
//            sending query to server
            if (group.isEmpty() || name.isEmpty()) {
                Toast.makeText(getActivity(), "You need to fill in all the fields.", Toast.LENGTH_LONG).show();
            } else {
                ((MainActivity) getActivity()).registerGroup(groupet.getText().toString(), nameet.getText().toString());
                getGroupInfo();
                (getActivity()).runOnUiThread(new Runnable() {
                    public void run() {
                        Groupadapter.notifyDataSetChanged();
                    }
                });
                //hides keyboard
                nameet.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameet.getWindowToken(), 0);
                //show user info
                Toast.makeText(getActivity(), "Successfully registered to: " + groupet.getText().toString() +
                        " with username: " + nameet.getText().toString(), Toast.LENGTH_LONG).show();
                groupet.setText("");
                nameet.setText("");
            }
        }
    }

    private class ListenerUpdate implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            (getActivity()).runOnUiThread(new Runnable() {
                public void run() {
                    Groupadapter.notifyDataSetChanged();
                }
            });
        }
    }

    //REVENUES list onClick LISTENER
    private class listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            // ListView Clicked item index
            int itemPosition = position;
            ((MainActivity) getActivity()).getMembers(groupsList.get(itemPosition).toString());
            (getActivity()).runOnUiThread(new Runnable() {
                public void run() {
                    //open selected finanse and show more information
                    Dialog d = new Dialog(getActivity());
                    String title = getResources().getString(R.string.dialogTitle);
                    d.setTitle(title);
                    d.setCanceledOnTouchOutside(true);
                    //inserting xml file in Dialog
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View infoLayout = factory.inflate(R.layout.dialog, null);
                    memberList = (ListView) infoLayout.findViewById(R.id.memberList);
                    Button updatebt = (Button) infoLayout.findViewById(R.id.updateDialogBt);
                    updatebt.setOnClickListener(new updateDialogListener());
                    d.setContentView(infoLayout);
                    Nameadapter = new NameAdapter(getActivity(), R.layout.rownames, namesList);
                    memberList.setAdapter(Nameadapter);
                    Nameadapter.notifyDataSetChanged();
                    d.show();
                }
            });


        }

        private class updateDialogListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                (getActivity()).runOnUiThread(new Runnable() {
                    public void run() {
                        Nameadapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
