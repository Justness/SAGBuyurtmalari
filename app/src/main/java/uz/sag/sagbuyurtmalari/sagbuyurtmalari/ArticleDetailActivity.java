package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.Locale;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.OrderColourSize;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    public static final String ARG_QUALITY_DESIGN = "qualplusdesign";
    public static final int MINI_IMAGE_WIDTH = 300;
    public static final int MINI_IMAGE_HEIGHT = 600;
    private static final int DELETE_THE_LINE = 1;
    private ListView mRugListView;
    private ImageSwitcher mImageSwitcher;

    private Cursor mCursor;
    private Bitmap mBitmap;
    private View.OnTouchListener mTouchListener;

    private ImageButton prevImgBtn;
    private ImageButton nextImgBtn;
    private Button colorBtn;

    private AddRugDialog mDialog;

    Animation animationOut;
    Animation animationIn;

    Animation animationOutRev;
    Animation animationInRev;

    View.OnContextClickListener mConListener;
    private String mCond;
    public static ArrayAdapter<String> listAdapter;

    // int imageSwitcherImages[] = {R.drawable.gilam1, R.drawable.gilam2, R.drawable.gilam3 };

    //  int switcherImage = imageSwitcherImages.length;
    int counter = 0;


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DELETE_THE_LINE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;

            OrderColourSize.removeItem(listAdapter.getItem(listPosition), mCond);
        }

        return super.onContextItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_details, menu);
        menu.findItem(R.id.cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOrderItemSelected("-1");
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void onOrderItemSelected(String id) {
        Intent detailIntent = new Intent(this, OrderDetailsActivity.class);
        detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(MainActivity.LOCAL);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        mCond = intent.getStringExtra(ARG_QUALITY_DESIGN);

        //fill in order article and quality
        //OrderQualityDesign.

        String cond = DatabaseOpenHelper.GALLERY_TABLE_FIELDS[1] + "=\'" + mCond.substring(0, 2) + "\' " + "AND " + DatabaseOpenHelper.GALLERY_TABLE_FIELDS[2] + "=\'" + mCond.substring(2) + "\' ";
        mCursor = DatabaseOpenHelper.getInstance(getBaseContext()).getImagesCursor(cond);


        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(mCond);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent detailIntent = new Intent(getBaseContext(), OrderDetailActivity.class);
//                detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, "-1");
//                startActivity(detailIntent);
//            }
//        });
        mDialog = new AddRugDialog();
        final Button addRugBtn = (Button) findViewById(R.id.addrug);
        final Button resetRugBtn = (Button) findViewById(R.id.resetrug);

        resetRugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderColourSize.clearItem(mCond);
            }
        });
        addRugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddRug(mDialog);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        colorBtn = (Button) findViewById(R.id.button2);
        mCursor.moveToFirst();
        colorBtn.setText(mCursor.getString(3) + mCursor.getString(4));
        mCursor.moveToPrevious();
        //Set order characteristics panel
        mRugListView = (ListView) findViewById(R.id.characs_of_design);
        //Clear size and colors of current quality and design
        OrderColourSize.CART_SUB_ITEMS.clear();
        //and fill with data from the common map
        if (OrderColourSize.CART_ITEM_MAP.containsKey(mCond))
            for (OrderColourSize.ColourSizeItem item : OrderColourSize.CART_ITEM_MAP.get(mCond)) {
                String phone = item.content;

                OrderColourSize.CART_SUB_ITEMS.add(phone);
            }

        listAdapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                OrderColourSize.CART_SUB_ITEMS);
        mRugListView.setAdapter(listAdapter);
        mRugListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


                contextMenu.add(0, DELETE_THE_LINE, 0, getResources().getString(R.string.delete));

            }
        });



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


            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.decodeFile(path, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, MINI_IMAGE_WIDTH, MINI_IMAGE_HEIGHT);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            mBitmap = BitmapFactory.decodeFile(path, options);


            mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                          @Override
                                          public View makeView() {
                                              ImageView myView = new ImageView(getApplicationContext());
                                              myView.setLayoutParams(new ImageSwitcher.LayoutParams(
                                                      ActionBar.LayoutParams.MATCH_PARENT,
                                                      ActionBar.LayoutParams.MATCH_PARENT
                                              ));
                                              myView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                                              myView.setImageDrawable(new BitmapDrawable(mBitmap));
                                              myView.setOnTouchListener(mTouchListener);
                                              return myView;
                                          }
                                      }

            );
        }


        animationOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animationIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        animationOutRev = AnimationUtils.loadAnimation(this, R.animator.slide_out_left);
        animationInRev = AnimationUtils.loadAnimation(this, R.animator.slide_in_right);

        mImageSwitcher.setOutAnimation(animationOut);
        mImageSwitcher.setInAnimation(animationIn);

