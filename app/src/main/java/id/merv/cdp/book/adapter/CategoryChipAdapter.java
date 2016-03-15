package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.dnabook.R;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.activity.MainActivity;
import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.job.CategoryChildJob;
import id.merv.cdp.book.job.ContentJob;

/**
 * Created by akm on 08/03/16.
 */
public class CategoryChipAdapter extends RecyclerView.Adapter<CategoryChipAdapter.ViewHolder>  {

    private Context context;
    private List<Categories> categoriesList = new ArrayList<>();
    private JobManager jobManager;

    public CategoryChipAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_chip_recycler_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Categories categories = categoriesList.get(position);
        holder.categoryTitle.setText(categories.getName());
        holder.itemView.setTag(categories.getId());

        jobManager = MeruvianBookApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(ContentJob.newInstance(categories.getId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesList.clear();
                jobManager.addJobInBackground(CategoryChildJob.newInstance(categories.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public void addItem(Collection<Categories> categoriesList) {
        this.categoriesList.addAll(categoriesList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        }
    }
}
