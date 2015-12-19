package id.merv.cdp.book.job;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.fragment.ChoosedBookCategoryFragment;
import id.merv.cdp.book.service.ContentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 18/12/15.
 */
public class ContentsJob extends Job {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private MainBody<Contents> mainBody;

    private String id;

    public static ContentsJob newInstance(String id) {
        ContentsJob job = new ContentsJob();
        job.id = id;

        return job;
    }

    public ContentsJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new ContentsJobEvent(JobStatus.ADDED));

    }

    @Override
    public void onRun() throws Throwable {
        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        ContentService service = app.getRetrofit().create(ContentService.class);
        try {
            Map<String, String> param = new HashMap<>();
            param.put("category", id);
            Call<MainBody<Contents>>getContentsByCategory = service.getContentsById(param);
            getContentsByCategory.enqueue(new Callback<MainBody<Contents>>() {
                @Override
                public void onResponse(Response<MainBody<Contents>> response, Retrofit retrofit) {
                    if (response.isSuccess()){
                        mainBody = response.body();
                        List<Contents> contentsList = mainBody.getContent();
                        for (int i = 0; i < contentsList.size(); i++) {
                            String getContentId = contentsList.get(i).getId();

                            ArrayList<String> contentArrayList = new ArrayList<String>();
                            contentArrayList.add(getContentId);
//                            ChoosedBookCategoryFragment.newInstance(contentArrayList);

                        }

                        EventBus.getDefault().post(new ContentsJobEvent(JobStatus.SUCCESS));

                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }catch (Exception e) {

        }


    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class ContentsJobEvent {
        private int status;
        public ContentsJobEvent(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
