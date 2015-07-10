package com.example.cupidon.cloudinary_image_uploads_test;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    static Cloudinary cloudinary;
    static Resources resources;
    static String cloudName, apiKey, apiSecret;
    static Bitmap yourSelectedImage;
    static InputStream imageStream;
    static Uri selectedImage;

    private final static String MY_URL = "cloudinary://572176942673258:EBwYIz2pGosoqQC7qmgw6jyvt-0@claudy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        cloudName = resources.getString(R.string.cloudinary_cloud_name);
        apiKey = resources.getString(R.string.cloudinary_api_key);
        apiSecret = resources.getString(R.string.cloudinary_api_secret);


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

    public void pickImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void uploadImage(View view) {
        //configure cloudinary secure url

        String img = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/flower1.jpeg";

        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        //cloudinary = new Cloudinary(config);
        cloudinary = new Cloudinary(MY_URL);

        File f = new File(img);
        if(f.exists()){
            Log.i("file","url: "+img);
        }

        try {
            cloudinary.uploader().upload(yourSelectedImage, Cloudinary.asMap("public_id","my_test_image"));
            String url = cloudinary.url().generate();
            Log.i("cloudinary", "url: " + url);
        } catch (Exception e) {
            Log.e("cloudinary_error", "loading image", e);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    try {
                        selectedImage = data.getData();
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                        Log.i("image uri", "uri: " + selectedImage.toString());
                        ((ImageView) findViewById(R.id.uploaded_image)).setImageBitmap(yourSelectedImage);

                    } catch (Exception e) {

                    }
                }
        }
    }
}
