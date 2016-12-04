package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobRequest;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class CurrentConditionsViewModel extends BaseObservable
        implements Observer<CurrentConditionsResponse>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";
    private double lat = 0;
    private double lon = 0;

    private UpdateService updates;
    private CurrentConditionsMapper mapper;
    private SharedPreferences preferences;

    private CurrentConditions conditions;
    private Subscription subscription;
    private Observable<CurrentConditionsResponse> observable;


    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                        UpdateService updateService,
                                      CurrentConditionsMapper mapper) {
        this.preferences = preferences;
        this.updates = updateService;
        this.mapper = mapper;
    }

    public void onFragmentResume() {
        if (conditions == null) {
            Log.d(TAG, "populating data from memory");
            conditions = updates.getSavedConditions();
        }

        new JobRequest.Builder("WeatherDataUpdateJob")
                .setExecutionWindow(500, 1000)
                .build()
                .schedule();

        if (updates.needsManualUpdate()) {
            updates.getManualUpdateObservable()
                    .subscribe(this);
            //Log.d(TAG, "refreshing weather data manually");
        }

        observable = updates.getAutomaticUpdateObservable();
        //startUpdates();

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onFragmentPause() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        stopUpdates();
        updates.saveData(conditions);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (s.equals("CurrentConditions")) {
            conditions = updates.getSavedConditions();
            Log.i(TAG, "Data in SharedPreferences updated");
            notifyChange();
        }
    }

    private void startUpdates() {
        subscription = observable.subscribe(this);
        //Log.d(TAG, "Subscribing to observable: " + observable.toString());
    }

    private void stopUpdates() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
            //Log.d(TAG, "un-subscribing");
        }
    }

    @Override
    public void onCompleted() {
        //Log.i(TAG, "Current conditions weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        //Log.e(TAG, "Error retrieving current conditions weather data: \n" + e.toString());
    }

    @Override
    public void onNext(CurrentConditionsResponse currentConditionsResponse) {
        /*
        Log.d(TAG, "onNext called");
        conditions = mapper.mapCurrentConditions(currentConditionsResponse);
        notifyChange();
        updates.notifyUpdated();
        */
    }

    @Bindable
    public String getTemperature() {
        double temperature = conditions.getTemperature();
        Log.d(TAG, "getTemperature called");
        return "Temperature: " + String.valueOf(temperature);
    }

}
