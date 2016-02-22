package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.activity.MainActivity;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.MainBody;

/**
 * Created by akm on 18/12/15.
 */
public class BooksInsideCategoryAdapter extends RecyclerView.Adapter<BooksInsideCategoryAdapter.ViewHolder> {
    private Context context;
    List<Contents> bookList = new ArrayList<Contents>();
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BooksInsideCategoryAdapter(Context context) {
        this.context = context;
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choosed_book_category_inner_fragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contents contents = bookList.get(position);
        holder.bookTitle.setText(contents.getTitle());
        String imageUrl = MeruvianBookApplication.SERVER_URL + "/api/contents/" + contents.getId() + "/thumbnail";
        imageLoader.displayImage(imageUrl, holder.bookThumbnail);

        holder.itemView.setTag(contents.getId());
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getFragment((String) view.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void addItem(Contents contents) {
        bookList.add(contents);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView bookTitle;
        public ImageView bookThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
        }
    }
}
