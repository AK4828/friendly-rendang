package id.merv.cdp.book.job;

import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.utils.IoUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.ResponseBody;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import id.merv.cdp.book.MeruvianBookApplication;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.DocumentDao;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.entity.FileInfoDao;
import id.merv.cdp.book.service.AttachmentService;
import id.merv.cdp.book.service.JobStatus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 19/12/15.
 */
public class AttahmentsDownloadJob extends Job {

    private String id;
    private long dbId;
    private String subject;

    public static AttahmentsDownloadJob newInstance(String id, String subject) {
        AttahmentsDownloadJob job = new AttahmentsDownloadJob();
        job.id = id;
        job.subject = subject;

        return job;
    }

    protected AttahmentsDownloadJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.ADDED, dbId));

    }

    @Override
    public void onRun() throws Throwable {
        MeruvianBookApplication app = MeruvianBookApplication.getInstance();
        final DocumentDao documentDao = app.getDaoSession().getDocumentDao();
        AttachmentService service = app.getRetrofit().create(AttachmentService.class);
        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File outputFile = new File(downloadPath, UUID.randomUUID().toString());
        final FileOutputStream outputStream = new FileOutputStream(outputFile);

        try {
            Call<ResponseBody> getDocumentById = service.getAttachmentsFileByid(id);
            getDocumentById.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        try {
                            InputStream stream = response.body().byteStream();
                            IOUtils.copy(stream, outputStream);
                            IOUtils.closeQuietly(outputStream);
                            IOUtils.closeQuietly(stream);

                            Document document = new Document();

                            document.setDbCreateDate(new Date());
                            document.setId(id);
                            document.setSubject(subject);
                            document.setPath(outputFile.getAbsolutePath());

                            long id = documentDao.insert(document);


                            EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.SUCCESS, id));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.SYSTEM_ERROR, dbId));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.ABORTED, dbId));

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class AttachmentsDownloadEvent {
        private int status;
        private long dbId;

        public AttachmentsDownloadEvent(int status, long dbId) {
            this.dbId = dbId;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getDbId() {
            return dbId;
        }

        public void setDbId(long dbId) {
            this.dbId = dbId;
        }
    }
}
