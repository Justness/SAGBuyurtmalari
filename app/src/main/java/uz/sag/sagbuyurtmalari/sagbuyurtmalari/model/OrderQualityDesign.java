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
public class OrderQualityDesign {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<QualityDesignItem> CART_ITEMS = new ArrayList<QualityDesignItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, QualityDesignItem> CART_ITEM_MAP = new HashMap<String, QualityDesignItem>();

    private static final int COUNT = 25;


    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createQualityDesignItem(i));
        }
    }

    private static void addItem(QualityDesignItem item) {
        CART_ITEMS.add(item);
        CART_ITEM_MAP.put(String.valueOf(item.id), item);
    }


    private static QualityDesignItem createQualityDesignItem(int position) {
        return new QualityDesignItem(position);
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
    public static class QualityDesignItem {
        public int id;
        public int quality_id;
        public int design_id;
        public int pallete_id;
        public int order_id;

        //public String meaning;

        public QualityDesignItem(int id) {
            this.id = id;

        }

    }
}
