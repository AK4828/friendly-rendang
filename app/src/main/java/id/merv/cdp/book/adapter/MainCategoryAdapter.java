package id.merv.cdp.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.dnabook.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import id.merv.cdp.book.activity.CategoryChildActivity;
import id.merv.cdp.book.activity.MainActivity;
import id.merv.cdp.book.entity.Categories;

/**
 * Created by akm on 07/03/16.
 */
public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    private Context context;
    private List<Categories> categoriesList = new ArrayList<>();

    public MainCategoryAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_recycler_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Categories categories = categoriesList.get(position);
        holder.categoryTitle.setText(categories.getName());
        holder.itemView.setTag(categories.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryChildActivity.class);
                intent.putExtra("parentId", categories.getId());
                context.startActivity(intent);
            }
        });

    }

    public void addItem(Collection<Categories> categoriesList) {
        this.categoriesList.addAll(categoriesList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        }
    }
}
