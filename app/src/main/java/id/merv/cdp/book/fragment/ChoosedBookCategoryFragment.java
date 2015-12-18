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

import butterknife.Bind;
import butterknife.ButterKnife;
import id.merv.cdp.book.adapter.BookGridAdapter;
import id.merv.cdp.book.adapter.BooksInsideCategoryAdapter;

/**
 * Created by akm on 18/12/15.
 */
public class ChoosedBookCategoryFragment extends Fragment {

    private BooksInsideCategoryAdapter adapter;
    @Bind(R.id.book_inside_category) RecyclerView booksRecycler;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.Adapter mAdapter;

    public static ChoosedBookCategoryFragment newInstance() {
        ChoosedBookCategoryFragment fragment = new ChoosedBookCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choosed_book_category, container, false);
        ButterKnife.bind(this, view);

        booksRecycler.setHasFixedSize(true);
        booksRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        layoutManager = new GridLayoutManager(getActivity(), 2);
        booksRecycler.setLayoutManager(layoutManager);
        booksRecycler.setAdapter(adapter = new BooksInsideCategoryAdapter(getActivity()));

        return view;
    }
}
