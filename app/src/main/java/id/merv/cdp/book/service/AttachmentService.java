package id.merv.cdp.book.service;

import com.squareup.okhttp.ResponseBody;

import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.entity.MainBody;
import retrofit.Call;
import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by akm on 19/12/15.
 */
public interface AttachmentService {

    @GET("/api/attachments/{id}/file")
    Call<ResponseBody> getAttachmentsFileByid(@Path("id") String id);
}
