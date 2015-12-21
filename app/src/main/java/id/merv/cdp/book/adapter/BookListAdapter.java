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
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Attachments;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.job.AttahmentsDownloadJob;

/**
 * Created by akm on 19/12/15.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private long documentId;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    List<Document> bookList = new ArrayList<Document>();
    private JobManager jobManager;

    public BookListAdapter(Context context, Long documentId) {
        this.context = context;
        this.documentId = documentId;
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
        holder.bookThumbnail.setImageResource(R.drawable.no_image);
        holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobManager = MeruvianBookApplication.getInstance().getJobManager();
                jobManager.addJobInBackground(AttahmentsDownloadJob.newInstance(documents.getId(), documentId));

            }
        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void addItem(Document attachments) {
        bookList.add(attachments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView bookTitle;
        public ImageView bookThumbnail;
        public TextView detailButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
            detailButton = (TextView) itemView.findViewById(R.id.detail_button);
        }
    }
}
