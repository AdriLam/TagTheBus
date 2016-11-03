package Bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BddOpenHelper extends SQLiteOpenHelper {

    private static final int BASE_VERSION = 2;
    private static final String BASE_NOM = "TagTheBus.db";

    /*Table Photo*/
    public static final String TABLE_PHOTO = "table_photo";
    public static final String COLONNE_ID_PHOTO = "Id_photo";
    public static final String COLONNE_PHOTO = "Photo";
    public static final String COLONNE_DATE_PHOTO = "Date_Creation_photo";
    public static final String COLONNE_ID_STATION = "Id_station";
    public static final String COLONNE_NOM_PHOTO = "Nom_photo";

    // Création table photo.
    private static final String DATABASE_CREATE_PHOTO = "create table "
            + TABLE_PHOTO + "("
            + COLONNE_ID_PHOTO + " integer primary key autoincrement,"
            + COLONNE_ID_STATION + " integer not null,"
            + COLONNE_PHOTO + " blob not null,"
            + COLONNE_DATE_PHOTO + " text,"
            + COLONNE_NOM_PHOTO + " text);";

    private static final String TABLE_PHOTO_DROP = "DROP TABLE IF EXISTS " + TABLE_PHOTO;
    private static final String TABLE_PHOTO_DELETE = "DELETE FROM " + TABLE_PHOTO;


    /**
     * Constructor
     * @param context
     */
    public BddOpenHelper(Context context) {
        super(context, BASE_NOM, null, BASE_VERSION);
    }

    /**
     * Create DataBase
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(BddOpenHelper.class.getName(), " Creating " + BASE_NOM);
        database.execSQL(DATABASE_CREATE_PHOTO);
    }

    /**
     * Update DataBase
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BddOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        // Supprime les entrées de toutes les tables.
        db.execSQL(TABLE_PHOTO_DROP);

        onCreate(db);
    }

    /**
     * Drop entry in DataBase
     * @param db
     */
    public static void truncateBase(SQLiteDatabase db) {
        Log.w(BddOpenHelper.class.getName(), "Deleting tables.");
        db.execSQL(TABLE_PHOTO_DELETE);
    }
}
