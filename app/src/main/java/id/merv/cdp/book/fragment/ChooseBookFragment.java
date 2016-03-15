package id.merv.cdp.book.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meruvian.dnabook.R;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.MainCategoryAdapter;
import id.merv.cdp.book.job.CategoryJob;
import id.merv.cdp.book.service.JobStatus;

/**
 * Created by akm on 03/03/16.
 */
public class ChooseBookFragment extends Fragment {

    private MainCategoryAdapter mainCategoryAdapter;
    private JobManager jobManager;
    private RecyclerView.LayoutManager layoutManager;
    @Bind(R.id.category_recycler) RecyclerView categoryRecycler;


    public static ChooseBookFragment newInstance() {

        ChooseBookFragment fragment = new ChooseBookFragment();

        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_book_fragment, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setAdapter(mainCategoryAdapter = new MainCategoryAdapter(getActivity()));

        jobManager = MeruvianBookApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(CategoryJob.newInstance());

        return view;
    }

    public void onEventMainThread(CategoryJob.CategoryJobEvent event) {
        if (event.getStatus() == JobStatus.ADDED) {
        }
        if (event.getStatus() == JobStatus.SUCCESS) {
            mainCategoryAdapter.addItem(event.getCategoriesList());
        }
    }
}