//        Button prevBtn = (Button) findViewById(R.id.prevImg);
//        Button nextBtn = (Button) findViewById(R.id.nextImg);

//        prevBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                prevImageButton(view);
//            }
//        });
//
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nextImageButton(view);
//            }
//        });

        prevImgBtn = (ImageButton) findViewById(R.id.prevImgButton);
        nextImgBtn = (ImageButton) findViewById(R.id.nextImgButton);

        prevImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prevImageButton(view);
            }
        });
        //prevImgBtn.setImageBitmap();

        Button nBtn = (Button) findViewById(R.id.buttonNext);
        Button pBtn = (Button) findViewById(R.id.buttonPrev);


        nextImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImageButton(view);
            }
        });
        //nextImgBtn.setImageBitmap();
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

        if (mCursor.moveToPrevious()) {
            prevImgBtn.setVisibility(View.VISIBLE);
            prevImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
            mCursor.moveToNext();
        } else {
            mCursor.moveToFirst();
            prevImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
            prevImgBtn.setVisibility(View.INVISIBLE);
        }

        if (mCursor.moveToNext()) {
            nextImgBtn.setVisibility(View.VISIBLE);
            nextImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
            mCursor.moveToPrevious();
        } else {
            mCursor.moveToLast();
            nextImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
            nextImgBtn.setVisibility(View.INVISIBLE);
        }

        nBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImageButton(view);
            }
        });

        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevImageButton(view);
            }
        });
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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

    public void confirmAddRug(AddRugDialog dialog) {
//        DialogFragment newFragment = new AddRugDialog();
//        newFragment.show(getFragmentManager(), "addrug");
        String width = dialog.getWidthValue();
        String height = dialog.getHeightValue();
        int quantity = Integer.parseInt(dialog.getQuantityValue());
        boolean finishing = dialog.getFinishingValue();
        DatabaseOpenHelper dbhelper = DatabaseOpenHelper.getInstance(this.getBaseContext());
        int sizeId = dbhelper.getSizeId(width, height, finishing);

        if (sizeId != -1) {
            //record found
            int rugcolourId = dbhelper.getRugcolourId(mCursor.getString(2), mCursor.getString(3), mCursor.getString(4));
            String sizeName = dbhelper.getSizeNameById(sizeId);
            if (rugcolourId != -1) {
                String colorName = dbhelper.getColorNameById(rugcolourId);
                String quantityName = String.valueOf(quantity) + " units";
                String listitem = colorName + " " + sizeName + " R " + quantityName;
                if (finishing)
                    listitem = colorName + " " + sizeName + " O " + quantityName;


                OrderColourSize.addItem(new OrderColourSize.ColourSizeItem(sizeId, rugcolourId, 1, quantity, listitem),
                        listitem,
                        mCond);
                mRugListView.refreshDrawableState();
            } else {
                Snackbar.make(mRugListView, getResources().getString(R.string.color_not_found), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            Snackbar.make(mRugListView, getResources().getString(R.string.size_not_found), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    public void nextImageButton(View view) {
//        counter++;
//        if (counter == switcherImage)
//            counter = 0;
        boolean cursormove = mCursor.moveToNext();
        if (cursormove) {
            mCursor.moveToPrevious();
            prevImgBtn.setVisibility(View.VISIBLE);

            prevImgBtn.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1))));
            mCursor.moveToNext();
            if (cursormove) {

                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1), options);
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, MINI_IMAGE_WIDTH, MINI_IMAGE_HEIGHT);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1), options);
                colorBtn.setText(mCursor.getString(3) + mCursor.getString(4));
            }
            else {
                mCursor.moveToLast();
//              mCursor.moveToFirst();
//              mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
            }


            //miniature navigation
            if (mCursor.moveToNext()) {
                nextImgBtn.setVisibility(View.VISIBLE);
                nextImgBtn.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1))));

                mCursor.moveToPrevious();
            } else {
                mCursor.moveToLast();
//              mCursor.moveToFirst();
//              nextImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
//              mCursor.moveToLast();
                nextImgBtn.setVisibility(View.INVISIBLE);
            }


            mImageSwitcher.setOutAnimation(animationOutRev);
            mImageSwitcher.setInAnimation(animationInRev);

            mImageSwitcher.setImageDrawable(new BitmapDrawable(mBitmap));
        } else {
            mCursor.moveToLast();
//            mCursor.moveToFirst();
//            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
        }
    }
    public void prevImageButton(View view) {
//        counter--;
//        if (counter == -1)
//            counter = switcherImage-1;
        boolean cursormove = mCursor.moveToPrevious();
        if (cursormove) {
            mCursor.moveToNext();
            nextImgBtn.setVisibility(View.VISIBLE);
            nextImgBtn.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1))));
            mCursor.moveToPrevious();
            if (cursormove) {
                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1), options);
                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, MINI_IMAGE_WIDTH, MINI_IMAGE_HEIGHT);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1), options);
                colorBtn.setText(mCursor.getString(3) + mCursor.getString(4));
            } else {
                mCursor.moveToFirst();
//            mCursor.moveToLast();
//            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
            }
            //miniature navigation
            if (mCursor.moveToPrevious()) {
                prevImgBtn.setVisibility(View.VISIBLE);
                prevImgBtn.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1))));
                mCursor.moveToNext();
            } else {
                mCursor.moveToFirst();
//            mCursor.moveToLast();
//            prevImgBtn.setImageDrawable(Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mCursor.getString(1)));
//            mCursor.moveToFirst();

                prevImgBtn.setVisibility(View.INVISIBLE);
            }

            mImageSwitcher.setOutAnimation(animationOut);
            mImageSwitcher.setInAnimation(animationIn);

            mImageSwitcher.setImageDrawable(new BitmapDrawable(mBitmap));
        } else {
            mCursor.moveToFirst();
//            mCursor.moveToLast();
//            mBitmap = Drawable.createFromPath(Environment.getExternalStorageDirectory() + MyCollectionRecyclerViewAdapter.IMAGES_DIRECTORY + mCursor.getString(1));
        }
    }


    public static final int[] mainWidths = {
            50,
            60,
            70,
            75,
            80,
            100,
            120,
            150,
            200,
            250,
            300,
            350,
            400,
            500};
    public static final int[] mainHeights = {
            80,
            100,
            110,
            125,
            150,
            200,
            230,
            300,
            350,
            400,
            450,
            500,
            550,
            600,
            700,
            800,
            900,
            1000,
            2500};

    public class AddRugDialog {

        private EditText mWidthValue;


        public final int[][] mainSizes = {{3028, 50, 80, 0},
                {3027, 50, 100, 0},
                {3039, 60, 110, 0},
                {3059, 80, 125, 0},
                {3029, 80, 150, 0},
                {2050, 100, 150, 0},
                {2051, 100, 200, 0},
                {2052, 100, 300, 0},
                {2053, 100, 400, 0},
                {2045, 150, 230, 0},
                {2046, 150, 300, 0},
                {2047, 150, 400, 0},
                {2039, 200, 300, 0},
                {2041, 200, 400, 0},
                {2043, 200, 500, 0},
                {2032, 250, 350, 0},
                {2033, 250, 400, 0},
                {2034, 250, 450, 0},
                {2035, 250, 500, 0},
                {2036, 250, 550, 0},
                {2026, 300, 400, 0},
                {2027, 300, 500, 0},
                {2028, 300, 550, 0},
                {2029, 300, 600, 0},
                {2030, 300, 700, 0},
                {2019, 350, 400, 0},
                {2112, 350, 450, 0},
                {2020, 350, 500, 0},
                {2107, 350, 550, 0},
                {2021, 350, 600, 0},
                {2022, 350, 700, 0},
                {2023, 350, 800, 0},
                {2018, 400, 400, 0},
                {2017, 400, 500, 0},
                {2114, 400, 550, 0},
                {2016, 400, 600, 0},
                {2015, 400, 700, 0},
                {2014, 400, 800, 0},
                {2013, 400, 900, 0},
                {2012, 400, 1000, 0},
                {2054, 50, 2500, 0},
                {2055, 60, 2500, 0},
                {2056, 70, 2500, 0},
                {2057, 75, 2500, 0},
                {2058, 80, 2500, 0},
                {2059, 100, 2500, 0},
                {2061, 120, 2500, 0},
                {2063, 150, 2500, 0},
                {2065, 200, 2500, 0},
                {2096, 250, 2500, 0},
                {3034, 50, 80, 1},
                {3035, 50, 100, 1},
                {3049, 60, 110, 1},
                {3067, 80, 125, 1},
                {3032, 80, 150, 1},
                {2085, 100, 150, 1},
                {2084, 100, 200, 1},
                {2083, 100, 300, 1},
                {2082, 100, 400, 1},
                {2081, 150, 230, 1},
                {2080, 150, 300, 1},
                {2047, 150, 400, 1},
                {2073, 200, 300, 1}};

        public AddRugDialog() {
            mWidthValue = (EditText) findViewById(R.id.widthvalue);
            mWidthValue.setText(String.valueOf(mainWidths[0]));
            mHeightValue = (EditText) findViewById(R.id.heightvalue);
            mHeightValue.setText(String.valueOf(mainHeights[0]));
            mQuantityValue = (EditText) findViewById(R.id.quantity);

            CheckBox chbox = (CheckBox) findViewById(R.id.checkBox);
            chbox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mHeightValue.isEnabled()) {
                        mHeightValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        mHeightValue.setEnabled(true);
                        mHeightValue.setVisibility(View.VISIBLE);
                        mWidthValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        mWidthValue.setVisibility(View.VISIBLE);
                        mWidthValue.setEnabled(true);

                        heightSeek.setVisibility(View.INVISIBLE);
                        widthSeek.setVisibility(View.INVISIBLE);
                    } else {
                        mHeightValue.setInputType(InputType.TYPE_NULL);
                        mHeightValue.setVisibility(View.INVISIBLE);
                        mHeightValue.setEnabled(false);
                        mWidthValue.setInputType(InputType.TYPE_NULL);
                        mWidthValue.setVisibility(View.INVISIBLE);
                        mWidthValue.setEnabled(false);

                        heightSeek.setVisibility(View.VISIBLE);
                        widthSeek.setVisibility(View.VISIBLE);
                    }
                }
            });


            widthSeek = (Button) findViewById(R.id.rugwidth);
            heightSeek = (Button) findViewById(R.id.rugheight);
            // widthSeek.setProgress(0);
            //widthSeek.incrementProgressBy(10);
            // mWidthValue.setText(Integer.toString(widthSeek.getProgress()).trim());

            widthSeek.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(widthSeek.getContext(), widthSeek);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.width_options, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            //Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                            widthSeek.setText(item.getTitle());
                            heightSeek.setText(item.getTitle());
                            mWidthValue.setText(item.getTitle());
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }

            });


