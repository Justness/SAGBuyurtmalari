package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

/**
 * Created by User on 07.07.2016.
 */
public class DesignsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyCollectionRecyclerViewAdapter mAdapter;

    private String mCurrentQuality = "AC";
    private int mColumnCount = 6;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrderItemSelected("-1");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
        if (!getIntent().getStringExtra(QualityListFragment.QUALITY_CODE).isEmpty()) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mCurrentQuality = getIntent().getStringExtra(QualityListFragment.QUALITY_CODE);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.article_design_list);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), mColumnCount));
        }

        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getBaseContext());

        // db.clearAllOrders();
        // mCurrentQuality = "AC";

        mAdapter = new MyCollectionRecyclerViewAdapter(getBaseContext(), db.getImages(
                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + mCurrentQuality + "\""), mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void onOrderItemSelected(String id) {
        Intent detailIntent = new Intent(this, OrderDetailActivity.class);
        detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        menu.findItem(R.id.clearsearch).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterItemList(getCurrentQuality());
                return true;
            }
        });

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    Snackbar.make(mDetailsFragment.getView(), "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    filterItemList(getCurrentQuality(), query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }

        // SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onSubItemSelected(String quality_design) {
            Intent detailIntent = new Intent(getBaseContext(), ArticleDetailActivity.class);
            detailIntent.putExtra(ArticleDetailActivity.ARG_QUALITY_DESIGN, quality_design);//todo
            startActivity(detailIntent);

        }
    };


    public Callbacks mCallbacks = sDummyCallbacks;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Activities containing this fragment must implement its callbacks.
//        if (!(activity instanceof Callbacks)) {
//            throw new IllegalStateException("Activity must implement fragment's callbacks.");
//        }
//        mCallbacks = (Callbacks) activity;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        // Reset the active callbacks interface to the dummy implementation.
//        mCallbacks = sDummyCallbacks;
//        // DatabaseOpenHelper.getInstance(getContext()).close();
//        mRecyclerView = null;
//    }
    public void filterItemList(String qual) {
        mCurrentQuality = qual;
        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getBaseContext());
        mAdapter = new MyCollectionRecyclerViewAdapter(getBaseContext(), db.getImages(
                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + qual + "\""), mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void filterItemList(String qual, String des) {
        DatabaseOpenHelper db = DatabaseOpenHelper.getInstance(getBaseContext());
        mAdapter = new MyCollectionRecyclerViewAdapter(getBaseContext(), db.getImages(
                DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\"" + qual + "\" AND " +
                        DatabaseOpenHelper.GALLERY_TABLE_FIELDS[2] + "=\"" + des + "\""), mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_collection_list, container, false);
//
//        // programmatically create a scrollview and textview for the text in
//        // the container/fragment layout. Set up the properties and add the view
//
//
//        return rootView;
//    }
}
