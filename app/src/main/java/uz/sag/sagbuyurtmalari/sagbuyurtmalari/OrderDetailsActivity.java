package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import uz.sag.sagbuyurtmalari.TableFixHeaders;
import uz.sag.sagbuyurtmalari.adapters.BaseTableAdapter;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.OrderColourSize;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class OrderDetailsActivity extends AppCompatActivity {

    public static final List<String> CART_SUB_ITEMS = new ArrayList<String>();

    public static final Set<String> CART_ITEMS = new HashSet<String>();

    public static final String ORDERS_DIRECTORY = "/sagorders/";

    public static int TOTAL_AREA = 1;

    public static int TOTAL_QUANTITY = 0;

    public static final Map<String, List<OrderColourSize.ColourSizeItem>> CART_ITEM_MAP = new HashMap<String, List<OrderColourSize.ColourSizeItem>>();

    //SimpleExpandableListAdapter seAdapter;

    //private AppCompatDelegate delegate;

    private TextView mTotalAreaView;

    //private JSONObject jsonOrder;

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
        Locale locale = new Locale(MainActivity.LOCAL);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_details_list);


        Intent intent = getIntent();
        String orderId = intent.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);


        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        //delegate = AppCompatDelegate.create(this, this);

        //we need to call the onCreate() of the AppCompatDelegate
        //delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        //delegate.setContentView(R.layout.order_detail_list);

        //Finally, let's add the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_detail_toolbar);
        toolbar.setTitle(R.string.title_order_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ActionBar ab = delegate.getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);


        // MenuBuilder menu = new MenuBuilder(getBaseContext());
        // getMenuInflater().inflate(R.menu.order_menu,menu);
        // toolbar.setMenu(menu);
        mSendOrderBtn = (Button) findViewById(R.id.sendOrder);
        if (orderId.equals("-1")) {

            mSendOrderBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(view.getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(R.string.send_order)
                                    .setMessage(R.string.really_send)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (DatabaseOpenHelper.getInstance(getBaseContext()).createNewOrder()) {


                                                OrderColourSize.CART_ITEM_MAP.clear();
                                                OrderColourSize.CART_ITEMS.clear();
                                                OrderColourSize.CART_SUB_ITEMS.clear();
                                                Toast toast = Toast.makeText(getBaseContext(),
                                                        getResources().getString(R.string.order_sent), Toast.LENGTH_SHORT);
                                                toast.show();
                                                OrderColourSize.notifyChange();
                                                finish();
                                            } else {

                                            }

                                        }

                                    })
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                        }
                    }
            );
        } else {
            mSendOrderBtn.setVisibility(View.INVISIBLE);
        }

        mTotalAreaView = (TextView) findViewById(R.id.totalsquare1);
        // preparing list data
        BaseTableAdapter baseTableAdapter;
        if (orderId.equals("-1")) {

            prepareListData(OrderColourSize.CART_ITEMS, OrderColourSize.CART_ITEM_MAP);
            baseTableAdapter = new FamilyNexusAdapter(this, OrderColourSize.CART_ITEMS, OrderColourSize.CART_ITEM_MAP);
        } else {
            DatabaseOpenHelper.getInstance(getBaseContext()).fillOrderFromDBtoLocalVars(orderId);
            prepareListData(OrderDetailsActivity.CART_ITEMS, OrderDetailsActivity.CART_ITEM_MAP);
            baseTableAdapter = new FamilyNexusAdapter(this, OrderDetailsActivity.CART_ITEMS, OrderDetailsActivity.CART_ITEM_MAP);
        }

        //listAdapter = new SimpleExpandableListAdapter(getBaseContext(), listDataHeader, listDataChild);


        TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);

        //tableFixHeaders.setOnClickListener();
        tableFixHeaders.setAdapter(baseTableAdapter);

        //setListAdapter(seAdapter);


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

        Cell c = null;

        Sheet sheet1 = null;
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
        int qarea[] = new int[]{0, 0, 0, 0, 0, 0};
        int qi = -1;
        String ssarea[] = new String[]{"", "", "", "", "", ""};


        String fgroup = "";
        for (String group : cartItems) {
            // заполняем список аттрибутов для каждой группы
            if (!group.substring(0, 2).equals(fgroup)) {
                fgroup = group.substring(0, 2);

                qi++;
                if (qi > 5) qi = 5;
                ssarea[qi] = fgroup;
            }
            m = new HashMap<String, String>();
            m.put("groupName", group); // имя компании
            groupData.add(m);

            childDataItem = new ArrayList<Map<String, String>>();
            // заполняем список аттрибутов для каждого элемента
            for (OrderColourSize.ColourSizeItem item : cartItemMap.get(group)) {
                String phone = item.content;
                int pval = DatabaseOpenHelper.getInstance(null).getSizeAreaById(item.size_id) * item.quantity / 10000;
                qarea[qi] += pval;
                sarea += pval;
                m = new HashMap<String, String>();
                m.put("phoneName", phone); // название телефона
                childDataItem.add(m);
            }
            // добавляем в коллекцию коллекций
            childData.add(childDataItem);
        }

        TOTAL_AREA = sarea;
        mTotalAreaView.setText(String.valueOf(TOTAL_AREA));

        TextView secondTotal = (TextView) findViewById(R.id.quality_name2);
        secondTotal.setText(ssarea[0]);
        secondTotal = (TextView) findViewById(R.id.totalsquare2);
        secondTotal.setText(String.valueOf(qarea[0]));
        if (qarea[1] > 0) {
            secondTotal = (TextView) findViewById(R.id.quality_name3);
            secondTotal.setText(ssarea[1]);
            secondTotal = (TextView) findViewById(R.id.totalsquare3);
            secondTotal.setText(String.valueOf(qarea[1]));
        }
        if (qarea[2] > 0) {
            secondTotal = (TextView) findViewById(R.id.quality_name4);
            secondTotal.setText(ssarea[2]);
            secondTotal = (TextView) findViewById(R.id.totalsquare4);
            secondTotal.setText(String.valueOf(qarea[2]));
        }
        if (qarea[3] > 0) {
            secondTotal = (TextView) findViewById(R.id.quality_name5);
            secondTotal.setText(ssarea[3]);
            secondTotal = (TextView) findViewById(R.id.totalsquare5);
            secondTotal.setText(String.valueOf(qarea[3]));
        }
        if (qarea[4] > 0) {
            secondTotal = (TextView) findViewById(R.id.quality_name6);
            secondTotal.setText(ssarea[4]);
            secondTotal = (TextView) findViewById(R.id.totalsquare6);
            secondTotal.setText(String.valueOf(qarea[4]));
        }
        if (qarea[5] > 0) {
            secondTotal = (TextView) findViewById(R.id.quality_name7);
            secondTotal.setText(ssarea[5]);
            secondTotal = (TextView) findViewById(R.id.totalsquare7);
            secondTotal.setText(String.valueOf(qarea[5]));
        }


        // список аттрибутов групп для чтения
        String groupFrom[] = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[]{android.R.id.text1};

        // список аттрибутов элементов для чтения
        String childFrom[] = new String[]{"phoneName"};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[]{android.R.id.text1};

