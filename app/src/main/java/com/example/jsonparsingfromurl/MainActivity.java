package com.example.jsonparsingfromurl;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list_view = (ListView)findViewById(R.id.listView);

        // calling the method here for execution
        new TestAsync().execute();
    }

    public class TestAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // using StringBuffer for immutability
            StringBuffer response = null;

            // Step 1: create a URL object
            URL url = null;

            try {
                url = new URL("https://ergast.com/api/f1/2008/5/results.json");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {

                // Step 2: HttpURLConnection is an android provided library
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

              //  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                response = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject object = new JSONObject(response.toString());
               // Log.e("Prints from the very beginning", object.toString());

                JSONObject mrDataObject = object.getJSONObject("MRData");
               // Log.e("After mrData: ", mrDataObject.toString());

                String total = mrDataObject.getString("total");
               // Log.e("total: ", total);

                JSONObject raceTable = mrDataObject.getJSONObject("RaceTable");
                // Log.e("Racetable: ", raceTable.toString());

                // here the RaceTable is an object of races and hence raceTable is used as the accessing object
                JSONArray racesArray = raceTable.getJSONArray("Races");
               // Log.e("Races: ", racesArray.toString());

                // in order to access the ArrayObject you need to have a for loop
                for (int i=0; i<racesArray.length(); i++) {
                    JSONObject object1 = racesArray.getJSONObject(i);
                    String round = object1.getString("round");
                    Log.e("Round: ", round );

                    // since Circuit object is an object of Races, we use the object of array
                    JSONObject circuitObject = object1.getJSONObject("Circuit");
                  //  Log.e("Circuit: ", circuitObject.toString());

                    // circuitId is an object of Circuit so,
                    String circuitID = circuitObject.getString("circuitId");
                    Log.e("Circuit ID: ", circuitID);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
