package uz.sag.sagbuyurtmalari.sagbuyurtmalari.model;

import java.util.Date;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Order {

    /**
     * An array of sample (dummy) items.
     */
    public static OrderItem CART_ITEM;


    private static final int COUNT = 25;


    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createOrderItem(i));
        }
    }

    private static void addItem(OrderItem item) {

    }


    private static OrderItem createOrderItem(int position) {
        return new OrderItem(position);
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
    public static class OrderItem {
        public static final int ORDERED = 0;
        public static final int READY = 1;
        public static final int SEND = 2;

        public int id;
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

        public OrderItem(int id) {
            this.id = id;

        }

    }
}
