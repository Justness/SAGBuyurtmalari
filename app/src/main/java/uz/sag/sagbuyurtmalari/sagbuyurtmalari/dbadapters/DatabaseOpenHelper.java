package uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.MyCollectionRecyclerViewAdapter;

/**
 * Created by Sardor on 01.06.2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String ORDERS_TABLE = "orders";
    private static DatabaseOpenHelper sInstance;

    //The Android's default system path of your application database.
    public static final String DATABASE_PATH = "/data/data/uz.sag.sagbuyurtmalari.sagbuyurtmalari/databases/";
    public static final String DATABASE_NAME = "sagforandroid.db";

    private static final String QUALITY_TABLE = "quality";
    private static final String[] QUALITY_TABLE_FIELDS = {"_id", "name", "code", "density_id"};

    public static final String GALLERY_TABLE = "gallery";
    public static final String[] GALLERY_TABLE_FIELDS = {"_id",
            "quality_code",
            "design_name",
            "pallete_name",
            "rugcolour_backgroundcolour_id",
            "rugcolour_maincolour_id",
            "size_code",
            "url"};
    private static final int GALLERY_IMAGE_URL_LENGTH = 19;


    private static final String TAG = "DatabaseOpenHelper :";
    private final Context myContext;

    private SQLiteDatabase myDataBase;


    public static synchronized DatabaseOpenHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseOpenHelper(context.getApplicationContext());
            initDBHelper();
        }
        return sInstance;
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseOpenHelper(Context context) {

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
            this.getWritableDatabase();

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
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

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

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    public static void initDBHelper() {

        DatabaseOpenHelper myDbHelper = sInstance;


        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
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


    //GET CURSOR to all qualities
    public Cursor getAllQualities() {
        //Log.v(this.mDb.execSQL("");)
        // Log.w(TAG,this.myDataBase.toString());
        //Cursor mCursor;
        Cursor mCursor = this.myDataBase.query(true, QUALITY_TABLE, new String[]{"_id", "name",
                "code", "density_id"}, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

        //return this.mDb.query(true, DATABASE_TABLE, new String[] { NAME }, null, null, null, null, null, null);
    }

    //synchronize images

    public synchronized boolean synchronizeImagesFromGallery(String path) {
        //String [] list;
        File f = new File(path);
        File list[] = f.listFiles();
        // try {
        int s = 0;
        long rs;
//            list = myContext.getAssets().list(path);
        if (list.length > 0) {
            // This is a folder
            for (File file2 : list) {
                s++;
                String file = file2.getName();

                if (file.length() == GALLERY_IMAGE_URL_LENGTH) {
                    insertImageExecSql(String.valueOf(s), file.substring(0, 2), file.substring(2, 6),
                            file.substring(7, 9), file.substring(9, 10),
                            file.substring(10, 11), file.substring(12, 15), file);
                }

                if (s % 100 == 0) Log.w(TAG, String.valueOf(s));

            }
        } else {
            // This is a file
            // TODO: add file name to an array list
            return false;
        }
//        } catch (IOException e) {
//            return false;
//        }

        return true;
    }

    public long insertImageIntoGallery(String quality_code, String design_name, String pallete_name, String rugcolour_backgroundcolour_id,
                                       String rugcolour_maincolour_id, String size_code, String url) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(GALLERY_TABLE_FIELDS[0], quality_code);
        initialValues.put(GALLERY_TABLE_FIELDS[1], design_name);
        initialValues.put(GALLERY_TABLE_FIELDS[2], pallete_name);
        initialValues.put(GALLERY_TABLE_FIELDS[3], rugcolour_backgroundcolour_id);
        initialValues.put(GALLERY_TABLE_FIELDS[4], rugcolour_maincolour_id);
        initialValues.put(GALLERY_TABLE_FIELDS[5], size_code);
        initialValues.put(GALLERY_TABLE_FIELDS[6], url);

        return this.myDataBase.insert(GALLERY_TABLE, null, initialValues);
    }

    public void insertImageExecSql(String s, String quality_code, String design_name, String pallete_name, String rugcolour_backgroundcolour_id,
                                   String rugcolour_maincolour_id, String size_code, String url) {
        String test = "INSERT INTO " +
                "gallery(_id, quality_code, " +
                "design_name, " +
                "pallete_name, " +
                "rugcolour_backgroundcolour_id, " +
                "rugcolour_maincolour_id, " +
                "size_code, " +
                "url) VALUES (" + s + ",\'" + quality_code + "\', " +
                "\'" + design_name + "\', " +
                "\'" + pallete_name + "\', " +
                "\'" + rugcolour_backgroundcolour_id + "\', " +
                "\'" + rugcolour_maincolour_id + "\', " +
                "\'" + size_code + "\', " +
                "\'" + url + "\' )";
        this.myDataBase.execSQL(test);

    }

    public List<MyCollectionRecyclerViewAdapter.Miniature> getImages(String cond) throws SQLException {

        Cursor mCursor = this.myDataBase.query(true, GALLERY_TABLE, new String[]{"MAX(" + GALLERY_TABLE_FIELDS[0] + ")", "MAX(" + GALLERY_TABLE_FIELDS[7] + ")"}, cond, null, GALLERY_TABLE_FIELDS[1] + "," + GALLERY_TABLE_FIELDS[2], null, null, null);

        List<MyCollectionRecyclerViewAdapter.Miniature> miniatures = new ArrayList<>();
        if (mCursor.moveToFirst()) {
            do {
                miniatures.add(new MyCollectionRecyclerViewAdapter.Miniature(mCursor.getString(0), mCursor.getString(1).substring(0, 6), mCursor.getString(1)));

            } while (mCursor.moveToNext());
        }


        return miniatures;
    }


    public Cursor getImagesCursor(String cond) throws SQLException {

        return this.myDataBase.query(true, GALLERY_TABLE, new String[]{GALLERY_TABLE_FIELDS[0], GALLERY_TABLE_FIELDS[7]}, cond, null, null, null, null, null);
    }

    public Cursor getAllOrders() {
        Cursor mCursor = this.myDataBase.query(ORDERS_TABLE, new String[]{"orderdata", "totalquantity",
                "totalarea", "status"}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public boolean createNewOrder() {

        return true;
    }


}