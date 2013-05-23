package com.salquestfl.readingplotter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
//import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener {
    
    //static final String TAG = "ReadingPlotter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button plotButton = (Button) findViewById(R.id.ButtonShowPlot);
        plotButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.ButtonShowSettings);
        settingsButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ButtonShowPlot:
                //Log.i(TAG, "Show plot clicked");
                Intent plotIntent = new Intent(this, PlotActivity.class);
                startActivity(plotIntent);
                break;
            case R.id.ButtonShowSettings:
                //Log.i(TAG, "Show settings clicked");
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
