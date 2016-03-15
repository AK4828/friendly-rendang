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
import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.ContentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;

/**
 * Created by akm on 08/03/16.
 */
public class ContentJob extends Job {

    private String id;
    private List<Contents> contentsList = new ArrayList<Contents>();

    public static ContentJob newInstance(String id) {

        ContentJob contentJob = new ContentJob();
        contentJob.id = id;

        return contentJob;
    }

    protected ContentJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new ContentJobEvent(JobStatus.ADDED, contentsList));
    }

    @Override
    public void onRun() throws Throwable {

        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);

        try {
            Map<String, String> param = new HashMap<>();
            param.put("category", id);

            Call<MainBody<Contents>> getContentsByCategory = service.getContentsById(param);
            MainBody<Contents> contentsMainBody = getContentsByCategory.execute().body();
            contentsList = contentsMainBody.getContent();
            Log.d("Content SIZE", String.valueOf(contentsList.size()));

            if (contentsList.size() != 0) {
                EventBus.getDefault().post(new ContentJob.ContentJobEvent(JobStatus.SUCCESS, contentsList));
            } else if (contentsList.size() == 0) {
                EventBus.getDefault().post(new ContentJob.ContentJobEvent(JobStatus.SYSTEM_ERROR, contentsList));
            }
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

    public static class ContentJobEvent {
        private int status;
        private List<Contents> contentsList;

        public ContentJobEvent(int status, List<Contents> contentsList) {
            this.status = status;
            this.contentsList = contentsList;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Contents> getContentsList() {
            return contentsList;
        }

        public void setContentsList(List<Contents> contentsList) {
            this.contentsList = contentsList;
        }
    }
}
