package id.merv.cdp.book.service;

import android.content.Entity;

import java.util.Map;

import id.merv.cdp.book.entity.Attachments;
import id.merv.cdp.book.entity.Contents;
import id.merv.cdp.book.entity.Document;
import id.merv.cdp.book.entity.FileInfo;
import id.merv.cdp.book.entity.MainBody;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by akm on 18/12/15.
 */
public interface ContentService {

    @GET("/api/contents")
    Call<MainBody<Contents>>getContentsById(@QueryMap Map<String, String> id);

    @GET("/api/contents/{id}/attachments")
    Call<MainBody<Document>>getContentsAttachment(@Path("id")String id, @QueryMap Map<String, String> limit);

    @GET("/api/contents/{id}")
    Call<Contents>getChoosedContentsById(@Path("id")String id);


}
