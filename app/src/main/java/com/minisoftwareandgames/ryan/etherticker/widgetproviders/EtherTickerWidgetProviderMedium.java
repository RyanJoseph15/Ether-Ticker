package com.minisoftwareandgames.ryan.etherticker.widgetproviders;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.minisoftwareandgames.ryan.etherticker.R;

/**
 * Created by ryan on 8/12/15.
 */
public class EtherTickerWidgetProviderMedium extends EtherTickerWidgetProvider {
//
//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//        super.onReceive(context, intent);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("ethertickerMedium", "onUpdate()");
        setUp("://widget12/id/", EtherTickerWidgetProviderMedium.class, R.layout.widget12);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
