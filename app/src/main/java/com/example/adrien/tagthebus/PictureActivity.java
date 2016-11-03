package com.example.adrien.tagthebus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Bdd.DataManager;
import Bdd.Photo;
import Bdd.PhotoData;

public class PictureActivity extends AppCompatActivity {

    private static final String TAG = "PictureActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean isPicsDisplay = false;

    private ImageView img;
    private TextView tw;
    private EditText et;
    private Button buttonSave;

    final String EXTRA_ID = "station_id";
    static String current_id = "";

    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Log.e(TAG, TAG);

        img = (ImageView) findViewById(R.id.imageViewPic);
        tw = (TextView) findViewById(R.id.textViewPicture);
        et = (EditText) findViewById(R.id.EditViewPicture);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        tw.setVisibility(View.INVISIBLE);
        et.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();
        if (intent != null) {
            current_id = intent.getStringExtra(EXTRA_ID);
            Log.e(TAG, TAG+" id = "+ current_id);
        }

        if(!isPicsDisplay){
            startNewPictureActivity();
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (isPicsDisplay) {
            tw.setVisibility(View.VISIBLE);
            et.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);

            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext()).setTitle(R.string.sauvegarder).setMessage(R.string.sauver_photo_msg).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog, int whichButton) {
                            saveAndQuit();
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
                }
            });
        }
    }

    /**
     * Démarre l'activité photo.
     *
     * @param
     */
    public void startNewPictureActivity() {

        Log.d(TAG, "startNewPictureActivity");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Get results from activityForResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("RESULT_OK", "REQUEST_IMAGE_CAPTURE: RESULT_OK");

            try {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
                isPicsDisplay = true;
            } catch (Exception e) {
                Log.i(TAG, "Erreur de récupératoin de REQUEST_IMAGE_CAPTURE. (continue avec null)");
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    /**
     * Add picture in database
     * @param bitmap
     */
    private void addPicture(Bitmap bitmap, String bitmapName) {

        PhotoData dm = new PhotoData(this);
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.FRANCE);
            String date = dateFormat.format(new Date());

            // Création photo
            Photo photo = new Photo();
            photo.setPhoto(bitmap);
            photo.setIdStation(Long.parseLong(current_id));
            photo.setNom(bitmapName);
            photo.setDate(date);

            // Persistance.
            dm.open(DataManager.WRITE);
            dm.persist(photo);

            Toast.makeText(this, "Picture saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error, Picture not saved", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Record picture failed");
        } finally {
            dm.close();
        }
    }

    /**
     * Save picture and return to the station activity
     */
    public void saveAndQuit() {

        String editTextValue = et.getText().toString();
        addPicture(imageBitmap, editTextValue);
        finish();
    }
}
