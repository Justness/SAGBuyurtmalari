package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ArticleListActivity extends FragmentActivity implements ArticleListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        if (findViewById(R.id.article_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.article_list)).setActivateOnItemClick(true);
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

    @Override
    public void onSubItemSelected(String item) {

    }
}

