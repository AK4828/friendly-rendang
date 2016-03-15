package id.merv.cdp.book.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.ContentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;

/**
 * Created by akm on 14/03/16.
 */
public class ContentBookJob extends Job {

    private String contentsId;
    private List<Document> documentList = new ArrayList<Document>();

    public static ContentBookJob newInstance(String contentsId) {

        ContentBookJob ContentBookJob = new ContentBookJob();
        ContentBookJob.contentsId = contentsId;

        return ContentBookJob;
    }

    protected ContentBookJob() {

        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {

        EventBus.getDefault().post(new ContentBookJobEvent(JobStatus.ADDED, documentList));

    }

    @Override
    public void onRun() throws Throwable {
        String limitData = "100";
        Map<String, String> limit = new HashMap<>();
        limit.put("max", limitData);

        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);
        Call<MainBody<Document>> getContentsAttachments = service.getContentsAttachment(contentsId, limit);
        MainBody<Document> document= getContentsAttachments.execute().body();
        documentList = document.getContent();

        Log.d("document Size", String.valueOf(documentList.size()));
        EventBus.getDefault().post(new ContentBookJobEvent(JobStatus.SUCCESS, documentList));

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class ContentBookJobEvent {
        private int status;
        private List<Document> documentList = new ArrayList<Document>();

        public ContentBookJobEvent(int status, List<Document> documentList) {
            this.status = status;
            this.documentList = documentList;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Document> getDocumentList() {
            return documentList;
        }

        public void setDocumentList(List<Document> documentList) {
            this.documentList = documentList;
        }
    }
}
