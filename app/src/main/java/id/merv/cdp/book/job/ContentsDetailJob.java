package id.merv.cdp.book.job;

import android.util.Log;
import android.widget.TextView;

import com.meruvian.dnabook.R;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.ContentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 20/12/15.
 */
public class ContentsDetailJob extends Job {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String id;
    private MainBody<Contents> mainBody;
    private Document document;
    private Contents contents;


    public static ContentsDetailJob newInstance(String id) {
        ContentsDetailJob job = new ContentsDetailJob();
        job.id = id;

        return job;
    }

    protected ContentsDetailJob() {
        super(new Params(1).requireNetwork().persist());
    }
    @Override
    public void onAdded() {

        EventBus.getDefault().post(new ContentJobEvent(contents, JobStatus.ADDED));

    }

    @Override
    public void onRun() throws Throwable {
        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);
        final DocumentDao documentDao = app.getDaoSession().getDocumentDao();
        try {
            Map<String, String> param = new HashMap<>();
            param.put("category", id);
            Call<MainBody<Contents>> getContentsByCategory = service.getContentsById(param);
            getContentsByCategory.enqueue(new Callback<MainBody<Contents>>() {
                @Override
                public void onResponse(Response<MainBody<Contents>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        mainBody = response.body();
                        for (Contents s : mainBody.getContent()) {

                            document = new Document();

                            document.setDbCreateDate(new Date());
                            document.setId(s.getId());
                            document.setSubject(s.getTitle());
                            document.setDescription(s.getDescription());

                            documentDao.insert(document);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post(new ContentJobEvent(contents, JobStatus.SUCCESS));

    }

    @Override
    protected void onCancel() {

        EventBus.getDefault().post(new ContentJobEvent(contents, JobStatus.ABORTED));

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class ContentJobEvent {
        private int status;
        private Contents contents;


        public ContentJobEvent(Contents contents ,int status) {
            this.status = status;
            this.contents = contents;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Contents getContents() {
            return contents;
        }

        public void setContents(Contents contents) {
            this.contents = contents;
        }
    }
}
