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

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String id;
    private long documentid;
    private Document document;

    public static AttahmentsDownloadJob newInstance(String id, long documentId) {
        AttahmentsDownloadJob job = new AttahmentsDownloadJob();
        job.id = id;
        job.documentid = documentId;

        return job;
    }

    protected AttahmentsDownloadJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.ADDED));

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
                            Log.d("cekId", outputFile.getAbsolutePath());

                            Document document = documentDao.queryBuilder().where(DocumentDao.Properties.DbId.eq(documentid)).build().unique();
                            Log.d("docuu", String.valueOf(document));
                            document.setPath(outputFile.getAbsolutePath());
                            document.update();

                            EventBus.getDefault().post(new AttachmentsDownloadEvent(JobStatus.SUCCESS));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

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

    public static class AttachmentsDownloadEvent {
        private int status;

        public AttachmentsDownloadEvent(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
