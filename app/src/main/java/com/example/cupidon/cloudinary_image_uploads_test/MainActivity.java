package com.example.cupidon.cloudinary_image_uploads_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.cloudinary.Cloudinary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    static Cloudinary cloudinary;
    static Resources resources;
    static String cloudName, apiKey, apiSecret;
    static Bitmap yourSelectedImage;
    static InputStream imageStream;
    static Uri selectedImage;
    static String imagePath;
    private static Activity activity;

    private  static ScrollView scrollview;
    private static RelativeLayout progress;

    private final static String MY_URL = "cloudinary://572176942673258:EBwYIz2pGosoqQC7qmgw6jyvt-0@claudy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        cloudName = resources.getString(R.string.cloudinary_cloud_name);
        apiKey = resources.getString(R.string.cloudinary_api_key);
        apiSecret = resources.getString(R.string.cloudinary_api_secret);

        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        cloudinary = new Cloudinary(config);
        activity = this;
        Button uploadBtn = ((Button) findViewById(R.id.upload_button));
        scrollview = (ScrollView) findViewById(R.id.content_scrollview);
        progress = (RelativeLayout) findViewById(R.id.progressLayout);
        uploadBtn.setVisibility(Button.GONE);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollview.setVisibility(ScrollView.GONE);
                progress.setVisibility(RelativeLayout.VISIBLE);
                uploadImage(view);
            }
        });

        ImageServer.getResources(this,cloudinary);
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
        if (id == R.id.action_all_image) {
            startActivity(new Intent(this,SavedImageActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public static void uploadImage(View view) {
        //configure cloudinary secure url
        Log.e("uploading", "upload in progress");

        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        //cloudinary = new Cloudinary(config);


        //test
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        final ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        //End test

//        File f = new File(imagePath);
//        if(f.exists()){
//            Log.i("file","url: "+img);
//        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    final String filename = "image_" + new Date();
                    cloudinary.uploader().upload(bs, Cloudinary.asMap("public_id", "TestFolder/"+filename));


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String url = cloudinary.url().generate(filename);
                            if (url != null) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        activity);
                                alertDialogBuilder.setTitle("Image Upload");

                                alertDialogBuilder
                                        .setMessage("Picture Uploaded! \n" + url)
                                        .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                                    }
                                }).show();
                            }
                        }
                    });
                    //Log.i("cloudinary", "url: " + url);
                } catch (Exception e) {
                    Log.e("cloudinary_error", "loading image", e);
                }

            }
        };

        new Thread(runnable).start();


    }

    @Override
    public void onBackPressed() {
       if (progress.getVisibility() == RelativeLayout.VISIBLE){

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

                        Log.i("image uri", "uri: " + selectedImage.getPath());
                        ((ImageView) findViewById(R.id.uploaded_image)).setImageBitmap(yourSelectedImage);

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imagePath = cursor.getString(columnIndex);
                        cursor.close();
                        ((Button) findViewById(R.id.upload_button)).setVisibility(Button.VISIBLE);

                    } catch (Exception e) {

                    }
                }
        }
    }
}
