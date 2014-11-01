package com.example.simon_000.buddy.Fragments;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.simon_000.buddy.MainActivity;
import com.example.simon_000.buddy.R;
import com.example.simon_000.buddy.TCPConnection;
import com.example.simon_000.buddy.customs.members;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class myMap extends Fragment {
    private MapFragment map;
    private GoogleMap gmap;
    private LocationManager manager;


    public myMap() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gmap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (gmap != null) {
                initiateMap();
            }
        }
    }

    @Override
    public void onDestroyView() {

        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);

        if (f != null) {
            try {
                getFragmentManager().beginTransaction().remove(f).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_map, container, false);
        initiateButtons(view);
        initiateMap();

        return view;
    }

    private void initiateButtons(View view) {
        Button nor = (Button) view.findViewById(R.id.btNormal);
        Button Sate = (Button) view.findViewById(R.id.btSatellite);
        Button hyb = (Button) view.findViewById(R.id.btHybrid);
        nor.setOnClickListener(listener);
        Sate.setOnClickListener(listener);
        hyb.setOnClickListener(listener);


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btSatellite:
                    selectMapType(0);
                    break;
                case R.id.btNormal:
                    selectMapType(1);
                    break;
                case R.id.btHybrid:
                    selectMapType(2);
                    break;

            }
        }
    };

    private void initiateMap() {
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        gmap = map.getMap();
        //Setting maptype
        gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // setting up map service
        manager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Log.d("TEST", "INNAN CONNECTIOn");
        //connecting to server
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("TEST", "ON LOCATION CHGANGED");
//                String mypos = getResources().getString(R.string.myposition);
                double lng = location.getLongitude();
                double lat = location.getLatitude();
                //adds a marker to my gps position
                gmap.setMyLocationEnabled(true);
//                 adds a marker at my current position
//                    gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(mypos));

                sendMyPosOnMap(lng, lat);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("mygpsapp", "ProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    //    * Metod som tar emot ett antal användare + position och visar på kartan
//    *  På något sätt visa markerade användares identitet
    public void sendMyPosOnMap(double lng, double lat) {
        String id = TCPConnection.id;
        Log.d("TEST", "ID:  " + id+ "  lng "+lng+"   lat  "+lat);

        ((MainActivity) getActivity()).myPosition(id, lng, lat);
    }


    public void selectMapType(Integer mapType) {

        switch (mapType) {
            case 0:
                gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 1:
                gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
    }

    public void addMarker(members m) {
        gmap.addMarker(new MarkerOptions().position(new LatLng(m.getLatitude(), m.getLongitude())).title(m.getName()));
    }
}

