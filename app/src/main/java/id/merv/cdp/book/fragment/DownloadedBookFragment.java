package id.merv.cdp.book.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meruvian.dnabook.R;
import id.merv.cdp.book.adapter.BookGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by akm on 17/11/15.
 */
public class DownloadedBookFragment extends Fragment {

    private BookGridAdapter bookGridAdapter;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;

    public static DownloadedBookFragment newInstance() {
        DownloadedBookFragment fragment = new DownloadedBookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadedBookFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downloaded_book_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookGridAdapter = new BookGridAdapter(getActivity()));

        return view;
    }
}
