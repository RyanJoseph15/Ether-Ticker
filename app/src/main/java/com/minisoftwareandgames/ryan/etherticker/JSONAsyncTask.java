package com.minisoftwareandgames.ryan.etherticker;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import java.util.List;

/**
 * Created by ryan on 8/14/15.
 */
public class JSONAsyncTask extends AsyncTask<URLandViews, Void, JSONObject> {

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
//                    Log.d("etherticker", "response: " + jsono.toString());
                    /* get options from settings */
//                    SharedPreferences prefs = urlAndviews[0].context.getSharedPreferences("EtherTicker", Context.MODE_PRIVATE);
//                    String exchange = prefs.getString("exchange", "Gatecoin");
				Widget widget = new SQLiteHelper(urlAndviews[0].context).getWidget(urlAndviews[0].widgetId);
				String exchange = widget.exchange;
				String currency = widget.currency;
				JSONArray array = null;
				String high = null;
				String low = null;
				String price = null;
				Log.d("etherticker", exchange + " " + currency);
				switch (exchange) {
					case "Gatecoin":
						/* TODO: if this get much bigger, change method to find by name with loop */
						array = jsono.getJSONArray("tickers");
						JSONObject ETHBTC = array.getJSONObject(3);
						JSONObject BTCEUR = array.getJSONObject(0);
						JSONObject BTCHKD = array.getJSONObject(1);
						JSONObject BTCUSD = array.getJSONObject(2);
						JSONObject from = ETHBTC;
						JSONObject to = BTCUSD;
						boolean convert = true;
						switch (currency) {
							case "BTC":
								convert = false;
								price = String.format("%07f", Float.valueOf(ETHBTC.getString("open")));
								low = String.format("%07f", Float.valueOf(ETHBTC.getString("low")));
								high = String.format("%07f", Float.valueOf(ETHBTC.getString("high")));
								break;
							case "HKD":
								to = BTCHKD;
								break;
							case "EUR":
								to = BTCEUR;
								break;
							case "USD":
								to = BTCUSD;
								break;
							default:
								Log.i("switch (currency)", "null");
								break;
						}
						if (convert) {
							price = convert(OP.MULT, from.getString("open"), to.getString("open"));
							low = convert(OP.MULT, from.getString("low"), to.getString("low"));
							high = convert(OP.MULT, from.getString("high"), to.getString("high"));
						}
						break;
					case "Poloniex":
						int id = R.array.poloniex_currency_array;
						JSONObject base = jsono.getJSONObject("BTC_ETH");
						String name = null;
						if (currency.equals("BTC")) {
							price = String.format("%07f", Float.valueOf(base.getString("last")));
							low = String.format("%07f", Float.valueOf(base.getString("low24hr")));
							high = String.format("%07f", Float.valueOf(base.getString("high24hr")));
						} else {
							name = "BTC_" + currency;
							from = jsono.getJSONObject(name);
							Log.d("etherticker", base.toString());
							Log.d("etherticker", from.toString());
							if (from.getString("isFrozen").equals("0")) {
								price = convert(OP.DIV, base.getString("last"), from.getString("last"));
								low = convert(OP.DIV, base.getString("low24hr"), from.getString("last"));
								high = convert(OP.DIV, base.getString("high24hr"), from.getString("last"));
							} else {
								low = "0.0";
								high = low;
								price = "FROZEN";
							}
						}
						break;
					default:
						Log.i("etherticker", "switch (exchange) is null");
						break;
				}
				Log.d("etherticker", low + " " + high + " " + price + " " + currency + " " + exchange);
				urlAndviews[0].views.setTextViewText(R.id.low, low);
				urlAndviews[0].views.setTextViewText(R.id.high, high);
				urlAndviews[0].views.setTextViewText(R.id.price, price);
				urlAndviews[0].views.setTextViewText(R.id.currency, currency);
				urlAndviews[0].views.setTextViewText(R.id.exchange, exchange);
				/* progress bar visual */
				urlAndviews[0].views.setProgressBar(R.id.progressBar, 100, 100, false);
				urlAndviews[0].views.setViewVisibility(R.id.progressBar, View.GONE);
				/* update the view */
				urlAndviews[0].manager.updateAppWidget(urlAndviews[0].widgetId, urlAndviews[0].views);
				return jsono;
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/* progress bar visual */
//		urlAndviews[0].views.setProgressBar(R.id.progressBar, 100, 0, false);
//		urlAndviews[0].views.setViewVisibility(R.id.progressBar, View.GONE);
//		/* update the view */
//		urlAndviews[0].manager.updateAppWidget(urlAndviews[0].widgetId, urlAndviews[0].views);
		return null;
	}

	protected void onPostExecute(boolean result) {
            /* eh */
	}

	private String convert(OP op, String first, String second) {
		if (op == OP.MULT)
			return String.format("%.07f", Float.valueOf(first) * Float.valueOf(second));
		else return String.format("%.07f", Float.valueOf(first) / Float.valueOf(second));
	}

	private String getRatio(String first, String second) {
		/* inverse each to get value per shared currency (BTC), then divide first by second. */
		return String.valueOf(Float.valueOf(first)/Float.valueOf(second));
	}

	private enum OP {MULT, DIV}

}
