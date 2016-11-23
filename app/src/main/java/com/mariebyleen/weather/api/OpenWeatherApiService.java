package com.mariebyleen.weather.api;

import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface OpenWeatherApiService {

    @GET("/weather?lat={lat}&lon={lon}")
    Observable<CurrentConditionsResponse> getCurrentConditions(
            @Path("lat") double lat, @Path("lon") double lon);
}