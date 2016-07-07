package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
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
public class OrderDetailActivity extends ExpandableListActivity {

    public static final List<String> CART_SUB_ITEMS = new ArrayList<String>();

    public static final Set<String> CART_ITEMS = new HashSet<String>();

    public static int TOTAL_AREA = 1;

    public static int TOTAL_QUANTITY = 0;

    public static final Map<String, List<OrderColourSize.ColourSizeItem>> CART_ITEM_MAP = new HashMap<String, List<OrderColourSize.ColourSizeItem>>();

    SimpleExpandableListAdapter seAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_list);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra(ArticleDetailFragment.ARG_ITEM_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.order_detail_toolbar);
        toolbar.setTitle(R.string.title_order_detail);

        mSendOrderBtn = (Button) findViewById(R.id.sendOrder);
        mSendOrderBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (DatabaseOpenHelper.getInstance(getBaseContext()).createNewOrder()) {
                            OrderColourSize.CART_ITEM_MAP.clear();
                            OrderColourSize.CART_ITEMS.clear();
                            OrderColourSize.CART_SUB_ITEMS.clear();
                            finish();
                        }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    }
                }
        );

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


    /*
 * Preparing the list data
 */
    private void prepareListData(Set<String> cartItems, Map<String, List<OrderColourSize.ColourSizeItem>> cartItemMap) {
        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<ArrayList<Map<String, String>>>();

        for (String group : cartItems) {
            // заполняем список аттрибутов для каждой группы
            m = new HashMap<String, String>();
            m.put("groupName", group); // имя компании
            groupData.add(m);

            childDataItem = new ArrayList<Map<String, String>>();
            // заполняем список аттрибутов для каждого элемента
            for (OrderColourSize.ColourSizeItem item : cartItemMap.get(group)) {
                String phone = item.content;
                m = new HashMap<String, String>();
                m.put("phoneName", phone); // название телефона
                childDataItem.add(m);
            }
            // добавляем в коллекцию коллекций
            childData.add(childDataItem);
        }

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
