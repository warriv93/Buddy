package com.example.simon_000.buddy;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import com.example.simon_000.buddy.Fragments.NavigationDrawerFragment;
import com.example.simon_000.buddy.Fragments.communication;
import com.example.simon_000.buddy.Fragments.myMap;
import com.example.simon_000.buddy.customs.members;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private myMap tempMapfragment = new myMap();
    private Menu menu;
    private String inetAddress = "195.178.232.7";
    private Integer port = 7117;
    private TCPConnection connect;
//    private DataOutputStream output;
//    private DataInputStream input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startNavDrawer();

//      startService(new Intent(this, serviceCommunication.class));
        connect = new TCPConnection(inetAddress, port, this);
        connect.connect();
    }

    public void myPosition(String id, double lng, double lat) {
//        Ange position
//        { “type”:”location”, ”id”:”ID”, ”longitude”:”LONGITUDE”, “latitude”:”LATITUDE” }
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.beginObject().name("type").value("location").name("id").value(id).name("longitude").value(String.valueOf(lng)).name("latitude").value(String.valueOf(lat)).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect.send(stringWriter.toString());
        Log.d("TEST", "   "+stringWriter.toString());
    }



    public void getMembers(String group) {
//        Medlemmar i grupp
//        { “type”:”members”, “group”,”NAME” }
//        { “type”:”members”, “group”:”NAME”, “members”:[ {“member”:”NAME”},…] }
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.beginObject().name("type").value("members").name("group").value(group).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect.send(stringWriter.toString());
    }

    public void getGroups() {
//    Aktuella grupper
//    { ”type”:”groups” }
//    { “type”:”groups”, ”groups”:[ {”group”:”NAME”}, …] }
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.beginObject().name("type").value("groups").endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect.send(stringWriter.toString());
    }

    public void registerGroup(String groupName, String userName) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.beginObject().name("type").value("register").name("group").value(groupName).name("member").value(userName).endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect.send(stringWriter.toString());
    }

    public void updateMapMarkers(ArrayList<members> memberList) {
        for(members m: memberList){
            tempMapfragment.addMarker(m);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connect.disconnect();
    }


    private void startNavDrawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        communication fragment2 = new communication();


        switch (position) {
            case 0:
                // fragment1
                // use fragment transaction and add the fragment to the container
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();

                break;
            case 1:
                // fragment2
                fragmentTransaction.replace(R.id.container, tempMapfragment);
                fragmentTransaction.commit();

                break;
            case 2:
                // fragment2
                break;
            default:
                // fragment1
                // use fragment transaction and add the fragment to the container


                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();
        }
        //det som stod från början
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Setting maptype

//        switch(id){
//            case R.id.satellitemaptype:
//                tempMapfragment.selectMapType(0);
//                break;
//            case R.id.normalmaptype:
//                tempMapfragment.selectMapType(1);
//                break;
//            case R.id.hybridmaptype:
//                tempMapfragment.selectMapType(2);
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
