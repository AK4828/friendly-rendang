package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.entity.FileInfo;

/**
 * Created by akm on 19/12/15.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    List<FileInfo> bookList = new ArrayList<FileInfo>();

    public BookListAdapter(Context context) {
        this.context = context;
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_inner_fragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo fileInfo = bookList.get(position);
        holder.bookThumbnail.setImageResource(R.drawable.ssssss);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void addItem(FileInfo fileInfo) {
        bookList.add(fileInfo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView bookTitle;
        public ImageView bookThumbnail;
        public TextView bookPrice;
        public ImageButton detailButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
            bookPrice = (TextView) itemView.findViewById(R.id.book_description);
            detailButton = (ImageButton) itemView.findViewById(R.id.detail_button);
        }
    }
}
