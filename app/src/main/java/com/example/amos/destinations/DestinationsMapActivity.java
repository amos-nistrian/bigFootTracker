package com.example.amos.destinations;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DestinationsMapActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private List<Marker> markersArray = new ArrayList<>();
    private List<LatLng> squatchArray = new ArrayList<>();
    private LatLng curr;
    private LatLng prev;
    private Marker tmp;
    private CheckBox linesCheckBox;
    private boolean paths = true;
    List<Polyline> polylines = new ArrayList<>();


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        List<String> locations = new ArrayList<>();
        locations.add("Washington1");
        locations.add("Washington2");
        locations.add("Washington3");
        locations.add("Oregon");
        locations.add("Iowa");
        locations.add("Ontario");
        locations.add("Quebec");
        locations.add("Alaska");
        locations.add("Ecuador");
        locations.add("Amapa");
        locations.add("Burma");
        locations.add("Russia");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //linesCheckBox = (CheckBox) findViewById(R.id.polylinesCheckbox);

        Button clearMarkers = (Button) findViewById(R.id.clear_button);
        clearMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMarkers();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // updateLinesToggled();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN/*SATELLITE*/);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        squatchArray.add(new LatLng(47.392365, -123.153535)); // Washington
        squatchArray.add(new LatLng(46.178342, -122.433358)); // Washington
        squatchArray.add(new LatLng(47.573789, -121.461276)); // Washinton
        squatchArray.add(new LatLng(44.853738, -121.874243)); // oregon
        squatchArray.add(new LatLng(42.514765, -90.842826));  // Iowa
        squatchArray.add(new LatLng(50.225079, -84.302193));  // ontario
        squatchArray.add(new LatLng(47.507228, -74.539378));  // quebec
        squatchArray.add(new LatLng(61.891443, -135.945008)); // Alaska
        squatchArray.add(new LatLng(-0.902108, -75.878711));  // Ecuador
        squatchArray.add(new LatLng(1.869607, -57.123469));   // Amapa
        squatchArray.add(new LatLng(27.290119, 97.754638));   // Burma
        squatchArray.add(new LatLng(67.750339, 127.916200));  // Russia

        MarkerOptions options = new MarkerOptions();
        for (LatLng point : squatchArray) {
            options.position(point);
            options.title(point.toString());
            googleMap.addMarker(options);
        }



//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            Context context = getApplicationContext();
//
//            @Override
//            public void onMapClick(LatLng position) {
//                Toast.makeText(context, position.latitude + " : " + position.longitude, Toast.LENGTH_SHORT).show();
//            }
//        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                curr = point;
                if (paths) {
                    if (prev == null){
                        prev = curr;
                    }
                    //make lines between last two points
                    addlines();
                    prev = curr;
                }

                tmp = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(point.toString()));

                markersArray.add(tmp); // Add marker to an array
            }
        });

//        linesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                paths = true;
//            }
//        });
    }

    private void addlines() {
            polylines.add(this.mMap.addPolyline((new PolylineOptions().add(prev, curr))));
    }/////////////////////////////////////////////////// line options ^^^

    private void removeMarkers() {

        for (Marker marker : markersArray) {
            marker.remove();
        }

        for (Polyline line : polylines)
            line.remove();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DestinationsMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.amos.destinations/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DestinationsMap Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.amos.destinations/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        switch (item) {

            case "Washington1": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(0)));
                break;
            case "Washington2": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(1)));
                break;
            case "Washington3": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(2)));
                break;
            case "Oregon": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(3)));
                break;
            case "Iowa": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(4)));
                break;
            case "Ontario": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(5)));
                break;
            case "Quebec": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(6)));
                break;
            case "Alaska": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(7)));
                break;
            case "Ecuador": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(8)));
                break;
            case "Amapa": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(9)));
                break;
            case "Burma": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(10)));
                break;
            case "Russia": mMap.moveCamera(CameraUpdateFactory.newLatLng(squatchArray.get(11)));
                break;
        }


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

//    /**
//     * Called when the lines checkbox is clicked.
//     */
//    private void updateLinesToggled() {
//        updateLines();
//    }
//
//    private void updateLines(){
//        if (linesCheckBox.isChecked()) {
//            paths = true;
//        }
//    }
}
