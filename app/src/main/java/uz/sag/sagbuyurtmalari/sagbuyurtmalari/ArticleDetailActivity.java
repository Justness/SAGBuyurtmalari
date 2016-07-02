package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import java.io.File;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.OrderColourSize;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.ui.AddRugDialog;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class ArticleDetailActivity extends AppCompatActivity implements AddRugDialog.AddRugDialogListener{

    public static final String ARG_QUALITY_DESIGN = "qualplusdesign";
    private ListView mRugListView;
    private ImageSwitcher mImageSwitcher;

    private Cursor mCursor;
    private Drawable mBitmap;
    private View.OnTouchListener mTouchListener;

    int imageSwitcherImages[] = {R.drawable.gilam1, R.drawable.gilam2, R.drawable.gilam3 };

    int switcherImage = imageSwitcherImages.length;
    int counter = 0;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //String myContent = DatabaseOpenHelper.getInstance(getBaseContext()).
        //  OrderColourSize
        //@TODO continue from here
        //get attributes from dialog (var) and get ids from appropriate tables
        // creat new ColorSizeItem from data taken and add it into the OrderColorSize static CART var
        // don't forget to to fill item's content field with human readable BEJ/RED 340x300 R record
        // create orderqualitydesign at the same time
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        String cond = intent.getStringExtra(ARG_QUALITY_DESIGN);

        cond = DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\'" + cond.substring(0, 2) + "\' " + "AND " + DatabaseOpenHelper.GALLERY_TABLE_FIELDS[2] + "=\'" + cond.substring(2) + "\' ";
        mCursor = DatabaseOpenHelper.getInstance(getBaseContext()).getImagesCursor(cond);


        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button addRugBtn = (Button) findViewById(R.id.addrug);

        addRugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddRug();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //Set order characteristics panel
        mRugListView = (ListView) findViewById(R.id.characs_of_design);
        mRugListView.setAdapter(new ArrayAdapter<OrderColourSize.ColourSizeItem>(getBaseContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                OrderColourSize.CART_ITEMS));

        mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                openInStandardGalleryApp();
                return false;
            }
        };

        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        if (mCursor != null /*&& isExternalStorageReadable()*/) {
            mCursor.moveToFirst();
            String path = Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1);
            mBitmap = Drawable.createFromPath(path);


            mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                          @Override
                                          public View makeView() {
                                              ImageView myView = new ImageView(getApplicationContext());
                                              myView.setLayoutParams(new ImageSwitcher.LayoutParams(
                                                      ActionBar.LayoutParams.MATCH_PARENT,
                                                      ActionBar.LayoutParams.MATCH_PARENT
                                              ));
                                              myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                              myView.setImageDrawable(mBitmap);
                                              myView.setOnTouchListener(mTouchListener);
                                              return myView;
                                          }
                                      }

            );
        }




    Animation animationOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
    Animation animationIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

    mImageSwitcher.setOutAnimation(animationOut);
    mImageSwitcher.setInAnimation(animationIn);

        Button prevBtn = (Button) findViewById(R.id.prevImg);
        Button nextBtn = (Button) findViewById(R.id.nextImg);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevImageButton(view);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImageButton(view);
            }
        });
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(ArticleDetailFragment.ARG_ITEM_ID,
//                    getIntent().getStringExtra(ArticleDetailFragment.ARG_ITEM_ID));
//            ArticleDetailFragment fragment = new ArticleDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.article_detail_container, fragment)
//                    .commit();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ArticleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openInStandardGalleryApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

    public void confirmAddRug() {
        DialogFragment newFragment = new AddRugDialog();
        newFragment.show(getFragmentManager(), "addrug");

    }

    public void nextImageButton(View view) {
//        counter++;
//        if (counter == switcherImage)
//            counter = 0;
        if (mCursor.moveToNext())
            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));

        else {
            mCursor.moveToFirst();
            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
        }

        mImageSwitcher.setImageDrawable(mBitmap);
    }
    public void prevImageButton(View view) {
//        counter--;
//        if (counter == -1)
//            counter = switcherImage-1;
        if (mCursor.moveToPrevious())
            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));

        else {
            mCursor.moveToLast();
            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
        }

        mImageSwitcher.setImageDrawable(mBitmap);
    }
}
