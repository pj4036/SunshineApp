
package com.example.android.sunshine.app;

/**
 * Created by prateekjain on 9/11/15
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ForecastAdapter extends CursorAdapter {

  private static final int VIEW_TYPE_COUNT = 2;
  private static final int VIEW_TYPE_TODAY = 0;
  private static final int VIEW_TYPE_FUTURE_DAY = 1;
  private boolean mUseTodayLayout = true;
  public static class ViewHolder {
    public final ImageView iconView;
    public final TextView dateView;
    public final TextView descriptionView;
    public final TextView highTempView;
    public final TextView lowTempView;

    public ViewHolder(View view) {
      iconView = (ImageView) view.findViewById(R.id.list_item_icon);
      dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
      descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
      highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
      lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
    }
  }

  public ForecastAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    int viewType = getItemViewType(cursor.getPosition());
    int layoutId = -1;
    switch (viewType) {
      case VIEW_TYPE_TODAY: {
        layoutId = R.layout.list_item_forecast_today;
        break;
      }
      case VIEW_TYPE_FUTURE_DAY: {
        layoutId = R.layout.list_item_forecast;
        break;
      }
    }

    View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

    ViewHolder viewHolder = new ViewHolder(view);
    view.setTag(viewHolder);

    return view;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    ViewHolder viewHolder = (ViewHolder) view.getTag();

    int viewType = getItemViewType(cursor.getPosition());
    switch (viewType) {
      case VIEW_TYPE_TODAY: {
        viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
            cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        break;
      }
      case VIEW_TYPE_FUTURE_DAY: {
        viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
            cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        break;
      }
    }

    String dateString = cursor.getString(ForecastFragment.COL_WEATHER_DATE);
    viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateString));
    String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
    viewHolder.descriptionView.setText(description);
    viewHolder.iconView.setContentDescription(description);
    boolean isMetric = Utility.isMetric(context);
    double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
    viewHolder.highTempView.setText(Utility.formatTemperature(context, high));
    double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
    viewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
  }

  public void setUseTodayLayout(boolean useTodayLayout) {
    mUseTodayLayout = useTodayLayout;
  }

  @Override
  public int getItemViewType(int position) {
    return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
  }

  @Override
  public int getViewTypeCount() {
    return VIEW_TYPE_COUNT;
  }
}