package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(long date);
    }

    private Cursor mCursor;


    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler) {

        mContext = context;
        mClickHandler = clickHandler;
    }


    //This gets called when each new ViewHolder is created. This happens when the RecyclerView
    //is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.forecast_list_item, viewGroup, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);

    }

    // Override onBindViewHolder
    // Set the text of the TextView to the weather for this list item's position

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);

        long dataInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        //human readable date conversion
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dataInMillis, false);
        //using weatherId to fetch weather description
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);

        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String highAndLowTemperature =
                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);

        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;

        forecastAdapterViewHolder.weatherSummary.setText(weatherSummary);


    }

    // This method simply returns the number of items to display. It is used behind the scenes
    //to help layout our Views and for animations.

    @Override
    public int getItemCount() {

        if (null == mCursor) return 0;

        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        //updating newCursor to notify change
        mCursor = newCursor;

        notifyDataSetChanged();


    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView weatherSummary;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            mCursor.moveToPosition(adapterPosition);
            long dataInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dataInMillis);

        }

    }


}
