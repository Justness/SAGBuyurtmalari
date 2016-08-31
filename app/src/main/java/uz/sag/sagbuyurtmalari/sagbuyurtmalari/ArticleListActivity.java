package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

public class ArticleListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);


        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ArticleAdapter(this, DatabaseOpenHelper.getInstance(null).getAllQualitiesItems()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(HelloGridView.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
            }
        });
//        if (findViewById(R.id.article_detail_container) != null) {
//        // The detail container view will be present only in the
//        // large-screen layouts (res/values-large and
//        // res/values-sw600dp). If this view is present, then the
//        // activity should be in two-pane mode.
//        mTwoPane = true;
//
//        // In two-pane mode, list items should be given the
//        // 'activated' state when touched.
//        ((ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.article_list)).setActivateOnItemClick(true);
//        }
    }

    public class ArticleAdapter extends BaseAdapter {
        private Context mContext;
        private int mSize;
        private List<MyCollectionRecyclerViewAdapter.Miniature> mValues;

        public ArticleAdapter(Context c, List<MyCollectionRecyclerViewAdapter.Miniature> values) {
            mContext = c;
            mValues = values;
            mSize = values.size();
        }


        @Override
        public int getCount() {
            return mSize;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return Integer.parseInt(mValues.get(position).id);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            String file = MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY_ABS + mValues.get(position).imgUrl;
            File f = new File(file);
            if (f.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file);
                imageView.setImageBitmap(myBitmap);
            }
            //imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }


    }

    /**
     * Callback method from {@link ArticleListFragment.Callbacks}
     * indicating that the article (rug) with the given name (ID) was selected.
     */
//    @Override
//    public void onItemSelected(String id) {
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, id);
//            ArticleDetailFragment fragment = new ArticleDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
//
//        } else {
//            // In single-pane mode, simply start the detail activity
//            // for the selected item ID.
//            Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
//            detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
//            startActivity(detailIntent);
//        }
//    }

//    @Override
//    public void onSubItemSelected(String item) {
//
//    }
}

