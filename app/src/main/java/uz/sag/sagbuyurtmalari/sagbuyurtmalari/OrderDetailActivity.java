package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.OrderColourSize;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class OrderDetailActivity extends ExpandableListActivity implements AppCompatCallback {

//    public String[][] array1 = new String[100][100];
//    public String[][] array2 = new String[100][100];


    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    public static final List<String> CART_SUB_ITEMS = new ArrayList<String>();

    public static final Set<String> CART_ITEMS = new HashSet<String>();

    public static final String ORDERS_DIRECTORY = "/sagorders/";

    public static int TOTAL_AREA = 1;

    public static int TOTAL_QUANTITY = 0;

    public static final Map<String, List<OrderColourSize.ColourSizeItem>> CART_ITEM_MAP = new HashMap<String, List<OrderColourSize.ColourSizeItem>>();

    SimpleExpandableListAdapter seAdapter;

    private AppCompatDelegate delegate;

    private TextView mTotalAreaView;

    public static void addItem(OrderColourSize.ColourSizeItem item, String name, String qualDes) {

//          m = new HashMap<String, String>();
//          m.put("groupName", qualDes); // имя компании
//          GROUP_DATA.add(m);
//
//        childDataItem = new ArrayList<Map<String, String>>();
//
//        m = new HashMap<String, String>();
//        m.put("phoneName", name);
//        childDataItem.add(m);
//
//        CHILD_DATA.add(childDataItem);

        CART_SUB_ITEMS.add(name);
        CART_ITEMS.add(qualDes);
        if (CART_ITEM_MAP.containsKey(qualDes))
            CART_ITEM_MAP.get(qualDes).add(item);
        else {
            CART_ITEM_MAP.put(qualDes, new ArrayList<OrderColourSize.ColourSizeItem>());
            CART_ITEM_MAP.get(qualDes).add(item);
        }

    }
//    List<String> listDataHeader;
//    HashMap<String, List<String>> listDataChild;

    // названия компаний (групп)
//    String[] groups = new String[]{"Imperial", "Almira", "Iran"};
//
//    // названия телефонов (элементов)
//    String[] phonesHTC = new String[]{"Sensation", "Desire", "Wildfire", "Hero"};
//    String[] phonesSams = new String[]{"Galaxy S II", "Galaxy Nexus", "Wave"};
//    String[] phonesLG = new String[]{"Optimus", "Optimus Link", "Optimus Black", "Optimus One"};

    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>

    // список аттрибутов группы или элемента
    Map<String, String> m;

    private Button mSendOrderBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        menu.findItem(R.id.action_open_xls).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //CONTINUE FROM HERE
                //
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.fromFile(createCSVfile()), "application/vnd.ms-excel");
                startActivity(intent);

