package id.merv.cdp.book.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.BookGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.merv.cdp.book.entity.DaoSession;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadBooks();
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

    private void loadBooks() {
        DaoSession daoSession = MeruvianBookApplication.getInstance().getDaoSession();
        final DocumentDao dao = daoSession.getDocumentDao();
        new AsyncTask<Void,Void, List<Document>>() {

            @Override
            protected List<Document> doInBackground(Void... voids) {
                return dao.queryBuilder()
                        .orderDesc(DocumentDao.Properties.DbCreateDate)
                        .limit(50)
                        .offset(bookGridAdapter.getItemCount())
                        .build().forCurrentThread()
                        .list();
            }

            @Override
            protected void onPostExecute(List<Document> documents) {
                bookGridAdapter.addItem(documents);
            }
        }.execute();

    }
}
