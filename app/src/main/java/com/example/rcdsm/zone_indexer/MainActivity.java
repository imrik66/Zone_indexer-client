package com.example.rcdsm.zone_indexer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.browse.MediaBrowser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rcdsm.zone_indexer.Entity.Coordinates;
import com.example.rcdsm.zone_indexer.Entity.Note;
import com.example.rcdsm.zone_indexer.Entity.User;
import com.example.rcdsm.zone_indexer.Entity.Zone;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polygon;

import com.example.rcdsm.zone_indexer.CustomPolygom.CustomPolygon;
import com.example.rcdsm.zone_indexer.CustomPolygom.Line;
import com.example.rcdsm.zone_indexer.CustomPolygom.Point;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements Note.NoteManagement, Zone.ZoneManagement, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    ListView list_notes;
    List<Note> notes;
    List<Zone> zones;
    NoteAdapter adapter;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    TextView userName;
    //test
    Coordinates testLocation;

    public static final String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.list_notes = (ListView)findViewById(R.id.main_list);
        this.notes = new ArrayList<Note>();
        Zone.getAllZones(this, this);

        userName = (TextView) findViewById(R.id.userName);
        userName.setText(User.currentUser.getMail());

        //Instanciate GoogleAPI client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //ListView
        this.list_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                Note.currentNote = notes.get(position);
                startActivity(intent);
            }
        });

        //For test
        this.testLocation = new Coordinates((float) 42.6986, (float) 2.8956);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

    public void createListAdaptater(){
        this.adapter = new NoteAdapter(this.notes, this);
        this.list_notes.setAdapter(this.adapter);
    }

    /* -------------------------------------- */
    /* Implements methods from Google Api --- */
    /* -------------------------------------- */

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        this.mLastLocation = location;
        Toast.makeText(this, "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude(), Toast.LENGTH_LONG).show();
        Log.d("Geolocalisation", location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed");
    }

    /* -------------------------------------- */
    /* ----------- End Google Api ----------- */
    /* -------------------------------------- */


    /* Appli main method */
    public boolean searchWitchZoneMatchesWithLocation(){

        boolean oneOrMoreMatches = false;
        //For each zones
        for(int i = 0; i < this.zones.size(); i++){

            Zone zone = zones.get(i);
            if(zone.getUsed() == 0){
                continue;
            }
            //Make a polygon
            CustomPolygon.Builder polyBuilder = CustomPolygon.Builder();
            List<Coordinates> coords= zone.getPointsArray();
            //Use all coordinates of the zone to make the polygon
            for(int y = 0; y < coords.size(); y++){
                Coordinates coord = coords.get(y);
                polyBuilder.addVertex(new Point(coord.getLatitude(), coord.getLongitude()));
            }
            CustomPolygon polygon = polyBuilder.build();
            Point point = new Point(0, 0);
            try {
                point = new Point(this.testLocation.getLatitude(), this.testLocation.getLongitude());
            }
            catch (Exception e){
                Log.d("a", "crash");
            }
            //Search if geolocation actual position is inside the polygon
            if(polygon.contains(point)){
                oneOrMoreMatches = true;
                Note.getAllNotesFromUserAndZone(User.currentUser.getUserId(), zone.getZoneId(), this, this);
            }
        }
        return oneOrMoreMatches;
    }

    /* -------------------------------------- */
    /* Implements methods from NoteManagement */
    /* -------------------------------------- */

    @Override
    public void noteActionSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void noteActionFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getNote(Note note) {

    }

    @Override
    public void getNotes(List<Note> notes) {
        Log.d("notes", notes.toString());
        this.notes.addAll(notes);
        this.createListAdaptater();
    }

    /* -------------------------------------- */
    /* ---------- End NoteManagement -------- */
    /* -------------------------------------- */

    /* -------------------------------------- */
    /* Implements methods from ZoneManagement */
    /* -------------------------------------- */

    @Override
    public void didDownloadAllZones(List<Zone> zones) {
        this.zones = zones;
        Toast.makeText(this, "Zones downloaded", Toast.LENGTH_LONG).show();
        if(!this.searchWitchZoneMatchesWithLocation()) {
            Toast.makeText(this, "No zone matched", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void zoneActionFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /* -------------------------------------- */
    /* ---------- End ZoneManagement -------- */
    /* -------------------------------------- */

}

