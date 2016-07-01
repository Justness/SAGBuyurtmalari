package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dummy.DummyContent.DummyItem;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.util.ImageFetcher;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCollectionRecyclerViewAdapter extends RecyclerView.Adapter<MyCollectionRecyclerViewAdapter.ViewHolder> {

    public static final String THUMBS_DIRECTORY = "/saggallery/thumbs/";
    public static String THUMBS_DIRECTORY_ABS;


    private static final int BUFFER_IO_SIZE = 8000;
    //public static final String THUMBS_DIRECTORY = ;

    private List<Miniature> mValues;
    private int mSize;
    private final ArticleListFragment.Callbacks mListener;

    private Context mContext;
    private ImageFetcher mImageFetcher;

    public MyCollectionRecyclerViewAdapter(Context context, List<Miniature> miniatures, ArticleListFragment.Callbacks listener) {
        mValues = miniatures;
        mListener = listener;
        // mImageFetcher = imageFetcher;
        mContext = context;

        if (isExternalStorageReadable()) {

            //String url = "http://thenextweb.com/apps/files/2010/03/google_logo.jpg";


            File file = Environment.getExternalStorageDirectory();
            THUMBS_DIRECTORY_ABS = file.getPath() + THUMBS_DIRECTORY;
            //holder.mContentView.setText(file.getPath()+THUMBS_DIRECTORY+"AC0023_PC73_028.jpg");
            // get input stream


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // holder.mIdView.setText(mValues.get(position).id);
       // mImageFetcher.loadImage(Images.imageThumbUrls[0 ],  holder.mImgUrl);
        holder.mContentView.setText(mValues.get(position).title);
        //Picasso.with(mContext).load(mContext.getAssets().
//        if (isExternalStorageReadable()) {

        //String url = "http://thenextweb.com/apps/files/2010/03/google_logo.jpg";

        //holder.mContentView.setText(file.getPath()+THUMBS_DIRECTORY+"AC0023_PC73_028.jpg");
        // get input stream
        Bitmap myBitmap = BitmapFactory.decodeFile(THUMBS_DIRECTORY_ABS + mValues.get(position).imgUrl);

//            // load image as Drawable

        holder.mImgUrl.setImageBitmap(myBitmap);

//        }

        // holder.mImgUrl.setImageResource(R.drawable.empty_photo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSubItemSelected(holder.mItem.title.substring(0, 6));
                }
            }
        });

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    private void copy(final InputStream bis, final OutputStream baos) throws IOException {
        byte[] buf = new byte[256];
        int l;
        while ((l = bis.read(buf)) >= 0) baos.write(buf, 0, l);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImgUrl;

        public Miniature mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImgUrl = (ImageView) view.findViewById(R.id.imgUrl);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public static class Miniature {
        public final String id;
        public final String title;
        public final String imgUrl;

        //public String meaning;

        public Miniature(String id, String title, String imgUrl) {
            this.id = id;
            this.title = title;
            this.imgUrl = imgUrl;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
