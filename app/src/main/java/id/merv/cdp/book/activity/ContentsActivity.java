package id.merv.cdp.book.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.BookListAdapter;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.job.AttahmentsDownloadJob;
import id.merv.cdp.book.job.CategoryChildJob;
import id.merv.cdp.book.job.ContentBookJob;
import id.merv.cdp.book.job.ContentDetailJob;
import id.merv.cdp.book.service.JobStatus;

/**
 * Created by akm on 14/03/16.
 */
public class ContentsActivity extends AppCompatActivity {

    @Bind(R.id.list_book_contents) RecyclerView recyclerView;
    @Bind(R.id.contents_title) TextView contenTitle;
    @Bind(R.id.contents_description) TextView contenDescription;
    @Bind(R.id.category_image) ImageView categoryImage;
    @Bind(R.id.downloading_progress) ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    private BookListAdapter adapter;
    private Contents content;
    private JobManager jobManager;
    private String contentsId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosed_category_book_detail);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter = new BookListAdapter(this));

        Bundle data = getIntent().getExtras();

        contentsId = data.getString("contentId");
        jobManager = MeruvianBookApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(ContentDetailJob.newInstance(contentsId));
        jobManager.addJobInBackground(ContentBookJob.newInstance(contentsId));


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

    public void onEventMainThread(ContentDetailJob.ContentDetailJobEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {
        }
        if (event.getStatus() == JobStatus.SUCCESS) {
            contenTitle.setText(event.getContents().getTitle());
            contenDescription.setText(Html.fromHtml(event.getContents().getDescription()));
            String imageUrl = MeruvianBookApplication.SERVER_URL + "/api/contents/" + contentsId + "/thumbnail";

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, categoryImage);
        }
    }

    public void onEventMainThread(ContentBookJob.ContentBookJobEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {
        }
        if (event.getStatus() == JobStatus.SUCCESS) {
            adapter.addItems(event.getDocumentList());
        }
    }

    public void onEventMainThread(AttahmentsDownloadJob.AttachmentsDownloadEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();
        }
        if (event.getStatus() == JobStatus.SUCCESS) {
            Intent intent = new Intent(this, BookViewActivity.class);
            intent.putExtra("attachmentsId", event.getDbId());
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (event.getStatus() == JobStatus.SYSTEM_ERROR) {
            Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (event.getStatus() == JobStatus.ABORTED) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
