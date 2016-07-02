package uz.sag.sagbuyurtmalari.sagbuyurtmalari.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<ColourSizeItem> CART_ITEMS = new ArrayList<ColourSizeItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ColourSizeItem> CART_ITEM_MAP = new HashMap<String, ColourSizeItem>();

    private static final int COUNT = 25;


//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createColourSizeItem(i));
//        }
//    }

    private static void addItem(ColourSizeItem item) {
        CART_ITEMS.add(item);
        CART_ITEM_MAP.put(String.valueOf(item.id), item);
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
        public int id;
        public int size_id;
        public int rugcolour_id;
        public int orderqualityanddesign_id;
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

    }
}
