package com.minisoftwareandgames.ryan.etherticker.widgetproviders;

import android.appwidget.AppWidgetManager;
import android.content.Context;
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
        setUp("://widget21/id/", EtherTickerWidgetProviderMedium.class, R.layout.widget21);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
