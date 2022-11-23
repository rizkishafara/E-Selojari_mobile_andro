package com.example.e_selojari.api;

/**
 * Created by Robby Dianputra on 10/31/2017.
 */

import com.example.e_selojari.model.city.ItemCity;
import com.example.e_selojari.model.cost.ItemCost;
import com.example.e_selojari.model.province.ItemProvince;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Province
    @GET("province")
    @Headers("key:90b0064dd20de7030079f6c62c7da7d1")
    Call<ItemProvince> getProvince();

    // City
    @GET("city")
    @Headers("key:90b0064dd20de7030079f6c62c7da7d1")
    Call<ItemCity> getCity (@Query("province") String province);

    // Cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost (@Field("key") String Token,
                            @Field("origin") String origin,
                            @Field("destination") String destination,
                            @Field("weight") String weight,
                            @Field("courier") String courier);

}
