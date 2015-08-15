package com.minisoftwareandgames.ryan.etherticker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.minisoftwareandgames.ryan.etherticker.objects.URLandViews;
import com.minisoftwareandgames.ryan.etherticker.objects.Widget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by ryan on 8/10/15.
 */
public class EtherTickerWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("etherticker", "onUpdate()");
        ComponentName widget = new ComponentName(context, EtherTickerWidgetProvider.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

//        Log.d("etherticker", "widgetIds.length = " + widgetIds.length);
        for (int widgetId : widgetIds) {
            Intent intent = new Intent(context, ConfigActivity.class);

            /* make the widget id's actually unique */
            Uri data = Uri.withAppendedPath(
                    Uri.parse("etherticker" + "://widget/id/")
                    ,String.valueOf(widgetId));
            intent.setData(data);
            /* ------------------------------------ */

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//            Log.d("etherticker", "Provider: " + widgetId);

            /* reference for the views associated with the widget */
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.widget_layout, pIntent);

            /* exchange_URL_array(index) is the associated URL for exchange_array(index) */
            ArrayList<String> exchanges = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.exchange_array)));
            ArrayList<String> urls = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.exchange_URL_array)));
            String url = urls.get(exchanges.indexOf(new SQLiteHelper(context).getWidget(widgetId).exchange));

            /* perform update */
            new JSONAsyncTask().execute(new URLandViews(url, appWidgetManager, widgetId, views, context));

            /* update views in case they weren't properly updated in AsyncTask */
            appWidgetManager.updateAppWidget(widgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
