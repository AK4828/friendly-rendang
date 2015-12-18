package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import id.merv.cdp.book.entity.Book;

/**
 * Created by akm on 18/12/15.
 */
public class BooksInsideCategoryAdapter extends RecyclerView.Adapter<BooksInsideCategoryAdapter.ViewHolder> {

    private Context context;
    List<Book> bookList = new ArrayList<>();
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BooksInsideCategoryAdapter(Context context) {
        this.context = context;

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        Book book = new Book();

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookDescription("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookDescription("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookDescription("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookDescription("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookDescription("IDR 50.000");
        bookList.add(book);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choosed_book_category_inner_fragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book books = bookList.get(position);
        holder.bookTitle.setText(books.getBookTitle());
        holder.bookThumbnail.setImageResource(books.getBookThumbnail());
        holder.bookPrice.setText(books.getBookDescription());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bookTitle;
        public ImageView bookThumbnail;
        public TextView bookPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.book_thumbnail);
            bookPrice = (TextView) itemView.findViewById(R.id.book_description);
        }
    }
}
