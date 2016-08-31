package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

/**
 * Created by User on 07.07.2016.
 */
public class DetailsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyCollectionRecyclerViewAdapter mAdapter;

    private String mCurrentQuality = "AC";
    private int mColumnCount = 4;

    public String getCurrentQuality() {
        return mCurrentQuality;
    }
    // The system calls this when it's time for the fragment to draw its
    // user interface for the first time. To draw a UI for your fragment,
    // you must return a View from this method that is the root of your
    // fragment's layout. You can return null if the fragment does not
    // provide a UI.

    // We create the UI with a scrollview and text and return a reference to
    // the scoller which is then drawn to the screen

    // private Callbacks.onS mListener;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */

        public void onSubItemSelected(String quality_design);
    }


    public Callbacks mCallbacks = sDummyCallbacks;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onSubItemSelected(String quality_design) {


        }
    };

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
        // DatabaseOpenHelper.getInstance(getContext()).close();
        mRecyclerView = null;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.article_list);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getContext());

        // db.clearAllOrders();
        // mCurrentQuality = "AC";
//        mAdapter = new MyCollectionRecyclerViewAdapter(getContext(), db.getImages(
//                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + mCurrentQuality + "\""), mCallbacks);
//        mRecyclerView.setAdapter(mAdapter);
    }

    public void filterItemList(String qual) {
//        mCurrentQuality = qual;
//        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getContext());
//        mAdapter = new MyCollectionRecyclerViewAdapter(getContext(), db.getImages(
//                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + qual + "\""), mCallbacks);
//        mRecyclerView.setAdapter(mAdapter);
    }

    public void filterItemList(String qual, String des) {
        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getContext());
//        mAdapter = new MyCollectionRecyclerViewAdapter(getContext(), db.getImages(
//                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + qual + "\" AND " +
//                        DatabaseOpenHelper.GALLERY_TABLE_FIELDS[2] + "=\"" + des + "\""), mCallbacks);
//        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_list, container, false);

        // programmatically create a scrollview and textview for the text in
        // the container/fragment layout. Set up the properties and add the view


        return rootView;
    }
}
