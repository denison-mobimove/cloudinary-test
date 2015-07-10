package com.example.cupidon.cloudinary_image_uploads_test;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    static Cloudinary cloudinary;
    static Resources resources;
    static String cloudName,apiKey,apiSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        cloudName = resources.getString(R.string.cloudinary_cloud_name);
        apiKey = resources.getString(R.string.cloudinary_api_key);
        apiSecret = resources.getString(R.string.cloudinary_api_secret);


        //configure cloudinary secure url
        Map config = new HashMap();
        config.put("cloud_name",cloudName);
        config.put("api_key",apiKey);
        config.put("api_secret",apiSecret);
        cloudinary = new Cloudinary(config);
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
}
