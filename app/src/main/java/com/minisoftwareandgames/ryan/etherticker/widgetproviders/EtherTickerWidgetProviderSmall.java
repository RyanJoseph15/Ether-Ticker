package com.minisoftwareandgames.ryan.etherticker.widgetproviders;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;
import com.minisoftwareandgames.ryan.etherticker.R;

/**
 * Created by ryan on 8/12/15.
 */
public class EtherTickerWidgetProviderSmall extends EtherTickerWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("ethertickerSmall", "onUpdate()");
        setUp("://widget11/id/", EtherTickerWidgetProviderSmall.class, R.layout.widget11);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
