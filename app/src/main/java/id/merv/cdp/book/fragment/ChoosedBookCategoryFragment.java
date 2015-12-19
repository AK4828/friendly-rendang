package id.merv.cdp.book.fragment;

import android.content.Entity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.dnabook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.BooksInsideCategoryAdapter;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.ContentService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 18/12/15.
 */
public class ChoosedBookCategoryFragment extends Fragment {

    @Bind(R.id.book_inside_category) RecyclerView booksRecycler;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    private MainBody<Contents> mainBody;
    private  BooksInsideCategoryAdapter adapter;

    public static ChoosedBookCategoryFragment newInstance(String id) {
        ChoosedBookCategoryFragment fragment = new ChoosedBookCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.getArguments().putString("id", id);
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

        final String id = getArguments().getString("id");
        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);

        try {
            Map<String, String> param = new HashMap<>();
            param.put("category",id);
            Call<MainBody<Contents>> getContentsByCategory = service.getContentsById(param);
            getContentsByCategory.enqueue(new Callback<MainBody<Contents>>() {
                @Override
                public void onResponse(Response<MainBody<Contents>> response, Retrofit retrofit) {
                    if (response.isSuccess()){
                        mainBody = response.body();
                        for (Contents s : mainBody.getContent()) {
                            Contents contents = new Contents();
                            contents.setTitle(s.getTitle());
                            Log.d("ID", s.getId());

                            adapter = new BooksInsideCategoryAdapter(getActivity(), id , s.getId());
                            booksRecycler.setHasFixedSize(true);
                            booksRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                            layoutManager = new GridLayoutManager(getActivity(), 2);
                            booksRecycler.setLayoutManager(layoutManager);
                            adapter.addItem(contents);
                            booksRecycler.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

//    private void getContentsImage(String contentsID) {
//        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
//        BooksInsideCategoryAdapter adapter = new BooksInsideCategoryAdapter(getActivity());
//        ContentService service = app.getRetrofit().create(ContentService.class);
//        Call<MainBody<Contents>>getContentsImage = service.getContentsImage(contentsID);
//        getContentsImage.enqueue(new Callback<MainBody<Contents>>() {
//            @Override
//            public void onResponse(Response<MainBody<Contents>> response, Retrofit retrofit) {
//                if (response.isSuccess()) {
//                    mainBody = response.body();
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
}
