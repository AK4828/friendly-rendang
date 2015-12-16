package id.merv.cdp.book.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.merv.cdp.book.entity.Book;
import com.meruvian.dnabook.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by akm on 19/11/15.
 */
public class BookGridAdapter extends RecyclerView.Adapter<BookGridAdapter.ViewHolder> {

    private Context context;
    List<Book> bookList = new ArrayList<>();
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BookGridAdapter(Context context) {
        this.context = context;

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        Book book = new Book();

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookPrice("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookPrice("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookPrice("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookPrice("IDR 50.000");
        bookList.add(book);

        book.setBookTitle("Sports");
        book.setBookThumbnail(R.drawable.ssssss);
        book.setBookPrice("IDR 50.000");
        bookList.add(book);


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_book_inner_fragment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book books = bookList.get(position);
        holder.bookTitle.setText(books.getBookTitle());
        holder.bookThumbnail.setImageResource(books.getBookThumbnail());
        holder.bookPrice.setText(books.getBookPrice());


    }

    @Override
    public int getItemCount() {

        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @Bind(R.id.card_title) TextView bookTitle;
//        @Bind(R.id.card_image) ImageView bookThumbnail;
        public TextView bookTitle;
        public ImageView bookThumbnail;
        public TextView bookPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = (TextView) itemView.findViewById(R.id.card_title);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.card_image);
            bookPrice = (TextView) itemView.findViewById(R.id.card_price);
        }
    }
}

