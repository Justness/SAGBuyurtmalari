package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ArticleListFragment.Callbacks,
        OrderListFragment.Callbacks {


    private boolean mTwoPane;
    private RecyclerView mRecyclerView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseOpenHelper.getInstance(this.getBaseContext()).close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT DATABASE
        //initializeDBHelper();

//        try
//        {
//            // get input stream
//            //FileInputStream ims = mContext.openFileInput("");
//            // load image as Drawable
//            //Drawable d = Drawable.createFromStream(ims, null);
//            // set image to ImageView
//            //holder.mImgUrl.setImageDrawable(d);
//            String FILENAME = "hello_file";
//            String string = "hello world!";
//
//            FileOutputStream fos = getBaseContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            fos.write(string.getBytes());
//            fos.close();
//        }
//        catch(IOException ex)
//        {
//            return;
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.article_list);

        if (mRecyclerView != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-large and
//            // res/values-sw600dp). If this view is present, then the
//            // activity should be in two-pane mode.
            mTwoPane = true;
//
//            // In two-pane mode, list items should be given the
//            // 'activated' state when touched.

            ((ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).setActivateOnItemClick(true);
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {//Catalog
            // Handle the camera action
            Bundle arguments = new Bundle();
            //  arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, id);
            ArticleListFragment fragment = new ArticleListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
            mRecyclerView.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) { //Orders
            // fragment transaction.
            Bundle arguments = new Bundle();
            //arguments.putString(OrderListFragment.ARG_ITEM_ID, id);
            OrderListFragment fragment = new OrderListFragment();
            fragment.setArguments(arguments);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment);
            // transaction.addToBackStack(null);
            transaction.commit();
            mRecyclerView.setVisibility(View.GONE);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSubItemSelected(String quality_design) {
        Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
        detailIntent.putExtra(ArticleDetailActivity.ARG_QUALITY_DESIGN, quality_design);//todo
        startActivity(detailIntent);

    }

//    @Override
//    public void onItemSelected(String id) {
        // if (mTwoPane) {
        // In two-pane mode, show the detail view in this activity by
        // adding or replacing the detail fragment using a
        // fragment transaction.
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, id);
//                    ArticleListFragment fragment = new ArticleListFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        // mRecyclerView.setAdapter(new MyCollectionRecyclerViewAdapter(DummyContent.ITEMS2, mCallbacks, mImageFetcher));
//                } else {
//                    // In single-pane mode, simply start the detail activity
//                    // for the selected item ID.
//                    Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
//                    detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
//                    startActivity(detailIntent);
//                }
    //}


    @Override
    public void onOrderItemSelected(String id) {
        Intent detailIntent = new Intent(this, OrderDetailActivity.class);
        detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }

}