//        seAdapter = new SimpleExpandableListAdapter(
//                this,
//                groupData,
//                android.R.layout.simple_expandable_list_item_1,
//                groupFrom,
//                groupTo,
//                childData,
//                android.R.layout.simple_list_item_1,
//                childFrom,
//                childTo);
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

    private class NexusTypes {
        public final String name;
        private final List<Nexus> list;

        NexusTypes(String name) {
            this.name = name;
            list = new ArrayList<Nexus>();
        }

        public int size() {
            return list.size();
        }

        public Nexus get(int i) {
            return list.get(i);
        }
    }

    private class Nexus {
        private final String[] data;

        private Nexus(String[] d, int size) {

            data = new String[size];
            for (int i = 0; i < size; i++) {
                data[i] = d[i];
            }

        }
    }

    public class FamilyNexusAdapter extends BaseTableAdapter {

        private NexusTypes familys[];
        private final String headers[];

        private final int[] widths;

        private int mColsSize = 0;
        private int mRowsSize = 0;

        private float density;


        public FamilyNexusAdapter(Context context, Set<String> CI, Map<String, List<OrderColourSize.ColourSizeItem>> CIM) {
            //@// TODO: 07.09.2016

            List<String> cartItemsList = new ArrayList(CI);
            Collections.sort(cartItemsList);

            // -----------------------start create excel file--------------------------------

            int i = 0; //
            Set<String> qualSet = new HashSet<>();
            //Set<String> desSet = new HashSet<>();
            Set<String> sizeSet = new HashSet<>();

            for (String group : cartItemsList) {
                //qualSet.add(group.substring(0, 2));
                //String design = group.substring(2, 6);

                for (OrderColourSize.ColourSizeItem item : CIM.get(group)) {
//                    String color = DatabaseOpenHelper.getInstance(null).getColorNameById(item.rugcolour_id);
//                    String colorNum = DatabaseOpenHelper.getInstance(null).getRugcolourCodeFromId(String.valueOf(item.rugcolour_id));
//
//                    desSet.add(design+" "+colorNum + " " + color);
                    OrderColourSize.QualityDesignItem qual = DatabaseOpenHelper.getInstance(null).getQualityItemByName(group, String.valueOf(item.rugcolour_id));
                    qualSet.add(DatabaseOpenHelper.getInstance(null).getArticleFromQualAndPal(qual.quality_id, qual.pallete_id));//EXP
                    String strSize = DatabaseOpenHelper.getInstance(null).getSizeNameById(item.size_id);
                    String shape = DatabaseOpenHelper.getInstance(null).getSizeShapeById(item.size_id);


                    sizeSet.add(strSize + " " + shape);
                }
            }

            List<String> sizeArray = new ArrayList<String>(sizeSet);
            Comparator<String> sizeCom = new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    String[] entries = s.split("x");
                    String[] entries2 = t1.split("x");
                    int width1 = Integer.parseInt(entries[0]);
                    int height1 = Integer.parseInt(entries[1].split(" ")[0]);


                    int width2 = Integer.parseInt(entries2[0]);
                    int height2 = Integer.parseInt(entries2[1].split(" ")[0]);

                    if (width1 != width2)
                        return width2 - width1;
                    else
                        return height1 - height2;
                }
            };
            Collections.sort(sizeArray, sizeCom);

            headers = new String[sizeSet.size() + 1];
            widths = new int[sizeSet.size() + 1];
            i = 0;
            headers[i] = "Name";
            widths[i++] = 60;
            for (String it : sizeArray) {
                headers[i] = it;
                widths[i++] = 60;
            }
            familys = new NexusTypes[qualSet.size()];
            i = 0;
            for (String it : qualSet) {
                familys[i++] = new NexusTypes(it);

            }
            mColsSize = sizeSet.size();

            List<String[]> tempD = new ArrayList<>();
            for (String group : cartItemsList) {

                String design = group.substring(2, 6);
                String quality = "IMPERIAL";// = group.substring(0, 2);


                for (OrderColourSize.ColourSizeItem item : CIM.get(group)) {

                    String color = DatabaseOpenHelper.getInstance(null).getColorNameById(item.rugcolour_id);
                    String colorNum = DatabaseOpenHelper.getInstance(null).getRugcolourCodeFromId(String.valueOf(item.rugcolour_id));
                    String resDes = design + " " + colorNum + " " + color;
                    OrderColourSize.QualityDesignItem qual = DatabaseOpenHelper.getInstance(null).getQualityItemByName(group, String.valueOf(item.rugcolour_id));
                    quality = DatabaseOpenHelper.getInstance(null).getArticleFromQualAndPal(qual.quality_id, qual.pallete_id);//EXP ;


                    String strSize = DatabaseOpenHelper.getInstance(null).getSizeNameById(item.size_id);
                    String shape = DatabaseOpenHelper.getInstance(null).getSizeShapeById(item.size_id);
                    String resSize = strSize + " " + shape;
                    int resId = 1;

                    for (i = 1; i <= mColsSize; i++) {

                        if (headers[i].equals(resSize)) {
                            resId = i;
                            break;
                        }

                    }
                    boolean rowFound = false;
                    for (String[] it2 : tempD) {
                        if (it2[0].equals(resDes)) {

                            it2[resId] = String.valueOf(item.quantity);
                            rowFound = true;
                            break;
                        }
                    }
                    if (!rowFound) {

                        String[] dd = new String[mColsSize + 1];
                        dd[0] = resDes;
                        for (i = 1; i <= mColsSize; i++) {
                            dd[i] = "";
                        }
                        dd[resId] = String.valueOf(item.quantity);
                        tempD.add(dd);
                    }


                }
                i = 0;
                for (String it : qualSet) {
                    if (familys[i].name.equals(quality)) {
                        for (String[] it2 : tempD) {
                            if (familys[i].list.isEmpty()) mRowsSize++;
                            familys[i].list.add(new Nexus(it2, mColsSize + 1));
                            mRowsSize++;
                        }
                        break;
                    }
                    i++;
                }
                tempD.clear();

            }

            density = context.getResources().getDisplayMetrics().density;

        }

        @Override
        public int getRowCount() {
            return mRowsSize;
        }

        @Override
        public int getColumnCount() {
            return mColsSize;
        }

        @Override
        public View getView(int row, int column, View convertView, ViewGroup parent) {
            final View view;
            switch (getItemViewType(row, column)) {
                case 0:
                    view = getFirstHeader(row, column, convertView, parent);
                    break;
                case 1:
                    view = getHeader(row, column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);
                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    break;
                case 4:
                    view = getFamilyView(row, column, convertView, parent);
                    break;
                default:
                    throw new RuntimeException("wtf?");
            }


            return view;
        }

        private View getFirstHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_header_first, parent, false);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[0]);
            return convertView;
        }

        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_header, parent, false);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[column + 1]);
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_first, parent, false);
            }
            convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column + 1]);
            return convertView;
        }

        private View getBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table, parent, false);
            }
            convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column + 1]);
            final int r = row;
            final int c = column;
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(getBaseContext(), ArticleDetailActivity.class);
                    String qualityDesign;
                    int i = 0;
                    int s = familys[i].list.size() + 1;
                    i++;
                    if (s >= r) {
                        s = 0;
                        i--;
                    } else {
                        while (s < r) {
                            s += familys[i].list.size() + 1;
                            i++;
                        }

                        --i;
                        s -= familys[i].list.size() + 1;
                    }
                    qualityDesign = familys[i].name + familys[i].list.get(r - s - 1).data[0].substring(0, 4);

                    detailIntent.putExtra(ArticleDetailActivity.ARG_QUALITY_DESIGN, qualityDesign);//todo
                    startActivity(detailIntent);
                    //Toast.makeText(getBaseContext(), "cell in row " + r +" and column "+ c, Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_family, parent, false);
            }
            final String string;
            if (column == -1) {
                string = getFamily(row).name;
            } else {
                string = "";
            }
//            final int r = row;
//            final int c = column;
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
//            convertView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getBaseContext(), "cell in row " + r +" and column "+ c, Toast.LENGTH_SHORT).show();
//                }
//            });
            return convertView;
        }

        @Override
        public int getWidth(int column) {
            return Math.round(widths[column + 1] * density);
        }

        @Override
        public int getHeight(int row) {
            final int height;
            if (row == -1) {
                height = 35;
            } else if (isFamily(row)) {
                height = 25;
            } else {
                height = 45;
            }
            return Math.round(height * density);
        }

        @Override
        public int getItemViewType(int row, int column) {
            final int itemViewType;
            if (row == -1 && column == -1) {
                itemViewType = 0;
            } else if (row == -1) {
                itemViewType = 1;
            } else if (isFamily(row)) {
                itemViewType = 4;
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
        }

        private boolean isFamily(int row) {
            int family = 0;
            while (row > 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            return row == 0;
        }

        private NexusTypes getFamily(int row) {
            int family = 0;
            while (row >= 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            return familys[family - 1];
        }

        private Nexus getDevice(int row) {
            int family = 0;
            while (row >= 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            family--;
            return familys[family].get(row + familys[family].size());
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }
    }
}
