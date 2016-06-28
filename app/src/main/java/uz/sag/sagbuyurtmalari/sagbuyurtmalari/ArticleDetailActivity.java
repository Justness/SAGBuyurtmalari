package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dummy.DummyContent;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.ui.AddRugDialog;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class ArticleDetailActivity extends AppCompatActivity implements AddRugDialog.AddRugDialogListener{

    private ListView mRugListView;
    private ImageSwitcher mImageSwitcher;

    int imageSwitcherImages[] = {R.drawable.gilam1, R.drawable.gilam2, R.drawable.gilam3 };

    int switcherImage = imageSwitcherImages.length;
    int counter = 0;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
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

        mRugListView = (ListView) findViewById(R.id.characs_of_design);
        mRugListView.setAdapter(new ArrayAdapter<DummyContent.DummyItem>(getBaseContext(),android.R.layout.simple_list_item_activated_1, android.R.id.text1, DummyContent.ITEMS));


        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                     @Override
                                     public View makeView() {
                                         ImageView myView = new ImageView(getApplicationContext());
                                         myView.setLayoutParams(new ImageSwitcher.LayoutParams(
                                                 ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT
                                         ));
                                         myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                         myView.setImageResource(R.drawable.gilam1);
                                         return myView;
                                     }
                                 });




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

    public void confirmAddRug() {
        DialogFragment newFragment = new AddRugDialog();
        newFragment.show(getFragmentManager(), "addrug");

    }

    public void nextImageButton(View view) {
        counter++;
        if (counter == switcherImage)
            counter = 0;
        mImageSwitcher.setImageResource(imageSwitcherImages[counter]);
    }
    public void prevImageButton(View view) {
        counter--;
        if (counter == -1)
            counter = switcherImage-1;
        mImageSwitcher.setImageResource(imageSwitcherImages[counter]);
    }
}