//                                Snackbar.make(item.getActionView(), "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.order_detail_list);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);


        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, this);

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.order_detail_list);

        //Finally, let's add the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_detail_toolbar);
        toolbar.setTitle(R.string.title_order_detail);
        delegate.setSupportActionBar(toolbar);

        ActionBar ab = delegate.getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        // MenuBuilder menu = new MenuBuilder(getBaseContext());
        // getMenuInflater().inflate(R.menu.order_menu,menu);
        // toolbar.setMenu(menu);


        mSendOrderBtn = (Button) findViewById(R.id.sendOrder);
        mSendOrderBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (DatabaseOpenHelper.getInstance(getBaseContext()).createNewOrder()) {
//
//                            OrderColourSize.CART_ITEM_MAP.clear();
//                            OrderColourSize.CART_ITEMS.clear();
//                            OrderColourSize.CART_SUB_ITEMS.clear();
//                            finish();
//                        }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    }
                }
        );

        mTotalAreaView = (TextView) delegate.findViewById(R.id.totalsquare);
        // preparing list data

        if (orderId.equals("-1"))

            prepareListData(OrderColourSize.CART_ITEMS, OrderColourSize.CART_ITEM_MAP);
        else {
            DatabaseOpenHelper.getInstance(getBaseContext()).fillOrderFromDBtoLocalVars(orderId);
            prepareListData(OrderDetailActivity.CART_ITEMS, OrderDetailActivity.CART_ITEM_MAP);
        }

        //listAdapter = new SimpleExpandableListAdapter(getBaseContext(), listDataHeader, listDataChild);


        setListAdapter(seAdapter);
    }

    private File createCSVfile() {


        List<String> cartItemsList = new ArrayList(OrderColourSize.CART_ITEMS);
        Collections.sort(cartItemsList);


        // -----------------------start create excel file--------------------------------

        org.apache.poi.ss.usermodel.Workbook wb;
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        Date date = new Date();
        String time = dateFormat.format(date);


        String fileName = time + ".xls";

        wb = new HSSFWorkbook();

        org.apache.poi.ss.usermodel.Cell c = null;

        org.apache.poi.ss.usermodel.Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");


        //-----------------------------start--------------------------------------

        int lengtSheet = wb.getNumberOfSheets();

        int indexSheet = 0;

        String lastSheetName = "myOrder";



        int i = 0; //
        for (String group : cartItemsList) {

            String sheetNameGroup = group.toString();
            String design = group.substring(2, 6);
            String quality = group.substring(0, 2);

            for (OrderColourSize.ColourSizeItem item : OrderColourSize.CART_ITEM_MAP.get(group)) {

                String strSize = DatabaseOpenHelper.getInstance(null).getSizeNameById(item.size_id);
                String codeSize = String.valueOf(item.size_id);

                String[] entries = strSize.split("x");
                String width = entries[0];
                String height = entries[1];
                String shape = DatabaseOpenHelper.getInstance(null).getSizeShapeById(item.size_id);

                String color = DatabaseOpenHelper.getInstance(null).getColorNameById(item.rugcolour_id);
                String colorNum = DatabaseOpenHelper.getInstance(null).getRugcolourCodeFromId(String.valueOf(item.rugcolour_id));
                color = colorNum + " " + color;

                String quantity = String.valueOf(item.quantity);


                if (lastSheetName.equals(quality)) {
                    Sheet sheet = wb.getSheet(quality);

                    Row row = sheet.createRow(i);

                    Cell cell = null;

                    cell = row.createCell(0);
                    cell.setCellValue(quality);

                    cell = row.createCell(1);
                    cell.setCellValue(design);

                    cell = row.createCell(2);
                    cell.setCellValue(color);

                    cell = row.createCell(3);
                    cell.setCellValue(shape);

                    cell = row.createCell(4);
                    cell.setCellValue(codeSize);

                    cell = row.createCell(5);
                    cell.setCellValue(width);

                    cell = row.createCell(6);
                    cell.setCellValue(height);

                    cell = row.createCell(7);
                    cell.setCellValue(quantity);

                    lastSheetName = quality;

                    i++;
                } else {
                    i = 0;
                    Sheet sheet = wb.createSheet(quality);

                    Row row = sheet.createRow(i);

                    Cell cell = null;

                    cell = row.createCell(0);
                    cell.setCellValue(quality);

                    cell = row.createCell(1);
                    cell.setCellValue(design);

                    cell = row.createCell(2);
                    cell.setCellValue(color);

                    cell = row.createCell(3);
                    cell.setCellValue(shape);

                    cell = row.createCell(4);
                    cell.setCellValue(codeSize);

                    cell = row.createCell(5);
                    cell.setCellValue(width);

                    cell = row.createCell(6);
                    cell.setCellValue(height);

                    cell = row.createCell(7);
                    cell.setCellValue(quantity);


                    lastSheetName = quality;
                }

            }

        }


        if (wb.getNumberOfSheets() > 0) {
            wb.removeSheetAt(0);
        }

        File file = new File(getApplicationContext().getExternalFilesDir(null) + "", fileName);
        FileOutputStream os = null;


        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return file;
        // -----------------------end create excel file--------------------------------
    }





    /*
 * Preparing the list data
 */
    private void prepareListData(Set<String> cartItems, Map<String, List<OrderColourSize.ColourSizeItem>> cartItemMap) {
        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<ArrayList<Map<String, String>>>();

        int sarea = 0;


        for (String group : cartItems) {
            // заполняем список аттрибутов для каждой группы
            m = new HashMap<String, String>();
            m.put("groupName", group); // имя компании
            groupData.add(m);

            childDataItem = new ArrayList<Map<String, String>>();
            // заполняем список аттрибутов для каждого элемента
            for (OrderColourSize.ColourSizeItem item : cartItemMap.get(group)) {
                String phone = item.content;
                sarea += DatabaseOpenHelper.getInstance(null).getSizeAreaById(item.size_id) * item.quantity / 10000;
                m = new HashMap<String, String>();
                m.put("phoneName", phone); // название телефона
                childDataItem.add(m);
            }
            // добавляем в коллекцию коллекций
            childData.add(childDataItem);
        }

        TOTAL_AREA = sarea;
        mTotalAreaView.setText(String.valueOf(TOTAL_AREA));
        // список аттрибутов групп для чтения
        String groupFrom[] = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[]{android.R.id.text1};

        // список аттрибутов элементов для чтения
        String childFrom[] = new String[]{"phoneName"};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[]{android.R.id.text1};

        seAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
//
//        // Adding child data
//        listDataHeader.add("Top 250");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");
//
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
//        top250.add("The Godfather: Part II");
//        top250.add("Pulp Fiction");
//        top250.add("The Good, the Bad and the Ugly");
//        top250.add("The Dark Knight");
//        top250.add("12 Angry Men");
//
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
//        nowShowing.add("Despicable Me 2");
//        nowShowing.add("Turbo");
//        nowShowing.add("Grown Ups 2");
//        nowShowing.add("Red 2");
//        nowShowing.add("The Wolverine");
//
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
//
//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
