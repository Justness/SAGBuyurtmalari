package uz.sag.sagbuyurtmalari.sagbuyurtmalari.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.ArticleDetailActivity;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class OrderColourSize {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<String> CART_SUB_ITEMS = new ArrayList<String>();

    public static final Set<String> CART_ITEMS = new HashSet<String>();

//    public static final List<Map<String, String> > GROUP_DATA = new ArrayList<Map<String, String> >();
//    public static final List<ArrayList<Map<String, String> > > CHILD_DATA = new ArrayList<ArrayList<Map<String, String> > >();

//    static Map<String, String> m;
//    static ArrayList<Map<String, String>> childDataItem;
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, List<ColourSizeItem>> CART_ITEM_MAP = new HashMap<String, List<ColourSizeItem>>();

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createColourSizeItem(i));
//        }
//    }

    public static void addItem(ColourSizeItem item, String name, String qualDes) {

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
            CART_ITEM_MAP.put(qualDes, new ArrayList<ColourSizeItem>());
            CART_ITEM_MAP.get(qualDes).add(item);
        }
        ArticleDetailActivity.listAdapter.notifyDataSetChanged();
    }

    public static void clearItem(String qualDes) {

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

        CART_SUB_ITEMS.clear();
        if (CART_ITEM_MAP.containsKey(qualDes)) {
            CART_ITEM_MAP.get(qualDes).clear();

        }
        ArticleDetailActivity.listAdapter.notifyDataSetChanged();
    }


    private static ColourSizeItem createColourSizeItem(int position) {
        return new ColourSizeItem(position);
    }


    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ColourSizeItem {
        public long id;
        public int size_id;
        public int rugcolour_id;
        public int orderqualityanddesign_id;
        public int quantity;
        public String content;

        @Override
        public String toString() {
            return content;
        }
//public String meaning;

        public ColourSizeItem(int id) {
            this.id = id;

        }

        public ColourSizeItem(int size, int rugcolour, int orderqualityanddesign_id) {
            this.size_id = size;
            this.rugcolour_id = rugcolour;
            this.orderqualityanddesign_id = orderqualityanddesign_id;

        }

        public ColourSizeItem(int size, int rugcolour, int orderqualityanddesign_id, int quantity, String cont) {
            this.size_id = size;
            this.rugcolour_id = rugcolour;
            this.orderqualityanddesign_id = orderqualityanddesign_id;
            this.content = cont;
            this.quantity = quantity;
        }

    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class QualityDesignItem {
        public long id;
        public String quality_id;
        public String design_id;
        public String pallete_id;
        public String order_id;

        //public String meaning;

        public QualityDesignItem(String quality_id, String design_id) {
            this.quality_id = quality_id;
            this.design_id = design_id;

        }

        public QualityDesignItem(String quality_id, String design_id, String pallete_id) {
            this.quality_id = quality_id;
            this.design_id = design_id;
            this.pallete_id = pallete_id;

        }

    }

    public static class OrderItem {
        public static final int ORDERED = 0;
        public static final int READY = 1;
        public static final int SEND = 2;

        public long id;
        public int customer_id;
        public int performer_id;
        public String comments;
        public int totalcost;
        public int status;
        public int priority;
        public Date orderdate;
        public Date completedate;
        public int totalquantity;
        public int totalarea;

        //public String meaning;

        public OrderItem(long id) {
            this.id = id;

        }

    }
}
