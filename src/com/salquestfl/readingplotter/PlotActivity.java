package com.salquestfl.readingplotter;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

class PlotTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "ReadingPlotter";
    static final int DATA_COUNT = 512;
    /* widgets */
    private LineGraphView mGraphView = null;
    /* data for the graph */
    private GraphData mData = null;

    PlotTask(Activity activity) {
        /* initialize the graph and its data */
        mData = new GraphData(null, Color.RED, DATA_COUNT);
        mGraphView = (LineGraphView) activity.findViewById(R.id.WidgetGraph);
        mGraphView.addDataSet(mData);
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection conn = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return null;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return builder.toString();
    }

    // This executes in UI thread
    @Override
    protected void onPostExecute(final String readFeed) {
        try {
            JSONArray readingsJson = new JSONArray(readFeed);
            Log.i(TAG, "Number of entries " + readingsJson.length());
            for (int i = 0; i < readingsJson.length(); i++) {
                JSONObject readingJson = readingsJson.getJSONObject(i);
                float value = Float.parseFloat(readingJson.getString("value"));
                mData.appendValue(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Draw the values
        mGraphView.invalidate();
    }
}

public class PlotActivity extends Activity {
    
    static final String TAG = "ReadingPlotter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String deviceId = sharedPref.getString(SettingsActivity.DEVICE_ID, "");
        String url = "http://jreadings-polyglot.rhcloud.com/api/1/devices/" + deviceId + "/readings";
        //Log.e(TAG, url);
        new PlotTask(this).execute(url);
    }
}
