package id.merv.cdp.book.fragment;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.BookListAdapter;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.ContentService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 18/12/15.
 */
public class ChoosedCategoryBookDetailFragment extends Fragment {

    @Bind(R.id.list_book_contents) RecyclerView recyclerView;
    @Bind(R.id.contents_title) TextView contenTitle;
    @Bind(R.id.contents_description) TextView contenDescription;
    RecyclerView.LayoutManager layoutManager;
    private BookListAdapter adapter;
    private MainBody<Contents> mainBody;
    private MainBody<FileInfo> attachmentsMainBody;

    public static ChoosedCategoryBookDetailFragment newInstance(String id, String attachmentsId) {
        ChoosedCategoryBookDetailFragment fragment = new ChoosedCategoryBookDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.getArguments().putString("id", id);
        fragment.getArguments().putString("attachmentsId", attachmentsId);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choosed_category_book_detail, container, false);

        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter = new BookListAdapter(getActivity()));

        String id = getArguments().getString("id");

        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);

        try {
            Map<String, String> param = new HashMap<>();
            param.put("category", id);
            Call<MainBody<Contents>> getContentsByCategory = service.getContentsById(param);
            getContentsByCategory.enqueue(new Callback<MainBody<Contents>>() {
                @Override
                public void onResponse(Response<MainBody<Contents>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        mainBody = response.body();
                        for (Contents s : mainBody.getContent()) {
                            Contents contents = new Contents();
                            contents.setTitle(s.getTitle());
                            contenTitle.setText(s.getTitle());
                            contenDescription.setText(s.getDescription());
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

        try {
            Log.d("I", id);
            String attachmentsId = getArguments().getString("attachmentsId");
            Log.d("I", attachmentsId);
            Call<MainBody<FileInfo>>getContentsAttachments = service.getContentsAttachment(attachmentsId);
            getContentsAttachments.enqueue(new Callback<MainBody<FileInfo>>() {
                @Override
                public void onResponse(Response<MainBody<FileInfo>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        attachmentsMainBody = response.body();

                        for (FileInfo f : attachmentsMainBody.getContent()) {
                            adapter.addItem(f);

                        }

                        attachmentsMainBody.getTotalElements();


                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        layoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }catch (Exception e) {

        }

        return view;
    }


}
