package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dummy.DummyContent.DummyItem;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.provider.Images;
import uz.sag.sagbuyurtmalari.sagbuyurtmalari.util.ImageFetcher;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCollectionRecyclerViewAdapter extends RecyclerView.Adapter<MyCollectionRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final ArticleListFragment.Callbacks mListener;

    private ImageFetcher mImageFetcher;

    public MyCollectionRecyclerViewAdapter(List<DummyItem> items, ArticleListFragment.Callbacks listener, ImageFetcher imageFetcher) {
        mValues = items;
        mListener = listener;
        mImageFetcher = imageFetcher;
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
        holder.mIdView.setText(mValues.get(position).id);
       // mImageFetcher.loadImage(Images.imageThumbUrls[0 ],  holder.mImgUrl);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mImgUrl.setImageResource(R.drawable.empty_photo);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSubItemSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImgUrl;

        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImgUrl = (ImageView) view.findViewById(R.id.imgUrl);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
