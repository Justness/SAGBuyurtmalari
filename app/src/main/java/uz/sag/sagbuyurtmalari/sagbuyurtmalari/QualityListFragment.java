package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

//import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.Order;


public class QualityListFragment extends Fragment {

    /**
     *
     */
    private static final String TAG = "QualityListFragment :";
    public static final String QUALITY_CODE = "QualityListFragmentQualityCode";
    private GridView gridview;
    private ArticleAdapter madapter;
    private Context mContext;

    //private RecyclerView mRecyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
     */
    public QualityListFragment() {
        mContext = this.getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //INIT CURRENT ORDER with id{OrderItem}
        //Order.CART_ITEM = new Order.OrderItem(DatabaseOpenHelper.getInstance(getContext()).getLastOrderId()+1);
        //setContentView(R.layout.activity_article_list);


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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridview = (GridView) getView().findViewById(R.id.gridview);
        madapter = new ArticleAdapter(this.getContext(), DatabaseOpenHelper.getInstance(this.getContext()).getAllQualitiesItems());
        gridview.setAdapter(madapter);
        mContext = this.getContext();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                Intent intent = new Intent(mContext, DesignsActivity.class);
                intent.putExtra(QUALITY_CODE, madapter.getItem(position));
                startActivity(intent);
//                Toast.makeText(HelloGridView.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Activities containing this fragment must implement its callbacks.
//        if (!(activity instanceof Callbacks)) {
//            throw new IllegalStateException("Activity must implement fragment's callbacks.");
//        }
//        mCallbacks = (Callbacks) activity;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, null);
        //super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    //Image adapter
//    private class ImageAdapter extends BaseAdapter {
//
//        private final Context mContext;
//        private int mItemHeight = 0;
//        private int mNumColumns = 0;
//        private int mActionBarHeight = 0;
//        private GridView.LayoutParams mImageViewLayoutParams;
//
//        public ImageAdapter(Context context) {
//            super();
//            mContext = context;
//            mImageViewLayoutParams = new GridView.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            // Calculate ActionBar height
//            TypedValue tv = new TypedValue();
//            if (context.getTheme().resolveAttribute(
//                    android.R.attr.actionBarSize, tv, true)) {
//                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
//                        tv.data, context.getResources().getDisplayMetrics());
//            }
//        }
//
//        @Override
//        public int getCount() {
//            // If columns have yet to be determined, return no items
//            if (getNumColumns() == 0) {
//                return 0;
//            }
//
//            // Size + number of columns for top empty row
//            return Images.imageThumbUrls.length + mNumColumns;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position < mNumColumns ?
//                    null : Images.imageThumbUrls[position - mNumColumns];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position < mNumColumns ? 0 : position - mNumColumns;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            // Two types of views, the normal ImageView and the top row of empty views
//            return 2;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return (position < mNumColumns) ? 1 : 0;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup container) {
//            //BEGIN_INCLUDE(load_gridview_item)
//            // First check if this is the top row
//            if (position < mNumColumns) {
//                if (convertView == null) {
//                    convertView = new View(mContext);
//                }
//                // Set empty view with height of ActionBar
//                convertView.setLayoutParams(new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
//                return convertView;
//            }
//
//            // Now handle the main ImageView thumbnails
//            ImageView imageView;
//            if (convertView == null) { // if it's not recycled, instantiate and initialize
//                imageView = new RecyclingImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setLayoutParams(mImageViewLayoutParams);
//            } else { // Otherwise re-use the converted view
//                imageView = (ImageView) convertView;
//            }
//
//            // Check the height matches our calculated column width
//            if (imageView.getLayoutParams().height != mItemHeight) {
//                imageView.setLayoutParams(mImageViewLayoutParams);
//            }
//
//            // Finally load the image asynchronously into the ImageView, this also takes care of
//            // setting a placeholder image while the background thread runs
//            mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
//            return imageView;
//            //END_INCLUDE(load_gridview_item)
//        }
//
//        /**
//         * Sets the item height. Useful for when we know the column width so the height can be set
//         * to match.
//         *
//         * @param height
//         */
//        public void setItemHeight(int height) {
//            if (height == mItemHeight) {
//                return;
//            }
//            mItemHeight = height;
//            mImageViewLayoutParams =
//                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
//            mImageFetcher.setImageSize(height);
//            notifyDataSetChanged();
//        }
//
//        public void setNumColumns(int numColumns) {
//            mNumColumns = numColumns;
//        }
//
//        public int getNumColumns() {
//            return mNumColumns;
//        }
//    }
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

        public String getItem(int position) {
            return mValues.get(position).title;
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
                imageView.setLayoutParams(new GridView.LayoutParams(300, 225));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(2, 2, 2, 2);
            } else {
                imageView = (ImageView) convertView;
            }

            File f = Environment.getExternalStorageDirectory();
            String file = f.getPath() + MyCollectionRecyclerViewAdapter.THUMBS_DIRECTORY + mValues.get(position).imgUrl;
            f = new File(file);
            if (f.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file);
                imageView.setImageBitmap(myBitmap);
            }
            //imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }


    }
}
