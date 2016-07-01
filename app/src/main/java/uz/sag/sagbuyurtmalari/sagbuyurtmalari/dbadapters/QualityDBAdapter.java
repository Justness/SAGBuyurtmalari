package uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by User on 29.06.2016.
 */

public class QualityDBAdapter {
    public static final String ROW_ID = "_id";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String DENSITY_ID = "density_id";

    public static final String DATABASE_PATH = "/data/data/uz.sag.sagbuyurtmalari.sagbuyurtmalari/databases/";
    public static final String DATABASE_NAME = "sagforandroid.db";


    private static final String DATABASE_TABLE = "quality";

    private static final String TAG = "QualityDBAdapter :";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    public class DatabaseHelper extends SQLiteOpenHelper {

        //The Android's default system path of your application database.


        // private SQLiteDatabase myDataBase;

        private final Context myContext;

        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         *
         * @param context
         */
        public DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, 1);
            this.myContext = context;
        }

        /**
         * Creates a empty database on the system and rewrites it with your own database.
         */
        public void createDataBase() throws IOException {

            boolean dbExist = checkDataBase();

            if (dbExist) {
                //do nothing - database already exist
            } else {

                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();

                try {

                    copyDataBase();

                } catch (IOException e) {

                    throw new Error("Error copying database");

                }
            }

        }

        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         *
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase() {

            SQLiteDatabase checkDB = null;

            try {
                String myPath = DATABASE_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            } catch (SQLiteException e) {

                //database does't exist yet.

            }

            if (checkDB != null) {

                checkDB.close();

            }

            return checkDB != null ? true : false;
        }

        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         */
        private void copyDataBase() throws IOException {

            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = DATABASE_PATH + DATABASE_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }

        public SQLiteDatabase openDataBase() throws SQLException {

            //Open the database
            String myPath = DATABASE_PATH + DATABASE_NAME;
            return SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }

        @Override
        public synchronized void close() {

//            if(myDataBase != null)
//                myDataBase.close();

            super.close();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        // Add your public helper methods to access and get content from the database.
        // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
        // to you to create adapters for your views.

    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public QualityDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the cars database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public QualityDBAdapter open() throws SQLException {
        // this.mDbHelper = new DatabaseHelper(this.mCtx);
        // this.db = this.DBHelper.getWritableDatabase();
        DatabaseHelper myDbHelper = new DatabaseHelper(this.mCtx);

        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            this.mDb = myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }

        return this;
    }

    //@TODO
//    @Override
//    public synchronized void close() {
//
//        if(myDataBase != null)
//            myDataBase.close();
//
//        super.close();
//
//    }

    /**
     * close return type: void
     */
    public void close() {
        if (this.mDb != null) this.mDb.close();
        this.mDbHelper.close();
    }

    /**
     * Create a new car. If the car is successfully created return the new
     * rowId for that car, otherwise return a -1 to indicate failure.
     *
     * @param name
     * @param model
     * @param year
     * @return rowId or -1 if failed
     */
    public long createCar(String name, String model, String year) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(CODE, model);
        initialValues.put(DENSITY_ID, year);
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the car with the given rowId
     *
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteCar(long rowId) {

        return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all cars in the database
     *
     * @return Cursor over all cars
     */
    public Cursor getAllQualityNames() {
        //Log.v(this.mDb.execSQL("");)
        Cursor cursor = this.mDb.query(true, DATABASE_TABLE, new String[]{NAME}, null, null, null, null, null, null);

        Cursor mCursor =

                this.mDb.query(true, DATABASE_TABLE, new String[]{ROW_ID, NAME,
                        CODE, DENSITY_ID}, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
//        if (cursor.moveToFirst()) {
//            do {
//                cursor.getString(0);
//                Log.w(TAG,cursor.getString(0));
//
//            } while (cursor.moveToNext());
//        }

//        return cursor;
        //return this.mDb.query(true, DATABASE_TABLE, new String[] { NAME }, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the car that matches the given rowId
     *
     * @param rowId
     * @return Cursor positioned to matching car, if found
     * @throws SQLException if car could not be found/retrieved
     */
    public Cursor getQuality(long rowId) throws SQLException {

        Cursor mCursor =

                this.mDb.query(true, DATABASE_TABLE, new String[]{ROW_ID, NAME,
                        CODE, DENSITY_ID}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the car.
     *
     * @param rowId
     * @param name
     * @param model
     * @param year
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateCar(long rowId, String name, String model,
                             String year) {
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(CODE, model);
        args.put(ROW_ID, year);

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
    }


    public static class QualityItem {
        public final int id;
        public final String name;

        //public String meaning;

        public QualityItem(int id, String name) {
            this.id = id;
            this.name = name;
        }


    }
}