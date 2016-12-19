package com.mariebyleen.weather.weather_display.forecast.view;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.model.DailyForecast;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

public class ForecastRecyclerAdapter
        extends RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastHolder>
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;
    private SavedDataRetriever savedData;

    private DailyForecast[] forecasts;

    public ForecastRecyclerAdapter(SharedPreferences preferences,
                                   SavedDataRetriever savedData) {
        this.preferences = preferences;
        this.savedData = savedData;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        getData();
    }

    private void getData() {
        if (forecasts == null)
            forecasts = savedData.getSavedWeatherData().getForecasts();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_row, parent, false);
        ForecastHolder holder = new ForecastHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        final DailyForecast forecast = forecasts[position];
        holder.getBinding().setVariable(BR.forecast, forecast);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return forecasts.length;
    }


    public class ForecastHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ForecastHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
