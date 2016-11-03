package Bdd;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by adrien on 28/10/16.
 */
public abstract class DataManager {

    public static final String READ  = "READ";
    public static final String WRITE  = "WRITE";
    public static final String DELETE  = "DELETE";

    protected SQLiteDatabase database;
    protected BddOpenHelper dbHelper;

    /**
     * Open DataBase in write mode
     * @throws SQLException
     */
    public void open() throws SQLException {
        open(WRITE);
    }

    /**
     * Open DataBase with the selected mode
     *
     * @param mode {READ, WRITE, DELETE}
     */
    public void open(String mode) {

        switch (mode) {
            case DataManager.WRITE:
                Log.i("DataStation", "Database opennig in write mode.");
                database = dbHelper.getWritableDatabase();
                if(database.isReadOnly())
                    Log.i(this.getClass().getName(), "Database access read only !");

                database.execSQL("PRAGMA foreign_keys = ON;");
                break;

            case DataManager.DELETE:
                Log.i("DataStation", "Database opennig in admin.");
                database = dbHelper.getWritableDatabase();
                break;

            default:
            case DataManager.READ:
                Log.i("DataStation", "Database opennig in write mode.");
                database = dbHelper.getReadableDatabase();

        }
    }

    /**
     * Close DataBase
     */
    public void close() {
        dbHelper.close();
    }

    public int persist(Object o) {
        Log.e(DataManager.class.getName(), "Persistance DataManager NYI");
        return -1;
    }

    /**
     * Drop every entry on every table
     */
    public void truncateBase() {

        BddOpenHelper.truncateBase(database);
    }

    /**
     * Return DataBase instance
     */
    public SQLiteDatabase getDb() {
        return database;
    }
}
