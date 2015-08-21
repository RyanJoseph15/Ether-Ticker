package com.minisoftwareandgames.ryan.etherticker.widgetproviders;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.minisoftwareandgames.ryan.etherticker.ConfigActivity;
import com.minisoftwareandgames.ryan.etherticker.JSONAsyncTask;
import com.minisoftwareandgames.ryan.etherticker.R;
import com.minisoftwareandgames.ryan.etherticker.SQLiteHelper;
import com.minisoftwareandgames.ryan.etherticker.SettingsFragment;
import com.minisoftwareandgames.ryan.etherticker.objects.URLandViews;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ryan on 8/10/15.
 */
public class EtherTickerWidgetProvider extends AppWidgetProvider {

    String id = "://etherwidget/id/";
    int layout = 0;
    Class<EtherTickerWidgetProvider> ETWPclass;
//    int widgetId = 0;

    public void setUp(String id, Class ETWPclass, int layout) {
        this.id = id;
        this.ETWPclass = ETWPclass;
        this.layout = layout;
    }
//
//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//        super.onReceive(context, intent);
//        this.widgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
//        Log.d("etherticker", widgetId + "");
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("etherticker", "onUpdate()");

        ComponentName widget = new ComponentName(context, ETWPclass);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

//        Log.d("etherticker", "widgetIds.length = " + widgetIds.length);
        for (int widgetId : widgetIds) {
            Intent intent = new Intent(context, ConfigActivity.class);

            /* make the widget12 id's actually unique */
            Uri data = Uri.withAppendedPath(
                    Uri.parse("etherticker" + id)
                    ,String.valueOf(widgetId));
            intent.setData(data);
            /* ------------------------------------ */

            intent.putExtra("classType", ETWPclass);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//            Log.d("etherticker", "Provider: " + widgetId);

            /* reference for the views associated with the widget12 */
            RemoteViews views = new RemoteViews(context.getPackageName(), layout);
            views.setOnClickPendingIntent(R.id.widget_layout, pIntent);

            /* exchange_URL_array(index) is the associated URL for exchange_array(index) */
            ArrayList<String> exchanges = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.exchange_array)));
            ArrayList<String> urls = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.exchange_URL_array)));
            String url = urls.get(exchanges.indexOf(new SQLiteHelper(context).getWidget(widgetId).exchange));

            /* perform update */

            views.setProgressBar(R.id.progressBar, 0, 0, false);
            views.setViewVisibility(R.id.progressBar, View.VISIBLE);
            new JSONAsyncTask().execute(new URLandViews(url, appWidgetManager, widgetId, views, context));

            /* update views in case they weren't properly updated in AsyncTask */
            appWidgetManager.updateAppWidget(widgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
