package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dummy.DummyContent;


public class OrderListFragment extends ListFragment {


    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    // private Callbacks.onS mListener;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */

    private SimpleCursorAdapter mAdapter;


    final int ADD_COMMENT = 1;
    final int VIEW_COMMENT = 2;
    public static String orderId;

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onOrderItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onOrderItemSelected(String id) {
        }

    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
     */
    public OrderListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = DatabaseOpenHelper.getInstance(getContext()).getAllOrders();


//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);

        if (cursor != null) {
            mAdapter = new SimpleCursorAdapter(
                getContext(), // Context.
                android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor   rows).
                cursor,                                              // Pass in the cursor to bind to.
                new String[]{"orderdate",
                        "totalarea"},           // Array of cursor columns to bind to.
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            //@TODO may be memory leaks
            setListAdapter(mAdapter);
        }
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,
//                DummyContent.ITEMS)
//        );

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onOrderItemSelected(DummyContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_list, null);
        //super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }
    //Ozod

    public void onActivityCreated(Bundle savedInstanceState) {
        ListView listView = getListView(); //EX:
        listView.setTextFilterEnabled(true);
        registerForContextMenu(listView);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, ADD_COMMENT, 0, "Добавить комментарий");
        menu.add(0, VIEW_COMMENT, 0, "Показать комментарии");


    }

    public boolean onContextItemSelected(MenuItem item) {
        orderId = mAdapter.getCursor().getString(0);
        if (item.getItemId() == ADD_COMMENT) {
            MyDialog myDialog = new MyDialog();
            orderId = mAdapter.getCursor().getString(0);
            myDialog.show(getFragmentManager(), "comment");
        } else {
            String[] arr_com = new String[1];
            String comment = DatabaseOpenHelper.getInstance(getContext()).getByIdOrderComment(orderId);
            arr_com = comment.split("#");

            ListView lV = getListView();

            if (arr_com[1].equals("null")) {
                //Snackbar.make(lV, "Статус неизвестен", Snackbar.LENGTH_LONG);
                Toast toast = Toast.makeText(getContext(),
                        "Статус неизвестен", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getContext(),
                        arr_com[1], Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return super.onContextItemSelected(item);
    }
}
