package com.mariebyleen.weather.location.view_model;

import android.databinding.BaseObservable;

import com.mariebyleen.weather.location.activity.LocationViewContract;
import com.mariebyleen.weather.location.model.WeatherLocation;

import javax.inject.Inject;

public class LocationViewModel extends BaseObservable {

    private final String dialogText = "";

    private WeatherLocation location;
    private LocationViewContract view;

    @Inject
    public LocationViewModel(LocationViewContract view, WeatherLocation location) {
        this.location = location;
        this.view = view;
    }

    public void useCurrentLocation() {
        view.checkPermissions();
    }

    public void onLocationPermissionGranted() {
        view.showProgressDialog(dialogText);
        location.updateLocationData();
        view.hideProgressDialog();
    }

    public void onLocationPermissionDenied() {
        view.disableUseCurrentLocationOption();
    }

    public void onStop() {

    }
}