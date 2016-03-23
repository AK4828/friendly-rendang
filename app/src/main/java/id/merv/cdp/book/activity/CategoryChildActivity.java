package id.merv.cdp.book.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.meruvian.dnabook.R;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.BooksInsideCategoryAdapter;
import id.merv.cdp.book.adapter.CategoryChipAdapter;
import id.merv.cdp.book.job.CategoryChildJob;
import id.merv.cdp.book.job.ContentJob;
import id.merv.cdp.book.service.JobStatus;

/**
 * Created by akm on 10/03/16.
 */
public class CategoryChildActivity extends AppCompatActivity {

    private String id;
    private CategoryChipAdapter categoryChipAdapter;
    private BooksInsideCategoryAdapter booksInsideCategoryAdapter;
    private JobManager jobManager;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    @Bind(R.id.category_chip_recycler) RecyclerView categoryRecycler;
    @Bind(R.id.category_content_recycler) RecyclerView categoryContentRecycler;
    @Bind(R.id.empty_indicator) LinearLayout emptyIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosed_category_child_fragment);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            id = data.getString("parentId");
        }

        jobManager = MeruvianBookApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(CategoryChildJob.newInstance(id));

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoryChipAdapter = new CategoryChipAdapter(this));

        layoutManager = new GridLayoutManager(this, 2);
        categoryContentRecycler.setLayoutManager(layoutManager);
        categoryContentRecycler.setAdapter(booksInsideCategoryAdapter = new BooksInsideCategoryAdapter(this));

    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
        super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(CategoryChildJob.CategoryChildJobEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {
        }
        if (event.getStatus() == JobStatus.SUCCESS) {
            categoryChipAdapter.addItem(event.getCategoriesList());
        }
    }

    public void onEventMainThread(ContentJob.ContentJobEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {

        } else if (event.getStatus() == JobStatus.SUCCESS) {
            emptyIndicator.setVisibility(View.GONE);
            categoryContentRecycler.setVisibility(View.VISIBLE);
            booksInsideCategoryAdapter.addItems(event.getContentsList());
        } else if (event.getStatus() == JobStatus.SYSTEM_ERROR) {
            emptyIndicator.setVisibility(View.VISIBLE);
        }

    }
}
