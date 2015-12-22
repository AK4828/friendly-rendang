package id.merv.cdp.book.fragment;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.meruvian.dnabook.R;
import com.path.android.jobqueue.JobManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;

/**
 * Created by akm on 19/12/15.
 */
public class BookViewFragment extends Fragment{
    @Bind(R.id.pdfview) PDFView pdfView;
    //    @Bind(R.id.fab_sign) FloatingActionButton fab;
    private JobManager jobManager;

    public static BookViewFragment newInstance(long id) {
        BookViewFragment instance = new BookViewFragment();
        instance.setArguments(new Bundle());
        instance.getArguments().putLong("id", id);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_view_fragment, container, false);
        ButterKnife.bind(this, view);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
//            p.setMargins(0, 0, dpToPx(getActivity(), 8), 0); // get rid of margins since shadow area is now the margin
//            fab.setLayoutParams(p);
//        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long id = getArguments().getLong("id");
        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        DocumentDao documentDao = application.getDaoSession().getDocumentDao();
        Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.DbId.eq(id)).build().unique();
        String path = doc.getPath();
        if (doc != null) {
            pdfView.fromFile(new File(path))
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();
        }else {
            Toast.makeText(getActivity(), "Failed receive Document", Toast.LENGTH_SHORT).show();
        }

    }

    private static int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
//    public void printDocument (View view) {
//        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
//        String jobName = this.getString(R.string.app_name) +
//                " Document";
//
//        printManager.print(jobName, new CustomPrintDocumentAdapter(getActivity()), null);
//    }

//    public class CustomPrintDocumentAdapter extends PrintDocumentAdapter {
//
//        Context context;
//        public PdfDocument pdfDocument;
//        public int totalPages = 4;
//
//        public CustomPrintDocumentAdapter (Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
//
//            pdfDocument = new PrintedPdfDocument(context,newAttributes);
//
//            if (cancellationSignal.isCanceled()) {
//                layoutResultCallback.onLayoutCancelled();
//                return;
//            }
//
//            if (totalPages > 0) {
//                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
//                        .Builder("print_output.pdf")
//                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                        .setPageCount(totalPages);
//
//                PrintDocumentInfo info = builder.build();
//                layoutResultCallback.onLayoutFinished(info,true);
//            } else {
//                layoutResultCallback.onLayoutFailed("No document page found");
//                Toast.makeText(context,"No document page found",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//        @Override
//        public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                long id = getArguments().getLong("id");
//                MeruvianBookApplication application = MeruvianBookApplication.getInstance();
//                DocumentDao documentDao = application.getDaoSession().getDocumentDao();
//                Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.DbId.eq(id)).build().unique();
//                String path = doc.getPath();
//                inputStream = new FileInputStream(new File(path));
//                outputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
//
//                byte[] buf = new byte[1024];
//                int bytesRead;
//
//                while ((bytesRead = inputStream.read(buf)) > 0) {
//                    outputStream.write(buf, 0, bytesRead);
//                }
//                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
//            }catch (FileNotFoundException e){
//                e.printStackTrace();
//                Toast.makeText(context, "No document page found", Toast.LENGTH_SHORT).show();
//            }catch (Exception e) {
//                Toast.makeText(context, "Choose document first", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            } finally {
//                try {
//                    inputStream.close();
//                    outputStream.close();
//                } catch (IOException e) {
//                    Toast.makeText(context, "Choose document first", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}