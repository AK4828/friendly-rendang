package id.merv.cdp.book.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.meruvian.dnabook.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;

/**
 * Created by akm on 21/12/15.
 */
public class BookViewActivity extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.book_view_fragment);

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

    }

    @Override
    public void onResume() {
        super.onResume();
        pdfView = (PDFView)findViewById(R.id.pdfview);
        Bundle data = getIntent().getExtras();
        long id = data.getLong("attachmentsId");
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
            Toast.makeText(this, "Failed receive Document", Toast.LENGTH_SHORT).show();
        }

    }
}
