package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import id.merv.cdp.book.activity.MainActivity;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.MainBody;

/**
 * Created by akm on 18/12/15.
 */
public class BooksInsideCategoryAdapter extends RecyclerView.Adapter<BooksInsideCategoryAdapter.ViewHolder> {

    private Context context;
    private String id;
    private long documentId;
    private String attachmentsId;
    List<Contents> bookList = new ArrayList<Contents>();
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BooksInsideCategoryAdapter(Context context, String id, String attachmentsId, long documentId) {
        this.attachmentsId = attachmentsId;
        this.id = id;
        this.documentId = documentId;
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
//        holder.bookThumbnail.setImageResource(R.drawable.no_image);
        holder.detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) context).getFragment(id, attachmentsId, documentId);

                Log.d("tes2", id);
                Log.d("contents", attachmentsId);
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
        public TextView bookPrice;
        public ImageView detailButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            detailButton = (ImageView) itemView.findViewById(R.id.detail_button);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
            String imageUrl = MeruvianBookApplication.SERVER_URL + "/api/contents/" + attachmentsId + "/thumbnail";
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, bookThumbnail);
            Log.d("cekServImage", imageUrl);
        }
    }
}
