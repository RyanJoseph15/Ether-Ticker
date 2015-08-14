package com.minisoftwareandgames.ryan.etherticker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

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

/**
 * Created by ryan on 8/10/15.
 */
public class EtherTickerWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("widget", "onUpdate()");
        ComponentName widget = new ComponentName(context, EtherTickerWidgetProvider.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

        for (int widgetId : widgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            /* make sure URL gets updated to whichever exchange is selected in the spinner */
            new JSONAsyncTask().execute(new URLandViews("https://gatecoin.com/api/Public/LiveTickers", appWidgetManager, widgetId, views, context));
            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }

    class JSONAsyncTask extends AsyncTask<URLandViews, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(URLandViews... urlAndviews) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urlAndviews[0].URL);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    Log.d("response", jsono.toString());
                    /* get options from settings */
                    SharedPreferences prefs = urlAndviews[0].context.getSharedPreferences("EtherTicker", Context.MODE_PRIVATE);
                    String exchange = prefs.getString("exchange", "GateCoin");
                    JSONArray array = null;
                    switch (exchange) {
                        case "GateCoin":
                            array = jsono.getJSONArray("tickers");
                            String currency = prefs.getString("currency", "BTC");
                            /* update views */
                            JSONObject ETHBTC = array.getJSONObject(3);
                            JSONObject BTCEUR = array.getJSONObject(0);
                            JSONObject BTCHKD = array.getJSONObject(1);
                            JSONObject BTCUSD = array.getJSONObject(2);
                            String high = null;
                            String low = null;
                            String price = null;
                            switch (currency) {
                                case "BTC":
                                    low = ETHBTC.getString("low");
                                    high = ETHBTC.getString("high");
                                    price = ETHBTC.getString("open");
                                    break;
                                case "HKD":
                                    low = convert(ETHBTC.getString("low"), BTCHKD.getString("low"));
                                    high = convert(ETHBTC.getString("high"), BTCHKD.getString("high"));
                                    price = convert(ETHBTC.getString("open"), BTCHKD.getString("open"));
                                    break;
                                case "EUR":
                                    low = convert(ETHBTC.getString("low"), BTCEUR.getString("low"));
                                    high = convert(ETHBTC.getString("high"), BTCEUR.getString("high"));
                                    price = convert(ETHBTC.getString("open"), BTCEUR.getString("open"));
                                    break;
                                case "USD":
                                    low = convert(ETHBTC.getString("low"), BTCUSD.getString("low"));
                                    high = convert(ETHBTC.getString("high"), BTCUSD.getString("high"));
                                    price = convert(ETHBTC.getString("open"), BTCUSD.getString("open"));
                                    break;
                                default:
                                    Log.i("switch (currency)", "null");
                                    break;
                            }
                            Log.d("crunched numbers", "low: " + low + ", high: " + high + ", open: " + price + ", currency: " + currency + ", exchange: " + exchange);
                            urlAndviews[0].views.setTextViewText(R.id.low, low);
                            urlAndviews[0].views.setTextViewText(R.id.high, high);
                            urlAndviews[0].views.setTextViewText(R.id.price, price);
                            urlAndviews[0].views.setTextViewText(R.id.currency, currency);
                            urlAndviews[0].views.setTextViewText(R.id.exchange, exchange);
                            urlAndviews[0].manager.updateAppWidget(urlAndviews[0].widgetId, urlAndviews[0].views);
                            break;
                        default:
                            Log.i("switch (exchange)", "null");
                            break;
                    }

                    return jsono;
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(boolean result) {
            /* eh */
        }
    }

    private class URLandViews {
        /* this class was created to pass in the views that need to be updated with the
         * information that is returned in the async task */
        public String URL;
        public AppWidgetManager manager;
        public int widgetId;
        public RemoteViews views;
        public Context context;
        public URLandViews(String URL, AppWidgetManager manager, int widgetId, RemoteViews views, Context context) {
            this.URL = URL;
            this.manager = manager;
            this.widgetId = widgetId;
            this.views = views;
            this.context = context;
        }
    }

    private String convert(String from, String to) {
        return String.valueOf(Float.valueOf(from)*Float.valueOf(to));
    }

}
