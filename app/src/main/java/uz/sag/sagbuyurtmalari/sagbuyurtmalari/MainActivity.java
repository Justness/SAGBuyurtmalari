package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,// DetailsFragment.Callbacks,
        OrderListFragment.Callbacks {


    private boolean mTwoPane;
    //  private RecyclerView mRecyclerView;
    private static OrderListFragment mOrderFragment;
    private static ArticleListFragment mArticleFragment;
    private static QualityListFragment mQualityFragment;
    public static DetailsFragment mDetailsFragment;
    public static Registration mRegFragment;

    private int navId = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseOpenHelper.getInstance(this.getBaseContext()).close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 45);
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
                onOrderItemSelected("-1");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        menu.findItem(R.id.action_sync).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.syncRugs();
                return true;
            }
        });

        menu.findItem(R.id.clearsearch).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mDetailsFragment.filterItemList(mDetailsFragment.getCurrentQuality());
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
                    mDetailsFragment.filterItemList(mDetailsFragment.getCurrentQuality(), query);
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


    public static boolean syncRugs() {

        File file = Environment.getExternalStorageDirectory();
//
        DatabaseOpenHelper.getInstance(null).synchronizeImagesFromGallery(file.getPath() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY);
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
        if (navId == id) return true;
        navId = id;

        if (id == R.id.nav_camera) {//Catalog
            //mRecyclerView.setVisibility(View.VISIBLE);
            // Handle the camera action
            // Arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, id);

            Bundle arguments = new Bundle();
//            Intent intent = new Intent(this,ArticleListActivity.class);
//            startActivity(intent);

            mQualityFragment.setArguments(arguments);

            //mArticleFragment.setArguments(arguments);
            //getSupportFragmentManager().beginTransaction().add(R.id.details, mDetailsFragment).commit();
            //.remove(mQualityFragment)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mQualityFragment).commit();

        } else if (id == R.id.nav_gallery) { //Users
            Bundle arguments = new Bundle();
            //arguments.putString(OrderListFragment.ARG_ITEM_ID, id);
            mRegFragment.setArguments(arguments);


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // transaction.remove(mRegFragment);
            transaction.replace(R.id.fragment, mRegFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) { //Orders
            // fragment transaction.

            //mRecyclerView.setVisibility(View.GONE);
            Bundle arguments = new Bundle();
            //arguments.putString(OrderListFragment.ARG_ITEM_ID, id);
            mOrderFragment.setArguments(arguments);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.remove(mQualityFragment);
            // transaction.remove(mOrderFragment);
            transaction.replace(R.id.fragment, mOrderFragment);
            // transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_share) { //News
            Intent intent = new Intent(this, News.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) { //About company
            Intent intent = new Intent(this, AboutCompany.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


//    @Override
//    public void onSubItemSelected(String quality_design) {
//        Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
//        detailIntent.putExtra(ArticleDetailActivity.ARG_QUALITY_DESIGN, quality_design);//todo
//        startActivity(detailIntent);
//
//    }

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

    @Override
    protected void onStart() {
        super.onStart();
        if (mOrderFragment == null)
            mOrderFragment = new OrderListFragment();
        if (mRegFragment == null)
            mRegFragment = new Registration();
        if (mQualityFragment == null)
            mQualityFragment = new QualityListFragment();
//        if (mArticleFragment == null)
//            mArticleFragment = new ArticleListFragment();
//        if (mDetailsFragment == null)
//            mDetailsFragment = new DetailsFragment();

        //if (getSupportFragmentManager().getFragments().size()==0){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        mArticleFragment = new ArticleListFragment();
//        mDetailsFragment = new DetailsFragment();
//        //mRecyclerView = (RecyclerView) findViewById(R.id.article_list);
//

//        transaction.replace(R.id.details, mQualityFragment).commit();
        transaction.replace(R.id.fragment, mQualityFragment).commit();
//        transaction.add(R.id.details, mQualityFragment).commit();
        //}
    }
    //}


    @Override
    public void onOrderItemSelected(String id) {
        Intent detailIntent = new Intent(this, OrderDetailActivity.class);
        detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }

}
