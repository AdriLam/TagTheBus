package Bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Extra.DbBitmapUtility;

/**
 * Created by adrien on 28/10/16.
 */
public class PhotoData extends DataManager {

    private String[] allColumns = {
            BddOpenHelper.COLONNE_ID_PHOTO,
            BddOpenHelper.COLONNE_ID_STATION,
            BddOpenHelper.COLONNE_PHOTO,
            BddOpenHelper.COLONNE_DATE_PHOTO,
            BddOpenHelper.COLONNE_NOM_PHOTO
    };


    /**
     * Constructor
     * @param context
     */
    public PhotoData(Context context) {
        dbHelper = new BddOpenHelper(context);
    }

    /**
     * Make persist a picture on database
     * @param photo
     * @return
     */
    public int persist(Photo photo) {

        ContentValues values = new ContentValues();
        values.put(BddOpenHelper.COLONNE_ID_STATION, photo.getIdStation());
        values.put(BddOpenHelper.COLONNE_PHOTO, DbBitmapUtility.getBytes(photo.getPhoto()));
        values.put(BddOpenHelper.COLONNE_DATE_PHOTO, photo.getDate());
        values.put(BddOpenHelper.COLONNE_NOM_PHOTO, photo.getNom());

            try {
            if(photo.getId() > 0) {
                int affectedRows = database.update(BddOpenHelper.TABLE_PHOTO, values, BddOpenHelper.TABLE_PHOTO +"="+photo.getId(), null);
                Log.i(this.getClass().toString(), String.format("Updating %d %s (%d afffected)", photo.getId(), BddOpenHelper.TABLE_PHOTO, affectedRows));
                return affectedRows;
            }
            else {
                Log.i(this.getClass().toString(), "Inserting " + BddOpenHelper.TABLE_PHOTO);
                return (int) database.insert(BddOpenHelper.TABLE_PHOTO, null, values);
            }
        }
        catch (Exception e) {
            Log.e(this.getClass().toString(), "Update/Insert problem.");
            return -1;
        }
    }

    /**
     * Delete a picture
     * @param photo
     */
    public void deletePhoto(Photo photo) {
        long id = photo.getId();
        System.out.println("Photo d'id: " + id + " suprimé");
        database.delete(BddOpenHelper.TABLE_PHOTO, BddOpenHelper.COLONNE_ID_PHOTO
                + " = '" + id + "'", null);
    }

    /**
     * Delete a picture by id
     * @param id
     */
    public void deletePhotoById(long id) {
        System.out.println("Photo d'id: "+id+" suprimé");
        database.delete(BddOpenHelper.TABLE_PHOTO, BddOpenHelper.COLONNE_ID_PHOTO
                + " = '" + id + "'", null);
    }

    /**
     * Delete a picture by date
     * @param date
     */
    public void deletePhotoByDate(String date) {
        database.delete(BddOpenHelper.TABLE_PHOTO, BddOpenHelper.COLONNE_DATE_PHOTO
                + " = '" + date + "'", null);
    }

    /**
     * Get all picture
     * @return
     */
    public List<Photo> getAllPhotos() {
        List<Photo> photos = new ArrayList<>();

        Cursor cursor = database.query(BddOpenHelper.TABLE_PHOTO, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Photo photo = cursorToPhoto(cursor);
            photos.add(photo);
        }
        cursor.close();

        return photos;
    }


    /**
     * Get all picture with condition
     * @param where
     * @return
     */
    public List<Photo> getAllPhotos(String where) {

        List<Photo> photos = new ArrayList<>();

        Cursor cursor = database.query(BddOpenHelper.TABLE_PHOTO, allColumns, where, null, null, null, null);

        Log.d(this.getClass().getName()+":count_photo", String.valueOf( cursor.getCount()));

        while (cursor.moveToNext()) {
            Photo photo = cursorToPhoto(cursor);
            photos.add(photo);
        }
        cursor.close();
        return photos;
    }

    /**
     * Cursor to picture
     * @param cursor
     * @return
     */
    private Photo cursorToPhoto(Cursor cursor) {

        Photo photo = new Photo();
        photo.setId(cursor.getLong(0));
        photo.setIdStation(cursor.getLong(1));
        photo.setPhoto(DbBitmapUtility.getImage(cursor.getBlob(2)));
        photo.setDate(cursor.getString(3));
        photo.setNom(cursor.getString(4));
        return photo;
    }
}

