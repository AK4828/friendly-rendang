package id.merv.cdp.book.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.joanzapata.pdfview.PDFView;
import com.meruvian.dnabook.R;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.adapter.PrintAdapter;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;

/**
 * Created by akm on 21/12/15.
 */
public class BookViewActivity extends AppCompatActivity {
    @Bind(R.id.pdfview) PDFView pdfview;
    @Bind(R.id.fab_print) FloatingActionButton fabPrint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_view_fragment);
        ButterKnife.bind(this);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    }, 3000);
                } else {
                }
            }
        });

        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDocument(view);
            }
        });

    }

    public void printDocument(View view) {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        Bundle data = getIntent().getExtras();
        long id = data.getLong("attachmentsId");
        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        DocumentDao documentDao = application.getDaoSession().getDocumentDao();
        Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.DbId.eq(id)).build().unique();

        try {
            PdfReader pdfReader = new PdfReader(doc.getPath());
            int totalPages = pdfReader.getNumberOfPages();
            Log.d("PAGES", String.valueOf(totalPages));

        } catch (IOException e) {
            e.printStackTrace();
        }

        printManager.print(jobName, new PrintAdapter(this, doc.getPath()), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle data = getIntent().getExtras();
        long id = data.getLong("attachmentsId");
        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        DocumentDao documentDao = application.getDaoSession().getDocumentDao();
        Document doc = documentDao.queryBuilder().where(DocumentDao.Properties.DbId.eq(id)).build().unique();
        String path = doc.getPath();
        if (doc != null) {
            pdfview.fromFile(new File(path))
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();
        } else {
            Toast.makeText(this, "Failed receive Document", Toast.LENGTH_SHORT).show();
        }

    }
}
