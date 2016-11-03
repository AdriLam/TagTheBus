package com.example.adrien.tagthebus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Extra.HttpHandler;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    ArrayList<HashMap<String, String>> stationList;

    // JSON Node names
    private static final String TAG_DATA = "data";
    private static final String TAG_NEAREST_STATION = "nearstations";
    private static final String TAG_DATA_ID = "id";
    private static final String TAG_DATA_STREET_NAME = "street_name";
    private static final String TAG_DATA_BUSES = "buses";

    //Extra variable
    final String EXTRA_ID = "station_id";
    final String EXTRA_NAME = "station_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        Log.e(TAG, TAG);

        //Set the listView
        stationList= new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        //Get data from url
        new GetStation().execute();

        //Make list's item clickable and lanuch next activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap a = (HashMap) parent.getItemAtPosition(position);
                Log.e(TAG, a.toString());
                String this_id = a.get("id").toString();
                String this_name = a.get("street_name").toString();
                Intent intent = new Intent(MainActivity.this, StationActivity.class);
                intent.putExtra(EXTRA_ID, this_id);
                intent.putExtra(EXTRA_NAME, this_name);

                startActivity(intent);
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject data = jsonObj.getJSONObject(TAG_DATA);
                    JSONArray stations = data.getJSONArray(TAG_NEAREST_STATION);

                    // looping through All stations
                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject c = stations.getJSONObject(i);

                        String id = c.getString(TAG_DATA_ID);
                        String name = c.getString(TAG_DATA_STREET_NAME);
                        String buses = c.getString(TAG_DATA_BUSES);

                        // tmp hash map for single station
                        HashMap<String, String> station = new HashMap<>();

                        // adding each child node to HashMap key => value
                        station.put("id", id);
                        station.put("street_name", name);
                        station.put("buses", buses);

                        // adding station to station list
                        stationList.add(station);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, stationList,
                    R.layout.list_main, new String[]{TAG_DATA_ID,TAG_DATA_STREET_NAME, TAG_DATA_BUSES}, new int[]{R.id.id,R.id.name,R.id.buses});

            lv.setAdapter(adapter);
        }
    }
}