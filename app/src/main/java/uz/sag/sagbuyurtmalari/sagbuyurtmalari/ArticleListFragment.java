package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.util.ImageCache;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.util.ImageFetcher;

//import uz.sag.sagbuyurtmalari.sagbuyurtmalari.model.Order;


public class ArticleListFragment extends ListFragment {

    /**
     *
     */
    private static final String TAG = "ArticleListFragment :";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */


    private int mActivatedPosition = ListView.INVALID_POSITION;

    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageFetcher mImageFetcher;
    private SimpleCursorAdapter mAdapter;

    //private RecyclerView mRecyclerView;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
     */
    public ArticleListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //INIT CURRENT ORDER with id{OrderItem}
        //Order.CART_ITEM = new Order.OrderItem(DatabaseOpenHelper.getInstance(getContext()).getLastOrderId()+1);

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.3f); // Set memory cache to 25% of app memory

        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

        mAdapter = new SimpleCursorAdapter(
                getContext(), // Context.
                android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor   rows).
                DatabaseOpenHelper.getInstance(getContext()).getAllQualities(),                                              // Pass in the cursor to bind to.
                new String[]{"name", "code"},           // Array of cursor columns to bind to.
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        //@TODO may be memory leaks
        setListAdapter(mAdapter);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setActivateOnItemClick(true);
        setActivatedPosition(0);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

//        mRecyclerView = (RecyclerView) getView().findViewById(R.id.article_list);
//        if (mColumnCount <= 1) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        } else {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//        }
        // mRecyclerView.setHasFixedSize(true);
        //recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//                // Pause fetcher to ensure smoother scrolling when flinging
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Before Honeycomb pause image loading on scroll to help with performance
//                    if (!Utils.hasHoneycomb()) {
//                        mImageFetcher.setPauseWork(true);
//                    }
//                } else {
//                    mImageFetcher.setPauseWork(false);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//            }
//        });



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
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        Cursor cursor = (Cursor) mAdapter.getItem(position);

        MainActivity.mDetailsFragment.filterItemList(cursor.getString(2));
        setActivatedPosition(position);

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
        View view = inflater.inflate(R.layout.article_list,null);
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

    /**
     * The current activated item position. Only used on tablets.
     */
    public int getActivatedPosition() {
        return mActivatedPosition;
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
}
