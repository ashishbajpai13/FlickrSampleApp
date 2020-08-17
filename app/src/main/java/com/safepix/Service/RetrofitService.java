package com.safepix.Service;
import com.safepix.Model.Images;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("rest/")
    Observable<Images> getAllImages(@Query("method") String method,
                                    @Query("api_key") String apiKey,
                                    @Query("text") String searchText,
                                    @Query("format") String format,
                                    @Query("nojsoncallback") String noJsonCallBack,
                                    @Query("page") String page,
                                    @Query("per_page") String perPage);
}
