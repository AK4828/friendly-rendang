package id.merv.cdp.book.service;

import id.merv.cdp.book.entity.Categories;
import id.merv.cdp.book.entity.MainBody;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by akm on 16/12/15.
 */
public interface CategoriesService {

    @GET("/api/categories")
    Call<MainBody<Categories>>getParentNameCategory();

    @GET("/api/categories/{id}/categories")
    Call<MainBody<Categories>> getChildNameCategories(@Path("id") String parentId);

}
