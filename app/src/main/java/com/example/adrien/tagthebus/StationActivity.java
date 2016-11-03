package com.example.adrien.tagthebus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Bdd.BddOpenHelper;
import Bdd.PhotoData;
import Bdd.DataManager;
import Bdd.Photo;
import Extra.CustomAdapter;
import Extra.ListItem;

public class StationActivity extends AppCompatActivity {

    private static final String TAG = "StationActivity";
    private final String EXTRA_ID = "station_id";
    private final String EXTRA_NAME = "station_name";
    static String current_id="";
    static String station_name="";

    private ListView lv_station;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.station_toolbar);
        setSupportActionBar(myToolbar);
        Log.e(TAG, TAG);

        Intent intent = getIntent();
        if (intent != null) {
            current_id = intent.getStringExtra(EXTRA_ID);
            station_name= intent.getStringExtra(EXTRA_NAME);
        }

        setTitle(station_name);

        lv_station = (ListView) findViewById(R.id.pic_list);

        updateListView();
    }


    @Override
    public void onResume(){
        super.onResume();
        updateListView();
    }

    /**
     * Get all image from current station (with station's id)
     * @return List<Photo>
     */
    public List<Photo> getImageData(){
        PhotoData dm = new PhotoData(this);
        List<Photo> l = null;
        try{
            dm.open(DataManager.READ);
            l = dm.getAllPhotos(BddOpenHelper.COLONNE_ID_STATION + "=" + current_id);
            dm.close();

        }catch(Exception e){
            Log.e(TAG, "Erreur recuperation photo du bien : "+e.toString());
        }

        return l;
    }


    /**
     * Start New picture activity.
     * @param item
     */
    public void startNewTakePictureActivity(MenuItem item) {

        Log.d(TAG, "startNewTakePictureActivity");

        Intent intent = new Intent(StationActivity.this, PictureActivity.class);
        intent.putExtra(EXTRA_ID, current_id);
        startActivity(intent);
    }


    private void updateListView(){
        ArrayList<ListItem> myList = new ArrayList<ListItem>();
        List<Photo> data = getImageData();

        for(int i=0; i<data.size(); i++){
            myList.add(new ListItem(data.get(i).getDate(),data.get(i).getNom(), data.get(i).getPhoto()));
        }

        Collections.reverse(myList);

        CustomAdapter adapter = new CustomAdapter(this, myList);
        lv_station.setAdapter(adapter);
        lv_station.setOnItemLongClickListener(adapter);
        lv_station.setOnItemClickListener(adapter);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                startNewTakePictureActivity(item);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}