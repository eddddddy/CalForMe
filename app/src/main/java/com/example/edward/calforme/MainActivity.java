package com.example.edward.calforme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v4.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.ByteArrayOutputStream;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import org.json.*;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    String mCurrentPhotoPath;
    String url;
    FragmentTransaction transaction;
    /*
    DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
    HttpPost httpPost = new HttpPost("https://nickc158.lib.id/caloriecounter@dev/?bytes=food");
    httpPost.setHeader("Content-type", "application/json");
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transaction = getSupportFragmentManager().beginTransaction();

        imageView = findViewById(R.id.imageView);
        url = "https://nickc158.lib.id/caloriecounter@dev";

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.edward.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, 1);
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + "food" + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final Bitmap myBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageView.setImageBitmap(myBitmap);

            //ResultFragment resultFragment = new ResultFragment();
            //transaction.add(R.id.fragment_container, resultFragment);

            /*
            ListFragment listFragment = new ListFragment();
            transaction.add(R.id.fragment_container, listFragment);

            new Thread(new Runnable() {
                public void run() {
                    transaction.commit();
                }
            }).start();
            */

            new Thread(new Runnable() {
                public void run() {
                    sendToClarifai(myBitmap);
                }
            }).start();

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public void sendToClarifai(final Bitmap bitmap) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        System.out.println("responded");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                byte[] byteArray = getBytesFromBitmap(bitmap);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("file", "byteArray");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return obj.toString().getBytes();
            }
        };
        System.out.println(stringRequest.toString());
        queue.add(stringRequest);
    }

    public void onRedoClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.edward.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, 1);
        }
    }

}
