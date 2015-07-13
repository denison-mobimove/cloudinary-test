package com.example.cupidon.cloudinary_image_uploads_test;

import android.app.Activity;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Map;

/**
 * Created by cupidon on 7/13/15.
 */
public class ImageServer {

    private static final String  CLOUDINARY_TAG = "cloudinary_tag";


    public static void getResources(final Activity activity,final Cloudinary cloudinary){

        Ion.with(activity)
                .load("http://V572176942673258:EBwYIz2pGosoqQC7qmgw6jyvt-0@api.cloudinary.com/v1_1/claudy/resources/image/")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i(CLOUDINARY_TAG,"response: "+result.toString());
                    }
                });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Map result = cloudinary.api().resource("image_Mon Jul 13 14:05:03 GMT+04:00 2015", ObjectUtils.emptyMap());
                    Log.i(CLOUDINARY_TAG, "response: " + result.toString());
                }catch (Exception e){
                    Log.e(CLOUDINARY_TAG, " get Image", e);
                }
            }
        };

        new Thread(runnable).start();

    }

}
