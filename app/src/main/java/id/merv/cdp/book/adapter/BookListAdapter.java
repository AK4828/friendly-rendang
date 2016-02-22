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
import android.widget.Toast;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.job.AttahmentsDownloadJob;

/**
 * Created by akm on 19/12/15.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    List<Document> bookList = new ArrayList<Document>();
    private JobManager jobManager;

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
        final Document documents = bookList.get(position);
        holder.bookTitle.setText(documents.getSubject());
        String imageUrl = MeruvianBookApplication.SERVER_URL + "/api/attachments/" + documents.getId() + "/preview";
        imageLoader.displayImage(imageUrl, holder.bookThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobManager = MeruvianBookApplication.getInstance().getJobManager();
                jobManager.addJobInBackground(AttahmentsDownloadJob.newInstance(documents.getId(), documents.getSubject()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void addItem(Document documents) {
        bookList.add(documents);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView bookTitle;
        public ImageView bookThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.listed_book_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
        }
    }
}
