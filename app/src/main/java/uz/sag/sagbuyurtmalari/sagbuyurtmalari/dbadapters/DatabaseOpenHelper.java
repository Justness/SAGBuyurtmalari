package uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.Login;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.MyCollectionRecyclerViewAdapter;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.OrderDetailActivity;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.OrderDetailsActivity;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.OrderColourSize;

/**
 * Created by Sardor on 01.06.2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String ORDERS_TABLE = "orders";

    private static final String RUGCOLOUR_TABLE = "rugcolour";
    private static final String DESIGN_TABLE = "design";
    private static final String ORDER_QUALITY_DESIGN_TABLE = "orderqualityanddesign";
    private static final String ORDER_SIZE_COLOR_TABLE = "ordersizeandcolour";
    private static final String PALLETE_TABLE = "pallete";
    private static DatabaseOpenHelper sInstance;

    //The Android's default system path of your application database.
    public static final String DATABASE_PATH = "/data/data/uz.sag.sagbuyurtmalari.sagbuyurtmalari/databases/";
    public static final String DATABASE_NAME = "sagforandroid.db";

    private static final String QUALITY_TABLE = "quality";
    private static final String COLLECTION_TABLE = "collection";
    private static final String CUSTOMER_TABLE = "customer";
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


    public static final String SIZE_TABLE = "size";
    public static final String COLOR_TABLE = "rugcolour";



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

        sInstance = null;
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

    //GET CURSOR to all qualities
    public List<MyCollectionRecyclerViewAdapter.Miniature> getAllQualitiesItems() {
        //Log.v(this.mDb.execSQL("");)
        //Log.w(TAG,this.myDataBase.toString());
        //Cursor mCursor;
        Cursor mCursor = this.myDataBase.query(true, GALLERY_TABLE, new String[]{GALLERY_TABLE_FIELDS[1],
                GALLERY_TABLE_FIELDS[3]}, null, null, null, null, null, null);


        List<MyCollectionRecyclerViewAdapter.Miniature> miniatures = new ArrayList<>();
        int s = 0;
        if (mCursor.moveToFirst()) {
            do {

                miniatures.add(new MyCollectionRecyclerViewAdapter.Miniature(mCursor.getString(1), mCursor.getString(0), mCursor.getString(0) + mCursor.getString(1) + ".jpg"));

            } while (mCursor.moveToNext());
        }


        return miniatures;


        //return this.mDb.query(true, DATABASE_TABLE, new String[] { NAME }, null, null, null, null, null, null);
    }

    //synchronize images

    public synchronized boolean synchronizeImagesFromGallery() {
        //String [] list;
        File file22 = Environment.getExternalStorageDirectory();
        String path = file22.getPath() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY;

        File f = new File(path);
        //boolean b= f.canRead();
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
        return this.myDataBase.query(true, GALLERY_TABLE, new String[]{GALLERY_TABLE_FIELDS[0], GALLERY_TABLE_FIELDS[7],
                GALLERY_TABLE_FIELDS[3], GALLERY_TABLE_FIELDS[4], GALLERY_TABLE_FIELDS[5]}, cond, null, null, null, null, null);
    }

    public Cursor getImageParamsById(String id) throws SQLException {
        return this.myDataBase.query(true, GALLERY_TABLE, new String[]{GALLERY_TABLE_FIELDS[1], GALLERY_TABLE_FIELDS[2],
                GALLERY_TABLE_FIELDS[3], GALLERY_TABLE_FIELDS[4], GALLERY_TABLE_FIELDS[5], GALLERY_TABLE_FIELDS[6], GALLERY_TABLE_FIELDS[7]}, GALLERY_TABLE_FIELDS[0] + "=" + id, null, null, null, null, null);
    }

    public Cursor getAllOrders() {
        Cursor mCursor = this.myDataBase.query(ORDERS_TABLE, new String[]{"_id", "orderdate", "totalquantity",
                "totalarea", "status"}, "totalarea > 0", null, null, null, null);
        if (mCursor.moveToNext()) {
            mCursor.moveToFirst();
            return mCursor;
        } else return null;

    }


    public JSONObject createNewOrder() {
        ContentValues initialValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
        int newId = getNextId();
        initialValues.put("_id", newId); //@Todo solution
        initialValues.put("orderdate", dateFormat.format(calendar.getTime()));
        initialValues.put("status", "0");
        initialValues.put("totalarea", String.valueOf(OrderDetailActivity.TOTAL_AREA));
        initialValues.put("totalquantity", String.valueOf(OrderDetailActivity.TOTAL_QUANTITY));

        //JSON send to server
        JSONObject jsObj = new JSONObject();

        JSONArray jsList = new JSONArray();
        try {
            long orderId = this.myDataBase.insert(ORDERS_TABLE, null, initialValues);

            jsObj.putOpt("id", newId);
            jsObj.putOpt("client", Login.getUserId());
            jsObj.putOpt("mydate", dateFormat.format(calendar.getTime()));

            for (String group : OrderColourSize.CART_ITEMS) {

                // заполняем список аттрибутов для каждой группы
                String design = group.substring(2, 6);
                String quality = group.substring(0, 2);

                boolean firstRow = true;
                long qualId = -1;
                // заполняем список аттрибутов для каждого элемента
                String pallete = "PC";
                for (OrderColourSize.ColourSizeItem item : OrderColourSize.CART_ITEM_MAP.get(group)) {

                    //At first add quality and design and get its id
                    if (firstRow) {
                        OrderColourSize.QualityDesignItem qual = getQualityItemByName(group, String.valueOf(item.rugcolour_id));
                        qual.id = orderId;
                        firstRow = false;

                        ContentValues qualValues = new ContentValues();
                        qualValues.put("quality_id", qual.quality_id);
                        qualValues.put("design_id", qual.design_id);
                        qualValues.put("pallete_id", qual.pallete_id);
                        qualValues.put("order_id", orderId);
                        qualId = this.myDataBase.insert(ORDER_QUALITY_DESIGN_TABLE, null, qualValues);

                        pallete = DatabaseOpenHelper.getInstance(null).getPalleteNameById(String.valueOf(qual.pallete_id));
                    }
                    //Secondly add color and size in loop
                    if (qualId != -1) {
                        ContentValues sizeValues = new ContentValues();
                        sizeValues.put("size_id", item.size_id);
                        sizeValues.put("rugcolour_id", item.rugcolour_id);
                        sizeValues.put("orderqualityanddesign_id", qualId);
                        sizeValues.put("quantity", item.quantity);
                        this.myDataBase.insert(ORDER_SIZE_COLOR_TABLE, null, sizeValues);
                    }
                    /*---------------------------------------*/
                    JSONObject jsObjLine = new JSONObject();

                    String strSize = DatabaseOpenHelper.getInstance(null).getSizeNameById(item.size_id);
                    String codeSize = DatabaseOpenHelper.getInstance(null).getSizeCodeById(item.size_id);


                    String[] entries = strSize.split("x");
                    String width = entries[0];
                    String height = entries[1];
                    String shape = DatabaseOpenHelper.getInstance(null).getSizeShapeById(item.size_id);

                    String color = DatabaseOpenHelper.getInstance(null).getColorNameById(item.rugcolour_id);
                    String colorNum = DatabaseOpenHelper.getInstance(null).getRugcolourCodeFromId(String.valueOf(item.rugcolour_id));
                    color = colorNum + " " + color;

                    String quantity = String.valueOf(item.quantity);
                    /*---------------------------------------*/
                    jsObjLine.putOpt("quality", quality);
                    jsObjLine.putOpt("design", design);
                    jsObjLine.putOpt("color", color);
                    jsObjLine.putOpt("pallete", pallete);
                    jsObjLine.putOpt("color", color);
                    jsObjLine.putOpt("quantity", quantity);
                    jsObjLine.putOpt("size_code", codeSize);
                    jsObjLine.putOpt("width", width);
                    jsObjLine.putOpt("height", height);
                    jsObjLine.putOpt("shape", shape);

                    jsList.put(jsObjLine);

                }
                // добавляем в коллекцию коллекций
            }
            jsObj.putOpt("content", jsList);

        } catch (SQLException e) {
            return null;
        } catch (JSONException e2) {
            return null;
        }


        return jsObj;
    }



    public void clearAllOrders() {
        this.myDataBase.delete(ORDER_SIZE_COLOR_TABLE, null, null);
        this.myDataBase.delete(ORDER_QUALITY_DESIGN_TABLE, null, null);
        this.myDataBase.delete(ORDERS_TABLE, null, null);
        // this.myDataBase.execSQL("ALTER TABLE "+ORDER_SIZE_COLOR_TABLE+" ADD COLUMN quantity INTEGER");
    }

    public OrderColourSize.QualityDesignItem getQualityItemByName(String qualdes, String rugcolorId) throws SQLException {

        return new OrderColourSize.QualityDesignItem(getQualityIdByName(qualdes.substring(0, 2)), getDesignIdByName(qualdes.substring(2, 6)), getPalleteIdByRugColor(rugcolorId));
    }

    public String getPalleteIdByRugColor(String id) throws SQLException {
        Cursor cursor = this.myDataBase.query(RUGCOLOUR_TABLE, new String[]{"pallete_id"}, "_id = " + id, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "-1";

    }

    public String getPalleteNameById(String id) throws SQLException {
        Cursor cursor = this.myDataBase.query(PALLETE_TABLE, new String[]{"name"}, "_id = " + id, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "PC";

    }

    public String getQualityNameByCode(String code) throws SQLException {
        Cursor cursor = this.myDataBase.query(QUALITY_TABLE, new String[]{"name"}, "code = " + code, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "IMPERIAL";

    }

    public String getDesignIdByName(String id) throws SQLException {
        Cursor cursor = this.myDataBase.query(DESIGN_TABLE, new String[]{"_id"}, "name = \'" + id + "\'", null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "-1";

    }

    public String getQualityIdByName(String id) throws SQLException {
        Cursor cursor = this.myDataBase.query(QUALITY_TABLE, new String[]{"_id"}, "code = \'" + id + "\'", null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "-1";

    }

    //Size functions
    public int getSizeId(String width, String height, boolean finishing) throws SQLException {
        String fin;
        if (finishing) fin = "1";
        else fin = "0";
        Cursor cursor = this.myDataBase.query(SIZE_TABLE, new String[]{"_id"}, "width = " + width + " AND height = " + height + " AND finishing = " + fin, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } else return -1;
    }

    public int getNextId() {
        final int coef = 1000000;
        String startStr = String.valueOf(Login.getUserId() * coef);
        String endStr = String.valueOf((Login.getUserId() + 1) * coef - 1);
        String cond = "_id < " + endStr + " AND _id >= " + startStr;
        Cursor cursor = this.myDataBase.query(CUSTOMER_TABLE, new String[]{"_id"}, cond, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return Integer.parseInt(cursor.getString(0)) + 1;
        } else return Login.getUserId() * coef;

    }

    public String getSizeNameById(int id) throws SQLException {
        Cursor cursor = this.myDataBase.query(SIZE_TABLE, new String[]{"name"}, "_id = " + String.valueOf(id), null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "";
    }

    public String getSizeCodeById(int id) throws SQLException {
        Cursor cursor = this.myDataBase.query(SIZE_TABLE, new String[]{"code"}, "_id = " + String.valueOf(id), null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "";
    }

    public int getSizeAreaById(int id) throws SQLException {
        Cursor cursor = this.myDataBase.query(SIZE_TABLE, new String[]{"name"}, "_id = " + String.valueOf(id), null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();

            String strSize = cursor.getString(0);

            String[] entries = strSize.split("x");
            int width = Integer.parseInt(entries[0]);
            int height = Integer.parseInt(entries[1]);
            return width * height;
        } else return 0;
    }

    public String getSizeShapeById(int id) throws SQLException {
        Cursor cursor = this.myDataBase.query(SIZE_TABLE, new String[]{"finishing"}, "_id = " + String.valueOf(id), null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return "R";
            else
                return "O";
        } else return "";
    }

    public String getArticleFromQualAndPal(String qualid, String palid) throws SQLException {
        Cursor cursor = this.myDataBase.query(COLLECTION_TABLE, new String[]{"name"}, "quality_id = " + qualid + " AND pallete_id = " + palid, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else {
            Cursor cursor2 = this.myDataBase.query(QUALITY_TABLE, new String[]{"name"}, "_id = " + qualid, null, null, null, null);
            if (cursor2.moveToNext()) {
                cursor2.moveToFirst();
                return cursor2.getString(0);
            } else return "IMPERIAL";
        }
    }


    public String getColorNameById(int id) throws SQLException {
        Cursor cursor = this.myDataBase.query(COLOR_TABLE, new String[]{"name"}, "_id = " + String.valueOf(id), null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else return "";
    }

    public int getRugcolourId(String pallete, String bgcolor, String mncolor) throws SQLException {

        String cond = String.format("pallete_id = (SELECT _id FROM pallete WHERE pallete.name = \'%s\') AND backgroundcolour_id = %s AND maincolour_id = %s", pallete, bgcolor, mncolor);
        Cursor cursor = this.myDataBase.query(COLOR_TABLE, new String[]{"_id"}, cond, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } else return -1;
    }

    public String getRugcolourCodeFromId(String rugcolour) throws SQLException {


        Cursor cursor = this.myDataBase.rawQuery("SELECT backgroundcolour_id, maincolour_id FROM rugcolour WHERE _id = ?", new String[]{rugcolour});
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return String.valueOf(cursor.getInt(1)) + String.valueOf(cursor.getInt(0));
        } else return "";
    }

    public int getLastOrderId() throws SQLException {
        Cursor cursor = this.myDataBase.query(ORDERS_TABLE, new String[]{"MAX(_id)"}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } else return -1;
    }

    public int getCustomerIdAuth(String email, String password) throws SQLException {

        String cond = String.format("email =  \'%s\' AND password = \'%s\' ", email, password);
        Cursor cursor = this.myDataBase.query(CUSTOMER_TABLE, new String[]{"_id"}, cond, null, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getInt(0);
        } else return 0;
    }

    public void fillOrderFromDBtoLocalVars(String orderId) {


        DatabaseOpenHelper dbhelper = DatabaseOpenHelper.getInstance(null);
        String qual = "(SELECT quality.code FROM quality WHERE quality._id = orderqualityanddesign.quality_id) ";
        String des = "(SELECT design.name FROM design WHERE design._id = orderqualityanddesign.design_id) ";

        Cursor cursorQ = this.myDataBase.rawQuery("SELECT " + qual + " , " + des + ",_id FROM orderqualityanddesign WHERE orderqualityanddesign.order_id = " + orderId, null);//ORDER_QUALITY_DESIGN_TABLE, new String[]{qual,des,"_id"},"order_id="+orderId,null,null,null,null);
        if (cursorQ.moveToFirst()) {
            do {

                // OrderDetailActivity.CART_ITEMS.add(cursorQ.getString(0)+cursorQ.getString(1));

                String color = "(SELECT rugcolour.name FROM rugcolour WHERE rugcolour._id = ordersizeandcolour.rugcolour_id) ";
                String size = "(SELECT size.name FROM size WHERE size._id = ordersizeandcolour.size_id) ";
                String thquery = "SELECT " + color + "," + size + ",rugcolour_id,quantity,size_id FROM ordersizeandcolour WHERE orderqualityanddesign_id = " + cursorQ.getString(2);

                Cursor cursorC = this.myDataBase.rawQuery(thquery, null);//(ORDER_SIZE_COLOR_TABLE, new String[]{color,size,"rugcolour_id","quantity","size_id"},"orderqualityanddesign_id="+cursorQ.getString(2),null,null,null,null);
                List<OrderColourSize.ColourSizeItem> sizeList = new ArrayList<OrderColourSize.ColourSizeItem>();
                if (cursorC.moveToFirst()) {
                    do {
                        String sizeName = dbhelper.getSizeNameById(cursorC.getInt(4));
                        String colorName = dbhelper.getColorNameById(cursorC.getInt(2));
                        String quantityName = String.valueOf(cursorC.getInt(3)) + " units";
                        String listitem = colorName + " " + sizeName + " R " + quantityName;
//                            if (finishing)
//                                listitem = colorName + " " + sizeName + " O " +quantityName;
                        //@TODO get finishing type from size id (firstly create method in DatabaseOpenHelper)


                        OrderDetailsActivity.addItem(new OrderColourSize.ColourSizeItem(cursorC.getInt(4), cursorC.getInt(2), 1, cursorC.getInt(3), listitem), listitem,
                                cursorQ.getString(0) + cursorQ.getString(1));
                    } while (cursorC.moveToNext());
                }


            } while (cursorQ.moveToNext());
        }

    }

    //Ozod

    public void insertCustomer(String name, String email, String password, String performer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("performer_id", performer);
        this.myDataBase.insert(CUSTOMER_TABLE, null, contentValues);
        contentValues.clear();
    }

    public void readCustomerTable() {
        Cursor cursor = this.myDataBase.query(CUSTOMER_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("_id");
            int nameIndex = cursor.getColumnIndex("name");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", email = " + cursor.getString(emailIndex) +
                        ", passwor = " + cursor.getString(passwordIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

    }

    public void updateCustomerTable(String name, String mail, String password, String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", mail);
        contentValues.put("password", password);


        this.myDataBase.update(CUSTOMER_TABLE, contentValues, "_id" + "= ?", new String[]{id});
        contentValues.clear();

    }

    public void deleteCustomerTable(String id) {

        this.myDataBase.delete(CUSTOMER_TABLE, "_id" + "=" + id, null);

    }

    public Cursor getAllCustomer() {
        //Log.v(this.mDb.execSQL("");)
        // Log.w(TAG,this.myDataBase.toString());
        //Cursor mCursor;
        Cursor mCursor = this.myDataBase.query(true, CUSTOMER_TABLE, new String[]{"_id", "name",
                "email", "password"}, null, null, null, null, null, null);
        if (mCursor.moveToNext()) {
            mCursor.moveToFirst();
        }

        return mCursor;

        //return this.mDb.query(true, DATABASE_TABLE, new String[] { NAME }, null, null, null, null, null, null);
    }

    public String getByIdCustomer(String id) {
        Cursor cursor = this.myDataBase.query(CUSTOMER_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                int idIndex = cursor.getColumnIndex("_id");
                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");
                int passwordIndex = cursor.getColumnIndex("password");
                int performerIndex = cursor.getColumnIndex("performer_id");
                String idBase = String.valueOf(cursor.getInt(idIndex));
                if (id.equals(idBase)) {
                    return cursor.getInt(idIndex) + "#" + cursor.getString(nameIndex) + "#" + cursor.getString(emailIndex) + "#" + cursor.getString(passwordIndex) + "#" + cursor.getString(performerIndex);
                }
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();
        return "";
    }

    public String getByIdOrderComment(String id) {
        Cursor cursor = this.myDataBase.query(ORDERS_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                int idIndex = cursor.getColumnIndex("_id");
                int commentsIndex = cursor.getColumnIndex("comments");
                String idBase = String.valueOf(cursor.getInt(idIndex));
                if (id.equals(idBase)) {
                    return cursor.getInt(idIndex) + "#" + cursor.getString(commentsIndex);
                }
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();
        return "";
    }

    public void updateOrderComment(String id, String comments) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("comments", comments);
        this.myDataBase.update(ORDERS_TABLE, contentValues, "_id" + "= ?", new String[]{id});
        contentValues.clear();
    }

    //SYNC images
    private JSONObject getAllImageUrls() {
        JSONObject json = new JSONObject();
        JSONArray myArray = new JSONArray();
        Cursor mCursor = this.myDataBase.query(true, GALLERY_TABLE, new String[]{GALLERY_TABLE_FIELDS[7]}, null, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                myArray.put(mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        try {
            json.putOpt("existImageData", myArray);
        } catch (JSONException e) {

        }

        return json;
    }

    private void updateOrderStatus(JSONArray jsArr) {

        try {


            for (int i = 0; i < jsArr.length(); i++) {
                JSONObject jsId = jsArr.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put("comments", jsId.getString("comment"));
                this.myDataBase.update(ORDERS_TABLE, cv, "_id=?", new String[]{jsId.getString("id")});
            }
        } catch (JSONException e) {

        }
    }


    public void receiveCommentFromServer() {
        //TODO
        RequestQueue queue = Volley.newRequestQueue(myContext);
        String url = "http://www.dukon.uz/getStatuses.php";

        JSONObject jsondata = new JSONObject();
        try {
            jsondata.putOpt("userId", String.valueOf(Login.getUserId()));
        } catch (JSONException e) {

        }

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsondata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            updateOrderStatus(response.getJSONArray("orderIds"));
                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonRequest);

    }


    public void getUsersFromServer() {
        RequestQueue queue = Volley.newRequestQueue(myContext);
        String url = "http://www.dukon.uz/getUsers.php&sec=sac241cfSac41";


        // Request a string response from the provided URL.
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        updateUsersTable(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    private void updateUsersTable(String query) {
        this.myDataBase.execSQL("delete from " + CUSTOMER_TABLE);
        this.myDataBase.execSQL(query);
    }

    public void getNewImagesList() {

        synchronizeImagesFromGallery();
        //List<String> resList = new ArrayList<>();
//        RequestQueue queue = Volley.newRequestQueue(myContext);
//        String url = "http://www.dukon.uz/getNewImages.php";
//
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, getAllImageUrls(),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        downloadFiles(response);
//                        synchronizeImagesFromGallery();
//                        // Display the first 500 characters of the response string.
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //mTextView.setText("That didn't work!");
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(jsonRequest);

    }

    private void downloadFiles(JSONObject json) {

        try {
            JSONArray jsArr = json.getJSONArray("data");
            String url_Beginning = "http://www.dukon.uz/images/saggallery/thumbs/";

            for (int i = 0; i < jsArr.length(); i++) {
                String myUrl = url_Beginning + jsArr.getString(i);


                URL u = new URL(myUrl);

                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + jsArr.getString(i)));
                while ((length = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        } catch (JSONException sse) {
            Log.e("SYNC getUpdate", "security error", sse);
        }
    }


}