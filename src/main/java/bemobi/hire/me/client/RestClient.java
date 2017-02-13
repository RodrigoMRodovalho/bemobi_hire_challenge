package bemobi.hire.me.client;

import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.response.ExpandResponse;
import bemobi.hire.me.api.response.MostAccessedResponse;
import bemobi.hire.me.api.response.ReduceResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import java.io.IOException;

/**
 * Created by rrodovalho on 10/02/17.
 */
public class RestClient {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private ShortenApi api;

    private interface ShortenApi {
        @PUT(Constants.URL_MAPPING.REDUCE)
        Call<ReduceResponse> reduceUrl(@Query("url") String url, @Query("alias") String alias);

        @GET(Constants.URL_MAPPING.EXPAND)
        Call<ExpandResponse> expandUrl(@Query("alias")String alias);

        @GET(Constants.URL_MAPPING.TEN_MOST_ACCESSED)
        Call<MostAccessedResponse> getTopTen();
    }

    public RestClient() {

        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ShortenApi.class);

    }

    public Response<ReduceResponse> reduceUrl(String url,String alias) throws IOException {
        return api.reduceUrl(url,alias).execute();
    }

    public Response<ReduceResponse> reduceUrl(String url) throws IOException {
        return api.reduceUrl(url,null).execute();
    }

    public Response<ExpandResponse> expandUrl(String alias) throws IOException {
        return api.expandUrl(alias).execute();
    }

    public Response<MostAccessedResponse> getMostTenAcessed() throws IOException {
        return api.getTopTen().execute();
    }

}