//            setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                //@TODO count progress from mainSizes progress = DatabaseOpenHelper.getInstance(null).getNearestWidth(progress);
//                //ALSO COMPLETE SAVE/SEND ORDER
//
//                mWidthValue.setText(String.valueOf(mainWidths[progress]));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

            // widthSeek.setProgress(0);
            //widthSeek.incrementProgressBy(10);
            // mWidthValue.setText(Integer.toString(widthSeek.getProgress()).trim());

            heightSeek.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(heightSeek.getContext(), heightSeek);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.height_options, popup.getMenu());
                    int i = -1;

                    while ((Integer.parseInt(widthSeek.getText().toString()) > Integer.parseInt(popup.getMenu().getItem(++i).getTitle().toString()))) {


                        popup.getMenu().getItem(i).setVisible(false);

                    }
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            heightSeek.setText(item.getTitle());
                            mHeightValue.setText(item.getTitle());
                            //Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }

            });

//        SeekBar heightSeek = (SeekBar) dialogView.findViewById(R.id.rugheight);
//        //heightSeek.setProgress(0);
//        //heightSeek.incrementProgressBy(10);
//        //  mHeightValue.setText(Integer.toString(heightSeek.getProgress()).trim());
//        heightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                //progress = DatabaseOpenHelper.getInstance(null).getNearestHeight(progress);
//
//                mHeightValue.setText(String.valueOf(mainHeights[progress]));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

            mHeightValue.setInputType(InputType.TYPE_NULL);
            mHeightValue.setVisibility(View.INVISIBLE);
            mHeightValue.setEnabled(false);
            mWidthValue.setInputType(InputType.TYPE_NULL);
            mWidthValue.setVisibility(View.INVISIBLE);
            mWidthValue.setEnabled(false);

            heightSeek.setVisibility(View.VISIBLE);
            widthSeek.setVisibility(View.VISIBLE);


            final Button minusBtn = (Button) findViewById(R.id.quantityminus);
            final Button plusBtn = (Button) findViewById(R.id.quantityplus);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int intVal = Integer.parseInt(mQuantityValue.getText().toString());
                    mQuantityValue.setText(String.valueOf(intVal + 2));
                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int intVal = Integer.parseInt(mQuantityValue.getText().toString());
                    if (intVal > 1)
                        mQuantityValue.setText(String.valueOf(intVal - 2));
                    else
                        mQuantityValue.setText("0");
                }
            });

            mShape = (Switch) findViewById(R.id.shape);
            mShape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFinishingValue = !mFinishingValue;
                    mShape.setChecked(mFinishingValue);

                }
            });
        }

        public String getWidthValue() {
            return mWidthValue.getText().toString();
        }

        public String getHeightValue() {
            return mHeightValue.getText().toString();
        }

        public String getQuantityValue() {
            return mQuantityValue.getText().toString();
        }

        public boolean getFinishingValue() {
            return mFinishingValue;
        }

        Button widthSeek;
        Button heightSeek;
        private EditText mHeightValue;
        private Switch mShape;
        private EditText mQuantityValue;
        private boolean mFinishingValue; // R = False O = TRUE

        /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */


        // Use this instance of the interface to deliver action events







    }
}
