package id.merv.cdp.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.activity.BookViewActivity;
import id.merv.cdp.book.entity.Book;
import id.merv.cdp.book.entity.Document;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by akm on 19/11/15.
 */
public class BookGridAdapter extends RecyclerView.Adapter<BookGridAdapter.ViewHolder> {

    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<Document> documents = new ArrayList<>();

    public BookGridAdapter(Context context) {
        this.context = context;

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_book_inner_fragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Document document = documents.get(position);
        holder.bookTitle.setText(document.getSubject());
        String imageUrl = MeruvianBookApplication.SERVER_URL + "/api/attachments/" + document.getId() + "/preview";
        imageLoader.displayImage(imageUrl, holder.bookThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookViewActivity.class);
                intent.putExtra("attachmentsId", document.getDbId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return documents.size();
    }

    public void addItem(Collection<Document> documents) {
        this.documents.addAll(documents);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bookTitle;
        public ImageView bookThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.card_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
        }
    }
}

