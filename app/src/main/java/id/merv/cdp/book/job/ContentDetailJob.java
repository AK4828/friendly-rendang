package id.merv.cdp.book.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.service.ContentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;

/**
 * Created by akm on 14/03/16.
 */
public class ContentDetailJob extends Job {

    private String contentsId;
    private Contents contents;

    public static ContentDetailJob newInstance(String contentsId) {

        ContentDetailJob contentDetailJob = new ContentDetailJob();
        contentDetailJob.contentsId = contentsId;

        return contentDetailJob;
    }

    protected ContentDetailJob() {

        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new ContentDetailJobEvent(JobStatus.ADDED, contents));
    }

    @Override
    public void onRun() throws Throwable {

        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);
        try {
            Call<Contents> getContentsById = service.getChoosedContentsById(contentsId);
            contents = getContentsById.execute().body();
            Log.d("Content Title", contents.getTitle());
            EventBus.getDefault().post(new ContentDetailJobEvent(JobStatus.SUCCESS, contents));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class ContentDetailJobEvent {
        private int status;
        private Contents contents;

        public ContentDetailJobEvent(int status, Contents contents) {
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
