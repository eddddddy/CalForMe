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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    String mCurrentPhotoPath;
    String url;
    FragmentTransaction transaction;
    private final String key = "AKIAIPS5WU5KQZXC5T6Q";
    private final String secret = "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";
    private final String bucket = "arn:aws:s3:::calforme-deployments-mobilehub-129629321";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transaction = getSupportFragmentManager().beginTransaction();
        AWSMobileClient.getInstance().initialize(this).execute();

        imageView = findViewById(R.id.imageView);

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


            uploadData();

        }
    }


    public void uploadData() {

        // Initialize AWSMobileClient if not initialized upon the app startup.
        // AWSMobileClient.getInstance().initialize(this).execute();

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        File f = new File(mCurrentPhotoPath);
        System.out.println(f);

        TransferObserver uploadObserver = transferUtility.upload("test.jpg", f);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }

        });

        // If your upload does not trigger the onStateChanged method inside your
        // TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }


    /*
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
*/


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
