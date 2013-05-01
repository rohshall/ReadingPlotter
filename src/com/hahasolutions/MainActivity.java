package com.hahasolutions;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.graphics.Color;
import android.util.Log;

import com.drakenclimber.graph.GraphData;
import com.drakenclimber.graph.LineGraphView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    
    static final int                    DATA_COUNT    = 512;
    /* widgets */
    private LineGraphView               mGraphView     = null;
    /* data for the graph */
    private GraphData                   mData         = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        
        /* initialize the graph and its data */
        mData = new GraphData(null, Color.RED, DATA_COUNT);
        mGraphView = (LineGraphView) findViewById(R.id.WidgetGraph);
        mGraphView.addDataSet(mData);
        String readFeed = readFeed();
        try {
          JSONObject jsonFeed = new JSONObject(readFeed);
          JSONArray jsonArray = jsonFeed.getJSONArray("data");
          Log.i(MainActivity.class.getName(), "Number of entries " + jsonArray.length());
          for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            float value = Integer.parseInt(jsonObject.getString("java"));
            mData.appendValue(value);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        // Draw the values
        mGraphView.invalidate();
    }

    public String readFeed() {
	    StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://drtom.ch/posts/2012/06/19/Visualizing_Programming_Language_Popularity_with_D3/so_data.json");
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        Log.e(MainActivity.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return builder.toString();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
