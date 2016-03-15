package id.merv.cdp.book.job;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.entity.MainBody;
import id.merv.cdp.book.service.CategoriesService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by akm on 08/03/16.
 */
public class CategoryChildJob extends Job {

    private List<Categories> categoriesList;
    private CategoriesService service;
    private String id;

    public static CategoryChildJob newInstance(String id) {
        CategoryChildJob categoryChildJob = new CategoryChildJob();

        categoryChildJob.id = id;

        return categoryChildJob;
    }


    protected CategoryChildJob() {

        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new CategoryChildJob.CategoryChildJobEvent(JobStatus.ADDED, categoriesList));
    }

    @Override
    public void onRun() throws Throwable {

        MeruvianBookApplication application = MeruvianBookApplication.getInstance();
        Retrofit retrofit = application.getRetrofit();
        service = retrofit.create(CategoriesService.class);

        try {
            Call<MainBody<Categories>> getParentCategories = service.getChildNameCategories(id);
            MainBody<Categories> categories = getParentCategories.execute().body();
            categoriesList = categories.getContent();

            if (categoriesList.size() != 0) {
                EventBus.getDefault().post(new CategoryChildJobEvent(JobStatus.SUCCESS, categoriesList));
            }

        } catch (Exception e) {
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

    public static class CategoryChildJobEvent {
        private int status;
        private List<Categories> categoriesList;

        public CategoryChildJobEvent(int status, List<Categories> categoriesList) {
            this.categoriesList = categoriesList;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Categories> getCategoriesList() {
            return categoriesList;
        }

        public void setCategoriesList(List<Categories> categoriesList) {
            this.categoriesList = categoriesList;
        }
    }
}
